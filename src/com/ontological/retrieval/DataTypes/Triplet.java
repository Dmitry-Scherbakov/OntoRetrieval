package com.ontological.retrieval.DataTypes;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

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

    private String m_Relation;

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

    public void setRelation( String relation ) {
        m_Relation = relation;
    }

    public void setScore( TripletScore score ) {
        m_Score = score;
    }

    public TripletField getObject() {
        return m_Object;
    }

    public TripletField getSubject() {
        return m_Subject;
    }

    public String getRelation() {
        return m_Relation;
    }

    public TripletScore getScore() {
        return m_Score;
    }

    public Sentence getContext() {
        return m_Context;
    }

    public boolean isRelation() {
        return m_Relation != null && !m_Relation.isEmpty();
    }

    public boolean isValid() {
        return  ( m_Subject != null && m_Subject.isValid() ) &&
                ( m_Object != null && m_Object.isValid() ) && isRelation();
    }

    public Triplet clone() {
        try {
            Triplet triplet = new Triplet( getCAS().getJCas(), m_Context );
            triplet.setRelation( m_Relation );
            triplet.setSubject( m_Subject.clone() );
            triplet.setObject( m_Object.clone() );
            triplet.setScore( m_Score );
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

        System.out.printf( "[triplet] <%s%s>/%s/ :%s <%s%s>/%s/\n",
                subject, ( m_Subject != null && m_Subject.isCoreference() ? "$coref" : "" ), sbjPosVal,m_Relation,
                object, ( m_Object != null && m_Object.isCoreference() ? "$coref" : "" ), objPosVal);
    }
}
