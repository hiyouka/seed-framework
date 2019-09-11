package seed.seedframework.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class PropertiesUtils {


    public static Map<String,Object> propertiesForHashMap(Properties properties){
        Map<String,Object> result = new HashMap<>();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()){
            String key = (String) enumeration.nextElement();
            result.put(key,properties.getProperty(key));
        }
        return result;
    }




}