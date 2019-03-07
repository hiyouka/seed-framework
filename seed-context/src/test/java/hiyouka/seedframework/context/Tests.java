package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.definition.AnnotatedGenericBeanDefinition;
import hiyouka.seedframework.beans.factory.BeanDefinitionRegistry;
import hiyouka.seedframework.beans.factory.BeanFactory;
import hiyouka.seedframework.beans.factory.DefaultBenFactory;
import hiyouka.seedframework.context.config.ConfigurationClassPostProcessor;
import hiyouka.seedframework.util.BeanUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class Tests {


    @Test
    public void test() {
//
        Method getLess = BeanUtils.findMethod(AnoTest.class, "getLess", null);
        BeanDefinitionRegistry registry = new DefaultBenFactory();
        registry.registerBeanDefinition("config",new AnnotatedGenericBeanDefinition(ConfigClass.class));
        ConfigurationClassPostProcessor postProcessor = new ConfigurationClassPostProcessor();
        postProcessor.postProcessBeanDefinitionRegistry(registry);
        if(registry instanceof BeanFactory){
            AnoTest bean = ((BeanFactory) registry).getBean(AnoTest.class);
        }
//        System.out.println(registry.getBeanDefinitionCount());
//        for(String beanName : registry.getBeanDefinitionNames()){
//            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
//            System.out.println(beanName);
//        }


//        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner();
//        DefaultBenFactory defaultBenFactory = new DefaultBenFactory();
//        scanner.setRegistry(defaultBenFactory);
//        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getAnnotationAttributes(ConfigClass.class, ComponentScan.class);
//        Set<BeanDefinitionHolder> parse = scanner.parse(annotationAttributes, ConfigClass.class.getName());
//        String[] beanDefinitionNames = defaultBenFactory.getBeanDefinitionNames();
//        System.out.println(Arrays.asList(beanDefinitionNames));
//        Class<? extends Annotation> annotationType = AnnotatedElementUtils.getAnnotationType(ConfigClass.class, null);


//        List<Tes> list = new ArrayList<>();
//        list.add( new Tes(1,2));
//        list.add(new Tes(2,3));
//        list.add(new Tes(3,null));
//        list.add(new Tes(4,1));
//        List<Tes> cache = new ArrayList();
//        cache.addAll(list);
//        for(Tes tes : list){
//            tes.setChild(getChild(cache, tes.getId()));
//        }
//        for(Tes tes : cache){
//            if(tes.getPid() != null){
//                list.remove(tes);
//            }
//        }
//        System.out.println(list);
    }

    public List<Tes> getChild(List<Tes> tes, Integer id){
        List<Tes> result = new ArrayList<>();
        for(Tes tes1 : tes){
            if(tes1.getPid()== id){
                result.add(tes1);
            }
        }
        return result;
//        return tes.stream().filter(te -> te.getPid() == id)
//                    .collect(Collectors.toList());
    }


    class Tes{
        private Integer id;

        private Integer pid;

        private List<Tes> child;

        Tes(Integer id,Integer pid){
            this.id = id;
            this.pid = pid;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPid() {
            return pid;
        }

        public void setPid(Integer pid) {
            this.pid = pid;
        }

        public List<Tes> getChild() {
            return child;
        }

        public void setChild(List<Tes> child) {
            this.child = child;
        }
    }

}