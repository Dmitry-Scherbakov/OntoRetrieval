package com.ontological.retrieval;

import com.ontological.retrieval.Utilities.*;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class QuestionAnalyzer extends AbstractTripletsAnalyzer
{
    private static QuestionAnalyzer self = null;

    private List<Question> m_QuestionTriplet = new ArrayList<>();

    public static final String PARAM_SINGLETON = "singleton";
    @ConfigurationParameter(name = PARAM_SINGLETON, defaultValue = "true", mandatory = true)
    private boolean singleton;

    static QuestionAnalyzer getInstance() {
        //
        // @note We must not instantiate an object of this class here. It must be done
        //       by UIMA. This pointer is only for our internal usage, mostly for
        //       SimpleAnswerMatcher class.
        //
        return self;
    }
    @Override
    public void process( JCas aJCas ) throws AnalysisEngineProcessException {
        System.out.println( "Started QuestionAnalyzer" );
        if ( self == null ) {
            self = this;
        }
        String language = aJCas.getDocumentLanguage();
        if ( language.equals( "en" ) ) {
            analyzeQuestion( aJCas );
        } else {
            System.out.println( "QuestionAnalyzer: currently, the only english language is supported." );
            throw new AnalysisEngineProcessException();
        }
    }

    public void analyzeQuestion( JCas aJCas ) {
        m_QuestionTriplet.clear();
        List<Triplet> triplets = new ArrayList<>();
        for ( Sentence sentence : JCasUtil.select( aJCas, Sentence.class ) ) {
            List<Entity> entitiesIndex = Utils.parseForEntities( aJCas, sentence );
            TripletScore forwardScore = parseForScore( aJCas, entitiesIndex );
            Triplet triplet = null;
            for ( Entity en : entitiesIndex ) {
                triplet = parseForTriplet( aJCas, sentence, en, forwardScore );

                if ( triplet != null ) {
                    //
                    // @todo
                    //      Implement 'Local Coreference Resolver, for questions which have
                    //      structure like that: "Who invented 'some_thing' and used 'it/them/...'
                    //      for 'something'
                    //
//                    triplet.printShortCoref();
//                    resolvePosCollisions( triplet );
                    if ( triplet.isValid() ) {
                        triplets.add( triplet );
                    } else {
//                        triplet.printShortCoref();
                        System.out.println( "QuestionAnalyzer: invalid tripled has been parsed." );
                    }
                    break;
                }

            }
        }
        if ( triplets.size() == 0 ) {
            System.out.println( "QuestionAnalyzer: none questions has been parsed." );
        } else {
            for ( Triplet tr : triplets ) {
                tr.addToIndexes( aJCas );
                m_QuestionTriplet.add( new Question( tr ) );
            }
        }
    }

    public List<Question> getQuestions() {
        return m_QuestionTriplet;
    }
}
