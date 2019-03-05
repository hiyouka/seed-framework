package hiyouka.seedframework.beans.factory;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.factory.config.BeanPostProcessor;
import hiyouka.seedframework.beans.factory.config.ConfigurableBeanFactory;
import hiyouka.seedframework.exception.BeansException;
import hiyouka.seedframework.exception.NoSuchBeanDefinitionException;
import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {



    /** Names of beans that have already been created at least once */
    private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap<>(256));

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    protected boolean hasBeanCreationStarted() {
        return !this.alreadyCreated.isEmpty();
    }

    protected void addAlreadyCreated(String beanName){
        Assert.hasText(beanName,"beanName must not be null !!");
        this.alreadyCreated.add(beanName);
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = (beanClassLoader == null ? ClassUtils.getDefaultClassLoader() : beanClassLoader);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Assert.notNull(beanPostProcessor,"beanPostProcessor must not be null !!");
        this.beanPostProcessors.remove(beanPostProcessor); //只注册一个同样的处理器
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors(){
        return this.beanPostProcessors;
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return null;
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return null;
    }

    protected <T> T doGetBean(String name, Class<T> requiredType, Object[] args){
        return null;
    }

    protected abstract  <T> T createBean(String beanName, BeanDefinition beanDefinition, @Nullable Object[] args);

}