package hiyouka.seedframework.util;

import hiyouka.seedframework.beans.metadata.AnnotatedTypeMetadata;
import hiyouka.seedframework.common.AnnotationAttributes;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotationConfigUtils {

    public static AnnotationAttributes getAnnotationAttributes(AnnotatedTypeMetadata metadata, String annotationName){
        return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationName));
    }

}