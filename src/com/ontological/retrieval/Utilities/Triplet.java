package com.ontological.retrieval.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email dm.scherbakov@yandex.ru
 */
public class Triplet {
    private String m_Subject;
    private String m_Object;
    private String m_Relation;

    //
    // preposition type { 'in', 'of', 'for' }
    private String m_SubjectContext;
    private String m_ObjectContext;

    private String m_FullContext;

    private List<String> m_SubjectAttrs;
    private List<String> m_ObjectAttrs;

    private TripletScore m_Score;

    @Deprecated
    public Triplet( EntityWrapper obj, EntityWrapper subj, String relation ) {
        m_Object = obj.getEntity();
        m_Subject = subj.getEntity();
        m_Relation = relation;
        addObjectAttrs( obj.getAttributes() );
        addSubjectAttrs( subj.getAttributes() );
    }
    public Triplet( String fullContext ) {
        m_FullContext = fullContext;
        m_ObjectAttrs = new ArrayList<String>();
        m_SubjectAttrs = new ArrayList<String>();
    }

    public void setObject( String obj ) {
        m_Object = obj;
    }
    public void setSubject( String subj ) {
        m_Subject = subj;
    }
    public void setRelation( String relation ) {
        m_Relation = relation;
    }
    public void setFullContext( String context ) {
        m_FullContext = context;
    }
    public void setSubjectContext( String context ) {
        m_SubjectContext = context;
    }
    public void setObjectContext( String context ) {
        m_ObjectContext = context;
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
    public String getObject() {
        return m_Object;
    }
    public String getSubject() {
        return m_Subject;
    }
    public String getRelation() {
        return m_Relation;
    }
    public String getFullContext() {
        return m_FullContext;
    }
    public TripletScore getScore() {
        return m_Score;
    }

    public boolean isObject() {
        return m_Object != null && !m_Object.isEmpty();
    }
    public boolean isSubject() {
        return m_Subject != null && !m_Subject.isEmpty();
    }
    public boolean isRelation() {
        return m_Relation != null && !m_Relation.isEmpty();
    }

    public boolean isValid() {
        return m_Object != null && m_Subject != null && m_Relation != null && !m_Object.isEmpty() && !m_Subject.isEmpty() && !m_Relation.isEmpty();
    }
    public Triplet clone() {
        Triplet cl = new Triplet( m_FullContext );
        cl.setObject( m_Object );
        cl.setSubject( m_Subject );
        cl.setRelation( m_Relation );
        cl.addObjectAttrs( m_ObjectAttrs );
        cl.addSubjectAttrs( m_SubjectAttrs );
        return cl;
    }
    // aux
    public void print() {
        //
        // Only for debug
        //
        String objAttr = "";
        for ( String s : m_ObjectAttrs ) {
            objAttr = objAttr + " /" + s;
        }
        String sbjAttr = "";
        for ( String s : m_SubjectAttrs ) {
            sbjAttr = sbjAttr + " /" + s;
        }
        String result = "< " + m_Subject + " > ";
        if ( !sbjAttr.isEmpty() ) {
            result += "{ " + sbjAttr + " }";
        }
        if ( !m_SubjectContext.isEmpty() ) {
            result += "$" + m_SubjectContext;
        }

        result += " :" + m_Relation + " < " + m_Object + " > ";
        if ( !objAttr.isEmpty() ) {
            result += "{ " + objAttr + " }";
        }
        if ( !m_ObjectContext.isEmpty() ) {
            result += "$" + m_ObjectContext;
        }
        System.out.println( "[ TRIPLET ]: " + result );
    }
    public void printShort() {
        System.out.printf( "[ short_triplet ] < %s > :%s < %s >\n", m_Subject, m_Relation, m_Object );
    }

    @Deprecated
    static public class EntityWrapper {
        private List<String> m_Attributes;
        private String m_Entity;
        public EntityWrapper( String entity, List<String> attributes ) {
            m_Entity = entity;
            m_Attributes = attributes;
        }
        public String getEntity() {
            return m_Entity;
        }
        public List<String> getAttributes() {
            return m_Attributes;
        }

    }
}
