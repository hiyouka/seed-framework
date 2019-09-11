package seed.seedframework.context.paser;

import seed.seedframework.beans.annotation.Bean;
import seed.seedframework.beans.annotation.Import;
import seed.seedframework.beans.definition.*;
import seed.seedframework.beans.factory.BeanDefinitionRegistry;
import seed.seedframework.common.AnnotationAttributes;
import seed.seedframework.context.annotation.ComponentScan;
import seed.seedframework.context.annotation.Configuration;
import seed.seedframework.context.annotation.PropertySources;
import seed.seedframework.context.config.AnnotationConfigUtils;
import seed.seedframework.context.config.BeanMethod;
import seed.seedframework.context.config.ConfigurationClass;
import seed.seedframework.context.config.ConfigurationUtils;
import seed.seedframework.core.asm.ClassReaderUtils;
import seed.seedframework.core.env.ConfigurableEnvironment;
import seed.seedframework.core.env.Environment;
import seed.seedframework.core.io.resource.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import seed.seedframework.beans.metadata.*;
import seed.seedframework.util.*;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 解析配置类
 * @see Configuration
 * @author hiyouka
 * @since JDK 1.8
 */
public class ConfigurationClassParser {

    private final Log logger = LogFactory.getLog(getClass());

    public static final String BEAN_ANNOTATION_CLASSNAME = "hiyouka.seedframework.beans.annotation.Bean";

    private final BeanDefinitionRegistry registry;

    private final Set<ConfigurationClass> configurationClasses = new LinkedHashSet<>();

    private final ClassPathBeanDefinitionScanner componentScanParser;

    private final Environment environment;



    public ConfigurationClassParser(BeanDefinitionRegistry registry, Environment environment){
        this.registry = registry;
        this.componentScanParser = new ClassPathBeanDefinitionScanner(registry);
        this.environment = environment;
    }

    public Set<ConfigurationClass> getConfigurationClasses() {
        return configurationClasses;
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
        Class<?> clazz = null;
        if(metadata instanceof StandardClassMetadata) {
            clazz = ((StandardClassMetadata) metadata).getIntrospectedClass();
        }
        AnnotationAttributes property = AnnotationConfigUtils.getAnnotationAttributes(metadata, PropertySources.class.getName());
        if(property != null){
            processPropertySources(property,configClass);
        }

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

        processInterfaces(configClass, clazz);

    }

    private void processPropertySources(AnnotationAttributes property,ConfigurationClass configurationClass) {
        String[] values = property.getStringArray("value");
        for(String value : values){
            if(this.environment instanceof ConfigurableEnvironment){
                try {
                    Resource resource = this.componentScanParser.getResourcePatternResolver().getResource(value);
                    ((ConfigurableEnvironment) this.environment).loadResource(resource);
                } catch (IOException e) {
                    throw new IllegalStateException("not found properties in [" + value + "]" + " resolve class : " + ClassReaderUtils.getPackageName(configurationClass.getResource()));
                }
            }
        }
    }

    protected void processImports(ConfigurationClass configClass){
        AnnotationMetadata metadata = configClass.getMetadata();
        AnnotationAttributes attributes = AnnotationConfigUtils.getAnnotationAttributes(metadata, Import.class.getName());
        if(attributes != null){
            Class<?>[] values = attributes.getClassArray("value");
            for(Class clazz : values){
                StandardAnnotationMetadata smeta = new StandardAnnotationMetadata(clazz);
                // process as configuration class beanName default hiyouka.framework.test.bean class name
                configClass.addImportedBy(clazz);
                processConfigurationClass(new ConfigurationClass(smeta,clazz.getName()));
            }
        }
        // do something to get all @Import class
    }

    protected void processBean(ConfigurationClass configClass){
        AnnotationMetadata metadata = configClass.getMetadata();
        Set<MethodMetadata> annotatedMethods = retrieveBeanMethodMetadata(metadata);
        if(annotatedMethods.size() > 0){
            for(MethodMetadata meta : annotatedMethods){
                processBeanMethodClass(meta,configClass);
//                configClass.addBeanMethod(new BeanMethod(meta,configClass));
            }
        }
        // do something to get this class @Bean method
    }

