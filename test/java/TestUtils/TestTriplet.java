package TestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class TestTriplet
{
    private String m_Subject;
    private String m_Object;
    private String m_FormatedRelation;

    private String m_Context;

    private boolean m_IsSubjectCoref;
    private boolean m_IsObjectCoref;

    private String m_Definition;

    private List<NamedEntityShort> m_DefinitionNEs;
    private String m_SubjectFormatedNEs;
    private String m_ObjectFormatedNEs;

    public TestTriplet( String subj,
                        String obj,
                        String rel,
                        String context,
                        String definition,
                        String sbjFormatedNEs,
                        String objFormatedNEs,
                        boolean isSbjCoref,
                        boolean isObjCoref ) {
        m_Subject = subj;
        m_Object = obj;
        m_FormatedRelation = rel;
        m_Context = context;
        m_Definition = definition;
        m_IsSubjectCoref = isSbjCoref;
        m_IsObjectCoref = isObjCoref;
        m_SubjectFormatedNEs = sbjFormatedNEs;
        m_ObjectFormatedNEs = objFormatedNEs;
    }

    public void addDefinitionNE( NamedEntityShort ne ) {
        if ( m_DefinitionNEs == null ) {
            m_DefinitionNEs = new ArrayList<>();
        }
        m_DefinitionNEs.add( ne );
    }

    public String getSubject() {
        return m_Subject;
    }

    public String getObject() {
        return m_Object;
    }

    public String getFormatedRelation() {
        return m_FormatedRelation;
    }

    public String getContext() {
        return m_Context;
    }

    public String getDefinition() {
        return m_Definition;
    }

    public List<NamedEntityShort> getDeginitionNEs() {
        return m_DefinitionNEs;
    }

    public String getSubjectFormatedNEs() {
        return m_SubjectFormatedNEs;
    }

    public String getObjectFormatedNEs() {
        return m_ObjectFormatedNEs;
    }

    public boolean isSubjectCoref() {
        return m_IsSubjectCoref;
    }

    public boolean isObjectCoref() {
        return m_IsObjectCoref;
    }

    public static class NamedEntityShort
    {
        private String m_Type;
        private String m_Value;

        public NamedEntityShort( String type, String value ) {
            m_Type = type;
            m_Value = value;
        }

        public String getType() {
            return m_Type;
        }

        public String getValue() {
            return m_Value;
        }
    }
}
