package hiyouka.seedframework.beans.annotation;


import hiyouka.seedframework.beans.factory.config.ConfigurableBeanFactory;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    String value() default ConfigurableBeanFactory.SCOPE_SINGLETON;

}
