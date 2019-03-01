package hiyouka.seedframework.context.paser;

import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.context.annotation.Configuration;
import hiyouka.seedframework.context.config.ConfigurationClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 解析配置类
 * @see Configuration
 * @author hiyouka
 * @since JDK 1.8
 */
public class ConfigurationClassParser {

    private final Log logger = LogFactory.getLog(getClass());

    private final BeanDefinitionRegistry registry;

    private final Set<ConfigurationClass> configurationClasses = new LinkedHashSet<>();


    public ConfigurationClassParser(BeanDefinitionRegistry registry){
        this.registry = registry;
    }

    public void parse(Set<BeanDefinition> beanDefinitions){

    }

}