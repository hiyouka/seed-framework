package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.annotation.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
@Component
@Lazy
@Primary
public class AnoTest {

    Log logger = LogFactory.getLog(this.getClass());


    @InitMethod
    public void init(){
        logger.info(getClass().getName() + " init method invoke ....");
    }

    @DestroyMethod
    public void destroy(){
        logger.info(getClass().getName() + " init method destroy ....");
    }

    public void getLess(){

    }
}