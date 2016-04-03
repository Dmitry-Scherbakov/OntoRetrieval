package com.ontological.retrieval.DataTypes;

import com.ontological.retrieval.Utilities.Models;
import com.ontological.retrieval.Utilities.Utils;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @brief  This class implements data structure which describes information of main
 *         triplets fields, like 'Subject' and 'Object'. Also, this class handles
 *         'id' generation of this field, stores a set of attached attributes
 *         (adjectives, named entities, etc) and store coreference information.
 *
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class TripletField extends Annotation
{
    @SuppressWarnings ("hiding")
    public final static int typeIndexID = JCasRegistry.register( TripletField.class );

    @SuppressWarnings ("hiding")
    public final static int type = typeIndexID;
    @Override
    public int getTypeIndexID() {
        return typeIndexID;
    }

    public static enum AttributeType {
        NAMED_ENTITY,
        DESCRIPTION_ENTITY, // mostly, it is an 'adjective' entity.
        ABSTRACT_ENTITY
    }

    private HashMap<AttributeType, List<Object>> m_Attributes = new HashMap<>();

    private Token m_Field;
    private TripletField m_FieldCoref;
    private String m_FieldId;

    public TripletField( int addr, TOP_Type type ) {
        super( addr, type );
    }

    public TripletField( JCas jCas, Token token ) {
        super( jCas );

        setBegin( token.getBegin() );
        setEnd( token.getEnd() );

        m_Field = token;
        m_FieldId = Utils.TokenHash( m_Field );

        for ( AttributeType type : AttributeType.values() ) {
            m_Attributes.put( type, new ArrayList<>() );
        }
    }

    public void addAttribute( AttributeType type, Object attribute ) {
        List<Object> attributes = m_Attributes.get( type );
        attributes.add( attribute );
    }

    public List<Object> getAttributes( AttributeType type ) {
        return m_Attributes.get( type );
    }

    public void setFieldCoref( TripletField coref ) {
        m_FieldCoref = coref;
        m_FieldId = Utils.TokenHash( m_FieldCoref.getField() );
        setBegin( m_FieldCoref.getField().getBegin() );
        setEnd( m_FieldCoref.getField().getEnd() );
    }

    public Token getField() {
        return isCoreference() ? m_FieldCoref.getField() : m_Field;
    }

    public boolean isCoreference() {
        return m_FieldCoref != null;
    }

    public boolean isValid() {
        return m_Field != null || m_FieldCoref != null;
    }

    public String getId() {
        return m_FieldId;
    }

    public void resolveCollisions() {
        if ( m_FieldCoref != null ) {
            if ( !Utils.isNoun( m_FieldCoref.getField() ) ) {
                m_FieldCoref = null;
            }
        }
        if ( m_Field != null ) {
            if ( !Utils.isNoun( m_Field ) ) {
                m_Field = null;
            }
        }
        m_FieldId = Utils.TokenHash( getField() );
        if ( isValid() ) {
            setBegin( getField().getBegin() );
            setEnd( getField().getEnd() );
        }
    }

    public boolean isBaseFieldPositioned() {
        //
        // m_Field could be null if it was resolved for POS collision, by using resolveCollisions function
        return m_Field == null ? false : Models.isPositionedPronoun( m_Field.getCoveredText() );
    }

    public TripletField clone() {
        try {
            TripletField field = new TripletField( getCAS().getJCas(), m_Field );
            if ( isCoreference() ) {
                field.setFieldCoref( m_FieldCoref );
            }
            for ( AttributeType type : AttributeType.values() ) {
                for ( Object entity : getAttributes( type ) ) {
                    field.addAttribute( type, entity );
                }
            }
            return field;
        } catch ( CASException ex ) {
            return null;
        }
    }
}
