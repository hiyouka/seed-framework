package hiyouka.seedframework.context.config.filter;

import hiyouka.seedframework.beans.metadata.AnnotationMetadata;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@FunctionalInterface
public interface BeanTypeFilter {

    boolean match(AnnotationMetadata metadata);

}