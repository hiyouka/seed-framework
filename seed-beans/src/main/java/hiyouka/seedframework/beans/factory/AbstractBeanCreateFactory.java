package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.definition.BeanDefinition;
import hiyouka.seedframework.beans.definition.RootBeanDefinition;
import hiyouka.seedframework.exception.BeansException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AbstractBeanCreateFactory extends AbstractBeanFactory implements BeanCreateFactory{


//    private final Map<String, BeanHolder> factoryBeanInstanceCache = new ConcurrentHashMap<>(16);
//
//    ThreadLocal<String> currentlyCreatedBean = new ThreadLocal<>();

    @Override
    public <T> T createBean(Class<T> beanClass) throws BeansException {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(beanClass);
        return createBean(beanClass.getName(),rootBeanDefinition,null);
    }

    @Override
    protected <T> T createBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Object beanInstance = doCreateBean(beanName, beanDefinition, args);
        return null;
    }

    private Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        // 1. 创建初步的对象。。。

        // 2. 将初步对象放入早期对象缓存

        // 3. 给对象注入 @Autowire 和 @Value 的属性

        // 4. 前置处理方法执行

        // 5. init 方法执行

        // 6 后置处理方法执行

        return null;
    }
}