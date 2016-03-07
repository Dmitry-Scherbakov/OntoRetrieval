package com.ontological.retrieval.DataTypes;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.tcas.Annotation_Type;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov@yandex.ru
 */
public class Triplet_Type extends Annotation_Type
{
    @Override
    protected FSGenerator getFSGenerator() {return fsGenerator;}

    private final FSGenerator fsGenerator =
            new FSGenerator() {
                public FeatureStructure createFS( int addr, CASImpl cas ) {
                    if ( Triplet_Type.this.useExistingInstance ) {
                        FeatureStructure fs = Triplet_Type.this.jcas.getJfsFromCaddr( addr );
                        if ( fs == null ) {
                            fs = new Triplet( addr, Triplet_Type.this );
                            Triplet_Type.this.jcas.putJfsFromCaddr( addr, fs );
                        }
                        return fs;
                    } else return new Triplet( addr, Triplet_Type.this );
                }
            };

    public final static int typeIndexID = Triplet.typeIndexID;

    @SuppressWarnings ("hiding")
    public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.ontological.retrieval.DataTypes.Triplet");

    final Feature subject_f;
    final int subject_code;

    final Feature object_f;
    final int object_code;

    final Feature relation_f;
    final int relation_code;

    final Feature score_f;
    final int score_code;

    final Feature isRelation_f;
    final int isRelation_code;

    final Feature isValid_f;
    final int isValid_code;

    final Feature clone_f;
    final int clone_code;

    final Feature context_f;
    final int context_code;

    public int getSubject( int addr )
    {
        if ( featOkTst && subject_f == null ) {
            jcas.throwFeatMissing("subject", "com.ontological.retrieval.DataTypes.TripletField");
        }
        return ll_cas.ll_getRefValue( addr, subject_code );
    }

    public void setSubject( int addr, int v )
    {
        if ( featOkTst && subject_f == null ) {
            jcas.throwFeatMissing("subject", "com.ontological.retrieval.DataTypes.TripletField");
        }
        ll_cas.ll_setRefValue( addr, subject_code, v );
    }

    public int getObject( int addr )
    {
        if ( featOkTst && object_f == null ) {
            jcas.throwFeatMissing( "object", "com.ontological.retrieval.DataTypes.TripletField" );
        }
        return ll_cas.ll_getRefValue( addr, subject_code );
    }

    public void setObject( int addr, int v )
    {
        if ( featOkTst && object_f == null ) {
            jcas.throwFeatMissing( "object", "com.ontological.retrieval.DataTypes.TripletField" );
        }
        ll_cas.ll_setRefValue( addr, object_code, v );
    }

    public int getScore( int addr )
    {
        if ( featOkTst && score_f == null ) {
            jcas.throwFeatMissing( "score", "com.ontological.retrieval.DataTypes.TripletScore" );
        }
        return ll_cas.ll_getRefValue( addr, score_code );
    }

    public void setScore( int addr, int v )
    {
        if ( featOkTst && score_f == null ) {
            jcas.throwFeatMissing( "score", "com.ontological.retrieval.DataTypes.TripletScore" );
        }
        ll_cas.ll_setRefValue( addr, score_code, v );
    }

    public int getRelations( int addr )
    {
        if ( featOkTst && relation_f == null ) {
            jcas.throwFeatMissing( "relation", "uima.cas.FSList" );
        }
        return ll_cas.ll_getRefValue( addr, relation_code );
    }

    public void addRelations( int addr, int v )
    {
        if ( featOkTst && relation_f == null ) {
            jcas.throwFeatMissing( "relation", "uima.cas.FSList" );
        }
        ll_cas.ll_setRefValue( addr, relation_code, v );
    }

    public int isRelation( int addr, int v )
    {
        if ( featOkTst && isRelation_f == null ) {
            jcas.throwFeatMissing( "isRelation", "uima.cas.Boolean" );
        }
        return ll_cas.ll_getRefValue( addr, isRelation_code );
    }

    public int isValid( int addr, int v )
    {
        if ( featOkTst && isRelation_f == null ) {
            jcas.throwFeatMissing( "isValid", "uima.cas.Boolean" );
        }
        return ll_cas.ll_getRefValue( addr, isValid_code );
    }

    public int clone( int addr, int v )
    {
        if ( featOkTst && clone_f == null ) {
            jcas.throwFeatMissing( "clone", "com.ontological.retrieval.DataTypes.Triplet" );
        }
        return ll_cas.ll_getRefValue( addr, clone_code );
    }

    public int getContext( int addr )
    {
        if ( featOkTst && context_f == null ) {
            jcas.throwFeatMissing( "context", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence" );
        }
        return ll_cas.ll_getRefValue( addr, context_code );
    }

    public Triplet_Type( JCas jcas, Type casType )
    {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

        subject_f = jcas.getRequiredFeatureDE(casType, "subject", "com.ontological.retrieval.DataTypes.TripletField", featOkTst);
        subject_code  = (null == subject_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)subject_f).getCode();

        object_f = jcas.getRequiredFeatureDE(casType, "object", "com.ontological.retrieval.DataTypes.TripletField", featOkTst);
        object_code  = (null == object_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)object_f).getCode();

        relation_f = jcas.getRequiredFeatureDE(casType, "relation", "uima.cas.FSList", featOkTst);
        relation_code  = (null == relation_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)relation_f).getCode();

        isRelation_f = jcas.getRequiredFeatureDE(casType, "isRelation", "uima.cas.Boolean", featOkTst);
        isRelation_code  = (null == isRelation_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)isRelation_f).getCode();

        isValid_f = jcas.getRequiredFeatureDE(casType, "isValid", "uima.cas.Boolean", featOkTst);
        isValid_code  = (null == isValid_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)isValid_f).getCode();

        clone_f = jcas.getRequiredFeatureDE(casType, "clone", "com.ontological.retrieval.DataTypes.Triplet", featOkTst);
        clone_code  = (null == clone_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)clone_f).getCode();

        score_f = jcas.getRequiredFeatureDE(casType, "score", "com.ontological.retrieval.DataTypes.TripletScore", featOkTst);
        score_code  = (null == score_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)score_f).getCode();

        context_f = jcas.getRequiredFeatureDE(casType, "context", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence", featOkTst);
        context_code  = (null == context_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)context_f).getCode();
    }
}
