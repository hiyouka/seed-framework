package seed.seedframework.aop.interceptor;

import seed.seedframework.aop.adapter.PointcutAdvisor;
import seed.seedframework.aop.pointcut.AspectPointcut;
import seed.seedframework.aop.pointcut.Pointcut;
import seed.seedframework.common.AnnotationAttributes;
import seed.seedframework.core.Order;
import seed.seedframework.core.intercept.Advice;
import seed.seedframework.util.AnnotatedElementUtils;
import seed.seedframework.util.Assert;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectJPointcutAdvisor implements PointcutAdvisor {

    private AspectPointcut pointcut;

    private AbstractAspectJAdvice advice;

    private Integer order;

    public AspectJPointcutAdvisor(AbstractAspectJAdvice advice) {
        Assert.notNull(advice,"advice must not null");
        this.advice = advice;
        this.pointcut = this.advice.getAspectPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public int getOrder() {
        if(this.order == null){
            Method aspectJMethod = advice.getAspectJMethod();
            AnnotationAttributes attributes = AnnotatedElementUtils
                    .getAnnotationAttributes(aspectJMethod, Order.class.getName());
            if(attributes != null){
                this.order = attributes.getInteger("value");
            }
        }
        return this.order;
    }


    public void setOrder(int order){
        this.order = order;
    }


}