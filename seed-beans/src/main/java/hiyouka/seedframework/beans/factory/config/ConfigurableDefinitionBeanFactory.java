package hiyouka.seedframework.beans.factory.config;

import hiyouka.seedframework.beans.factory.DefinitionBeanFactory;
import hiyouka.seedframework.beans.factory.SingletonBeanRegistry;

/**
 * 给beanFactory添加管理忽略注册接口和给特定接口注册特定的bean功能
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ConfigurableDefinitionBeanFactory extends ConfigurableBeanFactory,DefinitionBeanFactory, SingletonBeanRegistry{

}