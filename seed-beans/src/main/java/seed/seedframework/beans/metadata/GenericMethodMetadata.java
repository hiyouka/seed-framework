package seed.seedframework.beans.metadata;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class GenericMethodMetadata extends StandardMethodMetadata {

    private final ParameterizedType genericType;

    public GenericMethodMetadata(Method introspectedMethod, ParameterizedType genericType) {
        super(introspectedMethod);
        this.genericType = genericType;
    }

    public ParameterizedType getParameterizedType() {
        return genericType;
    }

}