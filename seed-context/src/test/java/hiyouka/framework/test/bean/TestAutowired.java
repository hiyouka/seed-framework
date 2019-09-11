package hiyouka.framework.test.bean;

import seed.seedframework.beans.annotation.Autowired;
import seed.seedframework.beans.annotation.Component;
import seed.seedframework.beans.annotation.Specify;
import seed.seedframework.beans.annotation.Value;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Component
public class TestAutowired {

//    @Autowired
//    private TestBean1 testBean1;

    @Value("${spring.aop.auto}")
    private String auto;

    @Value("#{123}")
    private String li;

    @Autowired
    private TestBean1<String,Object> testBean2;


    @Autowired
    @Specify("testBean1")
    private TestFather1 testFatherPrimary;




//    @Autowired
//    private TestFather1<Test1,Test1> testFather1;
//
//    @Autowired
//    private TestFather1<Test2,Test2> testFather2;

}