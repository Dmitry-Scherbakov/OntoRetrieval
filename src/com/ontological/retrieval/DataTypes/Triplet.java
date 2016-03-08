package com.ontological.retrieval.DataTypes;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
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
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov@yandex.ru
 */
public class Triplet extends Annotation
{
    @SuppressWarnings ("hiding")
    public final static int typeIndexID = JCasRegistry.register( Triplet.class );

    @SuppressWarnings ("hiding")
    public final static int type = typeIndexID;
    @Override
    public int getTypeIndexID() {
        return typeIndexID;
    }

    private TripletField m_Subject;
    private TripletField m_Object;
    private TripletDefinition m_Definition;

    private HashMap<String, List<Token>> m_Relations;

    private Sentence m_Context;

    private TripletScore m_Score;

    public Triplet( int addr, TOP_Type type ) {
        super( addr, type );
    }

    public Triplet( JCas jCas, Sentence context ) {
        super( jCas );
        setBegin( context.getBegin() );
        setEnd( context.getEnd() );
        m_Context = context;
    }

    public void setObject( TripletField object ) {
        m_Object = object;
    }

    public void setSubject( TripletField subject ) {
        m_Subject = subject;
    }

    public void addRelation( String dep, Token relation ) {
        if ( m_Relations == null ) {
            m_Relations = new HashMap<>();
        }
        List<Token> currentRelations = m_Relations.get( dep );
        if ( currentRelations == null ) {
            currentRelations = new ArrayList<>();
            m_Relations.put( dep, currentRelations );
        }
        currentRelations.add( relation );
    }

    public void setScore( TripletScore score ) {
        m_Score = score;
    }

    public void setDefinition( TripletDefinition definition ) {
        m_Definition = definition;
    }

    public TripletField getObject() {
        return m_Object;
    }

    public TripletField getSubject() {
        return m_Subject;
    }

    public List<Token> getRelations( String dep ) {
        return m_Relations.get( dep );
    }

    public List<String> getRelationTypes() {
        List<String> list = new ArrayList<>();
        list.addAll( m_Relations.keySet() );
        return list;
    }

    public TripletScore getScore() {
        return m_Score;
    }

    public Sentence getContext() {
        return m_Context;
    }

    public boolean isRelation() {
        return m_Relations != null && !m_Relations.isEmpty();
    }

    public boolean isValid() {
        return  ( m_Subject != null && m_Subject.isValid() ) &&
                ( m_Object != null && m_Object.isValid() ) && isRelation();
    }

    public Triplet clone() {
        try {
            Triplet triplet = new Triplet( getCAS().getJCas(), m_Context );
            triplet.setSubject( m_Subject.clone() );
            triplet.setObject( m_Object.clone() );
            triplet.setScore( m_Score );
            if ( isRelation() ) {
                triplet.m_Relations = new HashMap<>();
                for ( String dep : getRelationTypes() ) {
                    triplet.m_Relations.put( dep, m_Relations.get( dep ) );
                }
            }
            return triplet;
        } catch ( CASException ex ) {
            return null;
        }
    }

    public void print() {
        String subject = ( m_Subject == null ? "" : m_Subject.getField().getLemma().getValue() );
        String object = ( m_Object == null ? "" : m_Object.getField().getLemma().getValue() );
        String sbjPosVal = ( m_Subject == null ? "" : m_Subject.getField().getPos().getPosValue() );
        String objPosVal = ( m_Object == null ? "" : m_Object.getField().getPos().getPosValue() );

        System.out.printf( "[triplet] <%s%s>/%s/ _:%s <%s%s>/%s/\n",
                subject, ( m_Subject != null && m_Subject.isCoreference() ? "$coref" : "" ), sbjPosVal,formattedRelations(),
                object, ( m_Object != null && m_Object.isCoreference() ? "$coref" : "" ), objPosVal);

        //
        // @todo
        //      Add check for PoS: it must be adjective or noun.
        //
        String subjectAttributes = "";
        String objectAttributes = "";
        if ( getObject() != null ) {
            for ( Object obj : getObject().getAttributes( TripletField.AttributeType.DESCRIPTION_ENTITY ) ) {
                Token tk = (Token)obj;
                if ( !objectAttributes.isEmpty() ) {
                    objectAttributes += ',';
                }
                objectAttributes += tk.getCoveredText() + '/' + tk.getPos().getPosValue() + '/';
            }
        }
        if ( getSubject() != null ) {
            for ( Object obj : getSubject().getAttributes( TripletField.AttributeType.DESCRIPTION_ENTITY ) ) {
                Token tk = (Token)obj;
                if ( !subjectAttributes.isEmpty() ) {
                    subjectAttributes += ',';
                }
                subjectAttributes += tk.getCoveredText() + '/' + tk.getPos().getPosValue() + '/';
            }
        }
        if ( !subjectAttributes.isEmpty() ) {
            System.out.println( "\t$ Subject attributes: [" + subjectAttributes + ']' );
        }
        if ( !objectAttributes.isEmpty() ) {
            System.out.println( "\t$ Object attributes: [" + objectAttributes + ']' );
        }
        if ( m_Definition != null ) {
            System.out.println( "\tDefinition: " + m_Definition.getCoveredText() );
        }
    }

    public String formattedRelations() {
        String relation = "";
        if ( isRelation() ) {
            for ( String dependency : getRelationTypes() ) {
                relation = relation + "{" + dependency + ":";
                String deepRelations = "";
                for ( Token rel : m_Relations.get( dependency ) ) {
                    if ( !deepRelations.isEmpty() ) {
                        deepRelations += ',';
                    }
                    deepRelations += rel.getCoveredText();
                }
                relation = relation + deepRelations + "}";
            }
        }
        return '[' + relation + ']';
    }
}
