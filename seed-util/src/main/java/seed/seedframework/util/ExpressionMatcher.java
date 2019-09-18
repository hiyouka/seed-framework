package seed.seedframework.util;

import seed.seedframework.exception.ExpressionParseException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ExpressionMatcher <T>{

    String allMatch = "*";

    boolean match(String expression, T obj) throws ExpressionParseException;

}