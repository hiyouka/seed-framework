package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.definition.AbstractBeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.definition.RootBeanDefinition;
import hiyouka.seedframework.beans.factory.config.BeanPostProcessor;
import hiyouka.seedframework.beans.factory.config.Initialization;
import hiyouka.seedframework.exception.BeanInstantiationException;
import hiyouka.seedframework.exception.BeansException;
import hiyouka.seedframework.util.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AbstractBeanCreateFactory extends AbstractBeanFactory implements BeanCreateFactory{


//    private final Map<String, BeanHolder> factoryBeanInstanceCache = new ConcurrentHashMap<>(16);
//
    ThreadLocal<List<String>> currentlyCreatedBean = new ThreadLocal<>();

    @Override
    public <T> T createBean(Class<T> beanClass) throws BeansException {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(beanClass);
        return (T) createBean(beanClass.getName(),rootBeanDefinition,null);
    }

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        return doCreateBean(beanName, beanDefinition, args);
    }

    protected void addCurrentlyCreatedBean(String beanName){
        List<String> beanNames = this.currentlyCreatedBean.get();
        if(beanNames == null){
            beanNames = new ArrayList<>();
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

    private Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) {

        addCurrentlyCreatedBean(beanName);
        // 1. 创建初步的对象。。。
        Object instance = createBeanInstance(beanName,beanDefinition,args);

        // 2. 将初步对象放入早期对象缓存
        if(beanDefinition.isSingleton()){
            addEarlySingleObjects(beanName, instance);
        }

        // 3. 给对象注入 @Autowire 和 @Value 的属性
        // do some things to resolver dependency

        // 4. 初始化方法执行
        instance = init(beanName,instance,beanDefinition);

        removeCurrentlyCreatedBean(beanName);

        return instance;
    }

    private Object init(String beanName, Object instance, BeanDefinition beanDefinition) {
        Object result = instance;
        // 1. 前置处理方法执行
        result = applyPostProcessBeforeInitialization(instance,beanName);

        // 2. 初始化方法执行
        try {
            initialization(result,beanDefinition);
        } catch (Exception e) {
            throw new IllegalStateException("bean : " + beanName + " init method invoke error !!",e);
        }

        // 3. 后置处理方法执行
        result = applyPostProcessAfterInitialization(instance,beanName);

        return result;
    }

    private void initialization(Object instance, BeanDefinition beanDefinition) throws Exception {
        if(instance instanceof Initialization){
            ((Initialization) instance).afterPropertiesSet();
        }
        if(beanDefinition instanceof AbstractBeanDefinition){
            String initMethodName = ((AbstractBeanDefinition) beanDefinition).getInitMethodName();
            if(StringUtils.hasText(initMethodName)){
                Method method = BeanUtils.findMethod(instance.getClass(), initMethodName, null);
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
        Assert.notNull(factoryMethod,"No Such Method ["+ factoryMethodName +"] from class " + bean.getClass().getName());
        return ReflectionUtils.invokeMethod(factoryMethod,bean);
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

}