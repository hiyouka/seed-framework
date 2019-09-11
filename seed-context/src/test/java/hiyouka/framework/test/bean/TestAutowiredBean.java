package hiyouka.framework.test.bean;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class TestAutowiredBean {

    private TestFather1<Test1,Test1> testFather;

    public void setTestFather(TestFather1<Test1, Test1> testFather) {
        this.testFather = testFather;
    }

    public TestFather1<Test1, Test1> getTestFather() {
        return testFather;
    }
}