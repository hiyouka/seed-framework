package hiyouka.seedframework.context.annotation;

import hiyouka.seedframework.beans.definition.AnnotatedGenericBeanDefinition;
import hiyouka.seedframework.beans.definition.AnnotationBeanNameGenerator;
import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinitionHolder;
import hiyouka.seedframework.core.asm.ClassReaderUtils;
import hiyouka.seedframework.core.io.resource.Resource;
import hiyouka.seedframework.core.io.resource.ResourceLoader;
import hiyouka.seedframework.core.io.resource.ResourcePatternResolver;
import hiyouka.seedframework.exception.BeanDefinitionStoreException;
import hiyouka.seedframework.util.AnnotatedElementUtils;
import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.ClassUtils;
import hiyouka.seedframework.util.ResourcePatternUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ClassPathScanningComponentProvider {

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    protected final Log logger = LogFactory.getLog(getClass());

    private ResourcePatternResolver resourcePatternResolver;

    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

    protected ClassPathScanningComponentProvider() {
        this(null);
    }

    public ClassPathScanningComponentProvider(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
    }

    public void setResourcePattern(String resourcePattern) {
        Assert.notNull(resourcePattern, "'resourcePattern' must not be null");
        this.resourcePattern = resourcePattern;
    }

    public void setResourceLoader(ResourceLoader resourceLoader){
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
    }

    public ResourcePatternResolver getResourcePatternResolver() {
        return resourcePatternResolver;
    }

    public void setResourcePatternResolver(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }



    public Set<BeanDefinition> findBeanDefinitions(String basePackage){
        return scanComponents(basePackage);
    }

    private Set<BeanDefinition> scanComponents(String basePackage){
        Set<BeanDefinition> searchBeanDefinitions = new LinkedHashSet<>(256);
        try{
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(basePackage)
                    + "/" + DEFAULT_RESOURCE_PATTERN;
            Resource[] resources = getResourcePatternResolver().getResources(packageSearchPath);
            for(Resource resource : resources){
                if(resource.isReadable()){
                    Class<?> clazz = ClassReaderUtils.getClassFromResource(resource);
                    if(isComponent(clazz)){
                        AnnotatedGenericBeanDefinition bea = new AnnotatedGenericBeanDefinition(clazz);
                        bea.setResource(resource);
                        searchBeanDefinitions.add(bea);
                    }
                }
            }
        }catch (IOException e){
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", e);
        }
        return searchBeanDefinitions;
    }

    protected boolean isComponent(Class<?> clazz) {
        return AnnotatedElementUtils.isAnnotated(clazz, AnnotationBeanNameGenerator.COMPONENT_ANNOTATION_CLASSNAME);
    }

}