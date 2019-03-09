package hiyouka.seedframework.context.config;

import hiyouka.seedframework.beans.exception.BeansException;
import hiyouka.seedframework.beans.factory.aware.Aware;
import hiyouka.seedframework.beans.factory.aware.BeanFactoryAware;
import hiyouka.seedframework.beans.factory.aware.EnvironmentAware;
import hiyouka.seedframework.beans.factory.config.BeanPostProcessor;
import hiyouka.seedframework.context.ConfigurableApplicationContext;
import hiyouka.seedframework.context.config.aware.ApplicationContextAware;

/**
 * 处理Aware接口赋值
 * @author hiyouka
 * @since JDK 1.8
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor{

    private final ConfigurableApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Aware){
            if(bean instanceof EnvironmentAware){
                ((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
            }
            if(bean instanceof BeanFactoryAware){
                ((BeanFactoryAware) bean).setBeanFactory(this.applicationContext.getBeanFactory());
            }
            if(bean instanceof ApplicationContextAware){
                ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
            }
        }
        return bean;
    }



}