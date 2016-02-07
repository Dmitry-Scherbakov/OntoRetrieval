package com.ontological.retrieval.Utilities;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov@yandex.ru
 */
public class TripletScore extends Annotation
{
    public static final int MAXIMUM_AUTHORITY_BOUND = 40;

    private static final int SENTENCE_SIZE_LOW_BOUND = 5;
    private static final int SENTENCE_SIZE_MIDDLE_BOUND = 9;
    private static final int SENTENCE_SIZE_HIGHEST_BOUND = 18;

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
        //
        // Calculate the impact of sentence difficulty based on the subjects/key_points/main_points
        // within the scope of this sentence.
        score = score * mainPointsCount;
        //
        // Calculate the impact of undetermined relations between the nodes/words.
        score = score * ( ( 1 + ( undeterminedRelationsCount / sentenceSize ) ) * 10 );
        return score;
    }
}