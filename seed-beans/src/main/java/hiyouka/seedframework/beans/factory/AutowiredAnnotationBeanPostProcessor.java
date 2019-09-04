package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.definition.RootBeanDefinition;
import hiyouka.seedframework.beans.exception.BeansException;
import hiyouka.seedframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import hiyouka.seedframework.beans.factory.config.MergedBeanDefinitionPostProcessor;
import hiyouka.seedframework.beans.metadata.PropertyValues;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AutowiredAnnotationBeanPostProcessor implements MergedBeanDefinitionPostProcessor, InstantiationAwareBeanPostProcessor {

    /**
     * 获取bean需要注入的类
     */
    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
//        AnnotatedElementUtils.
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return null;
    }
}