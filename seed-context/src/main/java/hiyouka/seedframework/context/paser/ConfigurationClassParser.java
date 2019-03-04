package hiyouka.seedframework.context.paser;

import hiyouka.seedframework.beans.annotation.Bean;
import hiyouka.seedframework.beans.annotation.ComponentScan;
import hiyouka.seedframework.beans.definition.AbstractBeanDefinition;
import hiyouka.seedframework.beans.definition.AnnotatedBeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinitionHolder;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.beans.metadata.AnnotationMetadata;
import hiyouka.seedframework.beans.metadata.MethodMetadata;
import hiyouka.seedframework.beans.metadata.StandardAnnotationMetadata;
import hiyouka.seedframework.beans.metadata.StandardMethodMetadata;
import hiyouka.seedframework.common.AnnotationAttributes;
import hiyouka.seedframework.context.annotation.Configuration;
import hiyouka.seedframework.context.annotation.Import;
import hiyouka.seedframework.context.config.BeanMethod;
import hiyouka.seedframework.context.config.ConfigurationClass;
import hiyouka.seedframework.context.config.ConfigurationUtils;
import hiyouka.seedframework.core.asm.ClassReaderUtils;
import hiyouka.seedframework.util.*;
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

    private final ClassPathBeanDefinitionScanner componentScanParser;



    public ConfigurationClassParser(BeanDefinitionRegistry registry){
        this.registry = registry;
        this.componentScanParser = new ClassPathBeanDefinitionScanner(registry);
    }

    public void parse(Set<BeanDefinitionHolder> configBeanDefinitions){
        for(BeanDefinitionHolder holder : configBeanDefinitions){
            BeanDefinition configBeanDefinition = holder.getBeanDefinition();
            if(configBeanDefinition instanceof AnnotatedBeanDefinition){
                parse(((AnnotatedBeanDefinition) configBeanDefinition).getMetadata(),holder.getBeanName());
            }
            else if(configBeanDefinition instanceof AbstractBeanDefinition
                    && ((AbstractBeanDefinition) configBeanDefinition).hasBeanClass()){
                parse(configBeanDefinition.getBeanClass(),holder.getBeanName());
            }
            else {
                logger.error("not support this beanDefinition : " + holder.getBeanName());
            }
        }

    }

    protected void parse(AnnotationMetadata metadata, String beanName){
        processConfigurationClass(new ConfigurationClass(metadata,beanName));
    }

    protected void parse(BeanDefinitionHolder holder){
        Assert.notNull(holder,"beanDefinitionHolder must not be null !!");
        ConfigurationClass configClass = null;
        BeanDefinition beanDefinition = holder.getBeanDefinition();
        if(beanDefinition instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) beanDefinition).hasBeanClass()){
            configClass = new ConfigurationClass(beanDefinition.getBeanClass(),holder.getBeanName());
        }
        else if(beanDefinition instanceof AnnotatedBeanDefinition){
            configClass = new ConfigurationClass(((AnnotatedBeanDefinition) beanDefinition).getMetadata(),holder.getBeanName());
        }
        else {
            logger.error("not support this beanDefinition : " + holder.getBeanName());
        }
        if(configClass != null){
            processConfigurationClass(configClass);
        }
    }

    protected void parse(Class<?> clazz, String beanName){
        processConfigurationClass(new ConfigurationClass(clazz,beanName));
    }

    protected void processConfigurationClass(ConfigurationClass configClass){
        if(this.configurationClasses.contains(configClass)){
            return;
        }
        doProcessConfigurationClass(configClass);
        this.configurationClasses.add(configClass);
    }

    protected void  doProcessConfigurationClass(ConfigurationClass configClass){
        AnnotationMetadata metadata = configClass.getMetadata();
        AnnotationAttributes annotationAttributes = AnnotationConfigUtils.getAnnotationAttributes(metadata, ComponentScan.class.getName());
        if(annotationAttributes != null){
            Set<BeanDefinitionHolder> holders = this.componentScanParser.parse(annotationAttributes, metadata.getClassName());
            for(BeanDefinitionHolder holder : holders){
                BeanDefinition beanDefinition = holder.getBeanDefinition();
                if(ConfigurationUtils.checkConfigurationClass(beanDefinition)){
                    parse(holder);
                }
            }
        }
        processImports(configClass); // @Import class resolver

        processBean(configClass);

        processInterfaces(configClass);

    }

    protected void processImports(ConfigurationClass configClass){
        AnnotationMetadata metadata = configClass.getMetadata();
        AnnotationAttributes attributes = AnnotationConfigUtils.getAnnotationAttributes(metadata, Import.class.getName());
        if(attributes != null){
            Class<?>[] values = attributes.getClassArray("value");
            for(Class clazz : values){
                StandardAnnotationMetadata smeta = new StandardAnnotationMetadata(clazz);
                // process as configuration class beanName default bean class name
                configClass.addImportedBy(clazz);
                processConfigurationClass(new ConfigurationClass(smeta,clazz.getName()));
            }
        }
        // do something to get all @Import class
    }

    protected void processBean(ConfigurationClass configClass){
        AnnotationMetadata metadata = configClass.getMetadata();
        Set<MethodMetadata> annotatedMethods = metadata.getAnnotatedMethods(Bean.class.getName());
        if(annotatedMethods.size() > 0){
            for(MethodMetadata meta : annotatedMethods){
                if(isBeanMethod(meta) && meta instanceof StandardMethodMetadata){
                    BeanMethod beanMethod = new BeanMethod(meta,configClass);
                    configClass.addBeanMethod(beanMethod);
                    AnnotationAttributes annotationAttributes =
                            AnnotatedElementUtils.getAnnotationAttributes(((StandardMethodMetadata) meta)
                                    .getIntrospectedMethod(), Bean.class.getName());
                    String name = annotationAttributes.getString("name");
                    if(!StringUtils.hasText(name)){
                        name = meta.getMethodName();
                    }
                    try {
                        //process this bean as configuration class
                        processConfigurationClass(new ConfigurationClass(ClassUtils.forName(meta.getReturnTypeName()),name));
                    } catch (ClassNotFoundException e) {
                        configClass.removeBeanMethod(beanMethod);
                        throw new IllegalStateException(" class not found : " + meta.getReturnTypeName());
                    }
                }
            }
        }
        // do something to get this class @Bean method
    }

    protected void processInterfaces(ConfigurationClass configClass){
        Class<?> clazz = ClassReaderUtils.getClassFromResource(configClass.getResource());
        for(Class cla : clazz.getInterfaces()){
            processBean(new ConfigurationClass(cla,null));
        }
        // do some thing to get interface @Bean method
    }


    protected boolean isBeanMethod(MethodMetadata metadata){
        return !metadata.isAbstract() && !metadata.isStatic()
                && !metadata.isFinal() && !metadata.getReturnTypeName().equals("void");
    }

}