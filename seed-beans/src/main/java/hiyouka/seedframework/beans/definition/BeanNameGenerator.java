package hiyouka.seedframework.beans.definition;

import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;

/**
 *
 * 用于beanName 生成
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanNameGenerator {


    String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry);

}