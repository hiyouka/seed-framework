package hiyouka.seedframework.beans.factory;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.exception.BeansException;
import hiyouka.seedframework.exception.NoSuchBeanDefinitionException;
import hiyouka.seedframework.exception.NoUniqueBeanDefinitionException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanFactory {


    /**
     * get bean instance with beanName
     * @param name beanName
     * @return java.lang.Object
     * @throws NoSuchBeanDefinitionException if there is no such bean definition
     * @throws BeansException if the bean could not be obtained
     */
    Object getBean(String name) throws BeansException;

    /**
     * @param name  bean name
     * @param requiredType bean class
     * @return bean instance
     * @throws
     */
    <T> T getBean(String name, @Nullable Class<T> requiredType) throws BeansException;


    /**
     * @param requiredType 
     * @return bean instance
     * @throws NoSuchBeanDefinitionException if no bean of the given type was found
     * @throws NoUniqueBeanDefinitionException if more than one bean of the given type was found
     * @throws BeansException if the bean could not be created
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;



    /**
     * get bean with constructor arguments
     * @param requiredType bean class
     * @param args 构造函数参数
     * @return bean instance
     * @throws NoSuchBeanDefinitionException if there is no such bean definition
     * @throws BeansException if the bean could not be created
     */
    <T> T getBean(Class<T> requiredType, Object... args) throws BeansException;

    boolean containsBean(String name);

    /**
     * @param name bean name
     * @return if beanDefinition is single return true
     * @throws NoSuchBeanDefinitionException
     */
    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

    /**
     * get beanClass
     * @param name bean name
     * @return this bean type
     * @throws NoSuchBeanDefinitionException if not found this beanName beanDefinition
     */
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;


}