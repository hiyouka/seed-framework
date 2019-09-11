package seed.seedframework.context.config.aware;

import seed.seedframework.beans.factory.aware.Aware;
import seed.seedframework.context.ApplicationContext;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);

}