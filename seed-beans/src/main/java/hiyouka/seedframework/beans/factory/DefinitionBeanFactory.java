package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.exception.BeansException;
import hiyouka.seedframework.beans.exception.NoSuchBeanDefinitionException;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface DefinitionBeanFactory extends BeanFactory{

    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(Class<?> type);

    /**
     *
     * @param type hiyouka.framework.test.bean class
     * @param includeNonSingletons is contain not single hiyouka.framework.test.bean
     * @param allowEagerInit is contain eager hiyouka.framework.test.bean
     * @return array of hiyouka.framework.test.bean name
     */
    String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit);


    /**
     * get all this type hiyouka.framework.test.bean
     * @param type hiyouka.framework.test.bean class
     * @return the collection of hiyouka.framework.test.bean name -> hiyouka.framework.test.bean instance
     * @throws if a hiyouka.framework.test.bean could not be created
     */
    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException;


    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException;

    /**
     * get this type annotation from the hiyouka.framework.test.bean
     * @param beanName hiyouka.framework.test.bean name
     * @param annotationType  annotation type
     * @return annotation
     * @throws if not found this name benDefinition
     */
    @Nullable
    <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
            throws NoSuchBeanDefinitionException;

    /**
     * get beanDefinition
     * @param beanName
     * @return hiyouka.framework.test.bean Definition
     * @throws NoSuchBeanDefinitionException if beanDefinition not found
     */
    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

}