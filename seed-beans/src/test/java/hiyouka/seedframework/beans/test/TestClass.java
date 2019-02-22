package hiyouka.seedframework.beans.test;

import hiyouka.seedframework.beans.metadata.StandardClassMetadata;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author hiyouka
 * Date: 2019/2/21
 * @since JDK 1.8
 */
public class TestClass {

    @Test
    public void test(){
        System.out.println(Tesscls.class.getDeclaringClass());
        System.out.println(Tesscls.Sta.class.getDeclaringClass());
        System.out.println(Tesscls.class.getEnclosingClass());
        System.out.println(Tesscls.Sta.class.getEnclosingClass());
        System.out.println(Tesscls.Sta.class.getEnclosingClass());
        StandardClassMetadata standardClassMetadata = new StandardClassMetadata(Tesscls.class);
        boolean independent = standardClassMetadata.isIndependent();

        System.out.println(Arrays.asList(standardClassMetadata.getMemberClassNames()));
    }

}