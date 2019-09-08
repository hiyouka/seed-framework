package hiyouka.seedframework.beans.factory.paser;

import hiyouka.seedframework.beans.factory.aware.EnvironmentAware;
import hiyouka.seedframework.core.env.Environment;
import hiyouka.seedframework.util.ExpressionResolver;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class SpelExpressionResolver implements ExpressionResolver, EnvironmentAware {

    private static final String DIRECT_VALUE_EXPRESSION_START = "#{";

    private static final String DIRECT_VALUE_EXPRESSION_END = "}";

    private static final String ENVIRONMENT_VALUE_EXPRESSION_START = "${";

    private static final String ENVIRONMENT_VALUE_EXPRESSION_END = "}";

    private Environment environment;

    @Override
    public String resolve(String expression) {
        String key = expression.substring(2,expression.length()-1);
        if (expression.startsWith(DIRECT_VALUE_EXPRESSION_START) &&
            expression.endsWith(DIRECT_VALUE_EXPRESSION_END)) {
            return key;
        }
        else if (expression.startsWith(ENVIRONMENT_VALUE_EXPRESSION_START) &&
                expression.endsWith(ENVIRONMENT_VALUE_EXPRESSION_END)) {
            return environment.getProperty(key);
        }
        return null;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}