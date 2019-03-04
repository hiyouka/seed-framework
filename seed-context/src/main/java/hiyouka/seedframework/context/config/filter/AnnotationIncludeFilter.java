package hiyouka.seedframework.context.config.filter;

import hiyouka.seedframework.beans.metadata.AnnotationMetadata;
import hiyouka.seedframework.beans.metadata.StandardAnnotationMetadata;
import hiyouka.seedframework.util.AnnotatedElementUtils;

import java.lang.annotation.Annotation;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotationIncludeFilter implements BeanTypeFilter {

    private Class<? extends Annotation> annotationType;

    public AnnotationIncludeFilter(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    public Class<? extends Annotation> getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }


    @Override
    public boolean match(AnnotationMetadata metadata) {
        if(metadata instanceof StandardAnnotationMetadata){
            return AnnotatedElementUtils.isAnnotated(((StandardAnnotationMetadata) metadata).getIntrospectedClass(),annotationType.getName());
        }
        return false;
    }
}