package hiyouka.seedframework.context.paser;

import hiyouka.seedframework.beans.definition.AnnotatedBeanDefinition;
import hiyouka.seedframework.beans.definition.AnnotatedGenericBeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.metadata.AnnotationMetadata;
import hiyouka.seedframework.beans.metadata.StandardAnnotationMetadata;
import hiyouka.seedframework.context.config.filter.BeanTypeFilter;
import hiyouka.seedframework.core.asm.ClassReaderUtils;
import hiyouka.seedframework.core.io.resource.Resource;
import hiyouka.seedframework.core.io.resource.ResourceLoader;
import hiyouka.seedframework.core.io.resource.ResourcePatternResolver;
import hiyouka.seedframework.exception.BeanDefinitionStoreException;
import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.ClassUtils;
import hiyouka.seedframework.util.ResourcePatternUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
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

    /** 过滤符合条件的bean Resource */
    private final List<BeanTypeFilter> excludeFilters = new LinkedList<>();

    /** bean所需的条件过滤 */
    private final List<BeanTypeFilter> includeFilters = new LinkedList<>();

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
                    StandardAnnotationMetadata meta = new StandardAnnotationMetadata(clazz);
                    if(isComponent(meta)){
                        AnnotatedGenericBeanDefinition bea = new AnnotatedGenericBeanDefinition(meta);
                        bea.setResource(resource);
                        if(isComponent(bea)){
                            logger.info("add BeanDefinition : " + bea.getBeanClassName());
                            searchBeanDefinitions.add(bea);
                        }else {
                            logger.error("not support abstract or inner class Component class : " + bea.getBeanClassName());
                        }
                    }
                }
            }
        }catch (IOException e){
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", e);
        }
        return searchBeanDefinitions;
    }

    protected boolean isComponent(AnnotationMetadata metadata) {
        for(BeanTypeFilter typeFilter : excludeFilters){
            if(typeFilter.match(metadata)){
                return false;
            }
        }
        for(BeanTypeFilter typeFilter : includeFilters){
            return typeFilter.match(metadata);
        }
        return false;
    }

    protected boolean isComponent(AnnotatedBeanDefinition beanDefinition){
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return (metadata.isConcrete() && metadata.isIndependent());
    }

    protected boolean addExcludeFilters(BeanTypeFilter excludeFilter) {
        return this.excludeFilters.add(excludeFilter);
    }

    protected boolean addIncludeFilters(BeanTypeFilter includeFilter) {
        return this.includeFilters.add(includeFilter);
    }
}