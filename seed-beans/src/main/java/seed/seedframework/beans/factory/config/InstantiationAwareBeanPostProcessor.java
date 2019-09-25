package seed.seedframework.beans.factory.config;

import seed.seedframework.beans.exception.BeansException;
import seed.seedframework.beans.metadata.PropertyValues;

import java.lang.reflect.Constructor;

/**
 * give an chance to return hiyouka.framework.test.bean before hiyouka.framework.test.bean create
 * after hiyouka.framework.test.bean create to change
 * 
 * @author hiyouka
 * @since JDK 1.8
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {



    default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    default boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    default PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
            throws BeansException {
        return null;
    }


    /**
     * determine constructor to use for the given bean
     * @param beanClass bean type
     * @param beanName bean name
     * @return java.lang.reflect.Constructor<?>[]
     */
    default Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName)
            throws BeansException {
        return null;
    }

}