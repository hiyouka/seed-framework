package hiyouka.seedframework.common;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hiyouka
 * Date: 2019/2/22
 * @since JDK 1.8
 */
public class AnnotationAttributes extends LinkedHashMap<String,Object>{


    private static final String UNKNOWN = "unknown";

    private final String displayName;

//    private final LinkedHashMap<String,Object> attributes = new LinkedHashMap<>(12);

    private final Class<? extends Annotation> annotationType;

    public AnnotationAttributes(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
        this.displayName = annotationType.getName();
    }

    public AnnotationAttributes(Class<? extends Annotation> annotationType, Map<String, Object> attributes) {
        super(attributes);
        Assert.notNull(annotationType, "'annotationType' must not be null");
        this.annotationType = annotationType;
        this.displayName = annotationType.getName();
    }

    public AnnotationAttributes(Map<String, Object> attributes) {
        super(attributes);
        this.annotationType = null;
        this.displayName = UNKNOWN;
    }

    public Class<? extends Annotation> annotationType() {
        return this.annotationType;
    }

    public boolean getBoolean(String attributeName) {
        return getRequiredAttribute(attributeName, Boolean.class);
    }

    public String getString(String attributeName) {
        return getRequiredAttribute(attributeName, String.class);
    }

    public String[] getStringArray(String attributeName) {
        return getRequiredAttribute(attributeName, String[].class);
    }

    public Object getValue(String attributeName){
        return getRequiredAttribute(attributeName,Object.class);
    }

    private <T> T getRequiredAttribute(String attributeName, Class<T> expectedType) {
        Assert.hasText(attributeName, "'attributeName' must not be null or empty");
        Object value = get(attributeName);
        assertAttributePresence(attributeName, value);
        assertNotException(attributeName, value);
        if (!expectedType.isInstance(value) && expectedType.isArray() &&
                expectedType.getComponentType().isInstance(value)) {
            Object array = Array.newInstance(expectedType.getComponentType(), 1);
            Array.set(array, 0, value);
            value = array;
        }
        assertAttributeType(attributeName, value, expectedType);
        return (T) value;
    }

    private void assertAttributePresence(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            throw new IllegalArgumentException(String.format(
                    "Attribute '%s' not found in attributes for annotation [%s]", attributeName, this.displayName));
        }
    }

    private void assertNotException(String attributeName, Object attributeValue) {
        if (attributeValue instanceof Exception) {
            throw new IllegalArgumentException(String.format(
                    "Attribute '%s' for annotation [%s] was not resolvable due to exception [%s]",
                    attributeName, this.displayName, attributeValue), (Exception) attributeValue);
        }
    }

    private void assertAttributeType(String attributeName, Object attributeValue, Class<?> expectedType) {
        if (!expectedType.isInstance(attributeValue)) {
            throw new IllegalArgumentException(String.format(
                    "Attribute '%s' is of type [%s], but [%s] was expected in attributes for annotation [%s]",
                    attributeName, attributeValue.getClass().getSimpleName(), expectedType.getSimpleName(),
                    this.displayName));
        }
    }

    @Nullable
    public static AnnotationAttributes fromMap(@Nullable Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        if (map instanceof AnnotationAttributes) {
            return (AnnotationAttributes) map;
        }
        return new AnnotationAttributes(map);
    }

}