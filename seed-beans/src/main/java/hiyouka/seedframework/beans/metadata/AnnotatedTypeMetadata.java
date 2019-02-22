package hiyouka.seedframework.beans.metadata;

import com.sun.istack.internal.Nullable;
import hiyouka.seedframework.util.MultiValueMap;

/**
 * @author hiyouka
 * Date: 2019/2/22
 * @since JDK 1.8
 */
public interface AnnotatedTypeMetadata {


    boolean isAnnotated(String annotationName);


    @Nullable
    MultiValueMap<String, Object> getAnnotationAttributes(String annotationName);



}