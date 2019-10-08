package seed.seedframework.web.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class Test {

    public static void main(String[] args) {
        Map<String,Object> pMap = new HashMap();
        pMap.put("method","post");
        pMap.put("action","/url");
//        XSLTProcess.Param param = new XSLTProcess.Param("json",pMap);
//        String type = param.getType();
    }

}