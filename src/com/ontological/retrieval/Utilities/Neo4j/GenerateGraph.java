package com.ontological.retrieval.Utilities.Neo4j;

import com.ontological.retrieval.Utilities.Constants;
import com.ontological.retrieval.Utilities.Entity;
import com.ontological.retrieval.Utilities.Triplet;

import java.util.*;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class GenerateGraph
{
    private GenerateGraph(){}
    //
    // The results could be visualized on the site http://console.neo4j.org/
    // For that use Cypher syntax.
    public static String generateSentenceGraph(List<Entity> entitiesIndex )
    {
        String sentenceGraph = new String();
        if ( !entitiesIndex.isEmpty() ) {
            List<String> nodes = new ArrayList<>();
            List<String> relations = new ArrayList<>();
            for ( Entity entity : entitiesIndex ) {
                String nd = String.format( "(_%d {name:\"%s\"})", entity.getBegin(), entity.getName().getCoveredText() );
                nodes.add( nd );
                Entity parent = entity.getParent();
                if ( parent != null ) {
                    String rel = String.format( "_%d-[:%s]->_%d", entity.getBegin(), entity.getType(), parent.getBegin() );
                    relations.add( rel );
                }
            }

            for ( String nodeString : nodes ) {
                sentenceGraph = sentenceGraph + ( sentenceGraph.isEmpty() ? "" : ",\n" ) + nodeString;
            }
            if ( !sentenceGraph.isEmpty() ) {
                for ( String relationString : relations ) {
                    sentenceGraph = sentenceGraph + ",\n" + relationString;
                }
                sentenceGraph = sentenceGraph + '\n';
            }
            sentenceGraph = "create\n" + sentenceGraph;
        }
        return sentenceGraph;
    }

    public static String generateTripletsGraph(List<Triplet> triplets )
    {
        String graph = "";
        HashMap<String, String> nodes = new HashMap<>();
        List<String> edges = new ArrayList<>();
        for ( Triplet triplet : triplets ) {

            String objectId = triplet.getObjectId();
            String subjectId = triplet.getSubjectId();

            if ( objectId != null && !objectId.equals( Constants.INVALID_HASH ) && !nodes.containsKey( objectId ) ) {
                nodes.put( objectId, getObjectLemma( triplet ) );
            }

            if ( subjectId != null && !subjectId.equals( Constants.INVALID_HASH ) && !nodes.containsKey( subjectId ) ) {
                nodes.put( subjectId, getSubjectLemma( triplet ) );
            }

            if ( triplet.isValid() && triplet.isRelation() ) {
                String rel = String.format( "_%s-[:%s]->_%s", subjectId, triplet.getRelation(), objectId );
                edges.add( rel );
            }
        }
        Iterator it = nodes.entrySet().iterator();
        while ( it.hasNext() ) {
            Map.Entry pair = (Map.Entry) it.next();
            String node = String.format( "(_%s {name:\"%s\"})", (String)pair.getKey(), (String)pair.getValue() );
            graph = graph + ( graph.isEmpty() ? "" : ",\n" ) + node;
        }

        if ( !graph.isEmpty() ) {
            for ( String relationString : edges ) {
                graph = graph + ",\n" + relationString;
            }
            graph = graph + '\n';
        }
        graph = "create\n" + graph;

        return graph;
    }

    private static String getObjectLemma( Triplet triplet ){
        if ( triplet.getObjectCoref() != null ) {
            if ( triplet.getObjectCoref().getLemma() != null ) {
                return triplet.getObjectCoref().getLemma().getValue();
            } else {
                return triplet.getObjectCoref().getCoveredText();
            }
        } else {
            if ( triplet.getObject().getLemma() != null ) {
                return triplet.getObject().getLemma().getValue();
            } else {
                return triplet.getObject().getCoveredText();
            }
        }
    }

    private static String getSubjectLemma( Triplet triplet ){
        if ( triplet.getSubjectCoref() != null ) {
            if ( triplet.getSubjectCoref().getLemma() != null ) {
                return triplet.getSubjectCoref().getLemma().getValue();
            } else {
                return triplet.getSubjectCoref().getCoveredText();
            }
        } else {
            if ( triplet.getSubject().getLemma() != null ) {
                return triplet.getSubject().getLemma().getValue();
            } else {
                return triplet.getSubject().getCoveredText();
            }
        }
    }
}
