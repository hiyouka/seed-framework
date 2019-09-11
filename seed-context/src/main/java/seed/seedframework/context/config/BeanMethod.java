package seed.seedframework.context.config;

import seed.seedframework.beans.metadata.MethodMetadata;
import seed.seedframework.context.annotation.Configuration;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class BeanMethod extends ConfigurationMethod {

    public BeanMethod(MethodMetadata metadata, ConfigurationClass configurationClass) {
        super(metadata, configurationClass);
    }


    @Override
    public void validate() {
        if (getMetadata().isStatic()) {
            // static @Bean methods have no constraints to validate -> return immediately
            return;
        }

        if (this.configurationClass.getMetadata().isAnnotated(Configuration.class.getName())) {
            if (!getMetadata().isOverridable()) {
                throw new IllegalStateException("@Bean method must not be private or final ");
            }
        }
    }

}