package com.ontological.retrieval.AnalysisEngines;

import com.ontological.retrieval.DataTypes.Entity;
import com.ontological.retrieval.DataTypes.Triplet;
import com.ontological.retrieval.DataTypes.TripletField;
import com.ontological.retrieval.DataTypes.TripletScore;
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
            triplet = new Triplet( aJCas, sentence );
            TripletField subjectField = new TripletField( aJCas, entity.getName() );
            Utils.parseProperties( subjectField, entity );
            triplet.setSubject( subjectField );
            Entity innerEntity = Utils.findEntityType( "AUXPASS", entity.getParent().getChildren() );
            if ( innerEntity != null ) {
                triplet.addRelation( innerEntity.getType(), innerEntity.getName() );
            }
            triplet.addRelation( entity.getType(), entity.getParent().getName() );
        } else if( entity.getType().equals( "NSUBJ" ) ) {
            triplet = new Triplet( aJCas, sentence );
            TripletField subjectField = new TripletField( aJCas, entity.getName() );
            Utils.parseProperties( subjectField, entity );
            triplet.setSubject( subjectField );
            Entity dobjEntity = Utils.findEntityType( "DOBJ", entity.getParent().getChildren() );
            if ( dobjEntity != null ) {
                TripletField objectField = new TripletField( aJCas, dobjEntity.getName() );
                Utils.parseProperties( objectField, dobjEntity );
                triplet.setObject( objectField );
                triplet.addRelation( dobjEntity.getType(), entity.getParent().getName() );
            } else if ( Utils.isNoun( entity.getParent() ) || Utils.isAdjective( entity.getParent() ) ) {
                TripletField objectField = new TripletField( aJCas, entity.getParent().getName() );
                Utils.parseProperties( objectField, entity.getParent() );
                triplet.setObject( objectField );
            }
            //
            // fill multiple relations
            Entity copEntity = Utils.findEntityType( "COP", entity.getParent().getChildren() );
            if ( copEntity != null ) {
                triplet.addRelation( copEntity.getType(), copEntity.getName() );
            }
            Entity negEntity = Utils.findEntityType( "NEG", entity.getParent().getChildren() );
            if ( negEntity != null ) {
                triplet.addRelation( negEntity.getType(), negEntity.getName() );
            }
            if ( triplet.getObject() == null && Utils.isVerb( entity.getParent() ) ) {
                //
                // It is a boosting for determination of relation type. Primary it MUST be 'entity.getParent().getType()'
                String relation = ( entity.getParent().getType() == null ? entity.getType() : entity.getParent().getType() );
                triplet.addRelation( relation, entity.getParent().getName() );
            }
        } else if ( entity.getType().equals( "CONJ" ) ) {
            if ( entity.getName().getPos().getPosValue().equals( "VBD" ) || entity.getName().getPos().getPosValue().equals( "VB" ) ) {
                triplet = new Triplet( aJCas, sentence );
                triplet.addRelation( entity.getType(), entity.getName() );
                Entity nsubjEntity = Utils.findEntityType( "NSUBJ", entity.getParent().getChildren() );
                if ( nsubjEntity != null ) {
                    TripletField field = new TripletField( aJCas, nsubjEntity.getName() );
                    Utils.parseProperties( field, nsubjEntity );
                    triplet.setSubject( field );
                }
                Entity dobjEntity = Utils.findEntityType( "DOBJ", entity.getChildren() );
                if ( dobjEntity != null ) {
                    TripletField field = new TripletField( aJCas, dobjEntity.getName() );
                    Utils.parseProperties( field, dobjEntity );
                    triplet.setObject( field );
                }
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
        if ( triplet.getSubject() != null ) {
            triplet.getSubject().resolveCollisions();
            if ( !triplet.getSubject().isValid() ) {
                triplet.setSubject( null );
            }
        }
        // @note
        // Looks like it is not effective to handle'object' field for POS exclusively.
        // This could be Adjective and such case is ok: 'The city is big'.
//        if ( triplet.getObject() != null ) {
//            triplet.getObject().resolveCollisions();
//            if ( !triplet.getObject().isValid() ) {
//                triplet.setObject( null );
//            }
//        }
    }
}
