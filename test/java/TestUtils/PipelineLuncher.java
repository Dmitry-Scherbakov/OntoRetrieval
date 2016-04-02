package TestUtils;

import com.ontological.retrieval.AnalysisEngines.TripletsExtractor;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.*;
import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReaderDescription;

import java.io.IOException;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class PipelineLuncher
{
    private static PipelineLuncher pipeline;
    private PipelineLuncher() throws UIMAException, IOException  {
        CollectionReaderDescription readerDescription = createReaderDescription( TextReader.class,
                TextReader.PARAM_SOURCE_LOCATION, "samples/en/encyclopedia/Angora-Goats_small.txt",
                TextReader.PARAM_LANGUAGE, "en" );

        runPipeline( readerDescription,
                createEngineDescription( StanfordSegmenter.class ),
                createEngineDescription( StanfordPosTagger.class ),
                createEngineDescription( StanfordLemmatizer.class ),
                createEngineDescription( StanfordParser.class, StanfordParser.PARAM_MODE, StanfordParser.DependenciesMode.TREE ),
                createEngineDescription( StanfordCoreferenceResolver.class ),
                createEngineDescription( StanfordNamedEntityRecognizer.class ),
                createEngineDescription( TripletsExtractor.class, TripletsExtractor.PARAM_FACTOR, TripletsExtractor.TripletValidationFactor.ALL ),
                createEngineDescription( TripletsFactory.class ) );
    }

    public static PipelineLuncher getInstance() throws UIMAException, IOException {
        if ( pipeline == null ) {
            pipeline = new PipelineLuncher();
        }
        return pipeline;
    }
}
