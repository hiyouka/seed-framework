package hiyouka.seedframework.util;

import hiyouka.seedframework.core.io.resource.Resource;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class PropertiesLoadUtils {

    public static Map loadProperties(Resource resource){
        Map properties = null;
        if(resource.isReadable()){
            try {
                String filename = resource.getFilename();
                if(filename.endsWith(".properties")){
                    properties = new Properties();
                    ((Properties) properties).load(resource.getInputStream());
                }
                if(filename.endsWith(".yml")){
                    properties = new YmlProperties();
                    ((YmlProperties) properties).load(resource.getInputStream());
                }
            } catch (IOException e) {
                throw new IllegalStateException("IO error in load resource : " + resource.getDescription());
            }
        }
        return properties;
    }

}