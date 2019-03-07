package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.exception.*;
import hiyouka.seedframework.beans.factory.config.BeanPostProcessor;
import hiyouka.seedframework.beans.factory.config.ConfigurableBeanFactory;
import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.ClassUtils;

import javax.annotation.Nullable;
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
    public Object getBean(String beanName) throws BeansException {
        return doGetBean(beanName,null,null);
    }

    @Override
    public <T> T getBean(String beanName, @Nullable Class<T> requiredType) throws BeansException{
        return doGetBean(beanName,requiredType,null);
    }


    @Override
    public boolean containsBean(String beanName) {
        return false;
    }

    @Override
    public boolean isSingleton(String beanName) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public boolean isPrototype(String beanName) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public Class<?> getType(String beanName) throws NoSuchBeanDefinitionException {
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            if(beanDefinition == null){
                throw new NoSuchBeanDefinitionException("not found bean : " + beanName + "beanDefinition");
            }
            return beanDefinition.getBeanClass();
    }

    protected <T> T doGetBean(String beanName, Class<T> requiredType, Object[] args)throws BeansException{
        Object bean;
        Object singleton = getSingleton(beanName);
        if(singleton != null){
            if(isCurrentlyCreated(beanName)){
                logger.debug("return early single bean : " + beanName);
            }
            bean = singleton;
        }
        else {
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            if (isPrototypeBeanCurrentlyInCreated(beanName, beanDefinition)) {
                //  如果该对象为多例对象并且该对象正在创建当中 说明循环创建了该对象
                List<String> allCurrentlyCreated = getAllCurrentlyCreated();
                StringBuffer buffer = new StringBuffer();
                int i = 0;
                for(String created : allCurrentlyCreated){
                    buffer.append("beanName:");
                    buffer.append(created);
                    i++;
                    if(i != allCurrentlyCreated.size()){
                        buffer.append("->");
                    }
                }

                throw new BeanCurrentlyInCreationException("bean is currently in creation, bean : " + beanName
                        + "..\n create relation :" + buffer.toString());
            }
            if(beanDefinition.isSingleton()){
                try{

                    bean = resolveBeanCreate(beanName, beanDefinition, args);
                    addSingleton(beanName,bean);
                }catch (BeanCreatedException e){
                    destroySingleton(beanName);
                    throw e;
                }
            }
            else if(beanDefinition.isPrototype()){
                bean = resolveBeanCreate(beanName, beanDefinition, args);
            }
            else {
                bean = resolveBeanCreate(beanName, beanDefinition, args);
            }
        }

        if(requiredType != null && bean.getClass() != requiredType ){
            throw new BeanNotRequiredException(" bean : "+beanName + " type["+bean.getClass().getName()
                    +"] not conform to " + requiredType.getName());
        }
        return (T) bean;
    }

    protected Object resolveBeanCreate(String beanName, BeanDefinition beanDefinition, Object[] args){
        beforeBeanCreated(beanName);
        Object bean = createBean(beanName, beanDefinition, args);
        afterBeanCreated(beanName);
        return bean;
    }

    protected void beforeBeanCreated(String beanName){
        if(isCurrentlyCreated(beanName)){
            throw new BeanCurrentlyInCreationException("bean : " + beanName + "in Creation before bean Create");
        }
    }


    protected void afterBeanCreated(String beanName){
        if(isCurrentlyCreated(beanName)){
            throw new BeanCurrentlyInCreationException("bean : " + beanName + "in Creation After bean Create");
        }
    }

    protected boolean isPrototypeBeanCurrentlyInCreated(String beanName, BeanDefinition beanDefinition){
        return beanDefinition.getScope().equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
                && isCurrentlyCreated(beanName);
    }



    @Override
    public Object getBean(String beanName, @Nullable Object[] args){
        return doGetBean(beanName,null,args);
    };

    @Override
    public <T> T getBean(String beanName, @Nullable Class<T> requiredType, @Nullable Object[] args) throws BeansException{
        return doGetBean(beanName,requiredType,args);
    }


    protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;

    protected abstract  <T> T createBean(String beanName, BeanDefinition beanDefinition, @Nullable Object[] args)throws BeanCreatedException;

    protected abstract boolean isCurrentlyCreated(String beanName);

    protected abstract List<String> getAllCurrentlyCreated();

}