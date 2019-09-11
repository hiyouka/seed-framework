package seed.seedframework.beans.metadata;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class GenericMethodMetadata extends StandardMethodMetadata {

    private final Type[] generics;

    public GenericMethodMetadata(Method introspectedMethod, Type[] generics) {
        super(introspectedMethod);
        this.generics = generics;
    }

    public Type[] getGenerics() {
        return generics;
    }

}