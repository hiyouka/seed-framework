package hiyouka.seedframework.beans.metadata;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface MethodMetadata extends AnnotatedTypeMetadata{

    /**
     * get the method name
     * @return method name
     */
    String getMethodName();

    /**
     * @param
     * @return java.lang.String
     */
    String getDeclaringClassName();

    String getReturnTypeName();

    boolean isAbstract();

    boolean isStatic();

    boolean isFinal();

    boolean isOverridable();

}