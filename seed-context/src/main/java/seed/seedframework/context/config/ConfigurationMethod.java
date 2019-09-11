package seed.seedframework.context.config;

import seed.seedframework.beans.metadata.MethodMetadata;
import seed.seedframework.core.io.resource.Resource;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
abstract class ConfigurationMethod {

    protected final MethodMetadata metadata;

    protected final ConfigurationClass configurationClass;

    public ConfigurationMethod(MethodMetadata metadata, ConfigurationClass configurationClass) {
        this.metadata = metadata;
        this.configurationClass = configurationClass;
    }

    public MethodMetadata getMetadata() {
        return this.metadata;
    }

    public ConfigurationClass getConfigurationClass() {
        return this.configurationClass;
    }

    public Resource getResource() {
        return this.configurationClass.getResource();
    }

    public abstract void validate();



}