package seed.seedframework.context.config.filter;

import seed.seedframework.beans.metadata.AnnotationMetadata;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@FunctionalInterface
public interface BeanTypeFilter {

    boolean match(AnnotationMetadata metadata);

}