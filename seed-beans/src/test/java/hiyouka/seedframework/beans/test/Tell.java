package hiyouka.seedframework.beans.test;

import java.lang.annotation.*;

/**
 * @author hiyouka
 * Date: 2019/2/22
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
@Tess(value = "666")
public @interface Tell {

    String value() default "777";

}