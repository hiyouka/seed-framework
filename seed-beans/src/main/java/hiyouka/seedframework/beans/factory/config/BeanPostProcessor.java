package hiyouka.seedframework.beans.factory.config;

import hiyouka.seedframework.beans.exception.BeansException;

/**
 * bean 后置处理器
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanPostProcessor {

    /**
     * post bean after bean create and before bean init 前置处理
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     *  post bean after bean init 后置处理
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}