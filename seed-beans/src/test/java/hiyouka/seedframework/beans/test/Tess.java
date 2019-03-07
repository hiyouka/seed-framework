package hiyouka.seedframework.beans.test;


import java.lang.annotation.*;

/**
 * @author hiyouka
 * Date: 2019/2/22
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Tess {


    String value() default "123";


}