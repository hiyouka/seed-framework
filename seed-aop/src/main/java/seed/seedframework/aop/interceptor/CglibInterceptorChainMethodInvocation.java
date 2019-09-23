package seed.seedframework.aop.interceptor;

import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import seed.seedframework.core.intercept.InterceptorChainMethodInvocation;
import seed.seedframework.core.intercept.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class CglibInterceptorChainMethodInvocation extends InterceptorChainMethodInvocation implements  AspectjMethodInvocation{

    public CglibInterceptorChainMethodInvocation(List<MethodInterceptor> chain, Method method, Object target, Object[] args) {
        super(chain, method, target, args);
    }

    @Override
    public String toShortString() {
        return null;
    }

    @Override
    public String toLongString() {
        return null;
    }

    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    public Object[] getArgs() {
        return new Object[0];
    }

    @Override
    public Signature getSignature() {
        return null;
    }

    @Override
    public SourceLocation getSourceLocation() {
        return null;
    }

    @Override
    public String getKind() {
        return null;
    }

    @Override
    public StaticPart getStaticPart() {
        return null;
    }
}