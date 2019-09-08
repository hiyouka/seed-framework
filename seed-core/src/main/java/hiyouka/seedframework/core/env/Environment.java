package hiyouka.seedframework.core.env;

import hiyouka.seedframework.core.prop.PropertyResolver;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface Environment extends PropertyResolver {

    /**
     * 获取当前的环境配置
     */
    String[] getActiveProfiles();

    /**
     * 设置环境配置
     */
    boolean acceptsProfiles(String... profiles);

}