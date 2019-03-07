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
public interface DefinitionBeanFactory extends BeanFactory {

    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(Class<?> type);

    /**
     *
     * @param type bean class
     * @param includeNonSingletons is contain not single bean
     * @param allowEagerInit is contain eager bean
     * @return array of bean name
     */
    String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit);


    /**
     * get all this type bean
     * @param type bean class
     * @return the collection of bean name -> bean instance
     * @throws if a bean could not be created
     */
    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException;


    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException;

    /**
     * get this type annotation from the bean
     * @param beanName bean name
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
     * @return bean Definition
     * @throws NoSuchBeanDefinitionException if beanDefinition not found
     */
    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

}