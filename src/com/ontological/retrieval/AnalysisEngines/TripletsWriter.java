package com.ontological.retrieval.AnalysisEngines;

import com.ontological.retrieval.DataTypes.Entity;
import com.ontological.retrieval.Utilities.Neo4j.GenerateGraph;
import com.ontological.retrieval.DataTypes.Triplet;
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
 * @brief  This class implements a set of algorithms which purpose is to write resulted extracted
 *         data to a file storage. Now, there is implemented:
 *
 *         - writing sentence-semantic-structure (sentence graph), this structure is coded by a
 *         script for Neo4j DB, so, to visualize this structure use Neo4j DB and his web-emulator;
 *         - writing the full graph of all extracted triplets, this structure is also coded by a
 *         script for Neo4j DB;
 *
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class TripletsWriter extends JCasConsumer_ImplBase
{
    private static final String FILE_ENCODING = "UTF-8";
    public static final String DEFAULT_TRIPLETS_PATH = "results/triplets.log";
    public static final String DEFAULT_TRIPLETS_MAP_PATH = "results/triplets-map.neo4j";
    public static final String DEFAULT_SENTENCE_SEMANTIC_PATH = "results/sentence.semantic.log";

    public static final String PARAM_SINGLETON = "singleton";
    @ConfigurationParameter(name = PARAM_SINGLETON, defaultValue = "true", mandatory = true)
    private boolean singleton;

    public static final String PARAM_TRIPLETS_PATH = "tripletsPath";
    @ConfigurationParameter(name = PARAM_TRIPLETS_PATH, mandatory = false, defaultValue = DEFAULT_TRIPLETS_PATH)
    protected String tripletsPath;

    public static final String PARAM_TRIPLETS_MAP_PATH = "tripletsMapPath";
    @ConfigurationParameter(name = PARAM_TRIPLETS_MAP_PATH, mandatory = false, defaultValue = DEFAULT_TRIPLETS_MAP_PATH)
    protected String tripletsMapPath;

    public static final String PARAM_SENTENCE_SEMANTIC_PATH = "sentenceSemanticPath";
    @ConfigurationParameter(name = PARAM_SENTENCE_SEMANTIC_PATH, mandatory = false, defaultValue = DEFAULT_SENTENCE_SEMANTIC_PATH)
    protected String sentenceSemanticPath;

    private long m_TripletsCount;
    private long m_SentenceCount;
    private long m_TokensCount;

    @Override
    public void process( JCas aJCas ) throws AnalysisEngineProcessException {
        System.out.println( "Started TripletsWriter" );
        m_TripletsCount = 0;
        m_SentenceCount = 0;
        m_TokensCount = 0;
        try {
            writeTripletsGraph( aJCas );
            writeSentenceSemantic( aJCas );
            writeTriplets( aJCas );
//            printTriplets( aJCas );
            printStatistic( aJCas );
        } catch ( UnsupportedEncodingException ex ) {
            ex.printStackTrace();
        } catch ( FileNotFoundException ex ) {
            ex.printStackTrace();
        }
    }

    private void writeTripletsGraph( JCas aJCas ) throws FileNotFoundException, UnsupportedEncodingException {
        List<Triplet> triplets = new ArrayList<>();
        for ( Sentence sentence : JCasUtil.select( aJCas, Sentence.class ) ) {
            ++m_SentenceCount;
            m_TokensCount += Utils.tokensCount( aJCas, sentence );
            for ( Triplet tr : JCasUtil.selectCovered( aJCas, Triplet.class, sentence ) ) {
                triplets.add( tr );
            }
        }
        m_TripletsCount = triplets.size();
        if ( m_TripletsCount > 0 ) {
            try ( PrintWriter writer = new PrintWriter( tripletsMapPath, FILE_ENCODING ) ) {
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
            try ( PrintWriter writer = new PrintWriter( sentenceSemanticPath, FILE_ENCODING ) ) {
                writer.write( Utils.listOfStringToString( out, "\n" ) );
            }
        }
    }

    private void writeTriplets( JCas aJCas ) throws FileNotFoundException, UnsupportedEncodingException  {
        List<Triplet> triplets = new ArrayList<>();
        for ( Sentence sentence : JCasUtil.select( aJCas, Sentence.class ) ) {
            for ( Triplet tr : JCasUtil.selectCovered( aJCas, Triplet.class, sentence ) ) {
                triplets.add( tr );
            }
        }
        if ( triplets.size() > 0 ) {
            try ( PrintWriter writer = new PrintWriter( tripletsPath, FILE_ENCODING ) ) {
                for ( Triplet triplet : triplets ) {
                    writer.write( triplet.toString() + '\n' );
                }
            }
        }
    }

    private void printTriplets( JCas aJCas ) {
        for ( Sentence sentence : JCasUtil.select( aJCas, Sentence.class ) ) {
            for ( Triplet tr : JCasUtil.selectCovered( aJCas, Triplet.class, sentence ) ) {
                System.out.println( tr.toString() + '\n' );
            }
        }
    }

    private void printStatistic( JCas aJCas ) {
        System.out.printf( "Document length [%d], sentence count [%d], tokens count [%d]. Extracted triplets count [%d].\n",
                aJCas.getDocumentText().length(), m_SentenceCount, m_TokensCount, m_TripletsCount );
    }
}
