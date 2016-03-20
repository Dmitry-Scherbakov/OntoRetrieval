package com.ontological.retrieval.Normalization;

import com.ontological.retrieval.Exceptions.InvalidOutPath;
import com.ontological.retrieval.Exceptions.NotSupportedLanguage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @brief  This class implements the plain text normalization. Now there is 2
 *         algorithms for normalization:
 *
 *         1. If line ends without 'dot' symbol, the 'dot' will be added.
 *         2. All single/double quotes/brackets will be replace by the universal format.
 *
 *         The result of normalized text will be saved to the given directory.
 *
 * @author Dmitry Scherbakov
 * @email  dm.scherbakov[_d0g_]yandex.ru
 */
public class TextNormalizer
{
    public static enum Language {
        EN,
        RUS
    }

    private File m_File;
    private String m_OutDirectory;

    public TextNormalizer( Language lang, String fileName, String outDirectory ) throws FileNotFoundException, InvalidOutPath, NotSupportedLanguage {
        if ( !isSupportedLang( lang ) ) {
            throw new NotSupportedLanguage( "Lang " + lang.toString() + " is currently not supported." );
        }
        m_File = new File( fileName );
        if ( !m_File.exists() ) {
            throw new FileNotFoundException();
        }
        File out = new File( outDirectory );
        if ( !out.exists() || out.isFile() ) {
            throw new InvalidOutPath( "Directory path " + outDirectory + " is not valid." );
        }
        m_OutDirectory = outDirectory + '\\' + m_File.getName();
    }

    public boolean normalize() {
        try {
            try ( Scanner scanner = new Scanner( m_File ) ) {
                List<String> lines = new ArrayList<>();
                while ( scanner.hasNextLine() ) {
                    String line = scanner.nextLine();
                    if ( !line.isEmpty() ) {
                        lines.add( processLine( line ) );
                    }
                }
                if ( lines.size() > 0 ) {
                    try ( PrintWriter writer = new PrintWriter( m_OutDirectory, "UTF-8" ) ) {
                        for ( String line : lines ) {
                            writer.write( line );
                        }
                    }
                }
            }
        } catch ( FileNotFoundException ex ) {
            System.out.println( "TextNormalizer.normalize:FileNotFoundException" );
            return false;
        } catch ( UnsupportedEncodingException ex ) {
            System.out.println( "TextNormalizer.normalize:UnsupportedEncodingException" );
            return false;
        }
        return true;
    }

    public String getNormalizedPath() {
        return m_OutDirectory;
    }

    private String processLine( String line ) {
        //
        // See @link http://unicode-table.com/ru/search/?q=Quotation+ for
        // symbols description and visualization.
        //

        //
        // @note handling symbols replacement is language specific; add appropriate
        //       validation in future.
        //

        // normalize single quites
        line = line.replaceAll( "\\u2019", "\'" );
        line = line.replaceAll( "\\u2018", "\'" );
        line = line.replaceAll( "\\u201A", "\'" );
        line = line.replaceAll( "\\u275C", "\'" );
        line = line.replaceAll( "\\u201B", "\'" );
        line = line.replaceAll( "\\u275B", "\'" );

        // normalize double quites
        line = line.replaceAll( "\\u201C", "\"" );
        line = line.replaceAll( "\\u201D", "\"" );
        line = line.replaceAll( "\\u201E", "\"" );
        line = line.replaceAll( "\\u275E", "\"" );
        line = line.replaceAll( "\\u201F", "\"" );
        line = line.replaceAll( "\\u2E42", "\"" );
        line = line.replaceAll( "\\u275D", "\"" );
        line = line.replaceAll( "\\u2760", "\"" );
        line = line.replaceAll( "\\uFF02", "\"" );
        line = line.replaceAll( "\\u301E", "\"" );
        line = line.replaceAll( "\\u301D", "\"" );
        line = line.replaceAll( "\\u301F", "\"" );

        // normalize opening double brackets
        line = line.replaceAll( "\\u00AB", "\"" );
        line = line.replaceAll( "\\u2039", "\"" );
        line = line.replaceAll( "\\u276E", "\"" );

        // normalize closing double brackets
        line = line.replaceAll( "\\u00BB", "\"" );
        line = line.replaceAll( "\\u203A", "\"" );
        line = line.replaceAll( "\\u276F", "\"" );

        // normalize ending; string break termination
        char lastChar = line.charAt( line.length() - 1 );
        if ( (lastChar >= 'a' || lastChar >= 'A') && (lastChar <= 'z' || lastChar <= 'Z') ) {
            line += '.';
        }
        return line + '\n';
    }

    private boolean isSupportedLang( Language lang ) {
        for ( Language lng : Language.values() ) {
            if ( lng == lang && isLangEnabled( lang ) ) {
                return true;
            }
        }
        return false;
    }

    private boolean isLangEnabled( Language lang ) {
        // for future usage
        return true;
    }
}
