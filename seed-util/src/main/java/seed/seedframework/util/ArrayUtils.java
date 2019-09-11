package seed.seedframework.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ArrayUtils {

    public static <T> List<T> asList(T... array){
        if(array == null)
            return null;
        return Arrays.asList(array);
    }

    public static <T> boolean isEmpty(T[] array){
        return array == null || array.length == 0;
    }

}