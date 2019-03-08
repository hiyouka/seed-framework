package hiyouka.seedframework.beans.factory.config;

import hiyouka.seedframework.beans.exception.BeansException;

/**
 *
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanFactoryPostProcessor {

    /** beanFactory 的后置处理 */
    void postProcessBeanFactory(ConfigurableDefinitionBeanFactory beanFactory) throws BeansException;

}