package seed.seedframework.aop.aspect;

import org.aspectj.lang.annotation.Aspect;
import seed.seedframework.aop.adapter.PointcutAdvisor;
import seed.seedframework.aop.interceptor.AbstractAspectJAdvice;
import seed.seedframework.aop.interceptor.AspectAdvisorManager;
import seed.seedframework.aop.interceptor.AspectJPointcutAdvisor;
import seed.seedframework.aop.pointcut.Pointcut;
import seed.seedframework.aop.proxy.AopProxyCreator;
import seed.seedframework.aop.proxy.DefaultAopProxyCreator;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.exception.BeansException;
import seed.seedframework.beans.factory.AbstractAutowiredBeanCreateFactory;
import seed.seedframework.beans.factory.BeanFactory;
import seed.seedframework.beans.factory.DefinitionBeanFactory;
import seed.seedframework.beans.factory.aware.BeanFactoryAware;
import seed.seedframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import seed.seedframework.common.AnnotationAttributes;
import seed.seedframework.core.Ordered;
import seed.seedframework.core.intercept.Advisor;
import seed.seedframework.util.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create proxy class before create bean
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectJAwareAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefinitionBeanFactory beanFactory;

    private Advisor[] advisors = null;

    private AopProxyCreator proxyCreator = new DefaultAopProxyCreator();

    private Map<Class<?>,Advisor[]> classAdvisorCache = new ConcurrentHashMap<>();

    private volatile boolean inBuildAdvisor = false;

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        findAspectAdvisor(beanClass);
        // if necessary create proxy bean before instance

        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Advisor[] advisors = findAspectAdvisor(bean.getClass());
        // not need proxy
        if(advisors.length == 0){
            return null;
        }
        else {
            AspectAdvisorManager aspectAdvisorManager = new AspectAdvisorManager(bean.getClass(),advisors);
            aspectAdvisorManager.setTarget(bean);
            if(this.beanFactory instanceof AbstractAutowiredBeanCreateFactory){
                Object[] args = ((AbstractAutowiredBeanCreateFactory) this.beanFactory).getConstructorArgs(beanName);
                aspectAdvisorManager.setArgs(args);
            }
            Object proxy = proxyCreator.createAopProxy(aspectAdvisorManager).getProxy();
            BeanDefinition beanDefinition = this.beanFactory.getBeanDefinition(beanName);
            beanDefinition.setBeanClass(proxy.getClass());
            beanDefinition.setBeanClassName(proxy.getClass().getName());
            return proxy;
        }
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefinitionBeanFactory) beanFactory;
    }

    private Advisor[] matchAdvisors(Advisor[] advisors, Class<?> clazz){
        if(advisors == null){
            return new Advisor[0];
        }
        List<Advisor> result = new LinkedList<>();
        for(Advisor advisor : advisors){
            if(advisor instanceof PointcutAdvisor){
                Pointcut pointcut = ((PointcutAdvisor) advisor).getPointcut();
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for(Method method : declaredMethods){
                    boolean match = pointcut.methodMatch().match(method);
                    if(match){
                        result.add(advisor);
                        break;
                    }
                }
            }
            else {
                // other advisor process
            }
        }
        return result.toArray(new Advisor[0]);
    }

    private Advisor[] findAspectAdvisor(Class<?> clazz){

        if (this.advisors == null){
            if(!this.inBuildAdvisor){
                synchronized (this){
                    this.inBuildAdvisor = true;
                    buildAspectAdvisor();
                    this.inBuildAdvisor = false;
                }
            }
        }
        Advisor[] advisors = this.classAdvisorCache.get(clazz);
        if(advisors == null){
            advisors = matchAdvisors(this.advisors,clazz);
            classAdvisorCache.put(clazz,advisors);
        }
        return advisors;
    }

    private void buildAspectAdvisor(){
        List<Advisor> advisors = new LinkedList<>();
        for(String name : beanFactory.getBeanDefinitionNames()){
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            Class<?> beanClass = beanDefinition.getBeanClass();
            if(beanClass == null){
                continue;
            }
            if(AnnotatedElementUtils.isAnnotated(beanClass,Aspect.class.getName())){
                Method[] methods = beanClass.getDeclaredMethods();
                for(Method method : methods){
                    Object bean = beanFactory.getBean(name);
                    AbstractAspectJAdvice advice = AspectJUtil.initAdvice(method,bean);
                    if(advice != null){
                        AspectJPointcutAdvisor advisor = new AspectJPointcutAdvisor(advice);
                        AnnotationAttributes attributes = AnnotatedElementUtils
                                .getAnnotationAttributes(method, seed.seedframework.core.annotation.Order.class.getName());
                        if(attributes != null){
                            advisor.setOrder(attributes.getInteger("value"));
                        }
                        else {
                            if(bean instanceof seed.seedframework.core.Order){
                                advisor.setOrder(((seed.seedframework.core.Order) bean).getOrder());
                            }
                            else {
                                advisor.setOrder(Ordered.LOWER_LEVEL);
                            }
                        }
                        advisors.add(advisor);
                    }
                }
            }
        }
        this.advisors = advisors.toArray(new Advisor[0]);
        Ordered.sortOrders(this.advisors);
    }

}