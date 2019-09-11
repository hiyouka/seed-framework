package seed.seedframework.context.config.filter;

import seed.seedframework.beans.metadata.AnnotationMetadata;
import seed.seedframework.beans.metadata.StandardClassMetadata;
import seed.seedframework.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ClassTypeFilter implements BeanTypeFilter{

    private List<Class<?>> beanClasses = new ArrayList<>();


    public ClassTypeFilter(Class<?> ... classes) {
        if(classes.length != 0)
            this.beanClasses.addAll(ArrayUtils.asList(classes));
    }

    public void addBeanClass(Class<?> clazz) {
        this.beanClasses.add(clazz);
    }

    public void addAll(List<Class<?>> classes){
        this.beanClasses.addAll(classes);
    }

    @Override
    public boolean match(AnnotationMetadata metadata) {
        if(metadata instanceof StandardClassMetadata){
            Class<?> introspectedClass = ((StandardClassMetadata) metadata).getIntrospectedClass();
            if(!beanClasses.isEmpty() && beanClasses.contains(introspectedClass)){
                return true;
            }
        }
        return false;
    }


}