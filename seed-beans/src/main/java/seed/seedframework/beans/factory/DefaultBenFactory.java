package seed.seedframework.beans.factory;

import seed.seedframework.beans.annotation.Primary;
import seed.seedframework.beans.annotation.Specify;
import seed.seedframework.beans.annotation.Value;
import seed.seedframework.beans.definition.AbstractBeanDefinition;
import seed.seedframework.beans.definition.AnnotatedGenericBeanDefinition;
import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.definition.BeanHolder;
import seed.seedframework.beans.exception.*;
import seed.seedframework.beans.factory.config.ConfigurableDefinitionBeanFactory;
import seed.seedframework.beans.metadata.DependencyDescriptor;
import seed.seedframework.beans.metadata.GenericMethodMetadata;
import seed.seedframework.beans.metadata.MethodMetadata;
import seed.seedframework.core.annotation.Priority;
import seed.seedframework.exception.SeedCoreException;
import seed.seedframework.util.*;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
    public class DefaultBenFactory extends AbstractAutowiredBeanCreateFactory implements ConfigurableDefinitionBeanFactory, BeanDefinitionRegistry , Serializable {


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
        return list.toArray(new String[0]);
    }

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
        if (oldBeanDefinition != null && containsSingleton(beanName)) {
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
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        if(beanDefinition == null){
            throw new NoSuchBeanDefinitionException("no bean named '" + beanName + "' found in " + this);
        }
        return beanDefinition;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionNames.toArray(new String[0]);
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
            throw new BeanNotFoundException("not found bean for type : " + requiredType.getName() + " args : " + ArrayUtils.asList(args));
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

    private String determinePrimary(String[] beanNames,Class<?> requiredType){
        return determinePrimaryIfNecessary(beanNames,requiredType);
    }

    private String determinePrimaryIfNecessary(String[] beanNames,Class<?> requiredType){

        List<String> primaryBeanName = new ArrayList<>();
        Map<String,Class> types = new HashMap<>(beanNames.length);
        for(String beanName : beanNames){
            Class<?> type = getType(beanName);
            types.put(beanName,type);
            if(AnnotatedElementUtils.isAnnotated(type, Primary.class.getName())){
                primaryBeanName.add(beanName);
            }
        }
        // no primary bean order by priority
        if(primaryBeanName.size() == 0){
            Integer orderNum = Integer.MAX_VALUE;
            for(Map.Entry<String,Class> entry : types.entrySet()){
                if(AnnotatedElementUtils.isAnnotated(entry.getValue(),Priority.class.getName())){
                    Integer value = (Integer) AnnotatedElementUtils.getAttribute(entry.getValue(), Priority.class, "value");
                    if(orderNum.equals(value)){
                        primaryBeanName.add(entry.getKey());
                    }
                    else if(orderNum > value){
                        primaryBeanName.clear();
                        primaryBeanName.add(entry.getKey());
                    }
                    orderNum = value;
                }
            }
        }

        // no bean use @Primary or @priority to mark , so all match
        if(primaryBeanName.size() == 0){
            primaryBeanName.addAll(ArrayUtils.asList(beanNames));
        }

        if(primaryBeanName.size() == 1){
            return primaryBeanName.get(0);
        }

        String message;
        if(primaryBeanName.size() > 0){
            message = ", found "+primaryBeanName.size()+" : " + primaryBeanName;
            throw new NoUniqueBeanException("not found unique bean for type : " + requiredType.getName()
                    +  message);
        }
        else {
            throw new BeanNotFoundException("not found bean for type : " + requiredType.getName());
        }

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
                    logger.error("create bean : "+beanName+" error, this bean is in creation");
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
        return result.toArray(new String[0]);
    }

    private boolean isCreateBean(String beanName,BeanDefinition beanDefinition){
        if(beanDefinition.isSingleton()){
            Object singleton = getSingleton(beanName);
            return singleton != null;
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


    @Override
    public Object resolveDepend(DependencyDescriptor dsr, String beanName) {
        // todo 解决方法依赖注入
        ReturnWrapper value;
        if(DependencyDescriptor.DependencyType.AUTOWIRED.equals(dsr.getAutowiredType())){
            value = doResolveDependForAutowired(dsr,beanName);
        }
        else {
            value = doResolveDependForValue(dsr,beanName);
        }
        if(!value.hasReturn() && dsr.isRequired()){
            String errorMsg = "resolve depend error of bean :"  + beanName;
            if(dsr.getField() != null){
                errorMsg += ", field name : " + dsr.getField().getName()+", field type: "
                        + dsr.getField().getGenericType();
            }
            else {
                errorMsg += ",method : "+dsr.getDeclaringClass().getName()+"."+dsr.getMethodName()
                         +", parameter name : " + dsr.getParameter().getParameterName()+", parameter type: "
                         + dsr.getParameter().getGenericParameterType();
            }
            errorMsg += " \n Caused by: " + value.getErrorMessage();
            throw new BeanAutowiredException(errorMsg);
        }
        return value.getValue();
    }



    private ReturnWrapper doResolveDependForValue(DependencyDescriptor dsr,String beanName){
        Annotation valAnn = dsr.getAnnotationForType(Value.class);
        String value = (String) AnnotationUtils.getAttribute("value", valAnn);
        if(StringUtils.hasText(value)){
            ExpressionResolver resolver = getBean(ExpressionResolver.class);
            if(resolver != null){
                return new ReturnWrapper(resolver.resolve(value),null);
            }
        }
        return new ReturnWrapper(null," no value resolver from expression : " + value);
    }



    private ReturnWrapper doResolveDependForAutowired(DependencyDescriptor dsr,String beanName){
        Class<?> type = dsr.getType();
        // first to get beanName from @Specify annotation
        Annotation specify = dsr.getAnnotationForType(Specify.class);
        if(specify != null){
            String matchBeanName = (String)AnnotationUtils.getAttribute("value", specify);
            BeanDefinition beanDefinition = this.beanDefinitionMap.get(matchBeanName);
            if(beanDefinition == null){
                return new ReturnWrapper(null," not found beanDefinition " +
                        "for @Specify assign beanName :" + matchBeanName);
            }
            Class<?> beanClass = beanDefinition.getBeanClass();
            if(!type.isAssignableFrom(beanClass)){
                return new ReturnWrapper(null," @Specify assign beanName type :" + beanClass
                        + " not match field type :" + type);
            }else {
                return getBeanReturnWrapper(matchBeanName);
            }
        }

        // second autowired by dsr name to find bean name
        String attributeName = dsr.getAttributeName();
        BeanDefinition attributeBeanDefinition = this.beanDefinitionMap.get(attributeName);
        if(attributeBeanDefinition != null && type.isAssignableFrom(attributeBeanDefinition.getBeanClass())){
            return getBeanReturnWrapper(attributeName);
        }

        String[] names = this.getBeanNamesForType(type);
        if(names.length == 0){
            return new ReturnWrapper(null," not found BeanDefinition of type : " + type);
        }

        List<String> matchName = new ArrayList<>();
        // if it is no generic bean
        Type genericType = dsr.getGenericType();
        if(genericType instanceof Class){
            logger.trace(" filed : " + attributeName + " in create bean :" + beanName + ", no generic");

            // is need skip oneself ?
            matchName.addAll(ArrayUtils.asList(names));

        }
        else{
            for(String name : names){
                if(!isSelfReference(beanName,name)){
                    BeanDefinition beanDefinition = this.getBeanDefinition(name);
                    // resolve Bean method generic type
                    if(beanDefinition instanceof AnnotatedGenericBeanDefinition){
                        MethodMetadata metadata = ((AnnotatedGenericBeanDefinition) beanDefinition).getFactoryMethodMetadata();
                        // have generic bean write method
                        if(metadata instanceof GenericMethodMetadata){
                            ParameterizedType parameterizedType = ((GenericMethodMetadata) metadata).getParameterizedType();
                            if(ResolverTypeUtil.isAssignableFrom(genericType,parameterizedType)){
                                matchName.add(name);
                                continue;
                            }
                        }
                    }
                    if(ResolverTypeUtil.isAssignableFrom(genericType,beanDefinition.getBeanClass())){
                        matchName.add(name);
                    }
                }
            }
        }

        // support autowired self
        if(matchName.size() == 0){
            for(String name : names){
                if(isSelfReference(beanName,name)){
                    matchName.add(name);
                }
            }
        }

        String matchNameStr;
        if(matchName.size() == 0){
            return new ReturnWrapper(null," not found match bean of type : " + genericType);
        }
        else if(matchName.size() == 1){
            matchNameStr = matchName.get(0);
        }else {
            try{
                matchNameStr  = determinePrimaryIfNecessary(matchName.toArray(new String[0]),type);
            }
            catch (BeansException ex){
                return new ReturnWrapper(null,ex.getMessage());
            }
        }

        if(StringUtils.hasText(matchNameStr)){
            return getBeanReturnWrapper(matchNameStr);
        }
        else {
            return new ReturnWrapper(null," not found unique bean for type ：" + genericType);
        }

    }

    private ReturnWrapper getBeanReturnWrapper(String beanName){
        ReturnWrapper wrapper;
        try {
            Object bean = this.getBean(beanName);
            wrapper = new ReturnWrapper(bean,null);
        }catch (BeansException e){
            wrapper = new ReturnWrapper(null,e.getMessage());
        }
        return wrapper;
    }


    private static class ReturnWrapper{

        private final Object value;

        private final String errorMessage;

        ReturnWrapper(Object value, String errorMessage) {
            this.value = value;
            if(!hasReturn() && StringUtils.isEmpty(errorMessage)){
                throw new SeedCoreException("if return is null, must have errorMessage");
            }
            this.errorMessage = errorMessage;
        }

        Object getValue() {
            return value;
        }

        String getErrorMessage() {
            return errorMessage;
        }

        boolean hasReturn(){
            return this.value != null;
        }

    }


    protected boolean isSelfReference(String beanName, String currentBeanName){
        return (beanName != null && (beanName.equals(currentBeanName)));
    }


}