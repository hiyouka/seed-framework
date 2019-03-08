package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.exception.BeansException;
import hiyouka.seedframework.beans.exception.NoSuchBeanDefinitionException;
import hiyouka.seedframework.beans.factory.DefinitionBeanFactory;
import hiyouka.seedframework.beans.factory.config.BeanFactoryPostProcessor;
import hiyouka.seedframework.beans.factory.config.ConfigurableDefinitionBeanFactory;
import hiyouka.seedframework.core.env.ConfigurableEnvironment;
import hiyouka.seedframework.core.env.StandardEnvironment;
import hiyouka.seedframework.core.io.resource.DefaultResourceLoader;
import hiyouka.seedframework.core.io.resource.ResourcePatternResolver;
import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.ClassUtils;
import hiyouka.seedframework.util.ResourcePatternUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
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

            ConfigurableDefinitionBeanFactory beanFactory = getBeanFactory();
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

            destroyBeans();

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
        beanFactory.registerSingleton("",null);
    }

    protected DefinitionBeanFactory obtainFreshBeanFactory(){
        refreshBeanFactory();
        DefinitionBeanFactory beanFactory = getBeanFactory();
        return beanFactory;
    }

    public ClassLoader getClassLoader(){
        ClassLoader classLoader = super.getClassLoader();
        return (classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader);
    }

    protected abstract void refreshBeanFactory()throws IllegalStateException;

    protected abstract void closeBeanFactory();

    protected void invokeBeanFactoryPostProcessors(DefinitionBeanFactory beanFactory) {

    }

    protected void registerBeanPostProcessor(DefinitionBeanFactory beanFactory){};


    protected void finishBeanFactoryInitialization(DefinitionBeanFactory beanFactory){};

    protected void finishRefresh(){};

    protected void destroyBeans(){};

    protected void cancelRefresh(){};

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