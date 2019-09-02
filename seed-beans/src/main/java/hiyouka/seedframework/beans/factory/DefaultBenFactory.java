package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.annotation.Primary;
import hiyouka.seedframework.beans.definition.AbstractBeanDefinition;
import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.definition.BeanHolder;
import hiyouka.seedframework.beans.exception.*;
import hiyouka.seedframework.beans.factory.config.ConfigurableDefinitionBeanFactory;
import hiyouka.seedframework.util.AnnotatedElementUtils;
import hiyouka.seedframework.util.ArrayUtils;
import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.ReflectionUtils;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class DefaultBenFactory extends AbstractBeanCreateFactory implements ConfigurableDefinitionBeanFactory, BeanDefinitionRegistry , Serializable {


    /** 是否允许重复注册 */
    private boolean allowBeanDefinitionOverriding = true;

    /** Map of singleton and non-singleton hiyouka.framework.test.bean names, keyed by dependency type */
    private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap<>(64);

    /** Map of singleton-only hiyouka.framework.test.bean names, keyed by dependency type */
    private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap<>(64);

    /** Map of hiyouka.framework.test.bean definition objects, keyed by hiyouka.framework.test.bean name */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /** List of hiyouka.framework.test.bean definition names, in registration order */
    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    /** List of names of manually registered singletons, in registration order (手动创建的bean不会生成beanDefinition)*/
    private volatile Set<String> manualSingletonNames = new LinkedHashSet<>(16);

    private String serializationId;

    @Nullable
    public String getSerializationId() {
        return serializationId;
    }

    public void setSerializationId(@Nullable String serializationId) {
        this.serializationId = serializationId;
    }

    protected void addBeanNamesByType(Class<?> clazz, String name){
        allBeanNamesByType.put(clazz,add(name,allBeanNamesByType.get(clazz)));
    }

    protected void addSingletonBeanNamesByType(Class<?> clazz, String name){
        singletonBeanNamesByType.put(clazz,add(name,singletonBeanNamesByType.get(clazz)));
    }

    private String[] add(String name, String[] names){
        List<String> list;
        if(names == null){
            list = new ArrayList<>(1);
        }else {
            list = new ArrayList<>(names.length + 1);
            list.addAll(Arrays.asList(names));
        }
        list.add(name);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        BeanDefinition oldBeanDefinition = this.beanDefinitionMap.get(beanName);
        if(oldBeanDefinition != null){
            if(!isAllowBeanDefinitionOverriding()){
                throw new BeanDefinitionStoreException("can not register this hiyouka.framework.test.bean : " + beanName + "there is already registered !! ",beanName);
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
        if (oldBeanDefinition != null && containsSingleton(beanName)) {
            resetBeanDefinition(beanName,oldBeanDefinition);
        }
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition remove = this.beanDefinitionMap.remove(beanName);
        if(remove == null)
            logger.error("no hiyouka.framework.test.bean to remove : " + beanName);
        throw new NoSuchBeanDefinitionException(beanName);

    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        if(beanDefinition == null){
            throw new NoSuchBeanDefinitionException("no hiyouka.framework.test.bean named '" + beanName + "' found in " + this);
        }
        return beanDefinition;
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

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        super.registerSingleton(beanName, singletonObject);
        if(hasBeanCreationStarted()){
            synchronized (this.beanDefinitionMap){
                if(!this.beanDefinitionMap.containsKey(beanName)){
                    Set<String> update = new LinkedHashSet<>(this.manualSingletonNames.size() + 1);
                    update.addAll(this.manualSingletonNames);
                    update.add(beanName);
                    this.manualSingletonNames = update;
                }
            }
        }else {
            if(!this.beanDefinitionMap.containsKey(beanName)){
                this.manualSingletonNames.add(beanName);
            }
        }

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

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBean(requiredType,null);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        BeanHolder<T> beanHolder= resolveBeanForType(requiredType, args);
        if(beanHolder.getBean() == null){
            throw new BeanNotFoundException("not found hiyouka.framework.test.bean for type : " + requiredType.getName() + " args : " + ArrayUtils.asList(args));
        }
        return beanHolder.getBean();
    }


    protected <T> BeanHolder<T> resolveBeanForType(Class<T> requiredType, Object... args){
        Assert.notNull(requiredType,"requireType must not be null");
        String[] beanNames = getBeanNamesForType(requiredType);
        String beanName;
        if(beanNames.length == 1){
            beanName = beanNames[0];
        }
        else{
            beanName = determinePrimary(beanNames,requiredType);
        }
        T bean;
        if(ArrayUtils.isEmpty(args)){
            bean = (T) getBean(beanName);
        }else {
            bean = (T) getBean(beanName,args);
        }
        if(bean == null)
            return null;
        return new BeanHolder<T>(bean,beanName);
    }

    protected String determinePrimary(String[] beanNames,Class<?> requiredType){
        String result;
        List<String> primaryBeanName = new ArrayList<>();
        for(String beanName : beanNames){
            Class<?> type = getType(beanName);
            if(AnnotatedElementUtils.isAnnotated(type,Primary.class.getName())){
                primaryBeanName.add(beanName);
            }
        }
        if(primaryBeanName.size() == 1){
            result = primaryBeanName.get(0);
        }
        else{
            String message = null;
            if(primaryBeanName.size() > 0){
                message = ",primary beanName : " + primaryBeanName;
            }
            throw new NoUniqueBeanException("not found unique hiyouka.framework.test.bean for type : " + requiredType.getName()
                    + (message == null ? "" : message));
        }
        return result;
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type){
        return getBeanNamesForType(type,true,true);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean allowNoSingleton, boolean allowEarlyInit){
        Map<Class<?>, String[]> cache;
        if(allowNoSingleton){
            cache = allBeanNamesByType;
        }
        else {
            cache = singletonBeanNamesByType;
        }
        String[] names = cache.get(type);
        if(names != null){
            return names;
        }
        names = doGetBeanNamesForType(type,allowNoSingleton,allowEarlyInit);
        cache.put(type,names);
        return names;
    }

    @Override
    public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
        return getBeansOfType(type,true,true);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
        String[] beanNames = getBeanNamesForType(type,includeNonSingletons,allowEagerInit);
        Map<String,T> result = new HashMap<>(beanNames.length);
        for(String beanName : beanNames){
            try{
                Object bean = getBean(beanName);
                result.put(beanName,bean == null ? null : (T)bean);
            }catch (BeanCreatedException e){
                if(isCurrentlyCreated(beanName)){
                    logger.error("create hiyouka.framework.test.bean : "+beanName+" error, this hiyouka.framework.test.bean is in creation");
                }
                throw e;
            }

        }
        return result;
    }

    @Nullable
    @Override
    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
        return null;
    }

    /**
     * @param type  类型
     * @param allowNoSingleton 是否获取非单例的对象名称
     * @param allowEarlyInit 是否允许提前创建(lazy 对象)
     * @return  符合类型的集合
     */
    private String[] doGetBeanNamesForType(Class<?> type, boolean allowNoSingleton, boolean allowEarlyInit){
        List<String> result = new ArrayList<>();
        for(String name : beanDefinitionNames){
            BeanDefinition beanDefinition = getBeanDefinition(name);
            if(beanDefinition != null){
                boolean isMatch = false;
                Class<?> beanClass = getType(name);
                if(beanClass != null && !beanDefinition.isAbstract()){
                    isMatch = (beanDefinition.isSingleton() || allowNoSingleton)
                            &&(allowEarlyInit || (!beanDefinition.isLazyInit() && isCreateBean(name,beanDefinition)))
                            &&(isBeanTypeOf(beanClass,type));
                }
                if(isMatch){
                    result.add(name);
                }
            }
       }
        return result.toArray(new String[result.size()]);
    }

    private boolean isCreateBean(String beanName,BeanDefinition beanDefinition){
        if(beanDefinition.isSingleton()){
            Object singleton = getSingleton(beanName);
            if(singleton == null){
                return false;
            }
        }
        return true;
    }

    private boolean isBeanTypeOf(Class<?> type, Class<?> compareType){
        if(type == compareType){
            return true;
        }
        return compareType.isAssignableFrom(type);
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        String[] beanNames = getBeanDefinitionNames();
        for(String beanName : beanNames){
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            if(!beanDefinition.isAbstract() && beanDefinition.isSingleton()
                    && !beanDefinition.isLazyInit()){
                getBean(beanName);
            }
        }

    }
}