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
public class TripletDefinition_Type extends Annotation_Type
{
    @Override
    protected FSGenerator getFSGenerator() {return fsGenerator;}

    private final FSGenerator fsGenerator =
            new FSGenerator() {
                public FeatureStructure createFS( int addr, CASImpl cas ) {
                    if ( TripletDefinition_Type.this.useExistingInstance ) {
                        FeatureStructure fs = TripletDefinition_Type.this.jcas.getJfsFromCaddr( addr );
                        if ( fs == null ) {
                            fs = new TripletDefinition( addr, TripletDefinition_Type.this );
                            TripletDefinition_Type.this.jcas.putJfsFromCaddr( addr, fs );
                        }
                        return fs;
                    } else return new TripletDefinition( addr, TripletDefinition_Type.this );
                }
            };

    public final static int typeIndexID = TripletDefinition.typeIndexID;

    @SuppressWarnings ("hiding")
    public final static boolean featOkTst = JCasRegistry.getFeatOkTst( "com.ontological.retrieval.DataTypes.TripletDefinition" );

    final Feature namedEntities_f;
    final int namedEntities_Code;

    public int getAttributes( int addr )
    {
        if ( featOkTst && namedEntities_f == null ) {
            jcas.throwFeatMissing( "getNamedEntities", "uima.cas.FSList" );
        }
        return ll_cas.ll_getRefValue( addr, namedEntities_Code );
    }

    public TripletDefinition_Type(JCas jcas, Type casType )
    {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

        namedEntities_f = jcas.getRequiredFeatureDE(casType, "getNamedEntities", "uima.cas.FSList", featOkTst);
        namedEntities_Code = (null == namedEntities_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) namedEntities_f).getCode();
    }
}
