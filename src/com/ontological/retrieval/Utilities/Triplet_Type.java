package com.ontological.retrieval.Utilities;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
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
                    } else return new Token(addr, Triplet_Type.this);
                }
            };

    public final static int typeIndexID = Triplet.typeIndexID;

    @SuppressWarnings ("hiding")
    public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.ontological.retrieval.Utilities.Triplet");

    final Feature subjectCoref_f;
    final int subjectCoref_code;

    final Feature subject_f;
    final int subject_code;

    final Feature objectCoref_f;
    final int objectCoref_code;

    final Feature object_f;
    final int object_code;

    final Feature objectId_f;
    final int objectId_code;

    final Feature subjectId_f;
    final int subjectId_code;

    final Feature relation_f;
    final int relation_code;

    final Feature score_f;
    final int score_code;

    final Feature isSubject_f;
    final int isSubject_code;

    final Feature isObject_f;
    final int isObject_code;

    final Feature isRelation_f;
    final int isRelation_code;

    final Feature isValid_f;
    final int isValid_code;

    final Feature clone_f;
    final int clone_code;

    public int getSubjectCoref( int addr )
    {
        if ( featOkTst && subjectCoref_f == null ) {
            jcas.throwFeatMissing("subjectCoreference", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token");
        }
        return ll_cas.ll_getRefValue( addr, subjectCoref_code );
    }

    public void setSubjectCoref( int addr, int v )
    {
        if ( featOkTst && subjectCoref_f == null ) {
            jcas.throwFeatMissing("subjectCoreference", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token");
        }
        ll_cas.ll_setRefValue( addr, subjectCoref_code, v );
    }

    public int getSubject( int addr )
    {
        if ( featOkTst && subject_f == null ) {
            jcas.throwFeatMissing("subject", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token");
        }
        return ll_cas.ll_getRefValue( addr, subject_code );
    }

    public void setSubject( int addr, int v )
    {
        if ( featOkTst && subject_f == null ) {
            jcas.throwFeatMissing("subject", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token");
        }
        ll_cas.ll_setRefValue( addr, subject_code, v );
    }

    public int getObjectCoref( int addr )
    {
        if ( featOkTst && objectCoref_f == null ) {
            jcas.throwFeatMissing( "objectCoreference", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token" );
        }
        return ll_cas.ll_getRefValue( addr, objectCoref_code );
    }

    public void setObjectCoref( int addr, int v )
    {
        if ( featOkTst && objectCoref_f == null ) {
            jcas.throwFeatMissing( "objectCoreference", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token" );
        }
        ll_cas.ll_setRefValue( addr, objectCoref_code, v );
    }

    public int getObject( int addr )
    {
        if ( featOkTst && object_f == null ) {
            jcas.throwFeatMissing( "object", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token" );
        }
        return ll_cas.ll_getRefValue( addr, subject_code );
    }

    public void setObject( int addr, int v )
    {
        if ( featOkTst && object_f == null ) {
            jcas.throwFeatMissing( "object", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token" );
        }
        ll_cas.ll_setRefValue( addr, object_code, v );
    }

    public int getScore( int addr )
    {
        if ( featOkTst && score_f == null ) {
            jcas.throwFeatMissing( "score", "com.ontological.retrieval.Utilities.TripletScore" );
        }
        return ll_cas.ll_getRefValue( addr, score_code );
    }

    public void setScore( int addr, int v )
    {
        if ( featOkTst && score_f == null ) {
            jcas.throwFeatMissing( "score", "com.ontological.retrieval.Utilities.TripletScore" );
        }
        ll_cas.ll_setRefValue( addr, score_code, v );
    }

    public int getRelation( int addr )
    {
        if ( featOkTst && relation_f == null ) {
            jcas.throwFeatMissing( "relation", "uima.cas.String" );
        }
        return ll_cas.ll_getRefValue( addr, relation_code );
    }

    public void setRelation( int addr, int v )
    {
        if ( featOkTst && relation_f == null ) {
            jcas.throwFeatMissing( "relation", "uima.cas.String" );
        }
        ll_cas.ll_setRefValue( addr, relation_code, v );
    }

    public int getSubjectId( int addr )
    {
        if ( featOkTst && subjectId_f == null ) {
            jcas.throwFeatMissing( "subjectId", "uima.cas.String" );
        }
        return ll_cas.ll_getRefValue( addr, subjectId_code );
    }

    public int getObjectId( int addr )
    {
        if ( featOkTst && objectId_f == null ) {
            jcas.throwFeatMissing( "objectId", "uima.cas.String" );
        }
        return ll_cas.ll_getRefValue( addr, objectId_code );
    }

    public int isSubject( int addr )
    {
        if ( featOkTst && isSubject_f == null ) {
            jcas.throwFeatMissing( "isSubject", "uima.cas.Boolean" );
        }
        return ll_cas.ll_getRefValue( addr, isSubject_code );
    }

    public int isObject( int addr, int v )
    {
        if ( featOkTst && relation_f == null ) {
            jcas.throwFeatMissing( "isObject", "uima.cas.Boolean" );
        }
        return ll_cas.ll_getRefValue( addr, isObject_code );
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
            jcas.throwFeatMissing( "clone", "com.ontological.retrieval.Utilities.Triplet" );
        }
        return ll_cas.ll_getRefValue( addr, clone_code );
    }

    public Triplet_Type( JCas jcas, Type casType )
    {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

        subjectCoref_f = jcas.getRequiredFeatureDE(casType, "subjectCoreference", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token", featOkTst);
        subjectCoref_code  = (null == subjectCoref_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)subjectCoref_f).getCode();

        subject_f = jcas.getRequiredFeatureDE(casType, "subject", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token", featOkTst);
        subject_code  = (null == subject_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)subject_f).getCode();

        subjectId_f = jcas.getRequiredFeatureDE(casType, "subjectId", "uima.cas.String", featOkTst);
        subjectId_code  = (null == subjectId_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)subjectId_f).getCode();

        objectCoref_f = jcas.getRequiredFeatureDE(casType, "objectCoreference", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token", featOkTst);
        objectCoref_code  = (null == objectCoref_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)objectCoref_f).getCode();

        object_f = jcas.getRequiredFeatureDE(casType, "object", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token", featOkTst);
        object_code  = (null == object_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)object_f).getCode();

        objectId_f = jcas.getRequiredFeatureDE(casType, "objectId", "uima.cas.String", featOkTst);
        objectId_code  = (null == objectId_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)objectId_f).getCode();

        relation_f = jcas.getRequiredFeatureDE(casType, "relation", "uima.cas.String", featOkTst);
        relation_code  = (null == relation_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)relation_f).getCode();

        isRelation_f = jcas.getRequiredFeatureDE(casType, "isRelation", "uima.cas.Boolean", featOkTst);
        isRelation_code  = (null == isRelation_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)isRelation_f).getCode();

        isObject_f = jcas.getRequiredFeatureDE(casType, "isObject", "uima.cas.Boolean", featOkTst);
        isObject_code  = (null == isObject_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)isObject_f).getCode();

        isSubject_f = jcas.getRequiredFeatureDE(casType, "isSubject", "uima.cas.Boolean", featOkTst);
        isSubject_code  = (null == isSubject_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)isSubject_f).getCode();

        isValid_f = jcas.getRequiredFeatureDE(casType, "isValid", "uima.cas.Boolean", featOkTst);
        isValid_code  = (null == isValid_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)isValid_f).getCode();

        clone_f = jcas.getRequiredFeatureDE(casType, "clone", "com.ontological.retrieval.Utilities.Triplet", featOkTst);
        clone_code  = (null == clone_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)clone_f).getCode();

        score_f = jcas.getRequiredFeatureDE(casType, "score", "com.ontological.retrieval.Utilities.TripletScore", featOkTst);
        score_code  = (null == score_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)score_f).getCode();
    }
}
