package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.factory.DefinitionBeanFactory;
import hiyouka.seedframework.core.env.EnvironmentCapable;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface ApplicationContext extends EnvironmentCapable, DefinitionBeanFactory{


    String getId();

    String getApplicationName();

}