package seed.seedframework.beans.definition;

import seed.seedframework.beans.factory.BeanDefinitionRegistry;

/**
 *
 * 用于beanName 生成
 * @author hiyouka
 * @since JDK 1.8
 */
@FunctionalInterface
public interface BeanNameGenerator {


    String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry);

}