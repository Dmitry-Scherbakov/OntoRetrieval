package com.ontological.retrieval.Utilities;

/**
 * Created by Administrator on 06.02.2016.
 */

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class Entity
{
    private Token m_Name;
    private String m_Type;
    private Entity m_Parent;
    List<Entity> m_Children = new ArrayList<>();

    public Entity( Entity parent, Token name, String type ) {
        m_Parent = parent;
        m_Name = name;
        m_Type = type;
    }
    public Entity getParent() {
        return m_Parent;
    }
    public void setParent( Entity parent ) {
        m_Parent = parent;
        m_Parent.addChild( this );
    }
    public Token getName() {
        return m_Name;
    }
    public String getType() {
        return m_Type;
    }
    public void setType( String type ) {
        m_Type = type;
    }
    public int getBegin() {
        return m_Name.getBegin();
    }
    public void addChild( Entity child ) {
        m_Children.add( child );
    }
    public List<Entity> getChildren() {
        return m_Children;
    }
    public boolean isLeaf() {
        return m_Parent == null;
    }
    public void print() {
        System.out.printf( "[ entity ] name [%s/%s], type [%s], begin [%d], parent_name [%s/%s]\n",
                m_Name.getCoveredText(), m_Name.getPos().getPosValue(),
                m_Type, m_Name.getBegin(),
                ( isLeaf() ? null : m_Parent.getName().getCoveredText() ),
                ( isLeaf() ? null : m_Parent.getName().getPos().getPosValue() ) );
    }
}
