package com.ontological.retrieval.DataTypes;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email dm.scherbakov[_d0g_]yandex.ru
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
