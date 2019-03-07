package hiyouka.seedframework.core.env;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface Environment {

    /**
     * 获取当前的环境配置
     */
    String[] getActiveProfiles();


    /**
     * 设置环境配置
     */
    boolean acceptsProfiles(String... profiles);

}