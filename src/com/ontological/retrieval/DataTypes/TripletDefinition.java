package com.ontological.retrieval.DataTypes;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief  This class implements the data structure for Triplet Object Field
 *         parametrization. The example could be: some real-world declaration
 *         or statement in some condition:
 *
 *         [ sentecnce ] Fiberproducing goats have occupied the area between the
 *              Black Sea and the Mediterranean Ocean for at least 2000 years.
 *         [ triplet ] <goat> _:occupied <area>
 *             Definition: between the Black Sea and the Mediterranean Ocean
 *
 *         So, the definition describes the scope of object (some particular
 *         scenario). All definitions begins after the 'pronoun' (which behinds
 *         the triplet object.
 *
 *         In any case, the definitio cold exist in case of absent triplet object.
 *         It is a normal case for Active/Passive voice:
 *
 *         [ sentence ] The animals were used for production of meat, milk, skins, and fiber.
 *         [ triplet ] <animal> _:[were, used] < >
 *             Definition: for production of meat, milk, skins, and fiber
 *
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class TripletDefinition extends Annotation
{
    @SuppressWarnings ("hiding")
    public final static int typeIndexID = JCasRegistry.register( TripletDefinition.class );

    @SuppressWarnings ("hiding")
    public final static int type = typeIndexID;
    @Override
    public int getTypeIndexID() {
        return typeIndexID;
    }

    private List<NamedEntity> m_NamedEntities;

    public TripletDefinition( int addr, TOP_Type type ) {
        super( addr, type );
    }

    public TripletDefinition( JCas jCas, Triplet parentTriplet, int beginIndex, int endIndex ) {
        super( jCas );
        setBegin( parentTriplet.getBegin() + beginIndex );
        setEnd( parentTriplet.getBegin() + endIndex );
    }

    public void addNamedEntity( NamedEntity entity ) {
        if ( m_NamedEntities == null ) {
            m_NamedEntities = new ArrayList<>();
        }
        m_NamedEntities.add( entity );
    }

    public List<NamedEntity> getNamedEntities() {
        return m_NamedEntities;
    }
}
