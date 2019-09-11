package seed.seedframework.context;

import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.exception.BeanDefinitionStoreException;
import seed.seedframework.beans.exception.NoSuchBeanDefinitionException;
import seed.seedframework.beans.factory.BeanDefinitionRegistry;
import seed.seedframework.beans.factory.DefaultBenFactory;
import seed.seedframework.beans.factory.config.ConfigurableDefinitionBeanFactory;
import seed.seedframework.util.Assert;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    private final DefaultBenFactory beanFactory;

    private final AtomicBoolean refreshed = new AtomicBoolean();

    public GenericApplicationContext(){
        this.beanFactory = new DefaultBenFactory();
    }
    public GenericApplicationContext(DefaultBenFactory beanFactory) {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        this.beanFactory = beanFactory;
    }


    protected final void refreshBeanFactory() throws IllegalStateException {
        if (!this.refreshed.compareAndSet(false, true)) {
            throw new IllegalStateException(
                    "GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
        }
        this.beanFactory.setSerializationId(getId());
    }

    @Override
    protected void destroyBeans() {
        super.destroyBeans();
        this.beanFactory.getBeanDefinitionNames();
    }

    @Override
    protected void cancelRefresh() {
        this.beanFactory.setSerializationId(null);
        super.cancelRefresh();
    }


    @Override
    protected  void closeBeanFactory(){
        this.beanFactory.setSerializationId(null);
    }


    @Override
    public ConfigurableDefinitionBeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        this.beanFactory.registerBeanDefinition(beanName,beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        this.beanFactory.removeBeanDefinition(beanName);
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return this.beanFactory.isBeanNameInUse(beanName);
    }
}