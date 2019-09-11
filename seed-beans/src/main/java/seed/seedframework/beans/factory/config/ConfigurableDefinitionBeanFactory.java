package seed.seedframework.beans.factory.config;

import seed.seedframework.beans.exception.BeansException;
import seed.seedframework.beans.factory.AutowiredSupportBeanFactory;
import seed.seedframework.beans.factory.DefinitionBeanFactory;

/**
 * 给beanFactory添加管理忽略注册接口和给特定接口注册特定的bean功能
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ConfigurableDefinitionBeanFactory extends ConfigurableBeanFactory,DefinitionBeanFactory, AutowiredSupportBeanFactory {


    /**
     * 创建所有的单例对象
     * @throws BeansException if hiyouka.framework.test.bean create error
     */
    void  preInstantiateSingletons() throws BeansException;


}