package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected final Log logger = LogFactory.getLog(getClass());

    /** cache of single bean : beanName -> bean instance */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /** Cache of early singleton objects: bean name --> bean instance */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    /** Set of registered singletons, containing the bean names in registration order */
    private final Set<String> registeredSingletons = new LinkedHashSet<>(256);

    /** Names of beans that are currently in creation */
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        Assert.notNull(beanName, "Bean name must not be null");
        Assert.notNull(singletonObject, "Singleton object must not be null");
        synchronized (this.singletonObjects){
            Object obj = this.singletonObjects.get(beanName);
            if(obj != null){
                throw new IllegalStateException("Could not register bean : " + beanName + "this name is already been register");
            }
            addSingleton(beanName, singletonObject);
        }
    }

    protected void addEarlySingleObjects(String beanName, Object instance){
        this.earlySingletonObjects.put(beanName,instance);
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return getSingleton(beanName,true);
    }

    protected Object getSingleton(String beanName, boolean allowEarlyReference) {
        Object singletonObject = this.singletonObjects.get(beanName);
        /**  如果该对象正在创建则先获取早期的创建对象 */
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            synchronized (this.singletonObjects) {
                singletonObject = this.earlySingletonObjects.get(beanName);
//                if (singletonObject == null && allowEarlyReference) {
//                    ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
//                    if (singletonFactory != null) {
//                        singletonObject = singletonFactory.getObject();
//                        this.earlySingletonObjects.put(beanName, singletonObject);
//                        this.singletonFactories.remove(beanName);
//                    }
//                }
            }
        }
        return singletonObject;
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        synchronized (this.registeredSingletons) {
            return StringUtils.toStringArray(this.registeredSingletons);
        }
    }

    @Override
    public int getSingletonCount() {
        synchronized (this.registeredSingletons) {
            return this.registeredSingletons.size();
        }
    }

    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.remove(beanName);
        }
    }

    /** 如果该bean正在被创建返回 true */
    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    protected void clearSingletonCache() {
        synchronized (this.singletonObjects) {
            this.singletonObjects.clear();
            this.earlySingletonObjects.clear();
            this.registeredSingletons.clear();
        }
    }

    public void destroySingleton(String beanName) {
        removeSingleton(beanName);
    }

}