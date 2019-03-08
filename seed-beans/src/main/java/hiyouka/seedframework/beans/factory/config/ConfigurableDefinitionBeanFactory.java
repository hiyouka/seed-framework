package hiyouka.seedframework.beans.factory.config;

import hiyouka.seedframework.beans.factory.DefinitionBeanFactory;
import hiyouka.seedframework.beans.factory.SingletonBeanRegistry;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ConfigurableDefinitionBeanFactory extends ConfigurableBeanFactory,DefinitionBeanFactory, SingletonBeanRegistry{

}