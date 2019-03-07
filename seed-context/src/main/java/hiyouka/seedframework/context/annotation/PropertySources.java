package hiyouka.seedframework.context.annotation;

import java.lang.annotation.*;

/**
 * 加载配置文件
 * @author hiyouka
 * @since JDK 1.8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertySources {

    String[] value() default {};

}