package hiyouka.seedframework.beans.exception;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanAutowiredException extends BeanCreatedException {

    private String TypeName;

    public BeanAutowiredException(String message) {
        super(message);
    }

    public BeanAutowiredException(String message, String beanName) {
        super(message, beanName);
    }

    public BeanAutowiredException(String message, String beanName, String typeName) {
        super(message, beanName);
        this.TypeName = typeName;
    }

    public String getTypeName() {
        return TypeName;
    }
}