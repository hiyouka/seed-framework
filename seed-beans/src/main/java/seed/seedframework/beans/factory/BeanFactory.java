package seed.seedframework.beans.factory;

import seed.seedframework.beans.exception.BeansException;
import seed.seedframework.beans.exception.NoSuchBeanDefinitionException;
import seed.seedframework.beans.exception.NoUniqueBeanDefinitionException;

import javax.annotation.Nullable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanFactory {

    /**
     * get hiyouka.framework.test.bean instance with beanName
     * @param name beanName
     * @return java.lang.Object
     * @throws NoSuchBeanDefinitionException if there is no such hiyouka.framework.test.bean definition
     * @throws BeansException if the hiyouka.framework.test.bean could not be obtained
     */
    Object getBean(String name) throws BeansException;

    /**
     * @param name  hiyouka.framework.test.bean name
     * @param requiredType hiyouka.framework.test.bean class
     * @return hiyouka.framework.test.bean instance
     * @throws
     */
    <T> T getBean(String name, @Nullable Class<T> requiredType) throws BeansException;

    Object getBean(String name, @Nullable Object[] args);

    <T> T getBean(String name, @Nullable Class<T> requiredType, @Nullable Object[] args) throws BeansException;


    /**
     * @param requiredType 
     * @return hiyouka.framework.test.bean instance
     * @throws NoSuchBeanDefinitionException if no hiyouka.framework.test.bean of the given type was found
     * @throws NoUniqueBeanDefinitionException if more than one hiyouka.framework.test.bean of the given type was found
     * @throws BeansException if the hiyouka.framework.test.bean could not be created
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;


    /**
     * get hiyouka.framework.test.bean with constructor arguments
     * @param requiredType hiyouka.framework.test.bean class
     * @param args 构造函数参数
     * @return hiyouka.framework.test.bean instance
     * @throws NoSuchBeanDefinitionException if there is no such hiyouka.framework.test.bean definition
     * @throws BeansException if the hiyouka.framework.test.bean could not be created
     */
    <T> T getBean(Class<T> requiredType, @Nullable Object... args) throws BeansException;

    boolean containsBean(String name);

    /**
     * @param name hiyouka.framework.test.bean name
     * @return if beanDefinition is single return true
     * @throws NoSuchBeanDefinitionException
     */
    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

    /**
     * get beanClass
     * @param name hiyouka.framework.test.bean name
     * @return this hiyouka.framework.test.bean type
     * @throws NoSuchBeanDefinitionException if not found this beanName beanDefinition
     */
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;


}