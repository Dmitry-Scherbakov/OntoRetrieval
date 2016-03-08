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
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class TripletField_Type extends Annotation_Type
{
    @Override
    protected FSGenerator getFSGenerator() {return fsGenerator;}

    private final FSGenerator fsGenerator =
            new FSGenerator() {
                public FeatureStructure createFS( int addr, CASImpl cas ) {
                    if ( TripletField_Type.this.useExistingInstance ) {
                        FeatureStructure fs = TripletField_Type.this.jcas.getJfsFromCaddr( addr );
                        if ( fs == null ) {
                            fs = new TripletField( addr, TripletField_Type.this );
                            TripletField_Type.this.jcas.putJfsFromCaddr( addr, fs );
                        }
                        return fs;
                    } else return new TripletField( addr, TripletField_Type.this );
                }
            };

    public final static int typeIndexID = TripletField.typeIndexID;

    @SuppressWarnings ("hiding")
    public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.ontological.retrieval.DataTypes.TripletField");

    final Feature attributes_f;
    final int attributes_code;

    final Feature field_f;
    final int field_code;

    final Feature fieldCoref_f;
    final int fieldCoref_code;

    final Feature isCoref_f;
    final int isCoref_code;

    final Feature isValid_f;
    final int isValid_code;

    final Feature id_f;
    final int id_code;

    final Feature clone_f;
    final int clone_code;

    public int getAttributes( int addr )
    {
        if ( featOkTst && attributes_f == null ) {
            jcas.throwFeatMissing("attributes", "uima.cas.FSList");
        }
        return ll_cas.ll_getRefValue( addr, attributes_code );
    }

    public void addAttribute( int addr, int v )
    {
        if ( featOkTst && attributes_f == null ) {
            jcas.throwFeatMissing("attributes", "uima.cas.FSList");
        }
        ll_cas.ll_setRefValue( addr, attributes_code, v );
    }

    public void setFieldCoref( int addr, int v )
    {
        if ( featOkTst && fieldCoref_f == null ) {
            jcas.throwFeatMissing("setFieldCoref", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token");
        }
        ll_cas.ll_setRefValue( addr, fieldCoref_code, v );
    }

    public int getField( int addr )
    {
        if ( featOkTst && field_f == null ) {
            jcas.throwFeatMissing("getField", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token");
        }
        return ll_cas.ll_getRefValue( addr, field_code );
    }

    public int isCoreference( int addr )
    {
        if ( featOkTst && isCoref_f == null ) {
            jcas.throwFeatMissing( "isCoreference", "uima.cas.Boolean" );
        }
        return ll_cas.ll_getRefValue( addr, isCoref_code );
    }

    public int isValid( int addr )
    {
        if ( featOkTst && isValid_f == null ) {
            jcas.throwFeatMissing( "isValid", "uima.cas.Boolean" );
        }
        return ll_cas.ll_getRefValue( addr, isValid_code );
    }

    public int getId( int addr )
    {
        if ( featOkTst && id_f == null ) {
            jcas.throwFeatMissing( "getField", "uima.cas.String" );
        }
        return ll_cas.ll_getRefValue( addr, id_code );
    }

    public int clone( int addr, int v )
    {
        if ( featOkTst && clone_f == null ) {
            jcas.throwFeatMissing( "clone", "com.ontological.retrieval.DataTypes.TripletField" );
        }
        return ll_cas.ll_getRefValue( addr, clone_code );
    }

    public TripletField_Type( JCas jcas, Type casType )
    {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

        attributes_f = jcas.getRequiredFeatureDE(casType, "attributes", "uima.cas.FSList", featOkTst);
        attributes_code = (null == attributes_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) attributes_f).getCode();

        field_f = jcas.getRequiredFeatureDE(casType, "getField", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token", featOkTst);
        field_code = (null == field_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) field_f).getCode();

        fieldCoref_f = jcas.getRequiredFeatureDE(casType, "setFieldCoref", "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token", featOkTst);
        fieldCoref_code = (null == fieldCoref_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) fieldCoref_f).getCode();

        isCoref_f = jcas.getRequiredFeatureDE(casType, "isCoreference", "uima.cas.Boolean", featOkTst);
        isCoref_code = (null == isCoref_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) isCoref_f).getCode();

        isValid_f = jcas.getRequiredFeatureDE(casType, "isValid", "uima.cas.Boolean", featOkTst);
        isValid_code = (null == isValid_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) isValid_f).getCode();

        id_f = jcas.getRequiredFeatureDE(casType, "getId", "uima.cas.String", featOkTst);
        id_code = (null == id_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) id_f).getCode();

        clone_f = jcas.getRequiredFeatureDE(casType, "clone", "com.ontological.retrieval.DataTypes.TripletField", featOkTst);
        clone_code = (null == clone_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) clone_f).getCode();
    }
}
