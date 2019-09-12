package seed.seedframework.util;

/**
 * @author hiyouka
 * Date: 2019/1/27
 * @since JDK 1.8
 */
public class ClassUtils {

    public static final char PACKAGE_SEPARATOR = '.';

    public static final char PATH_SEPARATOR = '/';

    /** 目前还没使用cglib */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

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

    public static Class<?> forName(String className) throws ClassNotFoundException {
        Class<?> aClass;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw e;
        }
        return aClass;
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

}