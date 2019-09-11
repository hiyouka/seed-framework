package seed.seedframework.context;

import seed.seedframework.beans.factory.DefinitionBeanFactory;
import seed.seedframework.core.env.EnvironmentCapable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ApplicationContext extends EnvironmentCapable, DefinitionBeanFactory{


    String getId();

    String getApplicationName();

}