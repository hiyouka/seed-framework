package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.exception.BeanCreatedException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanCreateFactory extends BeanFactory {

    <T> T createBean(Class<T> beanClass) throws BeanCreatedException;

}