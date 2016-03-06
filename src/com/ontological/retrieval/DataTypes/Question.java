package com.ontological.retrieval.DataTypes;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov@yandex.ru
 */
public class Question
{
    private String m_Subject;
    private String m_Object;
    private HashMap<String, List<String>> m_Relations;
    // The raw question itself
    private String m_Context;

    public Question( Triplet triplet ) {
        m_Subject = triplet.getSubject().getField().getLemma().getValue();
        m_Object = triplet.getObject().getField().getLemma().getValue();
        m_Context = triplet.getContext().getCoveredText();
        copyRelations( triplet );
    }

    public String getSubject() {
        return m_Subject;
    }

    public String getObject() {
        return m_Object;
    }

    public List<String> getRelations( String dependency ) {
        return m_Relations.get( dependency );
    }

    public List<String> getRelationTypes() {
        List<String> types = new ArrayList<>();
        types.addAll( m_Relations.keySet() );
        return types;
    }

    public String getContext() {
        return m_Context;
    }

    public void print() {
        System.out.printf( "[question]: %s\n\t\t<%s> /%s/ <%s>\n", m_Context, m_Subject, formattedRelations(), m_Object );
    }

    private void copyRelations( Triplet triplet ) {
        if ( triplet.isRelation() ) {
            m_Relations = new HashMap<>();
            for ( String dependency : triplet.getRelationTypes() ) {
                List<String> rels = new ArrayList<>();
                for ( Token relation : triplet.getRelations( dependency ) ) {
                    rels.add( relation.getLemma().getValue() );
                }
                m_Relations.put( dependency, rels );
            }
        }
    }

    private String formattedRelations() {
        String relation = "";
        if ( m_Relations != null && !m_Relations.isEmpty() ) {
            for ( String dependency : getRelationTypes() ) {
                relation = relation + "{" + dependency + ":";
                String deepRelations = "";
                for ( String rel : m_Relations.get( dependency ) ) {
                    deepRelations = deepRelations + ", " + rel;
                }
                relation = relation + deepRelations + "}";
            }
        }
        return relation;
    }
}
