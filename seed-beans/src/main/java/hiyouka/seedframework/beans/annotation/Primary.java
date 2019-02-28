package hiyouka.seedframework.beans.annotation;

import java.lang.annotation.*;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Primary {

}