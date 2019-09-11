package seed.seedframework.beans.annotation;

import java.lang.annotation.*;

/**
 * 组件
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    /**
     *  use to be beanName
     */
    String value() default "";

}
