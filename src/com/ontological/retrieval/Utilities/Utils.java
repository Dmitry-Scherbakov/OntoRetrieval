package com.ontological.retrieval.Utilities;

import com.ontological.retrieval.DataTypes.*;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov@yandex.ru
 */
public class Utils
{
    public static String TokenHash( Token tk ) {
        if ( tk == null || tk.getLemma() == null || tk.getLemma().getValue().isEmpty() ) {
            return Constants.INVALID_HASH;
        }
        return DigestUtils.md5DigestAsHex( tk.getLemma().getValue().getBytes() );
    }

    public static List<Entity> parseForEntities(JCas aJCas, Sentence sentence ) {
        List<Entity> entities = new ArrayList<>();
        for ( Dependency dep : JCasUtil.selectCovered(aJCas, Dependency.class, sentence) ) {

//            System.out.printf( "type_short_name [%s], governor [%s,%d-%d], dependent [%s,%d-%d]\n",
//                    dep.getType().getShortName(),
//                    dep.getGovernor().getCoveredText(),dep.getGovernor().getBegin(), dep.getGovernor().getEnd(),
//                    dep.getDependent().getCoveredText(),dep.getDependent().getBegin(), dep.getDependent().getEnd() );

            int uInnerEntityPos = dep.getDependent().getBegin();
            Entity upperEntity = findEntity( uInnerEntityPos, entities );
            if ( upperEntity == null ) {
                upperEntity = new Entity( null, dep.getDependent(), dep.getType().getShortName() );
                entities.add( upperEntity );
            } else {
                upperEntity.setType( dep.getType().getShortName() );
            }

            int dInnerEntityPos = dep.getGovernor().getBegin();
            Entity downEntity = findEntity( dInnerEntityPos, entities );
            if ( downEntity == null ) {
                downEntity = new Entity( null, dep.getGovernor(), null );
                entities.add( downEntity );
            }
            upperEntity.setParent( downEntity );
        }
        return entities;
    }

    public static Entity findEntity( int innerEntityPos, List<Entity> entities  ) {
        for ( Entity en : entities ) {
            if ( en.getBegin() == innerEntityPos ) {
                return en;
            }
        }
        return null;
    }

    public static Entity findEntityType( String type, List<Entity> entities  ) {
        for (Entity en : entities) {
            if (en.getType().equals(type)) {
                return en;
            }
        }
        return null;
    }

    public static List<Entity> findEntitiesType( String type, List<Entity> entities  ) {
        List<Entity> localEntities = new ArrayList<>();
        for ( Entity en : entities ) {
            if ( en.getType().equals( type ) ) {
                localEntities.add( en );
            }
        }
        return localEntities;
    }

    public static boolean isNoun( Token tk ) {
        String text = tk.getCoveredText().toUpperCase();
        boolean isSingleValid = !text.equals( 'I' ) && text.length() == 1;
        if ( isSingleValid ) {
            //
            // garbage case. need to move to another function.
            return false;
        }
        return ( tk.getPos().getPosValue().equals( "NN" ) ||
                tk.getPos().getPosValue().equals( "NNS" ) ||
                tk.getPos().getPosValue().equals( "NNP" ) ||
                Models.isPronoun( tk.getCoveredText() ) );
    }

    public static void debugCoreference( Sentence sentence, CoreferenceLink link ) {
        System.out.println( "\n[ Coref sentence ]: " + sentence.getCoveredText() );
        System.out.printf( "[ Coref cur text ]: %s.\n", link.getCoveredText() );
        System.out.printf( "[ Coref cur data ]: pos[%d-%d], ReferenceType[%s], ReferenceRelation[%s], Address[%d].\n",
                link.getBegin(), link.getEnd(), link.getReferenceType(), link.getReferenceRelation(), link.getAddress());
        CoreferenceLink next = link.getNext();
        while ( next != null ) {
            System.out.printf( "[ Coref next text ]: %s.\n", next.getCoveredText() );
            System.out.printf( "[ Coref next data ]: pos[%d-%d], ReferenceType[%s], ReferenceRelation[%s], Address[%d].\n",
                    next.getBegin(), next.getEnd(), next.getReferenceType(), next.getReferenceRelation(), next.getAddress());
            next = next.getNext();
        }
    }

    public static Triplet findTriplet( JCas aJCas, CoreferenceLink link, Sentence prevSentence )
    {
        for ( Triplet triplet : JCasUtil.selectCovered( aJCas, Triplet.class, prevSentence ) ) {
            TripletField subject = triplet.getSubject();
            if ( subject != null && subject.isCoreference() && subject.getBegin() == link.getBegin() ) {
                return triplet;
            }
        }
        return null;
    }

