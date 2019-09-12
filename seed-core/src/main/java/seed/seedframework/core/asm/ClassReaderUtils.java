package seed.seedframework.core.asm;

import org.objectweb.asm.*;
import seed.seedframework.core.io.resource.ClassResource;
import seed.seedframework.core.io.resource.FileSystemResource;
import seed.seedframework.core.io.resource.Resource;
import seed.seedframework.core.io.resource.UrlResource;
import seed.seedframework.exception.FileReadException;
import seed.seedframework.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ClassReaderUtils {


    public static Class<?> getClassFromResource(Resource resource){
        Class<?> aClass = null;
        String className = null;
        try {
            if(resource instanceof ClassResource){
                aClass = ((ClassResource) resource).getClazz();
            }
            else if(resource instanceof FileSystemResource
                    || resource instanceof UrlResource){
                className = getPackageName(resource);
                aClass = ClassUtils.forName(className);
            }
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

    public static String[] getParameterNamesByAsm5(Class<?> clazz,
                                                   final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            return new String[0];
        }
        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final String[] parameterNames = new String[parameterTypes.length];
        try(InputStream is = clazz.getResourceAsStream(ClassUtils.getShortClassName(clazz))){
            ClassReader classReader = new ClassReader(is);
            classReader.accept(new ClassVisitor(Opcodes.ASM5) {
                @Override
                public MethodVisitor visitMethod(int access, String name,
                                                 String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name)
                            || !Arrays.equals(argumentTypes, types)) {
                        return super.visitMethod(access, name, desc, signature,
                                exceptions);
                    }
                    return new MethodVisitor(Opcodes.ASM5) {
                        @Override
                        public void visitLocalVariable(String name, String desc,
                                                       String signature, org.objectweb.asm.Label start,
                                                       org.objectweb.asm.Label end, int index) {
                            // 非静态成员方法的第一个参数是this
                            if (Modifier.isStatic(method.getModifiers()) && index <= parameterNames.length) {
                                parameterNames[index] = name;
                            } else if (index > 0 && index <= parameterNames.length) {
                                parameterNames[index - 1] = name;
                            }
                        }
                    };
                }
            }, 0);
        } catch (IOException e) {
            throw new FileReadException("getParameterNamesByAsm5 read field error");
        }
        return parameterNames;
    }

}