package hiyouka.seedframework.core.annotation;

import java.lang.annotation.*;

/**
 * 优先级排序 数字越大优先级越高
 * @author hiyouka
 * @since JDK 1.8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Priority {

    int value();

}