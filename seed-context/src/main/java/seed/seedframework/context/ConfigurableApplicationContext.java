package seed.seedframework.context;

import seed.seedframework.Lifecycle;
import seed.seedframework.beans.exception.BeansException;
import seed.seedframework.beans.factory.config.BeanFactoryPostProcessor;
import seed.seedframework.beans.factory.config.ConfigurableDefinitionBeanFactory;
import seed.seedframework.core.env.ConfigurableEnvironment;
import seed.seedframework.core.env.Environment;

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