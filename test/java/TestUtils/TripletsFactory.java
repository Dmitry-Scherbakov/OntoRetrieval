package TestUtils;

import com.ontological.retrieval.DataTypes.Triplet;
import com.ontological.retrieval.DataTypes.TripletField;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class TripletsFactory extends JCasConsumer_ImplBase
{
    public static final String PARAM_SINGLETON = "singleton";
    @ConfigurationParameter(name = PARAM_SINGLETON, defaultValue = "true", mandatory = true)
    private boolean singleton;

    private static HashMap<Integer, ArrayList<TestTriplet>> m_ParsedTriplets = new HashMap<>();

    public static HashMap<Integer, ArrayList<TestTriplet>> getParsedTriplets() {
        return m_ParsedTriplets;
    }

    @Override
    public void process( JCas aJCas ) throws AnalysisEngineProcessException {
        int sentenceNumber = 0;
        for ( Sentence sentence : JCasUtil.select( aJCas, Sentence.class ) ) {
            ++sentenceNumber;
            ArrayList<TestTriplet> triplets = new ArrayList<>();
            for ( Triplet tr : JCasUtil.selectCovered( aJCas, Triplet.class, sentence ) ) {
                triplets.add( convertTriplet( tr ) );
            }
            m_ParsedTriplets.put( sentenceNumber, triplets );
        }
    }

    private TestTriplet convertTriplet( Triplet tr ) {
        String subject = tr.getSubject() == null ? "" : tr.getSubject().getField().getLemma().getValue();
        String object = tr.getObject() == null ? "" : tr.getObject().getField().getLemma().getValue();
        String definition = tr.getDefinition() == null ? "" : tr.getDefinition().getCoveredText();
        String subjectFormatedNEs = tr.getSubject() == null ? "" : Triplet.fieldAttributesToString( tr.getSubject() );
        String objectFormatedNEs = tr.getObject() == null ? "" : Triplet.fieldAttributesToString( tr.getObject() );

        TestTriplet convertedTriplet = new TestTriplet( subject, object, tr.formattedRelations(), tr.getContext().getCoveredText(),
                definition, subjectFormatedNEs, objectFormatedNEs,
                tr.getSubject() == null ? false : tr.getSubject().isCoreference(), tr.getObject() == null ? false : tr.getObject().isCoreference() );

        if ( tr.getDefinition() != null && tr.getDefinition().getNamedEntities() != null ) {
            for ( NamedEntity nEn : tr.getDefinition().getNamedEntities() ) {
                convertedTriplet.addDefinitionNE( new TestTriplet.NamedEntityShort( nEn.getType().getShortName(), nEn.getCoveredText() ) );
            }
        }

        return convertedTriplet;
    }
}
