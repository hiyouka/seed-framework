package hiyouka.seedframework.beans.factory.config;

import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.beans.exception.BeansException;

/**
 * 用于注册bean时修改注册数据 将在 {@link BeanFactoryPostProcessor} 之前执行。
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor{

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;

}