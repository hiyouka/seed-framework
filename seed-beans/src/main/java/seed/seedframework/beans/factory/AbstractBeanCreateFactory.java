package seed.seedframework.beans.factory;

import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.definition.RootBeanDefinition;
import seed.seedframework.beans.exception.BeanCreatedException;
import seed.seedframework.beans.exception.BeanInstantiationException;
import seed.seedframework.beans.factory.config.BeanPostProcessor;
import seed.seedframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import seed.seedframework.beans.factory.config.MergedBeanDefinitionPostProcessor;
import seed.seedframework.beans.metadata.PropertyValues;
import seed.seedframework.util.CollectionUtils;
import seed.seedframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractBeanCreateFactory extends AbstractBeanFactory
                        implements DefinitionBeanFactory, BeanCreateFactory{


//    private final Map<String, BeanHolder> factoryBeanInstanceCache = new ConcurrentHashMap<>(16);
//
    private ThreadLocal<List<String>> currentlyCreatedBean = new ThreadLocal<>();

    @Override
    public <T> T createBean(Class<T> beanClass) throws BeanCreatedException {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(beanClass);
        return (T) createBean(beanClass.getName(),rootBeanDefinition,null);
    }

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args)
                                                                throws BeanCreatedException{
        return doCreateBean(beanName, beanDefinition, args);
    }

    protected void addCurrentlyCreatedBean(String beanName){
        List<String> beanNames = this.currentlyCreatedBean.get();
        if(beanNames == null){
            beanNames = new ArrayList<>();
            this.currentlyCreatedBean.set(beanNames);
        }
        beanNames.add(beanName);
    }

    protected void removeCurrentlyCreatedBean(String beanName){
        List<String> beanNames = this.currentlyCreatedBean.get();
        if(beanNames == null){
            return;
        }
        beanNames.remove(beanName);
    }

    protected boolean isCurrentlyCreated(String beanName){
        List<String> beanNames = this.currentlyCreatedBean.get();
        if(beanNames == null){
            return false;
        }
        return beanNames.contains(beanName);
    }

    protected List<String> getAllCurrentlyCreated(){
        List<String> result = currentlyCreatedBean.get();
        if(CollectionUtils.isEmpty(result)){
            result = new ArrayList<>();
            currentlyCreatedBean.set(result);
        }
        return result;
    }

    protected abstract Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args)
                                                                    throws BeanCreatedException;

    protected void applyMergedBeanDefinitionPostProcessors(BeanDefinition mbd, Class<?> beanType, String beanName){
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof MergedBeanDefinitionPostProcessor) {
                MergedBeanDefinitionPostProcessor bdp = (MergedBeanDefinitionPostProcessor) bp;
                bdp.postProcessMergedBeanDefinition(mbd, beanType, beanName);
            }
        }
    }

    protected void populateInstance(String beanName, BeanDefinition beanDefinition, Object instance){
        List<InstantiationAwareBeanPostProcessor> inPs = new ArrayList<>();
        for(BeanPostProcessor beanPostProcessor : getBeanPostProcessors()){
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                inPs.add((InstantiationAwareBeanPostProcessor) beanPostProcessor);
                boolean continues = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessAfterInstantiation(instance, beanName);
                if(!continues){
                    return;
                }
            }
        }
        PropertyValues pvs = beanDefinition.getPropertyValues();
        for(InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor : inPs){
            pvs = instantiationAwareBeanPostProcessor.postProcessProperties(pvs,instance,beanName);
        }
    }


    protected Object applyPostProcessBeforeInitialization(Object instance, String beanName) {
        Object result = instance;
        for(BeanPostProcessor postProcessor : getBeanPostProcessors ()){
            Object current = postProcessor.postProcessBeforeInitialization(instance, beanName);
            if(current == null){
                return result;
            }
            result = current;
        }
        return result;
    }

    protected Object applyPostProcessAfterInitialization(Object instance, String beanName) {
        Object result = instance;
        for(BeanPostProcessor postProcessor : getBeanPostProcessors ()){
            Object current = postProcessor.postProcessAfterInitialization(instance, beanName);
            if(current == null){
                return result;
            }
            result = current;
        }
        return result;
    }



    protected void getRealMethod(Method factoryMethod, String methodName, Class<?> clazz){
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for(Method method : declaredMethods){
            if(factoryMethod == null && methodName.equals(method.getName())){
                factoryMethod =  method;
            }
        }
    }

    protected void validateSingletonBean(String beanName, BeanDefinition beanDefinition){
        if(beanDefinition.isSingleton()){
            Object singleton = this.getSingleton(beanName);
            if(singleton != null)
                throw new BeanInstantiationException("this bean : " + beanName + " already existing ...");
        }
    }

    protected boolean isFactoryBeanToCreate(BeanDefinition beanDefinition){
        return StringUtils.hasText(beanDefinition.getFactoryBeanName())
           && StringUtils.hasText(beanDefinition.getFactoryMethodName());
    }

    protected void validateBeanDefinition(String beanName, BeanDefinition beanDefinition){
        if(beanDefinition.isAbstract()){
            throw new BeanCreatedException("create bean : "+beanName+" must mot be abstract");
        }
    }

}