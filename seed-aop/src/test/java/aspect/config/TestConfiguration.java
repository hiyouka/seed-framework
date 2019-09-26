package aspect.config;

import aspect.bean.Test1;
import aspect.bean.TestAutowiredBean;
import aspect.bean.TestBean1;
import aspect.bean.TestFather1;
import seed.seedframework.beans.annotation.Autowired;
import seed.seedframework.beans.annotation.Bean;
import seed.seedframework.beans.annotation.Specify;
import seed.seedframework.context.annotation.ComponentScan;
import seed.seedframework.context.annotation.Configuration;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Configuration
@ComponentScan({"aspect.bean","aspect.aop"})
public class TestConfiguration {

    @Bean("testBeanOfManual")
    public TestBean1<String,Object> stringObjectTestBean1(){
        return new TestBean1<>();
    }

    @Bean("testAutowiredBean")
    public TestAutowiredBean testAutowiredBean(@Specify("testBean2") TestFather1 testFather1, @Autowired(required = false) Test1 test1){
        TestAutowiredBean testAutowiredBean = new TestAutowiredBean();
        testAutowiredBean.setTestFather(testFather1);
        testAutowiredBean.setTest1(test1);

        return  testAutowiredBean;
    }



}