    public static String listOfStringToString( List<String> list, String separator ) {
        String out = "";
        for ( String element : list ) {
            out = out + element + separator;
        }
        return out;
    }

    //
    // Now, it is only very simplified relations compatibility metric: if
    // || relations_union( triplet_set, question_set ) || >= 1 then returns 'true',
    // otherwise, 'false'.
    // In future, this method could implement Cartesian product.
    public static boolean isRelationsCompatible( Triplet triplet, Question question ) {
        for ( String qDependency : question.getRelationTypes() ) {
            for ( String qRelation : question.getRelations( qDependency ) ) {
                for ( String trDependency : triplet.getRelationTypes() ) {
                    for ( Token trRelation : triplet.getRelations( trDependency ) ) {
                        if ( trRelation.getLemma().getValue().toLowerCase().equals( qRelation ) ) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isNoun( Entity entity ) {
        return  entity.getName().getPos().getPosValue().equals( "NN" ) ||
                entity.getName().getPos().getPosValue().equals( "NNS" );
    }

    public static boolean isAdjective( Entity entity ) {
        // 'ADJ' ?
        return entity.getName().getPos().getPosValue().equals( "JJ" );
    }

    public static boolean isVerb( Entity entity ) {
        return  entity.getName().getPos().getPosValue().equals( "VB" ) ||
                entity.getName().getPos().getPosValue().equals( "VBD" ) ||
                entity.getName().getPos().getPosValue().equals( "VBN" );
    }

    public static void parseProperties( TripletField field, Entity entity ) {
        for ( Entity en : Utils.findEntitiesType( "AMOD", entity.getChildren() ) ) {
            field.addAttribute( TripletField.AttributeType.DESCRIPTION_ENTITY, en.getName() );
            for ( Entity deepEn : Utils.findEntitiesType( "CONJ", en.getChildren() ) ) {
                field.addAttribute( TripletField.AttributeType.DESCRIPTION_ENTITY, deepEn.getName() );
            }
        }
        for ( Entity en : Utils.findEntitiesType( "NN", entity.getChildren() ) ) {
            field.addAttribute( TripletField.AttributeType.DESCRIPTION_ENTITY, en.getName() );
            for ( Entity deepEn : Utils.findEntitiesType( "CONJ", en.getChildren() ) ) {
                field.addAttribute( TripletField.AttributeType.DESCRIPTION_ENTITY, deepEn.getName() );
            }
        }
    }

    public static void parseDefinition( Triplet triplet, Entity entity ) {
        final int DEFINITION_THRESHOLD = 1;
//        System.out.println( "parseDefinition: " + entity.getName().getCoveredText() + '/' + entity.getType() );
        HashMap<Integer, Integer> end = new HashMap<>(1);
        end.put( 1, Constants.INVALID_VALUE );
        flowEntity( 1, end, entity );
        int localEnd = end.get( 1 ).intValue();
        if ( localEnd != Constants.INVALID_VALUE ) {
            int beginIndex = entity.getName().getEnd() - triplet.getBegin() + 1;
            int endIndex = localEnd - triplet.getBegin();
            if ( ( endIndex - beginIndex ) > DEFINITION_THRESHOLD ) {
                try {
                    TripletDefinition def = new TripletDefinition( triplet.getCAS().getJCas(), triplet, beginIndex, endIndex );
                    triplet.setDefinition( def );
                } catch ( CASException ex ) {
                    System.out.println( "Exception, parseDefinition/CASException." );
                }
            }
        }
    }

    private static void flowEntity( int level, HashMap<Integer, Integer> end, Entity entity ) {
        for ( Entity en : entity.getChildren() ) {
            if ( level == 1 && !( en.getType().equals( "PREP" ) || en.getType().equals( "XCOMP" ) ) ) {
                //
                // 'PREP'  -- prepositions ('but', 'between', 'for', 'of')
                // 'XCOMP' -- open clauses ('to')
                //
                continue;
            }
            if ( isDefinitionAllowedDependency( en.getType() ) ) {
                int localEnd = end.get( 1 ).intValue();
                if ( en.getName().getEnd() > localEnd ) {
                    end.put( 1, en.getName().getEnd() );
                }
                flowEntity( level + 1, end, en );
            }
        }
    }

    private static boolean isDefinitionAllowedDependency( String dependency ) {
        return  dependency.equals( "PREP" )||
                dependency.equals( "DET" ) ||
                dependency.equals( "AMOD" )||
                dependency.equals( "CONJ" )||
                dependency.equals( "NN" );
    }
}
