package seed.seedframework.beans.factory.config;

import seed.seedframework.beans.exception.BeansException;

/**
 *
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanFactoryPostProcessor {

    /** beanFactory 的后置处理 */
    void postProcessBeanFactory(ConfigurableDefinitionBeanFactory beanFactory) throws BeansException;

}