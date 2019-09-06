package hiyouka.seedframework.beans.factory.config;

import hiyouka.seedframework.beans.definition.BeanDefinition;

/**
 * use to extended beanDefinition
 * @author hiyouka
 * @since JDK 1.8
 */
public interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor {

    /**
     * Post-process the given merged hiyouka.framework.test.bean definition for the specified hiyouka.framework.test.bean.
     * @param beanDefinition the merged hiyouka.framework.test.bean definition for the hiyouka.framework.test.bean
     * @param beanType the actual type of the managed hiyouka.framework.test.bean instance
     * @param beanName the name of the hiyouka.framework.test.bean
     */
    void postProcessMergedBeanDefinition(BeanDefinition beanDefinition, Class<?> beanType, String beanName);

}