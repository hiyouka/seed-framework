package hiyouka.seedfremework;

import hiyouka.seedframework.core.asm.ClassReaderUtils;
import hiyouka.seedframework.core.io.resource.PathMatchingResourcePatternResolver;
import hiyouka.seedframework.core.io.resource.Resource;
import hiyouka.seedframework.util.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class Tests {

    @Test
    public void test() throws IOException, ClassNotFoundException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();;
        Resource[] res = resolver.getResources("classpath*:com/synapse/common/sso/**/*.class");
        List<Resource> resources = ArrayUtils.asList(res);
        Resource[] resources1 = resolver.getResources("classpath:hiyouka/seedframework/common/**/*.class");
        List<Resource> resources2 = ArrayUtils.asList(resources1);
        List<Resource> list = new ArrayList<>();
        list.add(resources.get(0));
        list.addAll(resources2);
//        Class<?> aClass = defaultClassLoader.loadClass("");
        for(Resource resource : list){
            if(resource.isReadable()){
                ClassReader classReader = new ClassReader(ClassReaderUtils.readClass(resource,true));
//                ClassVisitor classVisitor = new ClassVisitor(1) {
//                    @Override
//                    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//                        super.visit(version, access, name, signature, superName, interfaces);
//                    }
//                };
//                ClassVisitor classVisitor = new ClassWriter(classReader,1);
//                AnnotationVisitor annotationVisitor = classVisitor.visitAnnotation("", true);
//                classReader.accept();
                String className = classReader.getClassName();
                System.out.println(className);
//                System.out.println(ClassUtils.convertResourcePathToClassName(path));
            }
        }
//        System.out.println(ClassUtils.getClassRootPath(null));

        byte[] bytes = new byte[1];
        bytes[0] = 9;
//        System.out.println(new String(bytes,"UTF-8"));
    }

}