package seed.seedframework.aop.interceptor;

import net.sf.cglib.proxy.MethodProxy;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;
import seed.seedframework.core.intercept.InterceptorChainMethodInvocation;
import seed.seedframework.core.intercept.MethodInterceptor;
import seed.seedframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class CglibInterceptorChainMethodInvocation extends InterceptorChainMethodInvocation implements  AspectjMethodInvocation{

    private MethodProxy methodProxy;

    public CglibInterceptorChainMethodInvocation(List<MethodInterceptor> chain, Method method, Object target, Object[] args, MethodProxy methodProxy) {
        super(chain, method, target, args);
        this.methodProxy = methodProxy;
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

    @Override
    protected Object invokeMethod(Method method, Object target, Object... args) throws Throwable {
        if(ClassUtils.isCglibProxyClass(target.getClass())){
            return this.methodProxy.invokeSuper(target,args);
        }
        else {
            return this.methodProxy.invoke(target, args);
        }
    }

    @Override
    public void set$AroundClosure(AroundClosure arc) {

    }

    @Override
    public Object proceed() throws Throwable {
        return this.process();
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        // todo arguments method match to invoke
        return this.process();
    }
}
