package hiyouka.seedframework.context.config;

import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinitionHolder;
import hiyouka.seedframework.beans.exception.BeansException;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.beans.factory.aware.EnvironmentAware;
import hiyouka.seedframework.beans.factory.config.BeanDefinitionRegistryPostProcessor;
import hiyouka.seedframework.beans.factory.config.ConfigurableDefinitionBeanFactory;
import hiyouka.seedframework.constant.SeedConstant;
import hiyouka.seedframework.context.paser.ConfigurationClassParser;
import hiyouka.seedframework.core.annotation.Priority;
import hiyouka.seedframework.core.env.Environment;
import hiyouka.seedframework.util.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Priority(SeedConstant.LOWEST_PRIORITY)
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor,EnvironmentAware{

    private final Log logger = LogFactory.getLog(this.getClass());

    private Environment environment;

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        Assert.notNull(environment,"environment must not be null");
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        processConfigurationBeanDefinitions(registry);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableDefinitionBeanFactory beanFactory) throws BeansException {
        if(beanFactory instanceof BeanDefinitionRegistry){
            processConfigurationBeanDefinitions((BeanDefinitionRegistry) beanFactory);
        }
    }


    protected void processConfigurationBeanDefinitions(BeanDefinitionRegistry registry){
        String[] beanNames = registry.getBeanDefinitionNames();
        Set<BeanDefinitionHolder> processBean = new LinkedHashSet<>(128);
        for(String beanName : beanNames){
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            if(alreadyProcessor(beanDefinition)){
                logger.error("this configuration hiyouka.framework.test.bean is hiyouka.framework.test.bean process : " + beanName );
            }
            if(ConfigurationUtils.checkConfigurationClass(beanDefinition)){
                processBean.add(new BeanDefinitionHolder(beanDefinition,beanName));
            }
        }
        ConfigurationClassParser parser = new ConfigurationClassParser(registry,this.environment);
        parser.parse(processBean);

        Set<ConfigurationClass> configurationClasses = parser.getConfigurationClasses();

        parser.loadBeanDefinition(configurationClasses);
    }



    private boolean alreadyProcessor(BeanDefinition beanDefinition){
        return ConfigurationUtils.isFullConfigurationClass(beanDefinition)
              || ConfigurationUtils.isLiteConfigurationClass(beanDefinition);
    }
}