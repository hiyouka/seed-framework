package hiyouka.seedframework.core.asm;

import hiyouka.seedframework.core.io.resource.Resource;
import hiyouka.seedframework.util.ClassUtils;
import jdk.internal.org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ClassReaderUtils {


    public static Class<?> getClassFromResource(Resource resource){
        Class<?> aClass;
        String className = null;
        try {
            className = getPackageName(resource);
            aClass = ClassUtils.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("class not found : " + className , e);
        }
        return aClass;
    }

    public static String getPackageName(Resource resource){
        String className;
        try {
            ClassReader classReader = new ClassReader(resource.getInputStream());
            className = classReader.getClassName().replace(ClassUtils.PATH_SEPARATOR,ClassUtils.PACKAGE_SEPARATOR);
        } catch (IOException e) {
            throw new IllegalStateException("read class I/O error !!",e);
        }
        return className;
    }

    public static byte[] readClass(Resource resource, boolean close)
            throws IOException {
        InputStream is = resource.getInputStream();
        if (is == null) {
            throw new IOException("Class not found");
        }
        try {
            byte[] b = new byte[is.available()];
            int len = 0;
            while (true){
                if(len == b.length)
                    return b;
                is.read(b,len,1);
                len ++;
            }
        } finally {
            if(close){
                is.close();
            }
        }
    }

}