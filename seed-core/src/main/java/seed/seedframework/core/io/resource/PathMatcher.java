package seed.seedframework.core.io.resource;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface PathMatcher {

    /**
     * 判断该字符串是否是匹配公式
     */
    boolean isPattern(String path);

    boolean match(String pattern, String path);

}