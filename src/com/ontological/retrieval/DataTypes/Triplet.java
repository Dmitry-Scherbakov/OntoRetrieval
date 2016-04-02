package com.ontological.retrieval.DataTypes;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
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
 * @brief  This class implements the main structural object, which represent
 *         the complex parametric linguistic 'Fact' (e.g. an RDF triplet is an
 *         ideal example of such fact/data-structure). The different with RDF-
 *         triplet is -- current triplet contains more/complex information.
 *
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

    public TripletDefinition getDefinition() {
        return m_Definition;
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

    @Override
    public String toString() {
        Token subject = ( m_Subject == null ? null : m_Subject.getField() );
        Token object =  ( m_Object == null ? null : m_Object.getField() );

        String result = String.format( "<%s%s>/%s/ _:%s <%s%s>/%s/\n",
                ( subject == null ? "" : subject.getLemma().getValue() ),
                ( m_Subject != null && m_Subject.isCoreference() ? "$coref" : "" ),
                ( subject == null ? "" : subject.getPos().getPosValue() ), formattedRelations(),
                ( object == null ? "" : object.getLemma().getValue() ),
                ( m_Object != null && m_Object.isCoreference() ? "$coref" : "" ),
                ( object == null ? "" : object.getPos().getPosValue() ) );

        if ( getSubject() != null ) {
            String sbjAttrs = fieldAttributesToString( getSubject() );
            if ( !sbjAttrs.isEmpty() ) {
                result += "\tSubj_attrs: " + sbjAttrs + '\n';
            }
        }

        if ( getObject() != null ) {
            String objAttrs = fieldAttributesToString( getObject() );
            if ( !objAttrs.isEmpty() ) {
                result += "\tObj_attrs: " + objAttrs + '\n';
            }
        }

        if ( m_Definition != null ) {
            result += "\tDefinition: " + m_Definition.getCoveredText() + '\n';
            List<NamedEntity> namedEntities = m_Definition.getNamedEntities();
            if ( namedEntities != null ) {
                String nEntities = "";
                for ( NamedEntity nEn : namedEntities ) {
                    if ( !nEntities.isEmpty() ) {
                        nEntities += ',';
                    }
                    nEntities += '{' + nEn.getType().getShortName() + ':' + nEn.getCoveredText() + '}';
                }
                result += "\t\tDefinition named entities: [" + nEntities + "]\n";
            }
        }

        return "# " + m_Context.getCoveredText() + '\n' + "# <Subject>/POS/ _:{Dependency:relation} <Object>/POS/\n" + result;
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

    static public String fieldAttributesToString( TripletField field ) {
        String result = "";
        for ( TripletField.AttributeType type : TripletField.AttributeType.values() ) {
            String deep = "";
            for ( Object object : field.getAttributes( type ) ) {
                if ( !deep.isEmpty() ) {
                    deep += ',';
                }
                if ( object instanceof NamedEntity ) {
                    deep += ((NamedEntity) object).getCoveredText();
                } else if ( object instanceof Token ) {
                    Token tk = (Token) object;
                    deep += tk.getLemma().getValue() + '/' + tk.getPos().getPosValue() + '/';
                } else {
                    System.out.println( "Error: [Triplet/fieldAttributesToString], undetermined attribute type." );
                    deep += object.toString();
                }
            }
            if ( !deep.isEmpty() ) {
                if ( !result.isEmpty() ) {
                    result += ',';
                }
                result += '{' + type.toString() + ':' + deep + '}';
            }
        }
        return result;
    }
}
