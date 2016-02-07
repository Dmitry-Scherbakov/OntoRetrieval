package com.ontological.retrieval.Utilities.Neo4j;

import com.ontological.retrieval.Utilities.Entity;

import java.util.ArrayList;
import java.util.List;

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
}
