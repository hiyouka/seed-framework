package seed.seedframework.context.annotation;

import seed.seedframework.beans.annotation.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    /** beanName */
    String value() default "";

}
