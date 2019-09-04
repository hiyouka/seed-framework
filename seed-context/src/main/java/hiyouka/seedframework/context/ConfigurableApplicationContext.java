package hiyouka.seedframework.context;

import hiyouka.seedframework.Lifecycle;
import hiyouka.seedframework.beans.exception.BeansException;
import hiyouka.seedframework.beans.factory.config.BeanFactoryPostProcessor;
import hiyouka.seedframework.beans.factory.config.ConfigurableDefinitionBeanFactory;
import hiyouka.seedframework.core.env.ConfigurableEnvironment;
import hiyouka.seedframework.core.env.Environment;

import java.io.Closeable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ConfigurableApplicationContext extends ApplicationContext, Closeable, Lifecycle{

    /**
     * Name of the {@link Environment} hiyouka.framework.test.bean in the factory.
     */
    String ENVIRONMENT_BEAN_NAME = "environment";

    /**
     * Name of the System properties hiyouka.framework.test.bean in the factory.
     */
    String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";

    /**
     * Name of the System environment hiyouka.framework.test.bean in the factory.
     */
    String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";

    ConfigurableEnvironment getEnvironment();


    void setEnvironment(ConfigurableEnvironment environment);

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);


    /** 容器刷新 */
    void refresh() throws BeansException, IllegalArgumentException;


    boolean isActive();


    ConfigurableDefinitionBeanFactory getBeanFactory();

}