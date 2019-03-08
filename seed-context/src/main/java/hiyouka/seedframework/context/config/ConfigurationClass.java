package hiyouka.seedframework.context.config;

import hiyouka.seedframework.beans.metadata.AnnotationMetadata;
import hiyouka.seedframework.beans.metadata.StandardAnnotationMetadata;
import hiyouka.seedframework.beans.metadata.StandardClassMetadata;
import hiyouka.seedframework.context.annotation.Configuration;
import hiyouka.seedframework.beans.annotation.Import;
import hiyouka.seedframework.core.io.resource.ClassResource;
import hiyouka.seedframework.core.io.resource.Resource;
import hiyouka.seedframework.util.Assert;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 封装配置类的信息 {@link Configuration}
 * @author hiyouka
 * @since JDK 1.8
 */
public class ConfigurationClass {

    private final AnnotationMetadata metadata;

    private final Resource resource;

    private String beanName;

    /** import class collections {@link Import} */
    private final Set<Class<?>> importedBy = new LinkedHashSet<>(1);

    private final Set<BeanMethod> beanMethods = new LinkedHashSet<>();

    public ConfigurationClass(Class<?> clazz, String beanName) {
        Assert.notNull(beanName, "Bean name must not be null");
        this.metadata = new StandardAnnotationMetadata(clazz);
        this.resource = new ClassResource(clazz);
        this.beanName = beanName;
    }

    public ConfigurationClass(AnnotationMetadata metadata, String beanName) {
        Assert.notNull(beanName, "Bean name must not be null");
        this.metadata = metadata;
        Class<?> clazz = null;
        if(metadata instanceof StandardClassMetadata){
            clazz = ((StandardClassMetadata) metadata).getIntrospectedClass();
        }
        this.resource = new ClassResource(beanName,clazz);
        this.beanName = beanName;
    }

    public AnnotationMetadata getMetadata() {
        return metadata;
    }

    public Resource getResource() {
        return resource;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * 是否导入类
     */
    public boolean isImported() {
        return !this.importedBy.isEmpty();
    }

    public boolean hasBeanMethod(){
        return !this.beanMethods.isEmpty();
    }

    public void addImportedBy(Class<?> clazz){
        this.importedBy.add(clazz);
    }

    public Set<Class<?>> getImportedBy(){
        return this.importedBy;
    }

    public void addBeanMethod(BeanMethod method) {
        this.beanMethods.add(method);
    }

    public void removeBeanMethod(BeanMethod beanMethod){
        this.beanMethods.remove(beanMethod);
    }

    public Set<BeanMethod> getBeanMethods() {
        return this.beanMethods;
    }

    @Override
    public String toString() {
        return "ConfigurationClass{" +
                ", resource=" + resource +
                ", beanName='" + beanName + '\'' +
                '}';
    }
}