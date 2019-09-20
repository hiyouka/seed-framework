package seed.seedframework.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    public static <T> T[] deleteElement(T[] array,T element){
        List<T> resultList = new ArrayList<>();
        for(T e : array){
            if(!e.equals(element)){
                resultList.add(e);
            }
        }
        Class<? extends T[]> newType = (Class<? extends T[]>) array.getClass();
        T[] copy = (T[]) Array.newInstance(newType.getComponentType(), resultList.size());
        return resultList.toArray(copy);
    }

}