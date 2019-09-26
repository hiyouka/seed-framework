package aspect.bean;

import seed.seedframework.beans.annotation.Autowired;
import seed.seedframework.beans.annotation.Component;
import seed.seedframework.beans.annotation.Primary;
import seed.seedframework.core.annotation.Priority;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Component
@Priority(100)
@Primary
public class TestBean1<T,D> extends TestFather1<Test2,Test2>{

    @Autowired
    private TestBean2 testBean2;

}