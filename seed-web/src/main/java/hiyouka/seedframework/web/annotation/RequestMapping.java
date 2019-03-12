package hiyouka.seedframework.web.annotation;


import java.lang.annotation.*;
/**
 * 配置web的映射路径
 * @author hiyouka
 * @since JDK 1.8
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String value();
}
