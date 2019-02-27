package hiyouka.seedframework.beans.factory;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface SingletonBeanRegistry {

    /**
     * 缓存单例对象
     */
    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonNames();

    int getSingletonCount();

}