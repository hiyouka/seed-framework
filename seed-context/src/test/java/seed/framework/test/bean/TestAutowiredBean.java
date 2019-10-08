package seed.framework.test.bean;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class TestAutowiredBean {

    private TestFather1<Test1,Test1> testFather;

    private Test1 test1;

    public void setTestFather(TestFather1<Test1, Test1> testFather) {
        this.testFather = testFather;
    }

    public TestFather1<Test1, Test1> getTestFather() {
        return testFather;
    }

    public Test1 getTest1() {
        return test1;
    }

    public void setTest1(Test1 test1) {
        this.test1 = test1;
    }
}