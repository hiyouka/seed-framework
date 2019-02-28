package hiyouka.seedframework.util;


/**
 * @author hiyouka
 * Date: 2019/1/28
 * @since JDK 1.8
 */
public class Assert {

    public static void notEmpty( Object[] array, String message) {
        if (ObjectUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }


    public static void notNull(Object item, String message) {
        if (ObjectUtils.isEmpty(item)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasText(String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 表达式
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }
}