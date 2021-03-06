package seed.seedframework.context.config;

import seed.seedframework.beans.annotation.Bean;
import seed.seedframework.beans.annotation.Component;
import seed.seedframework.context.annotation.ComponentScan;
import seed.seedframework.beans.definition.AbstractBeanDefinition;
import seed.seedframework.beans.definition.AnnotatedBeanDefinition;
import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.metadata.AnnotationMetadata;
import seed.seedframework.beans.metadata.StandardAnnotationMetadata;
import seed.seedframework.context.annotation.Configuration;
import seed.seedframework.beans.annotation.Import;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ConfigurationUtils {

    private static final Log logger = LogFactory.getLog(ConfigurationUtils.class);

    private static final Set<String> candidateIndicators = new HashSet<>(8);

    private static final String CONFIGURATION_ATTRIBUTE = "configurationClass";

    private static final String CONFIGURATION_CLASS_FULL = "full";

    private static final String CONFIGURATION_CLASS_LITE = "lite";

    static {
        candidateIndicators.add(Component.class.getName());
        candidateIndicators.add(ComponentScan.class.getName());
        candidateIndicators.add(Import.class.getName());
    }


    public static boolean isConfigurationCandidate(AnnotationMetadata metadata) {
        return (isFullConfigurationCandidate(metadata) || isLiteConfigurationCandidate(metadata));
    }


    public static boolean isFullConfigurationCandidate(AnnotationMetadata metadata) {
        return metadata.isAnnotated(Configuration.class.getName());
    }


    public static boolean isLiteConfigurationCandidate(AnnotationMetadata metadata) {
        if (metadata.isInterface()) {
            return false;
        }
        for (String indicator : candidateIndicators) {
            if (metadata.isAnnotated(indicator)) {
                return true;
            }
        }
        try {
            return metadata.hasAnnotatedMethods(Bean.class.getName());
        }
        catch (Throwable ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to introspect @Bean methods on class [" + metadata.getClassName() + "]: " + ex);
            }
            return false;
        }
    }

    public static boolean checkConfigurationClass(BeanDefinition beanDefinition){
        AnnotationMetadata metadata = null;
        if(beanDefinition instanceof AnnotatedBeanDefinition){
            metadata = ((AnnotatedBeanDefinition) beanDefinition).getMetadata();
        }else if(beanDefinition instanceof AbstractBeanDefinition){
            metadata = new StandardAnnotationMetadata(beanDefinition.getBeanClass());
        }
        if(isFullConfigurationCandidate(metadata)){
            beanDefinition.setAttribute(CONFIGURATION_ATTRIBUTE,CONFIGURATION_CLASS_FULL);
        }else if(isLiteConfigurationCandidate(metadata)){
            beanDefinition.setAttribute(CONFIGURATION_ATTRIBUTE,CONFIGURATION_CLASS_LITE);
        }else {
            return false;
        }
        return true;
    }

    public static boolean isFullConfigurationClass(BeanDefinition beanDef) {
        return CONFIGURATION_CLASS_FULL.equals(beanDef.getAttribute(CONFIGURATION_ATTRIBUTE));
    }

    public static boolean isLiteConfigurationClass(BeanDefinition beanDef) {
        return CONFIGURATION_CLASS_LITE.equals(beanDef.getAttribute(CONFIGURATION_ATTRIBUTE));
    }

}