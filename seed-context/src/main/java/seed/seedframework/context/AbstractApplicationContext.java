package seed.seedframework.context;

import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.exception.BeansException;
import seed.seedframework.beans.exception.NoSuchBeanDefinitionException;
import seed.seedframework.beans.factory.BeanDefinitionRegistry;
import seed.seedframework.beans.factory.BeanFactory;
import seed.seedframework.beans.factory.config.BeanDefinitionRegistryPostProcessor;
import seed.seedframework.beans.factory.config.BeanFactoryPostProcessor;
import seed.seedframework.beans.factory.config.BeanPostProcessor;
import seed.seedframework.beans.factory.config.ConfigurableDefinitionBeanFactory;
import seed.seedframework.context.config.ApplicationContextAwareProcessor;
import seed.seedframework.core.annotation.Priority;
import seed.seedframework.core.env.ConfigurableEnvironment;
import seed.seedframework.core.env.StandardEnvironment;
import seed.seedframework.core.io.resource.DefaultResourceLoader;
import seed.seedframework.core.io.resource.ResourcePatternResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import seed.seedframework.util.AnnotatedElementUtils;
import seed.seedframework.util.Assert;
import seed.seedframework.util.ClassUtils;
import seed.seedframework.util.ResourcePatternUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    protected final Log logger = LogFactory.getLog(getClass());

    private ConfigurableEnvironment environment;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>(32);

    private long startDate;

    private final AtomicBoolean active = new AtomicBoolean();

    private final AtomicBoolean closed = new AtomicBoolean();

    private ResourcePatternResolver resourcePatternResolver;

    protected static final String[] projectProperties = {"seed.properties","seed.yml"};

    public AbstractApplicationContext(){
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(this);
    }

    protected ResourcePatternResolver getResourcePatternResolver(){
        return this.resourcePatternResolver;
    }

    protected long getStartDate(){
        return this.startDate;
    }



    @Override
    public void setEnvironment(ConfigurableEnvironment environment){
        Assert.notNull(environment,"environment must not be null");
        this.environment = environment;
    }

    @Override
    public ConfigurableEnvironment getEnvironment() {
        if(this.environment == null){
            this.environment = createEnvironment();
        }
        return this.environment;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        Assert.notNull(beanFactoryPostProcessor,"beanFactoryPostProcessor must not be null");
        this.beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }

    @Override
    public void refresh() throws BeansException, IllegalArgumentException {
        try{

            // 环境变量配置，容器预启动
            prepareRefresh();

            ConfigurableDefinitionBeanFactory beanFactory = obtainFreshBeanFactory();
            //工厂中加入组件
            prepareBeanFactory(beanFactory);
            //执行beanFactoryProcessors
            invokeBeanFactoryPostProcessors(beanFactory);
            //注册beanPostProcessor
            registerBeanPostProcessor(beanFactory);
            //完成bean的创建
            finishBeanFactoryInitialization(beanFactory);
            //结束刷新
            finishRefresh();

        }catch (BeansException e){

            // todo
            destroyBeans();

            // todo
            cancelRefresh();

            throw e;
        }

    }


    protected void prepareRefresh(){
        this.startDate = System.currentTimeMillis();
        this.closed.set(false);
        this.active.set(true);

    }




    protected void prepareBeanFactory(ConfigurableDefinitionBeanFactory beanFactory) {
        beanFactory.setBeanClassLoader(getClassLoader());

        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        //注册环境信息
        beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME,this.environment);
        beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME,this.environment.getSystemEnvironment());
        beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME,this.environment.getSystemProperties());


        //TODO: 设置忽略注册的接口以及指定接口注册指定bean

    }

    protected ConfigurableDefinitionBeanFactory obtainFreshBeanFactory(){
        refreshBeanFactory();
        ConfigurableDefinitionBeanFactory beanFactory = getBeanFactory();
        return beanFactory;
    }

    public ClassLoader getClassLoader(){
        ClassLoader classLoader = super.getClassLoader();
        return (classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader);
    }

    protected abstract void refreshBeanFactory()throws IllegalStateException;

    protected abstract void closeBeanFactory();

    protected void invokeBeanFactoryPostProcessors(ConfigurableDefinitionBeanFactory beanFactory) {
        // TODO: 排序

        if(beanFactory instanceof BeanDefinitionRegistry){
            String[] registerPostProcessors = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class);
            List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();
            //确保每个类型的 BeanFactoryProcessor 只执行一次
            HashMap<String,Class<BeanDefinitionRegistryPostProcessor>> processed = new HashMap<>();
            for(String beanName: registerPostProcessors){
                BeanDefinitionRegistryPostProcessor bean = beanFactory.getBean(beanName, BeanDefinitionRegistryPostProcessor.class);
                processed.put(beanName,(Class<BeanDefinitionRegistryPostProcessor>) bean.getClass());
                currentRegistryProcessors.add(bean);
            }
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
            doInvokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors,registry);

            currentRegistryProcessors.clear();
            //执行自定义的BeanDefinitionRegistryPostProcessor
            registerPostProcessors = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class);
            for(String beanName: registerPostProcessors){
                if(!processed.containsKey(beanName)){
                    BeanDefinitionRegistryPostProcessor bean = beanFactory.getBean(beanName, BeanDefinitionRegistryPostProcessor.class);
                    if(!processed.keySet().contains(bean.getClass())){
                        currentRegistryProcessors.add(bean);
                        processed.put(beanName, (Class<BeanDefinitionRegistryPostProcessor>) bean.getClass());
                    }
                }
            }
            doInvokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors,registry);
            currentRegistryProcessors.clear();
        }

        List<BeanFactoryPostProcessor> currentFactoryProcessors = new ArrayList<>();
        //执行BeanFactoryProcessor
        String[] beanFactoryProcessors = beanFactory.getBeanNamesForType(BeanFactory.class);

        HashMap<String,Class<BeanFactoryPostProcessor>> processed = new HashMap<>();
        for(String beanName : beanFactoryProcessors){
            if(!processed.containsKey(beanName)){
                BeanFactoryPostProcessor bean = beanFactory.getBean(beanName, BeanFactoryPostProcessor.class);
                if(!processed.keySet().contains(bean.getClass())){
                    currentFactoryProcessors.add(bean);
                    processed.put(beanName, (Class<BeanFactoryPostProcessor>) bean.getClass());
                }
            }
        }

        doInvokeBeanFactoryPostProcessors(currentFactoryProcessors, beanFactory);
        currentFactoryProcessors.clear();

    }


    protected boolean hasPrority(Object bean){
        return AnnotatedElementUtils.isAnnotated(bean.getClass(), Priority.class.getName());
    }

    protected void doInvokeBeanFactoryPostProcessors(List<BeanFactoryPostProcessor> processors, ConfigurableDefinitionBeanFactory beanFactory){
        for(BeanFactoryPostProcessor postProcessor : processors){
            postProcessor.postProcessBeanFactory(beanFactory);
        }
    }


    protected void doInvokeBeanDefinitionRegistryPostProcessors(List<BeanDefinitionRegistryPostProcessor> processors, BeanDefinitionRegistry registry) {
        for(BeanDefinitionRegistryPostProcessor postProcessor : processors){
            postProcessor.postProcessBeanDefinitionRegistry(registry);
        }
    }

    protected void registerBeanPostProcessor(ConfigurableDefinitionBeanFactory beanFactory){
        //向BeanFactory中添加beanPostProcessor
        String[] postProcessors = beanFactory.getBeanNamesForType(BeanPostProcessor.class);
        for(String beanName : postProcessors){
            BeanPostProcessor bean = beanFactory.getBean(beanName, BeanPostProcessor.class);
            beanFactory.addBeanPostProcessor(bean);
        }
    }


    protected void finishBeanFactoryInitialization(ConfigurableDefinitionBeanFactory beanFactory){
        //创建所有的单例非懒加载对象
        beanFactory.preInstantiateSingletons();
    }

    protected void finishRefresh(){
        // todo 清除缓存，避免出现无法gc的关联数据
    };

    protected void destroyBeans(){
        // todo 清除缓存, 删除所有创建对象
        this.getBeanFactory();
    };

    protected void cancelRefresh(){
        this.active.set(false);
    };

    protected ConfigurableEnvironment createEnvironment() {
        return new StandardEnvironment();
    }

    protected List<BeanFactoryPostProcessor> getBeaProcessors(){
        return this.beanFactoryPostProcessors;
    }

    @Override
    public boolean isActive() {
        return this.active.get();
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public String getId() {
        return this.hashCode()+"";
    }

    @Override
    public String getApplicationName() {
        return null;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    @Override
    public String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return getBeanFactory().getBeanNamesForType(type,includeNonSingletons,allowEagerInit);
    }
    @Override
    public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
        return getBeanFactory().getBeansOfType(type,includeNonSingletons,allowEagerInit);
    }

    @Nullable
    @Override
    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
        return getBeanFactory().findAnnotationOnBean(beanName,annotationType);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return getBeanFactory().getBeanDefinition(beanName);
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public <T> T getBean(String name, @Nullable Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name,requiredType);
    }

    @Override
    public Object getBean(String name, @Nullable Object[] args) {
        return getBeanFactory().getBean(name,args);
    }

    @Override
    public <T> T getBean(String name, @Nullable Class<T> requiredType, @Nullable Object[] args) throws BeansException {
        return getBeanFactory().getBean(name,requiredType,args);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, @Nullable Object... args) throws BeansException {
        return getBeanFactory().getBean(requiredType,args);
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isPrototype(name);
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().getType(name);
    }

    @Override
    public void close() throws IOException {

    }
}