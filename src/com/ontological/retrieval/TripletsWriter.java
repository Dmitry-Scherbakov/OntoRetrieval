package com.ontological.retrieval;

import com.ontological.retrieval.Utilities.Entity;
import com.ontological.retrieval.Utilities.Neo4j.GenerateGraph;
import com.ontological.retrieval.Utilities.Triplet;
import com.ontological.retrieval.Utilities.Utils;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class TripletsWriter extends JCasConsumer_ImplBase
{
    private static final String FILE_ENCODING = "UTF-8";
    private static final String DEFAULT_TRIPLETS_PATH = "results/triplets.neo4j";
    private static final String DEFAULT_SENTENCE_SEMANTIC_PATH = "results/sentence.semantic.log";

    public static final String PARAM_SINGLETON = "singleton";
    @ConfigurationParameter(name = PARAM_SINGLETON, defaultValue = "true", mandatory = true)
    private boolean singleton;

    @Override
    public void process( JCas aJCas ) throws AnalysisEngineProcessException {
        System.out.println( "Started TripletsWriter" );
        try {
            writeTripletsGraph( aJCas );
            writeSentenceSemantic( aJCas );
        } catch ( UnsupportedEncodingException ex ) {
            ex.printStackTrace();
        } catch ( FileNotFoundException ex ) {
            ex.printStackTrace();
        }
    }

    private void writeTripletsGraph( JCas aJCas ) throws FileNotFoundException, UnsupportedEncodingException {
        List<Triplet> triplets = new ArrayList<>();
        for ( Sentence sentence : JCasUtil.select( aJCas, Sentence.class ) ) {
            for ( Triplet tr : JCasUtil.selectCovered( aJCas, Triplet.class, sentence ) ) {
                triplets.add( tr );
            }
        }
        if ( triplets.size() > 0 ) {
            try ( PrintWriter writer = new PrintWriter( DEFAULT_TRIPLETS_PATH, FILE_ENCODING ) ) {
                writer.write( GenerateGraph.generateTripletsGraph( triplets ) );
            }
        }
    }

    private void writeSentenceSemantic( JCas aJCas ) throws FileNotFoundException, UnsupportedEncodingException  {
        List<String> out = new ArrayList<>();
        for ( Sentence sentence : JCasUtil.select( aJCas, Sentence.class ) ) {
            List<Entity> entitiesIndex = Utils.parseForEntities(aJCas, sentence);
            if ( entitiesIndex.size() > 0 ) {
                out.add( "# " + sentence.getCoveredText() );
                out.add( GenerateGraph.generateSentenceGraph( entitiesIndex ) );
            }
        }
        if ( out.size() > 0 ) {
            try ( PrintWriter writer = new PrintWriter( DEFAULT_SENTENCE_SEMANTIC_PATH, FILE_ENCODING ) ) {
                writer.write( Utils.listOfStringToString( out, "\n" ) );
            }
        }
    }
}
