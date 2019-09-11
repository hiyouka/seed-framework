package seed.seedframework.beans.factory;

import seed.seedframework.beans.definition.AbstractBeanDefinition;
import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.exception.BeanCreatedException;
import seed.seedframework.beans.factory.config.Initialization;
import seed.seedframework.beans.metadata.DependencyDescriptor;
import seed.seedframework.beans.metadata.MethodParameter;
import seed.seedframework.util.*;

import java.lang.reflect.Method;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractAutowiredBeanCreateFactory extends AbstractBeanCreateFactory implements AutowiredSupportBeanFactory {

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args)
            throws BeanCreatedException {
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

    private Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args){
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
    private Object instanceBeanUsingFactoryMethod(String beanName, BeanDefinition beanDefinition) {
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

        int parameterCount = factoryMethod.getParameterCount();
        if(parameterCount == 0){
            return ReflectionUtils.invokeMethod(factoryMethod,bean);
        }else {
            Object[] args = resolveDependParameters(factoryMethod,beanName);
            return ReflectionUtils.invokeMethod(factoryMethod,bean,args);
        }

    }

    private Object[] resolveDependParameters(Method method,String beanName){
        Object[] result = new Object[method.getParameterCount()];
        for(int i=0; i<result.length; i++){
            try{
                result[i] =
                        resolveDepend(new DependencyDescriptor(new MethodParameter(method, i)),beanName);
            }catch (BeanCreatedException e){
                result[i] = null;
            }
        }
        return result;
    }


}