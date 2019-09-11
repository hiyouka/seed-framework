package seed.seedframework.beans.annotation;

import java.lang.annotation.*;


/**
 * 给组件定义销毁方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DestroyMethod {
}
