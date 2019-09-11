package hiyouka.framework.test.config;

import hiyouka.framework.test.bean.TestBean1;
import seed.seedframework.beans.annotation.Bean;
import seed.seedframework.context.annotation.ComponentScan;
import seed.seedframework.context.annotation.Configuration;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Configuration
@ComponentScan("hiyouka.framework.test")
public class TestConfiguration {

    @Bean("testBeanOfManual")
    public TestBean1<String,Object> stringObjectTestBean1(){
        return new TestBean1<>();
    }

}