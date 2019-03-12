package hiyouka.seedframework.util;

import hiyouka.seedframework.common.AnnotationAttributes;
import hiyouka.seedframework.core.annotation.Priority;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotationCoreUtils {


    public static void sortPriority(List<Object> sorts){
        Collections.sort(sorts, (s1, s2) -> {
            AnnotationAttributes aS1 = processNotFoundAnnotation(s1.getClass(),Priority.class);
            AnnotationAttributes aS2 = processNotFoundAnnotation(s2.getClass(),Priority.class);
            int vS1 = aS1.getInteger("value");
            int vS2 = aS2.getInteger("value");
            if (vS1 == vS2){
                return 0;
            }
            else if(vS1 < vS2){
                return -1;
            }
            return 1;
        });
    }

    private static AnnotationAttributes processNotFoundAnnotation(Class<?> clazz, Class<? extends Annotation> annotation){
        AnnotationAttributes attributes = AnnotatedElementUtils.getAnnotationAttributes(clazz, annotation.getName());
        if(attributes == null){
            throw new IllegalStateException("not found "+ annotation.getName() +" annotation from class : " + clazz.getName());
        }
        return attributes;
    }

}