    protected void processInterfaces(ConfigurationClass configClass,Class<?> clazz){
        if(clazz == null)
            return;
        for(Class cla : clazz.getInterfaces()){
            Set<MethodMetadata> methodMetadata = retrieveBeanMethodMetadata(new StandardAnnotationMetadata(cla));
            for(MethodMetadata metadata : methodMetadata){
                processBeanMethodClass(metadata,configClass);
//                configClass.addBeanMethod(new BeanMethod(metadata,configClass));
            }
            processInterfaces(configClass, cla);
        }
        // do some thing to get interface @Bean method
    }

    private Set<MethodMetadata> retrieveBeanMethodMetadata(AnnotationMetadata metadata){
        Set<MethodMetadata> result = new LinkedHashSet<>();
        Set<MethodMetadata> annotatedMethods = metadata.getAnnotatedMethods(Bean.class.getName());
        for(MethodMetadata meta : annotatedMethods){
            if(isBeanMethod(meta)){
                result.add(meta);
            }
        }
        return result;
    }

    /** 将@Bean导入的对象当作 Configuration 处理 */
    private void processBeanMethodClass(MethodMetadata metadata, ConfigurationClass configClass){
        if(isBeanMethod(metadata) && metadata instanceof StandardMethodMetadata){
            BeanMethod beanMethod = new BeanMethod(metadata,configClass);
            configClass.addBeanMethod(beanMethod);
            AnnotationAttributes annotationAttributes =
                    AnnotatedElementUtils.getAnnotationAttributes(((StandardMethodMetadata) metadata)
                            .getIntrospectedMethod(), Bean.class.getName());
            String name = annotationAttributes.getString("value");
            if(!StringUtils.hasText(name)){
                name = metadata.getMethodName();
            }
            try {
                //process this hiyouka.framework.test.bean as configuration class
                processConfigurationClass(new ConfigurationClass(ClassUtils.forName(metadata.getReturnTypeName()),name));
            } catch (ClassNotFoundException e) {
                configClass.removeBeanMethod(beanMethod);
                throw new IllegalStateException(" class not found : " + metadata.getReturnTypeName(),e);
            }
        }
    }

    protected boolean isBeanMethod(MethodMetadata metadata){
        return !metadata.isAbstract() && !metadata.isStatic()
                && !metadata.isFinal() && !metadata.getReturnTypeName().equals("void");
    }

    public void loadBeanDefinition(Set<ConfigurationClass> configurationClasses) {
        for(ConfigurationClass configurationClass : configurationClasses){
            loadBeanDefinitionsFormConfigurationClass(configurationClass);
        }
    }

    private void loadBeanDefinitionsFormConfigurationClass(ConfigurationClass configClass) {
        if(configClass.isImported()){
            registerBeanDefinitionFromImport(configClass);
        }
        if(configClass.hasBeanMethod()){
            registerBeanDefinitionFromBeanMethod(configClass);
        }
    }

    private void registerBeanDefinitionFromImport(ConfigurationClass configClass) {
        Set<Class<?>> importedBy = configClass.getImportedBy();
        for(Class<?> clazz : importedBy){
            AnnotatedBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(clazz);
            String benName = clazz.getName();
            this.componentScanParser.registerOriginBeanDefinition(benName,beanDefinition);
        }
    }

    private void registerBeanDefinitionFromBeanMethod(ConfigurationClass configClass) {
        Set<BeanMethod> beanMethods = configClass.getBeanMethods();
        for(BeanMethod beanMethod : beanMethods){
            MethodMetadata metadata = beanMethod.getMetadata();
            Class<?> aClass = ClassUtils.getClass(metadata.getReturnTypeName());
             AnnotatedBeanDefinition beanDefinition =
                    new AnnotatedGenericBeanDefinition(new StandardAnnotationMetadata(aClass),metadata);
            beanDefinition.setFactoryBeanName(configClass.getBeanName());
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(Bean.class.getName());
            String beanName = null;
            if(!annotationAttributes.isEmpty()){
                Object name = annotationAttributes.get("value");
                if((name instanceof String) && StringUtils.hasText((String)name)){
                    beanName = (String) name;
                }
            }
            if(!StringUtils.hasText(beanName)){
                beanName = metadata.getMethodName();
            }
            this.componentScanParser.registerOriginBeanDefinition(beanName,beanDefinition);
        }
    }

    private void processBeanMethodBeanDefinition(BeanDefinition beanDefinition, MethodMetadata methodMetadata){
        BeanDefinitionReaderUtils.processBeanDefinition(methodMetadata,beanDefinition);
    }


}