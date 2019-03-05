package hiyouka.seedframework.context.config;

import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinitionHolder;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.beans.factory.DefaultBenFactory;
import hiyouka.seedframework.beans.factory.config.BeanDefinitionRegistryPostProcessor;
import hiyouka.seedframework.context.paser.ConfigurationClassParser;
import hiyouka.seedframework.exception.BeansException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private final Log logger = LogFactory.getLog(this.getClass());



    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        processConfigurationBeanDefinitions(registry);
    }

    @Override
    public void postProcessBeanFactory(DefaultBenFactory beanFactory) throws BeansException {

    }


    public void processConfigurationBeanDefinitions(BeanDefinitionRegistry registry){
        String[] beanNames = registry.getBeanDefinitionNames();
        Set<BeanDefinitionHolder> processBean = new LinkedHashSet<>(128);
        for(String beanName : beanNames){
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            if(alreadyProcessor(beanDefinition)){
                logger.error("this configuration bean is bean process : " + beanName );
            }
            if(ConfigurationUtils.checkConfigurationClass(beanDefinition)){
                processBean.add(new BeanDefinitionHolder(beanDefinition,beanName));
            }
        }
        ConfigurationClassParser parser = new ConfigurationClassParser(registry);
        parser.parse(processBean);

        Set<ConfigurationClass> configurationClasses = parser.getConfigurationClasses();

        parser.loadBeanDefinition(configurationClasses);
    }



    private boolean alreadyProcessor(BeanDefinition beanDefinition){
        return ConfigurationUtils.isFullConfigurationClass(beanDefinition)
              || ConfigurationUtils.isLiteConfigurationClass(beanDefinition);
    }
}