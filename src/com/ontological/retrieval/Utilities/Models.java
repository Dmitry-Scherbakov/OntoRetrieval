package com.ontological.retrieval.Utilities;

import java.util.HashMap;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov@yandex.ru
 */
public class Models
{
    static final String PR_I = "I";
    static final String PR_YOU = "YOU";
    static final String PR_HE = "HE";
    static final String PR_SHE = "SHE";
    static final String PR_IT = "IT";
    static final String PR_THEY = "THEY";

    static final String PR_THAT = "THAT";
    static final String PR_THEM = "THEM";

    static HashMap<Integer,String> m_PronounModel;

    public static boolean isPronoun( String word ) {
        if ( m_PronounModel == null ) {
            m_PronounModel = new HashMap<>();
            m_PronounModel.put( PR_I.hashCode(), PR_I );
            m_PronounModel.put( PR_YOU.hashCode(), PR_YOU );
            m_PronounModel.put( PR_HE.hashCode(), PR_HE );
            m_PronounModel.put( PR_SHE.hashCode(), PR_SHE );
            m_PronounModel.put( PR_IT.hashCode(), PR_IT );
            m_PronounModel.put( PR_THEY.hashCode(), PR_THEY );
            m_PronounModel.put( PR_THAT.hashCode(), PR_THAT );
            m_PronounModel.put( PR_THEM.hashCode(), PR_THEM );
        }
        return m_PronounModel.containsKey( word.toUpperCase().hashCode() );
    }

    //
    // This a special kind of a Pronoun which very often is placed in a current sentence.
    public static boolean isPositionedPronoun( String word ) {
        return word.toUpperCase().equals( PR_THAT );
    }
}
