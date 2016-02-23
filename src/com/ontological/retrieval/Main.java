package com.ontological.retrieval;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.*;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosTagger;
import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReaderDescription;

import java.io.IOException;

public class Main {

//    private static String DOCUMENT_PATH_ENG = "samples/en/encyclopedia/Angora-Goats.txt";
    private static String DOCUMENT_PATH_ENG = "samples/en/simple-pen.txt";
    private static String DOCUMENT_PATH_RUS = "samples/ru/walt-disney-ru.txt";

    public static void main( String[] args ) throws Exception {
        boolean isRussian = false;
        if ( isRussian ) {
            parseRussian( DOCUMENT_PATH_RUS );
        } else { // english
            parseEnglish( DOCUMENT_PATH_ENG );
        }
        System.out.println( "Finished" );
    }

    static private void parseEnglish( String path ) {
        try {
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
                    createEngineDescription( TripletsExtractor.class, TripletsExtractor.PARAM_FACTOR, TripletsExtractor.TripletValidationFactor.ONLY_VALID ),
                    createEngineDescription( TripletsWriter.class ) );
        } catch ( UIMAException ex ) {
            System.out.print("[ EN ] UIMAException: " + ex.toString());
            ex.printStackTrace();
        } catch ( IOException ex ) {
            System.out.print("[ EN ] IOException: " + ex.toString());
            ex.printStackTrace();
        }
    }

    static private void parseRussian( String path ) {
        try {
            CollectionReaderDescription readerDescription = createReaderDescription( TextReader.class,
                    TextReader.PARAM_SOURCE_LOCATION, path,
                    TextReader.PARAM_LANGUAGE, "ru" );

            runPipeline( readerDescription,
                    createEngineDescription( BreakIteratorSegmenter.class ),
                    createEngineDescription( TreeTaggerPosTagger.class ) );
        } catch ( UIMAException ex ) {
            System.out.print( "[ RUS ] UIMAException: " + ex.toString() );
            ex.printStackTrace();
        } catch ( IOException ex ) {
            System.out.print( "[ RUS ] IOException: " + ex.toString() );
            ex.printStackTrace();
        }
    }
}
