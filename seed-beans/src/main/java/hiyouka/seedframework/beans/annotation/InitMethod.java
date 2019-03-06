package hiyouka.seedframework.beans.annotation;


import java.lang.annotation.*;

/**
 * 给组件定义初始化方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InitMethod {
}
