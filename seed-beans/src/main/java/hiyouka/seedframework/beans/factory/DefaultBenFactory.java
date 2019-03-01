package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.definition.AbstractBeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.exception.BeanDefinitionStoreException;
import hiyouka.seedframework.exception.BeansException;
import hiyouka.seedframework.exception.NoSuchBeanDefinitionException;
import hiyouka.seedframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class DefaultBenFactory extends AbstractBeanCreateFactory implements BeanDefinitionRegistry {


    /** 是否允许重复注册 */
    private boolean allowBeanDefinitionOverriding = true;

    /** Map of singleton and non-singleton bean names, keyed by dependency type */
    private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap<>(64);

    /** Map of singleton-only bean names, keyed by dependency type */
    private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap<>(64);

    /** Map of bean definition objects, keyed by bean name */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /** List of bean definition names, in registration order */
    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    /** List of names of manually registered singletons, in registration order (内部的bean)*/
    private volatile Set<String> manualSingletonNames = new LinkedHashSet<>(16);



    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        BeanDefinition oldBeanDefinition = this.beanDefinitionMap.get(beanName);
        if(oldBeanDefinition != null){
            if(!isAllowBeanDefinitionOverriding()){
                throw new BeanDefinitionStoreException("can not register this bean : " + beanName + "there is already registered !! ",beanName);
            }
            this.beanDefinitionMap.put(beanName,beanDefinition);
        }else {
            if(hasBeanCreationStarted()){ //已经开始bean的创建阶段
                synchronized (this.beanDefinitionMap){  //创建已经开始的话锁定保证稳定更新
                    this.beanDefinitionMap.put(beanName, beanDefinition);
                    List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
                    updatedDefinitions.addAll(this.beanDefinitionNames);
                    updatedDefinitions.add(beanName);
                    this.beanDefinitionNames = updatedDefinitions;
                    if (this.manualSingletonNames.contains(beanName)) {
                        Set<String> updatedSingletons = new LinkedHashSet<>(this.manualSingletonNames);
                        updatedSingletons.remove(beanName);
                        this.manualSingletonNames = updatedSingletons;
                    }
                }
            }else {
                this.beanDefinitionMap.put(beanName, beanDefinition);
                this.beanDefinitionNames.add(beanName);
                this.manualSingletonNames.remove(beanName);
            }
        }
        if (oldBeanDefinition != null || containsSingleton(beanName)) {
            resetBeanDefinition(beanName,oldBeanDefinition);
        }
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition remove = this.beanDefinitionMap.remove(beanName);
        if(remove == null)
            logger.error("no bean to remove : " + beanName);
        throw new NoSuchBeanDefinitionException(beanName);

    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return null;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionNames.toArray(new String[beanDefinitionNames.size()]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return 0;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return false;
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return false;
    }

    public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding) {
        this.allowBeanDefinitionOverriding = allowBeanDefinitionOverriding;
    }

    public boolean isAllowBeanDefinitionOverriding() {
        return this.allowBeanDefinitionOverriding;
    }

    protected void resetBeanDefinition(String beanName, BeanDefinition oldBeanDefinition) {
        destroySingleton(beanName,oldBeanDefinition);
        for (String bdName : this.beanDefinitionNames) {
            if (!beanName.equals(bdName)) {
               BeanDefinition bd = this.beanDefinitionMap.get(bdName);
                if (beanName.equals(bd.getParentName())) {
                    resetBeanDefinition(bdName,bd);
                }
            }
        }
    }

    public void destroySingleton(String beanName,BeanDefinition beanDefinition){
        if(beanDefinition instanceof AbstractBeanDefinition){
            Class<?> beanClass = beanDefinition.getBeanClass();
            String destroyMethodName = ((AbstractBeanDefinition) beanDefinition).getDestroyMethodName();
            try {
                Method declaredMethod = beanClass.getDeclaredMethod(destroyMethodName);
                ReflectionUtils.invokeMethod(declaredMethod,getSingleton(beanName));
            } catch (NoSuchMethodException e) {
                throw new BeansException("No such method : " + destroyMethodName);
            }
        }
        destroySingleton(beanName);
    }

    public void destroySingleton(String beanName) {
        super.destroySingleton(beanName);
        this.manualSingletonNames.remove(beanName);
    }
}