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

    private static String DOCUMENT_PATH = "docs/walt-disney-ru.txt";

    public static void main(String[] args) throws Exception {

        System.out.println( "Start" );

        boolean isRussian = false;
        if ( isRussian ) {
            parseRussian( "docs/walt-disney-ru.txt" );
        } else { // english
            parseEnglish( "docs/simple-pen.txt" );
        }
        System.out.println( "Finished" );
    }

    static private void parseEnglish( String path ) {
        try {
            CollectionReaderDescription readerDescription = createReaderDescription( TextReader.class,
                    TextReader.PARAM_SOURCE_LOCATION, path,
                    TextReader.PARAM_LANGUAGE, "en" );

            runPipeline( readerDescription,
                    //createEngineDescription(LanguageToolLemmatizer.class),
                    createEngineDescription( StanfordSegmenter.class ),
                    createEngineDescription( StanfordPosTagger.class ),
                    //createEngineDescription(TreeTaggerPosTagger.class),
                    createEngineDescription( StanfordParser.class, StanfordParser.PARAM_MODE, StanfordParser.DependenciesMode.TREE ),
                    createEngineDescription( StanfordCoreferenceResolver.class ),
                    createEngineDescription( StanfordNamedEntityRecognizer.class ),
                    createEngineDescription( TripletsExtractor.class ) );
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
            System.out.print( "[ EN ] UIMAException: " + ex.toString() );
            ex.printStackTrace();
        } catch ( IOException ex ) {
            System.out.print( "[ EN ] IOException: " + ex.toString() );
            ex.printStackTrace();
        }
    }
}
