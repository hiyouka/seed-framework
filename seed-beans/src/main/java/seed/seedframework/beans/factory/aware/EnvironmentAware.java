package seed.seedframework.beans.factory.aware;

import seed.seedframework.core.env.Environment;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public interface EnvironmentAware extends Aware{

    void setEnvironment(Environment environment);

}