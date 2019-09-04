package hiyouka.seedframework.beans.factory.config;

import hiyouka.seedframework.beans.exception.BeansException;
import hiyouka.seedframework.beans.metadata.PropertyValues;

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

}