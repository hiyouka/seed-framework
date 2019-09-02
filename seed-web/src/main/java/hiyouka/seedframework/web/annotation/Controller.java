package hiyouka.seedframework.web.annotation;


import hiyouka.seedframework.beans.annotation.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {

    String value() default "";

}
