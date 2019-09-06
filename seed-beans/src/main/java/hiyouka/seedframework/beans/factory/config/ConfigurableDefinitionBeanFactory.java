package hiyouka.seedframework.beans.factory.config;

import hiyouka.seedframework.beans.exception.BeansException;
import hiyouka.seedframework.beans.factory.DefinitionBeanFactory;
import hiyouka.seedframework.beans.factory.DependencyDescriptor;
import hiyouka.seedframework.beans.factory.SingletonBeanRegistry;

/**
 * 给beanFactory添加管理忽略注册接口和给特定接口注册特定的bean功能
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ConfigurableDefinitionBeanFactory extends ConfigurableBeanFactory,DefinitionBeanFactory, SingletonBeanRegistry{


    /**
     * 创建所有的单例对象
     * @throws BeansException if hiyouka.framework.test.bean create error
     */
    void  preInstantiateSingletons() throws BeansException;

    Object resolveDepend(DependencyDescriptor dsr,String beanName);

}