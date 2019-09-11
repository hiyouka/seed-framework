package seed.seedframework.beans.factory;

import seed.seedframework.beans.definition.AbstractBeanDefinition;
import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.definition.RootBeanDefinition;
import seed.seedframework.beans.exception.BeanCreatedException;
import seed.seedframework.beans.exception.BeanInstantiationException;
import seed.seedframework.beans.factory.config.BeanPostProcessor;
import seed.seedframework.beans.factory.config.Initialization;
import seed.seedframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import seed.seedframework.beans.factory.config.MergedBeanDefinitionPostProcessor;
import seed.seedframework.beans.metadata.PropertyValues;
import seed.seedframework.util.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractBeanCreateFactory extends AbstractBeanFactory implements BeanCreateFactory{


//    private final Map<String, BeanHolder> factoryBeanInstanceCache = new ConcurrentHashMap<>(16);
//
    private ThreadLocal<List<String>> currentlyCreatedBean = new ThreadLocal<>();

    @Override
    public <T> T createBean(Class<T> beanClass) throws BeanCreatedException {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(beanClass);
        return (T) createBean(beanClass.getName(),rootBeanDefinition,null);
    }

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeanCreatedException{
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

    private Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeanCreatedException{
        Object instance;
        try{
            addCurrentlyCreatedBean(beanName);
            // 1. 创建初步的对象。。。
            instance = createBeanInstance(beanName,beanDefinition,args);

            // 2. 处理需注入的bean
            applyMergedBeanDefinitionPostProcessors(beanDefinition,beanDefinition.getBeanClass(),beanName);

            // 3. 将初步对象放入早期对象缓存
            if(beanDefinition.isSingleton()){
                addEarlySingleObjects(beanName, instance);
            }

            // 4. 填充bean
            populateInstance(beanName,beanDefinition,instance);

             instance = initInstance(beanName,instance,beanDefinition);

        }catch (Exception e){
            throw new BeanCreatedException("create bean : "+ beanName +" error !!" , e);
        }
        finally {
            removeCurrentlyCreatedBean(beanName);
        }
        logger.info("create bean :" + beanName + " success");
        return instance;
    }

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

    private Object initInstance(String beanName, Object instance, BeanDefinition beanDefinition) {
        // 1. 前置处理方法执行
        Object result = applyPostProcessBeforeInitialization(instance,beanName);

        // 2. 初始化方法执行
        try {
            initialization(result,beanDefinition);
        } catch (Exception e) {
            throw new IllegalStateException("bean : " + beanName + " init method invoke error !!",e);
        }
        // 3. 后置处理方法执行
        result = applyPostProcessAfterInitialization(result,beanName);

        return result;
    }

    private void initialization(Object instance, BeanDefinition beanDefinition) throws Exception {
        if(instance instanceof Initialization){
            ((Initialization) instance).afterPropertiesSet();
        }
        if(beanDefinition instanceof AbstractBeanDefinition){
            String initMethodName = ((AbstractBeanDefinition) beanDefinition).getInitMethodName();
            if(StringUtils.hasText(initMethodName)){
                Method method = BeanUtils.findMethod(instance.getClass(), initMethodName);
                ReflectionUtils.invokeMethod(method,instance);
            }
        }
    }


    private Object applyPostProcessBeforeInitialization(Object instance, String beanName) {
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

    private Object applyPostProcessAfterInitialization(Object instance, String beanName) {
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

    protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args){
        Object bean;
        validateBeanDefinition(beanName,beanDefinition);
        if(isFactoryBeanToCreate(beanDefinition)){
            bean = instanceBeanUsingFactoryMethod(beanName,beanDefinition);
        }else {
            bean = instanceBean(beanName,beanDefinition,args);
        }
        addAlreadyCreated(beanName);
        return bean;
    }

    private Object instanceBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        validateSingletonBean(beanName,beanDefinition);
        Assert.notNull(beanDefinition.getBeanClass(),"bean : " + beanName + " beanDefinition must have beanClass");
        if(ArrayUtils.isEmpty(args)){
            return BeanUtils.instanceClass(beanDefinition.getBeanClass());
        }else {
            return BeanUtils.instanceClass(BeanUtils.findConstructor(beanDefinition.getBeanClass(),args),args);
        }
    }

    /** Bean method to get bean instance  */
    protected Object instanceBeanUsingFactoryMethod(String beanName, BeanDefinition beanDefinition) {
        validateSingletonBean(beanName,beanDefinition);
        String factoryBeanName = beanDefinition.getFactoryBeanName();
        String factoryMethodName = beanDefinition.getFactoryMethodName();
        Object bean = getBean(factoryBeanName);
        Method[] declaredMethods = bean.getClass().getDeclaredMethods();
        Method factoryMethod = null;
        for(Method method : declaredMethods){
            if(method.getName().equals(factoryMethodName)){
                factoryMethod = method;
            }
        }
        // 从接口中获取方法
        if(factoryMethod == null){
            getRealMethod(factoryMethod,factoryMethodName,bean.getClass());
        }

        Assert.notNull(factoryMethod,"No Such Method ["+ factoryMethodName +"] from class " + bean.getClass().getName());
        return ReflectionUtils.invokeMethod(factoryMethod,bean);
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