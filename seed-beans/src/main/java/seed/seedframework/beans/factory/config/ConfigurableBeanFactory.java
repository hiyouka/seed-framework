package seed.seedframework.beans.factory.config;

import seed.seedframework.beans.factory.BeanFactory;

import javax.annotation.Nullable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ConfigurableBeanFactory extends BeanFactory {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    @Nullable
    ClassLoader getBeanClassLoader();

    void setBeanClassLoader(@Nullable ClassLoader beanClassLoader);

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * return number of beanPostProcess {@link BeanPostProcessor}
     */
    int getBeanPostProcessorCount();

}