package seed.seedframework.aop.aspectJ;

import org.aspectj.lang.annotation.Aspect;
import seed.seedframework.aop.interceptor.AbstractAspectJAdvice;
import seed.seedframework.aop.interceptor.AspectJPointcutAdvisor;
import seed.seedframework.aop.util.AspectJUtil;
import seed.seedframework.beans.definition.AnnotatedBeanDefinition;
import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.exception.BeansException;
import seed.seedframework.beans.factory.BeanFactory;
import seed.seedframework.beans.factory.DefinitionBeanFactory;
import seed.seedframework.beans.factory.aware.BeanFactoryAware;
import seed.seedframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import seed.seedframework.beans.metadata.AnnotationMetadata;
import seed.seedframework.common.AnnotationAttributes;
import seed.seedframework.core.Order;
import seed.seedframework.core.Ordered;
import seed.seedframework.core.intercept.Advisor;
import seed.seedframework.util.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AspectJAwareAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefinitionBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefinitionBeanFactory) beanFactory;
    }

    public Advisor[] findAspectAdvisor(){
        List<Advisor> advisors = new LinkedList<>();
        for(String name : beanFactory.getBeanDefinitionNames()){
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            if(beanDefinition instanceof AnnotatedBeanDefinition){
                AnnotationMetadata metadata = ((AnnotatedBeanDefinition) beanDefinition).getMetadata();
                Set<String> annotationTypes = metadata.getAnnotationTypes();
                if(annotationTypes.contains(Aspect.class.getName())){
                    Class<?> beanClass = beanDefinition.getBeanClass();
                    Method[] methods = beanClass.getDeclaredMethods();
                    for(Method method : methods){
                        Object bean = beanFactory.getBean(name);
                        AbstractAspectJAdvice advice = AspectJUtil.initAdvice(method,bean);
                        if(advice != null){
                            AspectJPointcutAdvisor advisor = new AspectJPointcutAdvisor(advice);
                            AnnotationAttributes attributes = AnnotatedElementUtils
                                    .getAnnotationAttributes(method, Order.class.getName());
                            if(attributes != null){
                                advisor.setOrder(attributes.getInteger("value"));
                            }
                            else {
                                if(bean instanceof Order){
                                    advisor.setOrder(((Order) bean).getOrder());
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
        }
        return advisors.toArray(new Advisor[0]);
    }

}