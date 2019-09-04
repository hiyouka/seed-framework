package hiyouka.seedframework.beans.annotation;

import hiyouka.seedframework.beans.factory.AutowiredAnnotationBeanPostProcessor;

import java.lang.annotation.*;

/**
 * @see AutowiredAnnotationBeanPostProcessor
 * @author hiyouka
 * @since JDK 1.8
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    boolean required() default true;

}