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
 * @todo
 *      Join some fields to a single data structure. E.g.:
 *      - [m_SubjectCoref|m_Subject|m_SubjectAttrs] to a class TripletSubject
 *      - [m_ObjectCoref|m_Object|m_ObjectAttrs] to a class TripletObject
 *
 * @note
 *      The most probably, 'relation' field also will be extended to
 *      a class TripletRelation, which will have extended information.
 */

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

    private String m_SubjectId;
    private String m_ObjectId;

    private Token m_SubjectCoref;
    private Token m_Subject;

    private Token m_ObjectCoref;
    private Token m_Object;
    private String m_Relation;

    private List<String> m_SubjectAttrs;
    private List<String> m_ObjectAttrs;

    private String m_Context;

    private TripletScore m_Score;

    protected Triplet(){}

    public Triplet( int addr, TOP_Type type ) {
        super( addr, type );
    }

    public Triplet( JCas jCas, int begin, int end, String context ) {
        super( jCas );
        setBegin( begin );
        setEnd( end );
        m_Context = context;
    }

    public void setObjectCoref( Token coref ) {
        m_ObjectCoref = coref;
        m_ObjectId = Utils.TokenHash( m_ObjectCoref );
    }
    public void setObject( Token obj ) {
        m_Object = obj;
        if ( m_ObjectCoref == null ) {
            m_ObjectId = Utils.TokenHash( m_Object );
        }
    }
    public void setSubjectCoref( Token coref ) {
        m_SubjectCoref = coref;
        m_SubjectId = Utils.TokenHash( m_SubjectCoref );
    }
    public void setSubject( Token subj ) {
        m_Subject = subj;
        if ( m_SubjectCoref == null ) {
            m_SubjectId = Utils.TokenHash( m_Subject );
        }

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
    public Token getObjectCoref() {
        return m_ObjectCoref;
    }
    public Token getObject() {
        return m_Object;
    }
    public Token getSubjectCoref() {
        return m_SubjectCoref;
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
    public String getSubjectId() {
        return m_SubjectId;
    }
    public String getObjectId() {
        return m_ObjectId;
    }
    public String getContext() {
        return m_Context;
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
        return isObject() && isSubject() && isRelation();
    }

    /**
     * @todo
     *      Need to think about optimized clone function. Current implementation
     *      is very dirty.
     */
    public Triplet clone() {
        Triplet cl;
        try {
            cl = new Triplet( getCAS().getJCas(), getBegin(), getEnd(), m_Context );
        } catch ( CASException ex ) {
            cl = new Triplet();
            cl.m_Context = m_Context;
            cl.setBegin( getBegin() );
            cl.setEnd( getEnd() );
        }
        cl.setObject( m_Object );
        cl.setSubject( m_Subject );
        cl.setRelation( m_Relation );
        if ( m_ObjectAttrs != null ) {
            cl.addObjectAttrs( m_ObjectAttrs );
        }
        if ( m_SubjectAttrs != null ) {
            cl.addSubjectAttrs( m_SubjectAttrs );
        }
        cl.setScore( m_Score );
        return cl;
    }
    public void printShortCoref() {
        String sbj = m_SubjectCoref == null ? ( isSubject() ? m_Subject.getLemma().getValue() : "" ) : "coref$" + m_SubjectCoref.getLemma().getValue();
        String obj = m_ObjectCoref == null ? ( isObject() ? m_Object.getLemma().getValue() : "" ) : "coref$" + m_ObjectCoref.getLemma().getValue();
        String sbjPosVal = sbj.isEmpty() ? "" : (m_SubjectCoref == null ? m_Subject.getPos().getPosValue() : m_SubjectCoref.getPos().getPosValue());
        String objPosVal = obj.isEmpty() ? "" : (m_ObjectCoref == null ? m_Object.getPos().getPosValue() : m_ObjectCoref.getPos().getPosValue());
        System.out.printf( "[ short_coref_triplet ] < %s >/%s/ :%s < %s >/%s/\n",
                sbj,sbjPosVal,m_Relation, obj, objPosVal);
    }
}
