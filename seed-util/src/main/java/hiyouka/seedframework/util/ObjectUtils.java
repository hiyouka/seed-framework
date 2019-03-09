package hiyouka.seedframework.util;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author hiyouka
 * Date: 2019/1/28
 * @since JDK 1.8
 */
public class ObjectUtils {

    private static final String NULL_STRING = "null";
    private static final String EMPTY_STRING = "";
    private static final String ARRAY_START = "{";
    private static final String ARRAY_END = "}";
    private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;
    private static final String ARRAY_ELEMENT_SEPARATOR = ", ";

    public static boolean isNull(Object object){
        return object == null;
    }

    public static boolean isEmpty(Object... array) {
        return (array == null || array.length == 0);
    }


    public static boolean isToStringMethod(Method method) {
        return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
    }

    public static boolean isHashCodeMethod(Method method) {
        return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
    }

    public enum ObjectType{
        Object,String,Booble,Byte,Char,Double,Float,Int,Long,Short;
    }


    public static boolean nullSafeEquals(@Nullable Object o1, @Nullable Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return arrayEquals(o1, o2);
        }
        return false;
    }

    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) o1, (Object[]) o2);
        }
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        }
        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        }
        if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        }
        if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        }
        if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        }
        if (o1 instanceof short[] && o2 instanceof short[]) {
            return Arrays.equals((short[]) o1, (short[]) o2);
        }
        return false;
    }

    public static String nullSafeToString(@Nullable Object obj) {
        if (obj == null) {
            return NULL_STRING;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Object[]) {
            return nullSafeToString((Object[]) obj);
        }
        if (obj instanceof boolean[]) {
            return nullSafeToString((boolean[]) obj);
        }
        if (obj instanceof byte[]) {
            return nullSafeToString((byte[]) obj);
        }
        if (obj instanceof char[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof double[]) {
            return nullSafeToString((double[]) obj);
        }
        if (obj instanceof float[]) {
            return nullSafeToString((float[]) obj);
        }
        if (obj instanceof int[]) {
            return nullSafeToString((int[]) obj);
        }
        if (obj instanceof long[]) {
            return nullSafeToString((long[]) obj);
        }
        if (obj instanceof short[]) {
            return nullSafeToString((short[]) obj);
        }
        String str = obj.toString();
        return (str != null ? str : EMPTY_STRING);
    }


    public static String nullSafeToString(@Nullable Object[] array) {
        return nullSafeArrayToString(ObjectType.Object,array);
    }

    public static <T> String nullSafeToString(@Nullable char[] array) {
        return nullSafeArrayToString(ObjectType.Char,array);
    }

    public static <T> String nullSafeToString(@Nullable boolean[] array) {
        return nullSafeArrayToString(ObjectType.Booble,array);
    }

    public static <T> String nullSafeToString(@Nullable byte[] array) {
        return nullSafeArrayToString(ObjectType.Char,array);
    }

    public static <T> String nullSafeToString(@Nullable double[] array) {
        return nullSafeArrayToString(ObjectType.Char,array);
    }

    public static <T> String nullSafeToString(@Nullable float[] array) {
        return nullSafeArrayToString(ObjectType.Char,array);
    }

    public static <T> String nullSafeToString(@Nullable int[] array) {
        return nullSafeArrayToString(ObjectType.Char,array);
    }

    public static <T> String nullSafeToString(@Nullable long[] array) {
        return nullSafeArrayToString(ObjectType.Char,array);
    }

    public static <T> String nullSafeToString(@Nullable short[] array) {
        return nullSafeArrayToString(ObjectType.Char,array);
    }

    private static <T> String nullSafeArrayToString(ObjectType type, T... array){
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        boolean obj = type == ObjectType.Object;
        boolean cha = type == ObjectType.Char;
        StringBuilder sb = new StringBuilder();

        if(obj){
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    sb.append(ARRAY_START);
                }
                else {
                    sb.append(ARRAY_ELEMENT_SEPARATOR);
                }
                sb.append(String.valueOf(array[i]));

            }
        }else if(cha){
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    sb.append(ARRAY_START);
                }
                else {
                    sb.append(ARRAY_ELEMENT_SEPARATOR);
                }
                sb.append("'").append(array[i]).append("'");

            }
        }else {
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    sb.append(ARRAY_START);
                }
                else {
                    sb.append(ARRAY_ELEMENT_SEPARATOR);
                }
                sb.append(array[i]);
            }
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }


}