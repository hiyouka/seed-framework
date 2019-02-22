package hiyouka.seedframework.beans.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    String name() default "";

    String initMethod() default "";

    String destroyMethod() default "";

}