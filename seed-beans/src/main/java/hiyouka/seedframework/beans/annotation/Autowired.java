package hiyouka.seedframework.beans.annotation;

import java.lang.annotation.*;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    boolean required() default true;

    String value() default "";

}