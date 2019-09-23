package seed.seedframework.core.annotation;

import seed.seedframework.core.Ordered;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Order {

    int value() default Ordered.LOWER_LEVEL;

}
