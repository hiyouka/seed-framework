package hiyouka.seedframework.core.env;

import hiyouka.seedframework.core.prop.PropertySource;
import hiyouka.seedframework.core.prop.PropertySources;
import hiyouka.seedframework.core.prop.StandardPropertySources;
import hiyouka.seedframework.util.ClassUtils;
import hiyouka.seedframework.util.PropertiesUtils;
import hiyouka.seedframework.util.StringUtils;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractEnvironment implements ConfigurableEnvironment {



    /** 启动环境配置信息 */
    public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";

    protected final StandardPropertySources propertySources = new StandardPropertySources();

    protected final Set<String> activeProfiles = new LinkedHashSet<>();

    public AbstractEnvironment(){
        String property = getProperty(ACTIVE_PROFILES_PROPERTY_NAME);
        if(StringUtils.hasText(property)){
            acceptsProfiles(property.split(","));
        }
    }

    @Override
    public Map<String, Object> getSystemEnvironment() {
        return (Map)System.getenv();
    }

    @Override
    public Map<String, Object> getSystemProperties() {
        Properties properties = System.getProperties();
        return PropertiesUtils.propertiesForHashMap(properties);
    }

    @Override
    public PropertySources getPropertySources() {
        return this.propertySources;
    }



    protected void addPropertySource(PropertySource propertySource){
        this.propertySources.addProperty(propertySource);
    }

    @Override
    public String[] getActiveProfiles() {
        return activeProfiles.toArray(new String[activeProfiles.size()]);
    }

    @Override
    public boolean acceptsProfiles(String... profiles) {
        return activeProfiles.addAll(Arrays.asList(profiles));
    }

    public boolean isAccecptProfile(String profile){
        return activeProfiles.contains(profile);
    }

    @Override
    public boolean containsProperty(String key) {
        for(PropertySource propertySource : this.propertySources){
            boolean result = propertySource.containsProperty(key);
            if(result){
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public String getProperty(String key) {
        return getProperty(key,String.class);
    }

    @Nullable
    @Override
    public <T> T getProperty(String key, Class<T> requireType) {
        T val = null;
        for(PropertySource propertySource : this.propertySources){
            Object property = propertySource.getProperty(key);
            if(property != null){
                if(ClassUtils.isRequiredClass(property,requireType)){
                    val = (T) property;
                }
            }
        }
        return val;
    }
}