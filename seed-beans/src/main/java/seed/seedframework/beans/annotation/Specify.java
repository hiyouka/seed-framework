package seed.seedframework.beans.annotation;

import seed.seedframework.beans.factory.AutowiredAnnotationPostProcessor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Specify {

    /**
     * specifies the bean name
     * @see Autowired
     * @see AutowiredAnnotationPostProcessor
     */
    String value();

}
