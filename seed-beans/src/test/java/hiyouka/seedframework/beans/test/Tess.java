package hiyouka.seedframework.beans.test;

import com.sun.org.glassfish.external.probe.provider.annotations.ProbeProvider;

import java.lang.annotation.*;

/**
 * @author hiyouka
 * Date: 2019/2/22
 * @since JDK 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@ProbeProvider
public @interface Tess {


    String value() default "123";


}