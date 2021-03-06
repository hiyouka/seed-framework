package seed.seedframework.core.intercept;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface Joinpoint {

    Object getThis();

    Object process() throws Throwable;

}