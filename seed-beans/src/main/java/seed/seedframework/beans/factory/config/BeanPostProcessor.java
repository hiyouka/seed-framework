package seed.seedframework.beans.factory.config;

import seed.seedframework.beans.exception.BeansException;

/**
 * hiyouka.framework.test.bean 后置处理器
 * @author hiyouka
 * @since JDK 1.8
 */
public interface BeanPostProcessor {

    /**
     * post hiyouka.framework.test.bean after hiyouka.framework.test.bean create and before hiyouka.framework.test.bean init 前置处理
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     *  post hiyouka.framework.test.bean after hiyouka.framework.test.bean init 后置处理
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}