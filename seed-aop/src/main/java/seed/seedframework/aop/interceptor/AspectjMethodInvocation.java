package seed.seedframework.aop.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import seed.seedframework.core.intercept.MethodInvocation;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface AspectjMethodInvocation extends MethodInvocation, ProceedingJoinPoint {



}