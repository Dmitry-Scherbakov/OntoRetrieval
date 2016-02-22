package com.ontological.retrieval.Utilities;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.springframework.util.DigestUtils;

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
}
