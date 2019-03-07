package hiyouka.seedframework.beans.metadata;

import hiyouka.seedframework.util.MultiValueMap;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author hiyouka
 * Date: 2019/2/22
 * @since JDK 1.8
 */
public interface AnnotatedTypeMetadata {


    boolean isAnnotated(String annotationName);


    @Nullable
    Map<String, Object> getAnnotationAttributes(String annotationName);

    @Nullable
    MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName);


}