package hiyouka.seedframework.beans.factory.config;

/**
 * 用于bean创建完成之后的初始化工作
 * @author hiyouka
 * @since JDK 1.8
 */
public interface Initialization {

    void afterPropertiesSet() throws Exception;

}