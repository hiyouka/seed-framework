package seed.seedframework.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ExpressionParseException extends SeedCoreException {

    private final String expression;

    private static final String message = "expression parse error ";

    public ExpressionParseException(String expression){
        super(expression + "," +  expression );
        this.expression = expression;
    }

    public ExpressionParseException(String message,String expression) {
        super(message + "," + expression);
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }
}