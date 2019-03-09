package hiyouka.seedframework.context.config.aware;

import hiyouka.seedframework.beans.factory.aware.Aware;
import hiyouka.seedframework.context.ApplicationContext;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);

}