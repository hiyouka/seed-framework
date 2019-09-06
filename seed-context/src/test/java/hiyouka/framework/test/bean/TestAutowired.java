package hiyouka.framework.test.bean;

import hiyouka.seedframework.beans.annotation.Autowired;
import hiyouka.seedframework.beans.annotation.Component;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Component
public class TestAutowired {

//    @Autowired
//    private TestBean1 testBean1;

//    @Autowired
//    private TestBean1<String,Object> testBean2;


    @Autowired
    private TestFather1 testFatherPrimary;

    @Autowired
    private TestFather1<Test1,Test1> testFather1;

    @Autowired
    private TestFather1<Test2,Test2> testFather2;

}