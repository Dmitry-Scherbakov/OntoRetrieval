package com.ontological.retrieval;

import com.ontological.retrieval.Utilities.*;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @note   It is strongly recommended to use this extractor with the chain of StandfordParser
 *         which need to parametrize by StanfordParser.DependenciesMode.TREE.
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class TripletsExtractor extends AbstractTripletsAnalyzer
{
    public static enum TripletValidationFactor
    {
        /**
         * Save all extracted triplets. This case could be used for retrieval extended
         * information from the parsed data. Some of triplets could be not completed, e.g.
         * which have not some of mandatory fields (subject/relation/object), or could be
         * extracted from very difficult semantic tree, as a result such triplets will have
         * very low 'structured authority' (very big TripletScore.value).
         */
        ALL,
        /**
         * This case will save almost all triplets except one type: if a triplet is not valid
         * (has not at least one of mandatory field: subject or relation or object), it will
         * not be saved. This case is also will save the triplets which have very low 'structured
         * authority' as well as TripletValidationFactor.ALL.
         */
        ONLY_VALID,
        /**
         * This case will save all triplets, which has TripletsScore.value less then
         * TripletScore.MAXIMUM_AUTHORITY_BOUND threshold. So, invalid triplets (which could not
         * have some of mandatory fields) are also will be saved in case of TripletsScore.value
         * will be less then TripletScore.MAXIMUM_AUTHORITY_BOUND threshold as well.
         */
        MAXIMUM_AUTHORITY,
        /**
         * This case will save the most 'structured authority' and well formed triplets. It means
         * the only triplets which have TripletsScore.value less then TripletScore.MAXIMUM_AUTHORITY_BOUND
         * threshold AND 'valid' formed (have all mandatory fields: subject and relation and object)
         * will be saved.
         */
        MAXIMUM_AUTHORITY_AND_VALID
    }

    public static final String PARAM_FACTOR = "factor";
    @ConfigurationParameter(name = PARAM_FACTOR, mandatory = false, defaultValue = "ALL")
    protected TripletValidationFactor factor;

    public static final String PARAM_SINGLETON = "singleton";
    @ConfigurationParameter(name = PARAM_SINGLETON, defaultValue = "true", mandatory = true)
    private boolean singleton;

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        System.out.println( "Started TripletsExtractor annotator." );
        String language = aJCas.getDocumentLanguage();
        if ( language.equals( "en" ) ) {
            handleStandfordTree( aJCas );
        } else {
            System.out.println( "TripletsExtractor: currently, the only english language is supported." );
            throw new AnalysisEngineProcessException();
        }
    }

    private void handleStandfordTree( JCas aJCas ) throws AnalysisEngineProcessException
    {
        List<Triplet> triplets = new ArrayList<>();
        HashMap<Integer,CoreferenceLink> corefLinks = new HashMap<>();
        Sentence prevSentence = null;
        Triplet prevTr_InCurrSent = null;
        for ( Sentence sentence : JCasUtil.select( aJCas, Sentence.class ) ) {
            List<Entity> entitiesIndex = Utils.parseForEntities( aJCas, sentence );
            cacheCoreferenceLink( aJCas, sentence, corefLinks );
            TripletScore forwardScore = parseForScore( aJCas, entitiesIndex );
            Triplet triplet = null;
            for ( Entity en : entitiesIndex ) {
                triplet = parseForTriplet( aJCas, sentence, en, forwardScore );

                if ( triplet != null ) {
                    //
                    // @fixme
                    //      There is a bug: if triplets were filtered by `isTripletCorrect( )` condition
                    //      (based on the applied TripletValidationFactor) these triples wont be indexed
                    //      by UIMA. As a result `resolveCoreference` wont extract triples/dirty from
                    //      `prevSentence`. So, as a temporary solution use TripletValidationFactor.ALL.
                    //
                    resolveCoreference( aJCas, prevTr_InCurrSent, triplet, corefLinks, prevSentence );
                    resolvePosCollisions( triplet );
                    triplets.add( triplet );
                    prevTr_InCurrSent = triplet;
                    triplets.add( triplet );
                    if ( isTripletCorrect( triplet ) ) {
                        triplet.addToIndexes( aJCas );
                    }
                    // There could be another sentence subject/main_point
                    triplet = null;
                }

            }
            prevSentence = sentence;
            prevTr_InCurrSent = null;
        }
    }

    private boolean isTripletCorrect( Triplet triplet )
    {
        if ( triplet == null || !triplet.isSubject() ) {
            return false;
        }
        switch ( factor )
        {
            case ALL:
                return true;
            case ONLY_VALID:
                return triplet.isValid();
            case MAXIMUM_AUTHORITY:
                return triplet.getScore().getScoreValue() <= TripletScore.MAXIMUM_AUTHORITY_BOUND;
            case MAXIMUM_AUTHORITY_AND_VALID:
                return triplet.getScore().getScoreValue() <= TripletScore.MAXIMUM_AUTHORITY_BOUND && triplet.isValid();
            default:
                return false;
        }
    }

    private void cacheCoreferenceLink( JCas aJCas, Sentence sentence, HashMap<Integer,CoreferenceLink> corefLinks )
    {
        for ( CoreferenceLink link : JCasUtil.selectCovered( aJCas, CoreferenceLink.class, sentence ) ) {
//            Utils.debugCoreference( sentence, link );
            if ( link.getReferenceType().equals( "PRONOMINAL" ) ) {
                if ( link.getNext() != null && !corefLinks.containsKey( link.getNext().getBegin() ) ) {
                    corefLinks.put( link.getNext().getBegin(), link );
                }
            } // another is 'NOMINAL'
        }
    }

    private void resolveCoreference( JCas aJCas, Triplet prevTr_InCurrSent, Triplet triplet, HashMap<Integer,CoreferenceLink> corefLinks, Sentence prevSentence )
    {
        if ( triplet.isSubject() && corefLinks.containsKey( triplet.getSubject().getBegin() ) ) {
            CoreferenceLink corefEn = corefLinks.get( triplet.getSubject().getBegin() );
            //
            // @todo
            //          Resolve this case when the date for testing will be available. Looks like
            //          it could be implemented identical as in the case below for Triplet.Object.
        }

        if ( triplet.isObject() && corefLinks.containsKey( triplet.getObject().getBegin() ) ) {
            CoreferenceLink corefEn = corefLinks.get( triplet.getObject().getBegin() );
            Triplet corefDonor = Utils.findTriplet( aJCas, corefEn, prevSentence );
            if ( corefDonor != null ) {
                triplet.setObjectCoref( corefDonor.getSubjectCoref() );
            }
        }

        if ( triplet.isSubject() && triplet.getSubjectCoref() == null && Models.isPronoun( triplet.getSubject().getCoveredText() ) ) {
            if ( Models.isPositionedPronoun( triplet.getSubject().getCoveredText() ) ) {
                //
                // Some pronouns should searched in a current sentence.
                // The example of such pronoun is 'that'.
                //
                if ( prevTr_InCurrSent != null ) {
                    if ( prevTr_InCurrSent.getSubjectCoref() != null ) {
                        triplet.setSubjectCoref( prevTr_InCurrSent.getSubjectCoref() );
                    } else {
                        triplet.setSubjectCoref( prevTr_InCurrSent.getSubject() );
                    }
                }
            } else if ( prevSentence != null ) {
                List<Triplet> tripletsList = JCasUtil.selectCovered( aJCas, Triplet.class, prevSentence );
                if ( tripletsList != null && tripletsList.size() > 0 ) {
                    Triplet donorTriplet = tripletsList.get( tripletsList.size() - 1 );
                    if ( donorTriplet.getScore().getMainPointsCount() <= 2 ) {
                        if ( donorTriplet.getSubjectCoref() != null ) {
                            triplet.setSubjectCoref( donorTriplet.getSubjectCoref() );
                        } else {
                            triplet.setSubjectCoref( donorTriplet.getSubject() );
                        }
                    }
                }
            }
        }
    }
}
