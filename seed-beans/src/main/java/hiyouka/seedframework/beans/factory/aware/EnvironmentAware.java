package hiyouka.seedframework.beans.factory.aware;

import hiyouka.seedframework.core.env.Environment;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface EnvironmentAware extends Aware{

    void setEnvironment(Environment environment);

}