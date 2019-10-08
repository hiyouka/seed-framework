package seed.framework.test.bean;

import seed.seedframework.beans.annotation.Autowired;
import seed.seedframework.beans.annotation.Component;
import seed.seedframework.beans.annotation.Specify;
import seed.seedframework.core.annotation.Priority;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Component
@Priority(99)
public class TestBean2 extends TestFather1<Test1, Test1> {

    @Autowired
    @Specify("testBeanOfManual")
    private TestBean1 testBean1;

    @Autowired
    private TestBean2 testBean2;

}