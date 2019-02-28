package hiyouka.seedframework.beans.test;

import com.sun.org.glassfish.external.probe.provider.annotations.ProbeProvider;
import hiyouka.seedframework.beans.annotation.Component;

/**
 * @author hiyouka
 * Date: 2019/2/21
 * @since JDK 1.8
 */
@ProbeProvider
@Tell
@Tess
@Component("777")
public class AnoTest extends SupClass{

    @Tell
    public void test(){

    }


}