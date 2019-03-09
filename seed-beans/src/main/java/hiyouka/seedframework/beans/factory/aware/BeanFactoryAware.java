package hiyouka.seedframework.beans.factory.aware;

import hiyouka.seedframework.beans.factory.BeanFactory;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);

}