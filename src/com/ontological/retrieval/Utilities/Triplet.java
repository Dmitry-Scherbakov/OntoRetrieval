package com.ontological.retrieval.Utilities;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

import java.util.ArrayList;
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

    private Token m_Subject;
    private Token m_Object;
    private String m_Relation;

    private List<String> m_SubjectAttrs;
    private List<String> m_ObjectAttrs;

    private String m_FullContext;

    private TripletScore m_Score;

    protected Triplet(){}

    public Triplet( int addr, TOP_Type type ) {
        super(addr, type);
        readObject();
    }

    public Triplet( JCas jcas ) {
        super(jcas);
        readObject();
    }

    public Triplet( JCas jcas, int begin, int end ) {
        super(jcas);
        setBegin(begin);
        setEnd(end);
        readObject();
    }

    private void readObject() {/*default - does nothing empty block */}

    public Triplet( JCas jCas, String fullContext )
    {
        super( jCas );
        m_ObjectAttrs = new ArrayList<String>();
        m_SubjectAttrs = new ArrayList<String>();
        m_FullContext = fullContext;
    }

    public void setObject( Token obj ) {
        m_Object = obj;
    }
    public void setSubject( Token subj ) {
        m_Subject = subj;
    }
    public void setRelation( String relation ) {
        m_Relation = relation;
    }
    public void setScore( TripletScore score ) {
        m_Score = score;
    }
    public void addObjectAttrs( List<String> attrs ) {
        if ( m_ObjectAttrs == null ) {
            m_ObjectAttrs = new ArrayList<String>();
        }
        m_ObjectAttrs.addAll( attrs );
    }
    public void addObjectAttrs( String attr ) {
        if ( m_ObjectAttrs == null ) {
            m_ObjectAttrs = new ArrayList<String>();
        }
        m_ObjectAttrs.add( attr );
    }
    public void addSubjectAttrs( List<String> attrs ) {
        if ( m_SubjectAttrs == null ) {
            m_SubjectAttrs = new ArrayList<String>();
        }
        m_SubjectAttrs.addAll( attrs );
    }
    public void addSubjectAttrs( String attr ) {
        if ( m_SubjectAttrs == null ) {
            m_SubjectAttrs = new ArrayList<String>();
        }
        m_SubjectAttrs.add( attr );
    }
    public Token getObject() {
        return m_Object;
    }
    public Token getSubject() {
        return m_Subject;
    }
    public String getRelation() {
        return m_Relation;
    }
    public TripletScore getScore() {
        return m_Score;
    }

    public boolean isObject() {
        return m_Object != null && !m_Object.getCoveredText().isEmpty();
    }
    public boolean isSubject() {
        return m_Subject != null && !m_Subject.getCoveredText().isEmpty();
    }
    public boolean isRelation() {
        return m_Relation != null && !m_Relation.isEmpty();
    }

    public boolean isValid() {
        return isObject() && isObject() && isRelation();
    }
    public Triplet clone() {
        Triplet cl = null;
        try {
            cl = new Triplet( getCAS().getJCas(), m_FullContext );
        } catch ( CASException ex ) {
            cl = new Triplet();
            cl.m_FullContext = m_FullContext;
        }
        cl.setObject( m_Object );
        cl.setSubject( m_Subject );
        cl.setRelation( m_Relation );
        cl.addObjectAttrs( m_ObjectAttrs );
        cl.addSubjectAttrs( m_SubjectAttrs );
        cl.setScore( m_Score );
        return cl;
    }
    public void printShort() {
        System.out.printf( "[ short_triplet ] < %s > :%s < %s >\n", m_Subject == null ? "" : m_Subject.getCoveredText(),
                m_Relation, m_Object == null ? "" : m_Object.getCoveredText() );
    }
}
