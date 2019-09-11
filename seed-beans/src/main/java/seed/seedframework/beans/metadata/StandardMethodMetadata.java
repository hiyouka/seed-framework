package seed.seedframework.beans.metadata;

import seed.seedframework.util.AnnotatedElementUtils;
import seed.seedframework.util.Assert;
import seed.seedframework.util.MultiValueMap;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class StandardMethodMetadata implements MethodMetadata {

    private final Method introspectedMethod;

    public StandardMethodMetadata(Method introspectedMethod) {
        Assert.notNull(introspectedMethod, "Method must not be null");
        this.introspectedMethod = introspectedMethod;
    }

    /**
     * Return the underlying Method.
     */
    public final Method getIntrospectedMethod() {
        return this.introspectedMethod;
    }

    @Override
    public String getMethodName() {
        return this.introspectedMethod.getName();
    }

    @Override
    public String getDeclaringClassName() {
        return this.introspectedMethod.getDeclaringClass().getName();
    }

    @Override
    public String getReturnTypeName() {
        return this.introspectedMethod.getReturnType().getName();
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(this.introspectedMethod.getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(this.introspectedMethod.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(this.introspectedMethod.getModifiers());
    }

    @Override
    public boolean isOverridable() {
        return (!isStatic() && !isFinal() && !Modifier.isPrivate(this.introspectedMethod.getModifiers()));
    }

    @Override
    public boolean isAnnotated(String annotationName) {
        return AnnotatedElementUtils.isAnnotated(this.introspectedMethod, annotationName);
    }

    @Override
    @Nullable
    public Map<String, Object> getAnnotationAttributes(String annotationName) {
       return AnnotatedElementUtils.getAnnotationAttributes(this.introspectedMethod, annotationName);
    }


    @Override
    @Nullable
    public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
        return AnnotatedElementUtils.getAttributes(this.introspectedMethod,annotationName);
    }

}
