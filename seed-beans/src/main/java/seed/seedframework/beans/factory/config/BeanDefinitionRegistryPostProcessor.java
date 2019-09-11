package seed.seedframework.beans.factory.config;

import seed.seedframework.beans.factory.BeanDefinitionRegistry;
import seed.seedframework.beans.exception.BeansException;

/**
 * 用于注册bean时修改注册数据 将在 {@link BeanFactoryPostProcessor} 之前执行。
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor{

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;

}