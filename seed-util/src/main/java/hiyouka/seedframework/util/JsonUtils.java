package hiyouka.seedframework.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class JsonUtils {

    public static Gson gson = new Gson();

    public static <T> T toObject(String json, Class<T> classOfT){
        return gson.fromJson(json, classOfT);
    }

    public static <T> T toObjectByType(String json, Class<T> typeClass){
        Type type = new TypeToken<T>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static String toJson(Object o){
        Type type = new TypeToken<Object>(){}.getType();
        String json = gson.toJson(o, type);
        return json;
    }


}