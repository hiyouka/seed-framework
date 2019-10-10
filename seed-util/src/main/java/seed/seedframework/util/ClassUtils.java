package seed.seedframework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author hiyouka
 * Date: 2019/1/27
 * @since JDK 1.8
 */
public class ClassUtils {

    public static final char PACKAGE_SEPARATOR = '.';

    public static final String PACKAGE_SEPARATOR_MULTIPLE = "..";

    public static final char PATH_SEPARATOR = '/';

    /** 目前还没使用cglib */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    public static final String JDK_CLASS_SEPARATOR = "$Proxy";

    /** 内部类 */
    public static final char INNER_CLASS_SEPARATOR = '$';

    public static final String CLASS_SUFFIX = ".class";


    /**
     * get thread ClassLoader Object
     * @param
     * @return:java.lang.ClassLoader default ClassLoader
     * @Date: 2019/1/29
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public static Class<?> forNameSafe(String className){
        boolean include = includeClassOnDefaultClassLoader(className);
        Class<?> result = null;
        if(include){
            try {
                result = forName(className);
            } catch (ClassNotFoundException ignored) {}
        }
        return result;
    }

    public static Class<?> forName(String className) throws ClassNotFoundException {
        Class<?> aClass;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw e;
        }
        return aClass;
    }

    public static boolean includeClassOnDefaultClassLoader(String className){
        return includeClass(className,null);
    }

    public static boolean includeClass(String className, ClassLoader classLoader){
        if(classLoader == null){
            classLoader = ClassUtils.getDefaultClassLoader();
        }
        try {
            forName(className,classLoader);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static Class<?> forName(String className, ClassLoader classLoader) throws ClassNotFoundException {
        return classLoader != null ? classLoader.loadClass(className) : Class.forName(className);
    }

    public static Class<?> getClass(String className){
        Class<?> aClass;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class can not forName : " + className,e);
        }
        return aClass;
    }


    public static String getShortName(String className) {
        Assert.hasText(className, "Class name must not be empty");
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
        return shortName;
    }

    public static String getShortClassName(Class<?> clazz){
        return getShortName(clazz.getName()) + CLASS_SUFFIX;
    }

    public static String convertResourcePathToClassName(String resourcePath) {
        Assert.notNull(resourcePath, "resource path must not be null");
        String className = "";
        if(ResourceUtils.isJarPath(resourcePath)){
            int separator = resourcePath.indexOf("!");
            className = resourcePath.substring(separator + 1, resourcePath.length());

        }else if(ResourceUtils.isFilePath(resourcePath)){
            String classRootPath = getClassRootPath(null);
            className = StringUtils.replace(resourcePath,ResourceUtils.FILE_URL_PREFIX+classRootPath,"");
        }
        return className.replace(PATH_SEPARATOR,PACKAGE_SEPARATOR);
    }

    public static String convertClassNameToResourcePath(String className) {
        Assert.notNull(className, "Class name must not be null");
        return className.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }

    /**
     * 获取class文件所在目录
     */
    public static String getClassRootPath(ClassLoader classLoader){
        classLoader = (classLoader == null ? ClassUtils.class.getClassLoader() : classLoader);
        return classLoader.getResource("").getPath();
    }

    public static boolean isInnerClass(Class<?> clazz){
        Assert.notNull(clazz,"class must not be null !!");
        return clazz.getDeclaringClass() != null;
    }


    public static String getPackageName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return getPackageName(clazz.getName());
    }

    public static String getPackageName(String fqClassName) {
        Assert.notNull(fqClassName, "Class name must not be null");
        int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR);
        return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
    }

    public static boolean isRequiredClass(Object re, Class<?> required){
        Assert.notNull(re,"compare obj must not be null");
        return re.getClass().equals(required);
    }

    public static boolean isCglibProxyClass(Class clazz){
        return clazz != null && isCglibProxyClassName(clazz.getName());
    }

    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains(CGLIB_CLASS_SEPARATOR));
    }


    public static Class[] getAllInterface(Class<?> clazz){
        return getAllInterfacesForClassAsSet(clazz).toArray(new Class[0]);
    }

    public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface()) {
            return Collections.singleton(clazz);
        }
        Set<Class<?>> interfaces = new LinkedHashSet<>();
        Class<?> current = clazz;
        while (current != null) {
            Class<?>[] ifcs = current.getInterfaces();
            interfaces.addAll(Arrays.asList(ifcs));
            current = current.getSuperclass();
        }
        return interfaces;
    }

    public static Constructor[] getConstructorByParameter(Class<?> clazz, Type... parameterType){
        Constructor<?>[] declaredConstructors = getAllConstructorByClass(clazz);
        List<Constructor> result = new LinkedList<>();
        for(Constructor constructor : declaredConstructors){
            Type[] genericParameterTypes = constructor.getGenericParameterTypes();
            boolean match = ArrayUtils.isAllMemberMatch(parameterType, genericParameterTypes);
            if(match){
                result.add(constructor);
            }
        }
        return result.toArray(new Constructor[0]);
    }

    public static Constructor[] getAllConstructorByClass(Class<?> clazz){
        return clazz.getDeclaredConstructors();
    }

    public static boolean hasEmptyArgumentConstructor(Class<?> clazz) {
        Constructor[] constructors = getAllConstructorByClass(clazz);
        for(Constructor constructor : constructors){
            if(constructor.getParameterCount() == 0){
                return true;
            }
        }
        return false;
    }
}