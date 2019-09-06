package hiyouka.framework.test.bean;

import hiyouka.seedframework.beans.annotation.Autowired;
import hiyouka.seedframework.beans.annotation.Component;
import hiyouka.seedframework.core.annotation.Priority;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Component
@Priority(99)
public class TestBean2 extends TestFather1<Test1, Test1> {

    @Autowired
    private TestBean1 testBean1;

    @Autowired
    private TestBean2 testBean2;

}