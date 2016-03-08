package com.ontological.retrieval;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import com.ontological.retrieval.AnalysisEngines.QuestionAnalyzer;
import com.ontological.retrieval.AnalysisEngines.SimpleAnswerMatcher;
import com.ontological.retrieval.AnalysisEngines.TripletsExtractor;
import com.ontological.retrieval.AnalysisEngines.TripletsWriter;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.posfilter.PosFilter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.*;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosTagger;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;

import java.io.IOException;

public class Main {

//    private static String DOCUMENT_PATH_ENG = "samples/en/encyclopedia/Angora-Goats.txt";
    private static String DOCUMENT_PATH_ENG = "samples/en/encyclopedia/Angora-Goats_small.txt";
//    private static String DOCUMENT_PATH_ENG = "samples/tmp/ibm_small.txt";
//    private static String DOCUMENT_PATH_ENG = "samples/en/simple-pen.txt";
//    private static String QUESTIONS_PATH_ENG = "samples/en/questions/simple-pen-q.txt";
//    private static String DOCUMENT_PATH_ENG = "samples/en/complicated-pen.txt";
    private static String DOCUMENT_PATH_RUS = "samples/ru/walt-disney-ru.txt";

    public static void main( String[] args ) throws Exception {
        boolean isRussian = false;
        try {
            if ( isRussian ) {
                parseRussianCorpus( DOCUMENT_PATH_RUS );
            } else { // english
//                parseEnglishQuestions( QUESTIONS_PATH_ENG );
                parseEnglishCorpus( DOCUMENT_PATH_ENG );
            }
        } catch ( UIMAException ex ) {
            System.out.print("UIMAException: " + ex.toString());
            ex.printStackTrace();
        } catch ( IOException ex ) {
            System.out.print("IOException: " + ex.toString());
            ex.printStackTrace();
        }
        System.out.println( "Finished" );
    }

    static private void parseEnglishQuestions( String path ) throws UIMAException, IOException {
        CollectionReaderDescription readerDescription = createReaderDescription( TextReader.class,
                TextReader.PARAM_SOURCE_LOCATION, path,
                TextReader.PARAM_LANGUAGE, "en" );

        runPipeline( readerDescription,
                createEngineDescription( StanfordSegmenter.class ),
                createEngineDescription( StanfordPosTagger.class ),
                createEngineDescription( StanfordLemmatizer.class ),
                createEngineDescription( StanfordParser.class, StanfordParser.PARAM_MODE, StanfordParser.DependenciesMode.TREE ),
                createEngineDescription( StanfordCoreferenceResolver.class ),
                createEngineDescription( StanfordNamedEntityRecognizer.class ),
                createEngineDescription( QuestionAnalyzer.class ),
                createEngineDescription( TripletsWriter.class,
                        TripletsWriter.PARAM_TRIPLET_PATH, "results/q_triplets.neo4j",
                        TripletsWriter.PARAM_SENTENCE_SEMANTIC_PATH, "results/q_sentence.semantic.log" ) );
    }

    static private void parseEnglishCorpus( String path ) throws UIMAException, IOException {
        CollectionReaderDescription readerDescription = createReaderDescription( TextReader.class,
                TextReader.PARAM_SOURCE_LOCATION, path,
                TextReader.PARAM_LANGUAGE, "en" );

        AnalysisEngineDescription filter = createEngineDescription( PosFilter.class );
        runPipeline( readerDescription,
                createEngineDescription( StanfordSegmenter.class ),
                createEngineDescription( StanfordPosTagger.class ),
//                createEngineDescription( filter ),
                createEngineDescription( StanfordLemmatizer.class ),
                createEngineDescription( StanfordParser.class, StanfordParser.PARAM_MODE, StanfordParser.DependenciesMode.TREE ),
                createEngineDescription( StanfordCoreferenceResolver.class ),
                createEngineDescription( StanfordNamedEntityRecognizer.class ),
                createEngineDescription( TripletsExtractor.class, TripletsExtractor.PARAM_FACTOR, TripletsExtractor.TripletValidationFactor.ALL ),
                createEngineDescription( TripletsWriter.class,
                        TripletsWriter.PARAM_TRIPLET_PATH, TripletsWriter.DEFAULT_TRIPLETS_PATH,
                        TripletsWriter.PARAM_SENTENCE_SEMANTIC_PATH, TripletsWriter.DEFAULT_SENTENCE_SEMANTIC_PATH ),
                createEngineDescription( SimpleAnswerMatcher.class ) );
    }

    static private void parseRussianCorpus( String path ) throws UIMAException, IOException {
        CollectionReaderDescription readerDescription = createReaderDescription( TextReader.class,
                TextReader.PARAM_SOURCE_LOCATION, path,
                TextReader.PARAM_LANGUAGE, "ru" );

        runPipeline( readerDescription,
                createEngineDescription( BreakIteratorSegmenter.class ),
                createEngineDescription( TreeTaggerPosTagger.class ) );
    }
}
