package com.ontological.retrieval.DataTypes;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

/**
 * @todo
 *      In case DKpro transfer edu.stanford.nlp.trees.DependencyScoring data-type,
 *      need to add this factor to calculate score of a triplet.
 */

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov@yandex.ru
 */
public class TripletScore extends Annotation
{
    public static final int MAXIMUM_AUTHORITY_BOUND = 50;

    private static final int SENTENCE_SIZE_LOW_BOUND = 7;
    private static final int SENTENCE_SIZE_MIDDLE_BOUND = 13;
    private static final int SENTENCE_SIZE_HIGHEST_BOUND = 25;

    public final static int typeIndexID = JCasRegistry.register( TripletScore.class );

    public final static int type = typeIndexID;

    @Override
    public int getTypeIndexID() {
        return typeIndexID;
    }

    private int m_MainPointsCount = 0;

    private double m_Score = 1.0;

    protected TripletScore() {}

    public TripletScore( int addr, TOP_Type type ) {
        super( addr, type );
    }

    public TripletScore( JCas jCas, int sentenceSize, int undeterminedRelationsCount, int mainPointsCount )
    {
        super( jCas );
        m_MainPointsCount = mainPointsCount;
        m_Score = calculateScore( sentenceSize, undeterminedRelationsCount, mainPointsCount );
    }

    public int getMainPointsCount() {
        return m_MainPointsCount;
    }

    public double getScoreValue() {
        return m_Score;
    }

    private double calculateScore( int sentenceSize, int undeterminedRelationsCount, int mainPointsCount ) {
        double score;
        //
        // Calculate the impact by the sentence size ( count of syntactic relations, NOT the node/words count )
        if ( sentenceSize <= SENTENCE_SIZE_LOW_BOUND ) {
            score = 1.0;
        } else if ( sentenceSize > SENTENCE_SIZE_LOW_BOUND && SENTENCE_SIZE_LOW_BOUND <= SENTENCE_SIZE_MIDDLE_BOUND ) {
            score = 2.0;
        } else if ( sentenceSize > SENTENCE_SIZE_MIDDLE_BOUND && sentenceSize <= SENTENCE_SIZE_HIGHEST_BOUND ) {
            score = 4.0;
        } else { // > SENTENCE_SIZE_HIGHEST_BOUND
            //
            // The huge size of sentence. It is a headache for parsing such semantic.
            score = 8.0;
        }

        double relationViolationDeg = 1;
        //
        // relationViolationDeg = UpperRound{ 100 * | ln( u / sz ) | ^ (-e) }; u != 0;
        if ( undeterminedRelationsCount > 0 ) {
            relationViolationDeg = 100 * Math.pow( Math.abs( Math.log( (double) undeterminedRelationsCount / (double) sentenceSize )), -Math.E );
            if ( relationViolationDeg < 1.0 ) {
                relationViolationDeg = 1;
            }
        }
        return score * mainPointsCount * relationViolationDeg;
    }
}