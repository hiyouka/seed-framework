package hiyouka.framework.test.bean;

import hiyouka.seedframework.beans.annotation.Autowired;
import hiyouka.seedframework.beans.annotation.Bean;
import hiyouka.seedframework.beans.annotation.Component;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Component
public class TestBean1<T,D>{

    @Autowired
    private TestBean1 bean1;

    @Bean
    public TestBean1 testBean1(){
        return new TestBean1();
    }

}