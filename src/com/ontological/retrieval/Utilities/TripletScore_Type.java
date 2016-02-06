package com.ontological.retrieval.Utilities;

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
public class TripletScore_Type extends Annotation_Type
{
    @Override
    protected FSGenerator getFSGenerator() {return fsGenerator;}

    private final FSGenerator fsGenerator =
            new FSGenerator() {
                public FeatureStructure createFS( int addr, CASImpl cas ) {
                    if ( TripletScore_Type.this.useExistingInstance ) {
                        FeatureStructure fs = TripletScore_Type.this.jcas.getJfsFromCaddr( addr );
                        if ( null == fs ) {
                            fs = new Triplet( addr, TripletScore_Type.this );
                            TripletScore_Type.this.jcas.putJfsFromCaddr( addr, fs );
                        }
                        return fs;
                    } else return new TripletScore( addr, TripletScore_Type.this );
                }
            };

    public final static int typeIndexID = TripletScore.typeIndexID;

    @SuppressWarnings ("hiding")
    public final static boolean featOkTst = JCasRegistry.getFeatOkTst( "com.ontological.retrieval.Utilities.TripletScore" );

    final Feature scoreValue_f;
    final int scoreValue_Code;

    final Feature mainPointsCount_f;
    final int mainPointsCount_Code;

    public int getScoreValue( int addr )
    {
        if ( featOkTst && scoreValue_f == null ) {
            jcas.throwFeatMissing("subject", "uima.cas.Double");
        }
        return ll_cas.ll_getRefValue( addr, scoreValue_Code );
    }

    public int getMainPointsCount( int addr )
    {
        if ( featOkTst && mainPointsCount_f == null ) {
            jcas.throwFeatMissing("subject", "uima.cas.Integer");
        }
        return ll_cas.ll_getRefValue( addr, mainPointsCount_Code );
    }

    public TripletScore_Type( JCas jcas, Type casType )
    {
        super( jcas, casType );
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

        scoreValue_f = jcas.getRequiredFeatureDE(casType, "scoreValue", "uima.cas.Double", featOkTst);
        scoreValue_Code  = (null == scoreValue_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)scoreValue_f).getCode();

        mainPointsCount_f = jcas.getRequiredFeatureDE(casType, "mainPointsCount", "uima.cas.Integer", featOkTst);
        mainPointsCount_Code  = (null == mainPointsCount_f) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)mainPointsCount_f).getCode();
    }
}
