package hiyouka.seedframework.util;

import java.util.Collection;

/**
 * @author hiyouka
 * Date: 2019/2/21
 * @since JDK 1.8
 */
public class StringUtils {

    public static String[] toStringArray(Collection<String> collection) {
        return collection.toArray(new String[0]);
    }

    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }


    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }
}