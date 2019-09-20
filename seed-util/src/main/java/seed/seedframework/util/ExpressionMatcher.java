package seed.seedframework.util;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ExpressionMatcher <T>{

    String allMatch = "*";

    char allMatchChar = '*';

    boolean match(String expression, T obj) throws Throwable;

}