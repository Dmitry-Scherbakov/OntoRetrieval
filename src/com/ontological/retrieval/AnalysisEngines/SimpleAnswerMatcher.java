package com.ontological.retrieval.AnalysisEngines;

import com.ontological.retrieval.Utilities.Models;
import com.ontological.retrieval.DataTypes.Question;
import com.ontological.retrieval.DataTypes.Triplet;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class SimpleAnswerMatcher extends JCasConsumer_ImplBase
{
    public static final String PARAM_SINGLETON = "singleton";
    @ConfigurationParameter(name = PARAM_SINGLETON, defaultValue = "true", mandatory = true)
    private boolean singleton;

    public static final String PARAM_RELEVANT_CANDIDATE_COUNT = "relevantCount";
    @ConfigurationParameter(name = PARAM_RELEVANT_CANDIDATE_COUNT, defaultValue = "3", mandatory = false)
    protected int relevantCount;

    @Override
    public void process( JCas aJCas ) throws AnalysisEngineProcessException {
        System.out.println( "Started SimpleAnswerMatcher" );
        String language = aJCas.getDocumentLanguage();
        if ( language.equals( "en" ) ) {
            matchAnswer( aJCas );
        } else {
            System.out.println( "SimpleAnswerMatcher: currently, the only english language is supported." );
            throw new AnalysisEngineProcessException();
        }
    }

    private void matchAnswer( JCas aJCas ) {
        List<Triplet> answers = new ArrayList<>();
        if ( QuestionAnalyzer.getInstance() != null ) {
            List<Triplet> triplets = new ArrayList<>();
            for ( Sentence sentence : JCasUtil.select( aJCas, Sentence.class ) ) {
                for ( Triplet tr : JCasUtil.selectCovered( aJCas, Triplet.class, sentence ) ) {
                    triplets.add( tr );
                }
            }
            for ( Question question : QuestionAnalyzer.getInstance().getQuestions() ) {
                question.print();
                int count = 0;
                if ( Models.PR_WHO.equals( question.getSubject().toUpperCase() ) ) {
                    for ( Triplet candidate : triplets ) {
                        if ( !candidate.isValid() ) {
                            continue;
                        }
                        candidate.print();
                        String candidateObject = candidate.getObject().getField().getLemma().getValue();
                        if (    candidate.getRelation().equals( question.getRelation() ) &&
                                candidateObject.equals( question.getObject() ) ) {
                            answers.add( candidate );
                            ++count;
                        }
                        if ( count == relevantCount ) {
                            //
                            // We collected enough potential answers.
                            break;
                        }
                    }
                } else if ( Models.PR_WHAT.equals( question.getSubject().toUpperCase() ) ) {
                    for ( Triplet candidate : triplets ) {
                        if ( !candidate.isValid() ) {
                            continue;
                        }
                        candidate.print();
                        String candidateSubject = candidate.getSubject().getField().getLemma().getValue();
                        if (    candidate.getRelation().equals( question.getRelation() ) &&
                                candidateSubject.equals( question.getObject() ) ) {
                            answers.add( candidate );
                            ++count;
                        }
                        if ( count == relevantCount ) {
                            //
                            // We collected enough potential answers.
                            break;
                        }
                    }
                }
            }
        }
        System.out.println( "Answers: =>");
        for ( Triplet ansTriplet : answers ) {
            System.out.println( ansTriplet.getContext() );
            ansTriplet.print();
        }
        System.out.println( "<=");
    }
}
