package seed.seedframework.aop.pointcut;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParameter;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import seed.seedframework.aop.matcher.AspectMethodMatcher;
import seed.seedframework.aop.matcher.MethodMatcher;
import seed.seedframework.util.Assert;
import seed.seedframework.util.ClassUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectPointcut implements ExpressionPointcut {

    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    private final Class<?> scopePointClass;

    private final String expression;

    private volatile MethodMatcher methodMatcher;

    public AspectPointcut (String expression,Class<?> scopePointClass){
        Assert.notNull(expression,"expression must not null");
        this.expression = expression;
        this.scopePointClass = scopePointClass;
    }

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    @Override
    public MethodMatcher methodMatch() {
        if(methodMatcher == null){
            synchronized (this.methodMatcher){
                PointcutParser pointcutParser = PointcutParser
                        .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                                SUPPORTED_PRIMITIVES, ClassUtils.getDefaultClassLoader());
                PointcutExpression pointcutExpression = pointcutParser
                        .parsePointcutExpression(this.expression, this.scopePointClass, new PointcutParameter[0]);
                this.methodMatcher = new AspectMethodMatcher(pointcutExpression);
            }
        }

        return methodMatcher;
    }


    @Override
    public String getExpression() {
        return this.expression;
    }
}