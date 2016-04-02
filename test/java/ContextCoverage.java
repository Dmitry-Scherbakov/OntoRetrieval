import TestUtils.PipelineLuncher;
import TestUtils.TestTriplet;
import TestUtils.TripletsFactory;
import org.apache.uima.UIMAException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @brief  This class is a set of test to verify coverage of triplets extraction from the
 *         text data about 'Angora goats'. These tests are also measures coverage accuracy
 *         and completeness.
 *
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class ContextCoverage
{
    @Before
    public void setUp() throws UIMAException, IOException {
        PipelineLuncher.getInstance();
    }

    @Test
    public void testTripletsCount() {
        int count = 0;
        for ( int pos = 0; pos < TripletsFactory.getParsedTriplets().size(); ++pos ) {
            count += TripletsFactory.getParsedTriplets().get( pos + 1 ).size();
        }
        // Here is 15 sentences, 19 triplets at all
        Assert.assertTrue( count == 19 );
    }

    @Test
    public void testSentence1() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 1 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() == false );
        Assert.assertTrue( triplet.getSubject().equals( "dog" ) );
        Assert.assertTrue( triplet.getObject().equals( "animal" ) );
        Assert.assertTrue( triplet.getFormatedRelation().isEmpty() == false );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{COP:were}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getObjectFormatedNEs().equals( "{DESCRIPTION_ENTITY:first/JJ/}" ) );
        Assert.assertTrue( triplet.getContext().equals( "Dogs, goats, and sheep were the first animals to be domesticated by man." ) );
        Assert.assertTrue( triplet.getDefinition().isEmpty() );
        Assert.assertTrue( triplet.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence2() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 2 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() );
        Assert.assertTrue( triplet.getSubject().equals( "domestication" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{AUXPASS:is}{NSUBJPASS:considered}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getContext().equals( "Domestication of the goat is considered to have occurred at least 10000 years ago in the Near East and Africa." ) );
        Assert.assertTrue( triplet.getDefinition().isEmpty() );
        Assert.assertTrue( triplet.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence3() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 3 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() );
        Assert.assertTrue( triplet.getSubject().equals( "animal" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{AUXPASS:were}{NSUBJPASS:used}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getContext().equals( "The animals were used for production of meat, milk, skins, and fiber." ) );
        Assert.assertTrue( triplet.getDefinition().equals( "for production of meat, milk, skins, and fiber" ) );
        Assert.assertTrue( triplet.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence4() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 4 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() == false );
        Assert.assertTrue( triplet.getSubject().equals( "goat" ) );
        Assert.assertTrue( triplet.getObject().equals( "area" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{DOBJ:occupied}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().equals( "{DESCRIPTION_ENTITY:fiberproduce/VBG/}" ) );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getContext().equals( "Fiberproducing goats have occupied the area between the Black Sea and the Mediterranean Ocean for at least 2000 years." ) );
        Assert.assertTrue( triplet.getDefinition().equals( "between the Black Sea and the Mediterranean Ocean" ) );
        Assert.assertTrue( triplet.getDeginitionNEs() != null );

        Assert.assertTrue( triplet.getDeginitionNEs().size() == 2 );
        TestTriplet.NamedEntityShort ne1 = triplet.getDeginitionNEs().get( 0 );
        TestTriplet.NamedEntityShort ne2 = triplet.getDeginitionNEs().get( 1 );
        Assert.assertTrue( ne1.getType().equals( "Location" ) );
        Assert.assertTrue( ne1.getValue().equals( "Black Sea" ) );
        Assert.assertTrue( ne2.getType().equals( "Location" ) );
        Assert.assertTrue( ne2.getValue().equals( "Mediterranean Ocean" ) );
    }

    @Test
    public void testSentence5() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 5 );
        Assert.assertTrue( triplets.size() == 2 );

        //
        // triplet number 1
        //

        TestTriplet triplet1 = triplets.get( 0 );

        Assert.assertTrue( triplet1.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet1.getSubject().equals( "goat" ) );
        Assert.assertTrue( triplet1.getObject().isEmpty() );
        Assert.assertTrue( triplet1.getFormatedRelation() != null );

        Assert.assertTrue( triplet1.isSubjectCoref() == false );
        Assert.assertTrue( triplet1.isObjectCoref() == false );

        Assert.assertTrue( triplet1.getFormatedRelation().equals( "[{AUXPASS:was}{NSUBJPASS:developed}]" ) );
        Assert.assertTrue( triplet1.getSubjectFormatedNEs().isEmpty() == false );
        Assert.assertTrue( triplet1.getSubjectFormatedNEs().equals( "{DESCRIPTION_ENTITY:white/JJ/,lustrous-fleece/JJ/}" ) );
        Assert.assertTrue( triplet1.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet1.getContext().equals( "The white, lustrous-fleeced goat called the Angora (Capra hircus aegagrus) was developed on the Turkish plains close to Ankara, from which the name of the goat was derived." ) );
        Assert.assertTrue( triplet1.getDefinition().equals( "on the Turkish plains close to Ankara" ) );
        Assert.assertTrue( triplet1.getDeginitionNEs() != null );

        Assert.assertTrue( triplet1.getDeginitionNEs().size() == 2 );
        TestTriplet.NamedEntityShort ne1 = triplet1.getDeginitionNEs().get( 0 );
        TestTriplet.NamedEntityShort ne2 = triplet1.getDeginitionNEs().get( 1 );
        Assert.assertTrue( ne1.getType().equals( "Location" ) );
        Assert.assertTrue( ne1.getValue().equals( "Turkish" ) );
        Assert.assertTrue( ne2.getType().equals( "Location" ) );
        Assert.assertTrue( ne2.getValue().equals( "Ankara" ) );

        //
        // triplet number 2
        //

        TestTriplet triplet2 = triplets.get( 1 );

        Assert.assertTrue( triplet2.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet2.getSubject().equals( "name" ) );
        Assert.assertTrue( triplet2.getObject().isEmpty() );
        Assert.assertTrue( triplet2.getFormatedRelation() != null );

        Assert.assertTrue( triplet2.isSubjectCoref() == false );
        Assert.assertTrue( triplet2.isObjectCoref() == false );

        Assert.assertTrue( triplet2.getFormatedRelation().equals( "[{AUXPASS:was}{NSUBJPASS:derived}]" ) );
        Assert.assertTrue( triplet2.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet2.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet2.getContext().equals( "The white, lustrous-fleeced goat called the Angora (Capra hircus aegagrus) was developed on the Turkish plains close to Ankara, from which the name of the goat was derived." ) );
        Assert.assertTrue( triplet2.getDefinition().isEmpty() );
        Assert.assertTrue( triplet2.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence6() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 6 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() );
        Assert.assertTrue( triplet.getSubject().equals( "goat" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{AUXPASS:were}{NSUBJPASS:described}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() == false );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().equals( "{DESCRIPTION_ENTITY:original/JJ/,turkish/JJ/,angora/NN/}" ) );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getContext().equals( "The original Turkish Angora goats were described as small, refined, and delicate and annually produced 1 2 kg of mohair in ringlets 20 25 cm in length." ) );
        Assert.assertTrue( triplet.getDefinition().equals( "as small, refined, and delicate" ) );
        Assert.assertTrue( triplet.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence7() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 7 );
        Assert.assertTrue( triplets.size() == 3 );

        //
        // triplet number 1
        //

        TestTriplet triplet1 = triplets.get( 0 );

        Assert.assertTrue( triplet1.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet1.getObject().isEmpty() == false );
        Assert.assertTrue( triplet1.getSubject().equals( "follicle" ) );
        Assert.assertTrue( triplet1.getObject().equals( "fiber" ) );
        Assert.assertTrue( triplet1.getFormatedRelation() != null );

        Assert.assertTrue( triplet1.isSubjectCoref() == false );
        Assert.assertTrue( triplet1.isObjectCoref() == false );

        Assert.assertTrue( triplet1.getFormatedRelation().equals( "[{DOBJ:produce}]" ) );
        Assert.assertTrue( triplet1.getSubjectFormatedNEs().isEmpty() == false );
        Assert.assertTrue( triplet1.getSubjectFormatedNEs().equals( "{DESCRIPTION_ENTITY:primary/JJ/,secondary/JJ/}" ) );
        Assert.assertTrue( triplet1.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet1.getContext().equals( "The primary and secondary follicles of Angora goats produce fibers of similar diameter and length, giving rise to a nonshedding single-coated fleece that is quite distinct from cashmere and the fleece of other goats that produce double coats." ) );
        Assert.assertTrue( triplet1.getDefinition().equals( "of similar diameter and length" ) );
        Assert.assertTrue( triplet1.getDeginitionNEs() == null );

        //
        // triplet number 2
        //

        TestTriplet triplet2 = triplets.get( 1 );

        Assert.assertTrue( triplet2.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet2.getObject().isEmpty() == false );
        Assert.assertTrue( triplet2.getSubject().equals( "fiber" ) );
        Assert.assertTrue( triplet2.getObject().equals( "coat" ) );
        Assert.assertTrue( triplet2.getFormatedRelation() != null );

        Assert.assertTrue( triplet2.isSubjectCoref() );
        Assert.assertTrue( triplet2.isObjectCoref() == false );

        Assert.assertTrue( triplet2.getFormatedRelation().equals( "[{DOBJ:produce}]" ) );
        Assert.assertTrue( triplet2.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet2.getObjectFormatedNEs().isEmpty() == false );
        Assert.assertTrue( triplet2.getObjectFormatedNEs().equals( "{DESCRIPTION_ENTITY:double/JJ/}" ) );
        Assert.assertTrue( triplet2.getContext().equals( "The primary and secondary follicles of Angora goats produce fibers of similar diameter and length, giving rise to a nonshedding single-coated fleece that is quite distinct from cashmere and the fleece of other goats that produce double coats." ) );
        Assert.assertTrue( triplet2.getDefinition().isEmpty() );
        Assert.assertTrue( triplet2.getDeginitionNEs() == null );

        //
        // triplet number 3
        //

        TestTriplet triplet3 = triplets.get( 2 );

        Assert.assertTrue( triplet3.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet3.getObject().isEmpty() == false );
        Assert.assertTrue( triplet3.getSubject().equals( "fiber" ) );
        Assert.assertTrue( triplet3.getObject().equals( "distinct" ) );
        Assert.assertTrue( triplet3.getFormatedRelation() != null );

        Assert.assertTrue( triplet3.isSubjectCoref() );
        Assert.assertTrue( triplet3.isObjectCoref() == false );

        Assert.assertTrue( triplet3.getFormatedRelation().equals( "[{COP:is}]" ) );
        Assert.assertTrue( triplet3.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet3.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet3.getContext().equals( "The primary and secondary follicles of Angora goats produce fibers of similar diameter and length, giving rise to a nonshedding single-coated fleece that is quite distinct from cashmere and the fleece of other goats that produce double coats." ) );
        Assert.assertTrue( triplet3.getDefinition().equals( "from cashmere and the fleece of other goats" ) );
        Assert.assertTrue( triplet3.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence8() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 8 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() );
        Assert.assertTrue( triplet.getSubject().equals( "shipment" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{NSUBJ:occurred}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() == false );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().equals( "{DESCRIPTION_ENTITY:first/JJ/,record/JJ/}" ) );
        Assert.assertTrue( triplet.getContext().equals( "The first recorded shipment of Angora goats out of Turkey occurred in 1554." ) );
        Assert.assertTrue( triplet.getDefinition().equals( "in 1554" ) );
        //
        // Need to extract 1554. Now, stanford tools do not extract it.
        //
        Assert.assertTrue( triplet.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence9() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 9 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() );
        Assert.assertTrue( triplet.getSubject().equals( "shipment" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{NSUBJ:followed}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getContext().equals( "Shipments to South Africa (1838), the United States (1849), Australia (1850s), and the United Kingdom (1881) followed." ) );
        Assert.assertTrue( triplet.getDefinition().isEmpty() );
        Assert.assertTrue( triplet.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence10() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 10 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() );
        Assert.assertTrue( triplet.getSubject().equals( "production" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{NSUBJ:flourished}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() == false );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().equals( "{DESCRIPTION_ENTITY:mohair/NN/}" ) );
        Assert.assertTrue( triplet.getContext().equals( "Mohair production flourished in South Africa and the United States." ) );
        Assert.assertTrue( triplet.getDefinition().equals( "in South Africa and the United States" ) );

        Assert.assertTrue( triplet.getDeginitionNEs() != null );

        Assert.assertTrue( triplet.getDeginitionNEs().size() == 2 );
        TestTriplet.NamedEntityShort ne1 = triplet.getDeginitionNEs().get( 0 );
        TestTriplet.NamedEntityShort ne2 = triplet.getDeginitionNEs().get( 1 );
        Assert.assertTrue( ne1.getType().equals( "Location" ) );
        Assert.assertTrue( ne1.getValue().equals( "South Africa" ) );
        Assert.assertTrue( ne2.getType().equals( "Location" ) );
        Assert.assertTrue( ne2.getValue().equals( "United States" ) );
    }

    @Test
    public void testSentence11() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 11 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() );
        Assert.assertTrue( triplet.getSubject().equals( "goat" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{NSUBJ:shorn}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() == false );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().equals( "{DESCRIPTION_ENTITY:angora/NNP/}" ) );
        Assert.assertTrue( triplet.getContext().equals( "By 1909, 1.34 million Angora goats were shorn in Texas." ) );
        Assert.assertTrue( triplet.getDefinition().equals( "in Texas" ) );
        Assert.assertTrue( triplet.getDeginitionNEs() != null );

        Assert.assertTrue( triplet.getDeginitionNEs().size() == 1 );
        TestTriplet.NamedEntityShort ne1 = triplet.getDeginitionNEs().get( 0 );
        Assert.assertTrue( ne1.getType().equals( "Location" ) );
        Assert.assertTrue( ne1.getValue().equals( "Texas" ) );
    }

    @Test
    public void testSentence12() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 12 );
        Assert.assertTrue( triplets.size() == 2 );

        //
        // triplet number 1
        //

        TestTriplet triplet1 = triplets.get( 0 );

        Assert.assertTrue( triplet1.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet1.getObject().isEmpty() );
        Assert.assertTrue( triplet1.getSubject().equals( "population" ) );;
        Assert.assertTrue( triplet1.getFormatedRelation() != null );

        Assert.assertTrue( triplet1.isSubjectCoref() == false );
        Assert.assertTrue( triplet1.isObjectCoref() == false );

        Assert.assertTrue( triplet1.getFormatedRelation().equals( "[{NSUBJ:increased}]" ) );
        Assert.assertTrue( triplet1.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet1.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet1.getContext().equals( "The population increased to 4.61 million by 1965 but subsequently declined to the present-day 220000." ) );
        Assert.assertTrue( triplet1.getDefinition().equals( "to 4.61 million by 1965" ) );
        //
        // Need to extract '4.61' and '1965'. Now, stanford tools do not do it.
        //
        Assert.assertTrue( triplet1.getDeginitionNEs() == null );

        //
        // triplet number 2
        //

        TestTriplet triplet2 = triplets.get( 1 );

        Assert.assertTrue( triplet2.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet2.getObject().isEmpty() );
        Assert.assertTrue( triplet2.getSubject().equals( "population" ) );
        Assert.assertTrue( triplet2.getFormatedRelation() != null );

        Assert.assertTrue( triplet2.isSubjectCoref() == false );
        Assert.assertTrue( triplet2.isObjectCoref() == false );

        Assert.assertTrue( triplet2.getFormatedRelation().equals( "[{CONJ:declined}]" ) );
        Assert.assertTrue( triplet2.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet2.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet2.getContext().equals( "The population increased to 4.61 million by 1965 but subsequently declined to the present-day 220000." ) );
        Assert.assertTrue( triplet2.getDefinition().isEmpty() );
        Assert.assertTrue( triplet2.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence13() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 13 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() );
        Assert.assertTrue( triplet.getSubject().equals( "population" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{NSUBJ:peaked}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() == false );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().equals( "{DESCRIPTION_ENTITY:south/NNP/,african/NNP/,angora/NNP/,goat/NN/}" ) );
        Assert.assertTrue( triplet.getContext().equals( "In recent years, the South African Angora goat population peaked in 1989 with 3.0 million animals." ) );
        Assert.assertTrue( triplet.getDefinition().equals( "in 1989 with 3.0 million animals" ) );
        Assert.assertTrue( triplet.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence14() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 14 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() );
        Assert.assertTrue( triplet.getSubject().equals( "number" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{NSUBJ:declined}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getContext().equals( "By 2003, this number had declined to 1.1 million." ) );
        Assert.assertTrue( triplet.getDefinition().equals( "to 1.1 million" ) );
        //
        // Need to extract '1.1'. Now, stanford tools do not do it.
        //
        Assert.assertTrue( triplet.getDeginitionNEs() == null );
    }

    @Test
    public void testSentence15() {
        ArrayList<TestTriplet> triplets = TripletsFactory.getParsedTriplets().get( 15 );
        Assert.assertTrue( triplets.size() == 1 );

        TestTriplet triplet = triplets.get( 0 );

        Assert.assertTrue( triplet.getSubject().isEmpty() == false );
        Assert.assertTrue( triplet.getObject().isEmpty() );
        Assert.assertTrue( triplet.getSubject().equals( "population" ) );
        Assert.assertTrue( triplet.getFormatedRelation() != null );

        Assert.assertTrue( triplet.isSubjectCoref() == false );
        Assert.assertTrue( triplet.isObjectCoref() == false );

        Assert.assertTrue( triplet.getFormatedRelation().equals( "[{NSUBJ:declined}]" ) );
        Assert.assertTrue( triplet.getSubjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getObjectFormatedNEs().isEmpty() );
        Assert.assertTrue( triplet.getContext().equals( "Meanwhile, the population in Turkey had declined to about 100000 Angora goats." ) );
        Assert.assertTrue( triplet.getDefinition().equals( "to about 100000 Angora goats" ) );
        //
        // Need to extract '100000'. Now, stanford tools do not do it.
        //
        Assert.assertTrue( triplet.getDeginitionNEs() == null );
    }
}
