package hiyouka.seedframework.context.annotation;

import hiyouka.seedframework.beans.annotation.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    /** beanName */
    String value() default "";

}
