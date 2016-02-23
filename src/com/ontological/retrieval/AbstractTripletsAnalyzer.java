package com.ontological.retrieval;

import com.ontological.retrieval.Utilities.Entity;
import com.ontological.retrieval.Utilities.Triplet;
import com.ontological.retrieval.Utilities.TripletScore;
import com.ontological.retrieval.Utilities.Utils;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
abstract public class AbstractTripletsAnalyzer extends JCasConsumer_ImplBase
{
    protected Triplet parseForTriplet(JCas aJCas, Sentence sentence, Entity entity, TripletScore score ) {
        Triplet triplet = null;
        //
        // @todo
        //          Need to implement parsing of the adjacent attributes of the
        //          parsed entities.
        if ( entity.isLeaf() ) {
            //
            // It is normal case. Mostly, isLeaf == true means that this is a last element
            // in the graph path, but could not be the last element in the 'entitiesIndex'.
            return triplet;
        }
        if ( entity.getType().equals( "NSUBJPASS" ) ) {
            //
            // @todo
            //          Add Object. Need to find examples for such type. Probably, such type have not
            //          'object', but need to verify it.
            //
            triplet = new Triplet( aJCas, sentence.getBegin(), sentence.getEnd(), sentence.getCoveredText() );
            triplet.setSubject( entity.getName() );
            String relation = entity.getParent().getName().getCoveredText();
            Entity innerEntity = Utils.findEntityType( "AUXPASS", entity.getParent().getChildren() );
            if ( innerEntity != null ) {
                relation = innerEntity.getName().getCoveredText() + "_" + relation;
            }
            triplet.setRelation( relation );
        } else if( entity.getType().equals( "NSUBJ" ) ) {

            triplet = new Triplet( aJCas, sentence.getBegin(), sentence.getEnd(), sentence.getCoveredText() );
            triplet.setSubject( entity.getName() );
            Entity dobjEntity = Utils.findEntityType( "DOBJ", entity.getParent().getChildren() );
            if ( dobjEntity != null ) {
                triplet.setObject( dobjEntity.getName() );
                triplet.setRelation( entity.getParent().getName().getCoveredText() );
            } else if ( entity.getParent().getName().getPos().getPosValue().equals( "NN" ) ){
                triplet.setObject( entity.getParent().getName() );
            }
            Entity copEntity = Utils.findEntityType( "COP", entity.getParent().getChildren() );
            Entity negEntity = Utils.findEntityType( "NEG", entity.getParent().getChildren() );
            if ( triplet.getRelation() == null && ( copEntity != null || negEntity != null ) ) {
                String relation = "";
                if ( copEntity != null ) {
                    relation = copEntity.getName().getCoveredText();
                }
                if ( negEntity != null ) {
                    relation += ( relation.isEmpty() ? negEntity.getName().getCoveredText() : ("_" + negEntity.getName().getCoveredText() ) );
                }
                if ( !triplet.isObject() && entity.getParent().getName().getPos().getPosValue().equals( "VB" ) ) {
                    relation += "_" + entity.getParent().getName().getCoveredText();
                }
                triplet.setRelation( relation );
            }
        }
        if ( triplet != null ) {
            score.setBegin( sentence.getBegin() );
            score.setEnd( sentence.getEnd() );
            triplet.setScore( score );
        }
        return triplet;
    }

    protected TripletScore parseForScore(JCas aJCas, List<Entity> entitiesIndex )
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

    protected void resolvePosCollisions( Triplet triplet ) {
        if ( triplet.getSubjectCoref() != null && !Utils.isNoun( triplet.getSubjectCoref() ) ) {
            triplet.setSubjectCoref( null );
        }
        if ( triplet.getObjectCoref() != null && !Utils.isNoun( triplet.getObjectCoref() ) ) {
            triplet.setObjectCoref( null );
        }
        if ( triplet.isObject() && !Utils.isNoun( triplet.getObject() ) ) {
            triplet.setObject( null );
        }
        if ( triplet.isSubject() && !Utils.isNoun( triplet.getSubject() ) ) {
            triplet.setSubject( null );
        }
    }
}
