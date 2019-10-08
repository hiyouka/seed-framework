package seed.seedframework.aop.matcher;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectMethodMatcher implements MethodMatcher {

    private Map<Method, ShadowMatch> methodMatchCache = new ConcurrentHashMap<>();

    private final PointcutExpression pointcutExpression;


    public AspectMethodMatcher(PointcutExpression pointcutExpression) {
        this.pointcutExpression = pointcutExpression;
    }

    @Override
    public boolean match(Method method) {
        ShadowMatch shadowMatch = methodMatchCache.get(method);
//        shadowMatch.matchesJoinPoint()

        if(shadowMatch == null){
            shadowMatch = pointcutExpression.matchesMethodExecution(method);
            methodMatchCache.put(method,shadowMatch);
        }
        return shadowMatch.alwaysMatches();
    }

    public ShadowMatch getShowMatch(Method method){
        return this.methodMatchCache.get(method);
    }

}