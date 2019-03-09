package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.annotation.Bean;
import hiyouka.seedframework.beans.annotation.Import;
import hiyouka.seedframework.beans.annotation.Lazy;
import hiyouka.seedframework.beans.annotation.Primary;
import hiyouka.seedframework.context.annotation.ComponentScan;
import hiyouka.seedframework.context.annotation.Configuration;
import hiyouka.seedframework.context.annotation.PropertySources;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Configuration
@ComponentScan("hiyouka.seedframework.context")
@Import({ImportClass.class,ImportClass2.class})
@PropertySources("classpath:/test.properties")
public class ConfigClass {

    @Bean("innerBeanClass")
    @Lazy
    @Primary
    public BeanClass beanClass(){
        return new BeanClass();
    }

}