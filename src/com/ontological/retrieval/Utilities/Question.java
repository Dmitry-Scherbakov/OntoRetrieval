package com.ontological.retrieval.Utilities;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov@yandex.ru
 */
public class Question
{
    private String m_Subject;
    private String m_Object;
    private String m_Relation;
    // The raw question itself
    private String m_Context;

    public Question( Triplet triplet ) {
        m_Subject = triplet.getSubjectCoref() != null ? triplet.getSubjectCoref().getLemma().getValue() : triplet.getSubject().getLemma().getValue();
        m_Object = triplet.getObjectCoref() != null ? triplet.getObjectCoref().getLemma().getValue() : triplet.getObject().getLemma().getValue();
        m_Relation = triplet.getRelation();
        m_Context = triplet.getContext();
    }

    public String getSubject() {
        return m_Subject;
    }

    public String getObject() {
        return m_Object;
    }

    public String getRelation() {
        return m_Relation;
    }

    public String getContext() {
        return m_Context;
    }

    public void print() {
        System.out.printf( "[question]: %s\n\t\t<%s> /%s/ <%s>\n", m_Context, m_Subject, m_Relation, m_Object );
    }
}
