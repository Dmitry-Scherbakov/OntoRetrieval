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
                        if ( null == fs ) {
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

    final Feature subject;
    final int subjectCode;

    final Feature object;
    final int objectCode;

    final Feature relation;
    final int relationCode;

    final Feature score;
    final int scoreCode;

    final Feature isSubject;
    final int isSubjectCode;

    final Feature isObject;
    final int isObjectCode;

    final Feature isRelation;
    final int isRelationCode;

    final Feature isValid;
    final int isValidCode;

    final Feature clone;
    final int cloneCode;

    public int getSubject( int addr )
    {
        if ( featOkTst && subject == null ) {
            jcas.throwFeatMissing("subject", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token");
        }
        return ll_cas.ll_getRefValue( addr, subjectCode );
    }

    public void setSubject( int addr, int v )
    {
        if ( featOkTst && subject == null ) {
            jcas.throwFeatMissing("subject", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token");
        }
        ll_cas.ll_setRefValue( addr, subjectCode, v );
    }

    public int getObject( int addr )
    {
        if ( featOkTst && object == null ) {
            jcas.throwFeatMissing( "object", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token" );
        }
        return ll_cas.ll_getRefValue( addr, subjectCode );
    }

    public void setObject( int addr, int v )
    {
        if ( featOkTst && object == null ) {
            jcas.throwFeatMissing( "object", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token" );
        }
        ll_cas.ll_setRefValue( addr, objectCode, v );
    }

    public int getScore( int addr )
    {
        if ( featOkTst && score == null ) {
            jcas.throwFeatMissing( "score", "com.ontological.retrieval.Utilities.TripletScore" );
        }
        return ll_cas.ll_getRefValue( addr, scoreCode );
    }

    public void setScore( int addr, int v )
    {
        if ( featOkTst && score == null ) {
            jcas.throwFeatMissing( "score", "com.ontological.retrieval.Utilities.TripletScore" );
        }
        ll_cas.ll_setRefValue( addr, scoreCode, v );
    }

    public int getRelation( int addr )
    {
        if ( featOkTst && relation == null ) {
            jcas.throwFeatMissing( "relation", "uima.cas.String" );
        }
        return ll_cas.ll_getRefValue( addr, relationCode );
    }

    public void setRelation( int addr, int v )
    {
        if ( featOkTst && relation == null ) {
            jcas.throwFeatMissing( "relation", "uima.cas.String" );
        }
        ll_cas.ll_setRefValue( addr, relationCode, v );
    }

    public int isSubject( int addr )
    {
        if ( featOkTst && isSubject == null ) {
            jcas.throwFeatMissing( "isSubject", "uima.cas.Boolean" );
        }
        return ll_cas.ll_getRefValue( addr, isSubjectCode );
    }

    public int isObject( int addr, int v )
    {
        if ( featOkTst && relation == null ) {
            jcas.throwFeatMissing( "isObject", "uima.cas.Boolean" );
        }
        return ll_cas.ll_getRefValue( addr, isObjectCode );
    }

    public int isRelation( int addr, int v )
    {
        if ( featOkTst && isRelation == null ) {
            jcas.throwFeatMissing( "isRelation", "uima.cas.Boolean" );
        }
        return ll_cas.ll_getRefValue( addr, isRelationCode );
    }

    public int isValid( int addr, int v )
    {
        if ( featOkTst && isRelation == null ) {
            jcas.throwFeatMissing( "isValid", "uima.cas.Boolean" );
        }
        return ll_cas.ll_getRefValue( addr, isValidCode );
    }

    public int clone( int addr, int v )
    {
        if ( featOkTst && clone == null ) {
            jcas.throwFeatMissing( "clone", "com.ontological.retrieval.Utilities.Triplet" );
        }
        return ll_cas.ll_getRefValue( addr, cloneCode );
    }

    public Triplet_Type( JCas jcas, Type casType )
    {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

        subject = jcas.getRequiredFeatureDE(casType, "subject", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token", featOkTst);
        subjectCode  = (null == subject) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)subject).getCode();

        object = jcas.getRequiredFeatureDE(casType, "object", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token", featOkTst);
        objectCode  = (null == object) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)object).getCode();

        relation = jcas.getRequiredFeatureDE(casType, "relation", "uima.cas.String", featOkTst);
        relationCode  = (null == relation) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)relation).getCode();

        isRelation = jcas.getRequiredFeatureDE(casType, "isRelation", "uima.cas.Boolean", featOkTst);
        isRelationCode  = (null == isRelation) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)isRelation).getCode();

        isObject = jcas.getRequiredFeatureDE(casType, "isObject", "uima.cas.Boolean", featOkTst);
        isObjectCode  = (null == isObject) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)isObject).getCode();

        isSubject = jcas.getRequiredFeatureDE(casType, "isSubject", "uima.cas.Boolean", featOkTst);
        isSubjectCode  = (null == isSubject) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)isSubject).getCode();

        isValid = jcas.getRequiredFeatureDE(casType, "isValid", "uima.cas.Boolean", featOkTst);
        isValidCode  = (null == isValid) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)isValid).getCode();

        clone = jcas.getRequiredFeatureDE(casType, "clone", "com.ontological.retrieval.Utilities.Triplet", featOkTst);
        cloneCode  = (null == clone) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)clone).getCode();

        score = jcas.getRequiredFeatureDE(casType, "score", "com.ontological.retrieval.Utilities.TripletScore", featOkTst);
        scoreCode  = (null == score) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)score).getCode();
    }
}
