package com.ontological.retrieval;

import com.ontological.retrieval.Utilities.Entity;
import com.ontological.retrieval.Utilities.Models;
import com.ontological.retrieval.Utilities.Neo4j.GenerateGraph;
import com.ontological.retrieval.Utilities.Triplet;
import com.ontological.retrieval.Utilities.TripletScore;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceChain;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @note   It is strongly recommended to use this extractor with the chain of StandfordParser
 *         which need to parametize by StanfordParser.DependenciesMode.TREE.
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class TripletsExtractor extends JCasConsumer_ImplBase
{
    /**
     * DCoRef parameter: setting singleton predictor
     */
    public static final String PARAM_SINGLETON = "singleton";
    @ConfigurationParameter(name = PARAM_SINGLETON, defaultValue = "true", mandatory = true)
    private boolean singleton;

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        String language = aJCas.getDocumentLanguage();
        if ( language.equals( "en" ) ) {
            handleStandfordTree( aJCas );
        } else {
            System.out.println( "Currently, the only english language is supported." );
            throw new AnalysisEngineProcessException();
        }
    }

    private void handleStandfordTree( JCas aJCas ) throws AnalysisEngineProcessException
    {
        System.out.println( "[ Standford Tree Sentence scope ] " );
        List<Triplet> triplets = new ArrayList<>();
        List<String> scripts = new ArrayList<>();
        HashMap<Integer,CoreferenceLink> corefLinks = new HashMap<>();
        List<Entity> prevEntities = null;
        Sentence prevSentence = null;
        for ( Sentence sentence : JCasUtil.select( aJCas, Sentence.class ) ) {
            List<Entity> entitiesIndex = parseForGraph( aJCas, sentence );
            cacheCoreferenceLink( aJCas, sentence, corefLinks );
            TripletScore forwardScore = parseForScore( aJCas, entitiesIndex );
            Triplet triplet = null;
            for ( Entity en : entitiesIndex ) {

                //
                // @todo
                //          Need to implement parsing of the adjacent attributes of the
                //          parsed entities.
                //
                if ( en.isLeaf() ) {
                    //
                    // It is normal case. Mostly, isLeaf == true means that this is a last
                    // element in the graph path, but could not be the last element in the
                    // 'entitiesIndex'.
                    //
                    continue;
                }
                if ( en.getType().equals( "NSUBJPASS" ) ) {
                    //
                    // @todo
                    //          Add Object
                    //
//                    triplet = new Triplet( sentence.getCoveredText() );
                    triplet = new Triplet( aJCas );
                    triplet.setSubject( en.getName() );
                    String relation = en.getParent().getName().getCoveredText();
                    Entity innerEntity = findEntityType( "AUXPASS", en.getParent().getChildren() );
                    if ( innerEntity != null ) {
                        relation = innerEntity.getName().getCoveredText() + "_" + relation;
                    }
                    triplet.setRelation( relation );
                } else if( en.getType().equals( "NSUBJ" ) ) {
//                    triplet = new Triplet( sentence.getCoveredText() );
                    triplet = new Triplet( aJCas );
                    triplet.setSubject( en.getName() );
                    Entity dobjEntity = findEntityType( "DOBJ", en.getParent().getChildren() );
                    Entity copEntity = findEntityType( "COP", en.getParent().getChildren() );
                    Entity negEntity = findEntityType( "NEG", en.getParent().getChildren() );
                    if ( dobjEntity != null ) {
                        triplet.setObject( dobjEntity.getName() );
                        triplet.setRelation( en.getParent().getName().getCoveredText() );
                    } else if ( en.getParent().getName().getPos().getPosValue().equals( "NN" )/*copEntity == null && negEntity == null*/ ){
                        triplet.setObject( en.getParent().getName() );
                    }
                    if ( triplet.getRelation() == null && ( copEntity != null || negEntity != null ) ) {
                        String relation = "";
                        if ( copEntity != null ) {
                            relation = copEntity.getName().getCoveredText();
                        }
                        if ( negEntity != null ) {
                            relation += ( relation.isEmpty() ? negEntity.getName().getCoveredText() : ("_" + negEntity.getName().getCoveredText() ) );
                        }
                        if ( !triplet.isObject() && en.getParent().getName().getPos().getPosValue().equals( "VB" ) ) {
                            relation += "_" + en.getParent().getName().getCoveredText();
                        }
                        triplet.setRelation( relation );
                    }
                }
                if ( triplet != null ) {
                    resolveCoreference( aJCas, triplet, corefLinks, prevEntities, prevSentence );
                    forwardScore.setBegin( sentence.getBegin() );
                    forwardScore.setEnd( sentence.getEnd() );
                    triplet.setScore( forwardScore );
                    triplets.add( triplet );
                    triplet.addToIndexes( aJCas );
                    triplet.setBegin( sentence.getBegin() );
                    triplet.setEnd( sentence.getEnd() );
                    //
                    // There could be another sentence subject/main_point
                    //
                    triplet = null;
                }
            }
            prevEntities = entitiesIndex;
            prevSentence = sentence;
            //
            // Create scripts for modeling sentence graph for Neo4j.
            // It is for debug purpose.
            scripts.add( GenerateGraph.generateSentenceGraph( entitiesIndex ) );
        }
    }

    private void cacheCoreferenceLink( JCas aJCas, Sentence sentence, HashMap<Integer,CoreferenceLink> corefLinks )
    {
        for ( CoreferenceLink link : JCasUtil.selectCovered( aJCas, CoreferenceLink.class, sentence ) ) {
            System.out.printf( "Current name [%s:%d], type [%s]\n", link.getCoveredText(),
                    link.getBegin(), link.getReferenceType() );
            if ( link.getNext() != null ) {
                System.out.printf( "Next name [%s], next begin [%d], contains [%s].\n", link.getNext().getCoveredText(),
                        link.getNext().getBegin(), corefLinks.containsKey( link.getNext().getBegin() ) ? "true" : "false" );
            }
            if ( link.getReferenceType().equals( "PRONOMINAL" ) ) {
                if ( link.getNext() != null && !corefLinks.containsKey( link.getNext().getBegin() ) ) {
                    corefLinks.put( link.getNext().getBegin(), link );
                    System.out.printf( "Cache PRONOMINAL link name [%s] -> next_name [%s], begin [%d].\n",
                            link.getCoveredText(), link.getNext().getCoveredText(), link.getNext().getBegin() );
                }
            } // another is 'NOMINAL'
        }
    }

    private void resolveCoreference( JCas aJCas, Triplet triplet, HashMap<Integer,CoreferenceLink> corefLinks, List<Entity> prevEntities, Sentence prevSentence )
    {
        if ( triplet.isSubject() && corefLinks.containsKey( triplet.getSubject().getBegin() ) ) {
            CoreferenceLink corefEn = corefLinks.get( triplet.getSubject().getBegin() );
            System.out.printf( "Coreference for triplet subject [%s]->[%s]\n", triplet.getSubject().getCoveredText(),
                    corefEn.getCoveredText() );
        }
        if ( triplet.isObject() && corefLinks.containsKey( triplet.getObject().getBegin() ) ) {
            CoreferenceLink corefEn = corefLinks.get( triplet.getObject().getBegin() );
            System.out.printf( "Coreference for triplet object [%s]->[%s]\n", triplet.getObject().getCoveredText(),
                    corefEn.getCoveredText() );
        }

        if ( triplet.isSubject() && Models.isPronoun( triplet.getSubject().getCoveredText() ) ) {
            System.out.println( "Detected pronoun." );
//            for ( Entity en : prevEntities ) {
//                if ( !en.isLeaf() && en.getType().startsWith( "NSUBJ" ) ) {
//                    System.out.printf( "Detected substitution, incorrect [%s], sub [%s], parent [%s].\n", triplet.getSubject().getCoveredText(),
//                            en.getName().getCoveredText(), en.getParent() != null ? en.getParent().getName().getCoveredText() : "null" );
//                }
//            }
//            for ( Triplet tr : JCasUtil.selectCovered( aJCas, Triplet.class, prevSentence ) ) {
//                tr.printShort();
//            }
        }
    }

    private TripletScore parseForScore( JCas aJCas, List<Entity> entitiesIndex )
    {
        int sentenceSize = 0, mainPoints = 0, undeterminedRelations = 0;
        for ( Entity en : entitiesIndex ) {
            if ( !en.isLeaf() ) {
                ++sentenceSize;
                if ( en.getType().startsWith( "NSUBJ") ) {
                    ++mainPoints;
                } else if ( en.getType().equals( "Dependency" ) ) {
                    ++undeterminedRelations;
                }
            }
        }
        return new TripletScore( aJCas, sentenceSize, undeterminedRelations, mainPoints );
    }

    //
    // @todo
    //       Move to utils.
    //
    private List<Entity> parseForGraph( JCas aJCas, Sentence sentence ) {
        System.out.println("\n[ Sentence ] : " + sentence.getCoveredText());
        List<Entity> entities = new ArrayList<>();
        for ( Dependency dep : JCasUtil.selectCovered(aJCas, Dependency.class, sentence) ) {

            System.out.printf( "type_short_name [%s], governor [%s,%d-%d], dependent [%s,%d-%d]\n",
                    dep.getType().getShortName(),
                    dep.getGovernor().getCoveredText(),dep.getGovernor().getBegin(), dep.getGovernor().getEnd(),
                    dep.getDependent().getCoveredText(),dep.getDependent().getBegin(), dep.getDependent().getEnd() );

            int uInnerEntityPos = dep.getDependent().getBegin();
            Entity upperEntity = findEntity( uInnerEntityPos, entities );
            if ( upperEntity == null ) {
                upperEntity = new Entity( null, dep.getDependent(), dep.getType().getShortName() );
                entities.add( upperEntity );
            } else {
                upperEntity.setType( dep.getType().getShortName() );
            }

            int dInnerEntityPos = dep.getGovernor().getBegin();
            Entity downEntity = findEntity( dInnerEntityPos, entities );
            if ( downEntity == null ) {
                downEntity = new Entity( null, dep.getGovernor(), null );
                entities.add( downEntity );
            }
            upperEntity.setParent( downEntity );
        }
        return entities;
    }

    //
    // @todo
    //       Create a container for storing Entities and move @method findEntity
    //       and @method findEntityType to that container.
    //

    public Entity findEntity( int innerEntityPos, List<Entity> entities  ) {
        for ( Entity en : entities ) {
            if ( en.getBegin() == innerEntityPos ) {
                return en;
            }
        }
        return null;
    }
    public Entity findEntityType( String type, List<Entity> entities  ) {
        for (Entity en : entities) {
            if (en.getType().equals(type)) {
                return en;
            }
        }
        return null;
    }
}
