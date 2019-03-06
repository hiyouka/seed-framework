package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.exception.BeanDefinitionStoreException;
import hiyouka.seedframework.beans.exception.NoSuchBeanDefinitionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {

    protected final Log logger = LogFactory.getLog(getClass());

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
        return new String[0];
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


}