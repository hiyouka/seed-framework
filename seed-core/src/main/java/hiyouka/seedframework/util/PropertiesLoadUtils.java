package hiyouka.seedframework.util;

import hiyouka.seedframework.core.io.resource.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class PropertiesLoadUtils {

    public static Properties loadProperties(Resource resource){
        Properties properties = new Properties();
        if(resource.isReadable()){
            try {
                properties.load(resource.getInputStream());
            } catch (IOException e) {
                throw new IllegalStateException("IO error in load resource : " + resource.getDescription());
            }
        }
        return properties;
    }

}