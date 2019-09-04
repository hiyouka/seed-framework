package hiyouka.framework.test.config;

import hiyouka.framework.test.bean.TestBean1;
import hiyouka.seedframework.beans.annotation.Bean;
import hiyouka.seedframework.context.annotation.ComponentScan;
import hiyouka.seedframework.context.annotation.Configuration;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Configuration
@ComponentScan("hiyouka.framework.test")
public class TestConfiguration {

    @Bean
    public TestBean1<String,Object> stringObjectTestBean1(){
        return new TestBean1<>();
    }

}