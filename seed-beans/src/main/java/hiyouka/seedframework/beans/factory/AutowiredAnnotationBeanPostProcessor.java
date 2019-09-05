package hiyouka.seedframework.beans.factory;

import hiyouka.seedframework.beans.annotation.Autowired;
import hiyouka.seedframework.beans.annotation.Value;
import hiyouka.seedframework.beans.definition.RootBeanDefinition;
import hiyouka.seedframework.beans.exception.BeanAutowiredException;
import hiyouka.seedframework.beans.exception.BeanCreatedException;
import hiyouka.seedframework.beans.exception.BeansException;
import hiyouka.seedframework.beans.factory.aware.BeanFactoryAware;
import hiyouka.seedframework.beans.factory.config.ConfigurableDefinitionBeanFactory;
import hiyouka.seedframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import hiyouka.seedframework.beans.factory.config.MergedBeanDefinitionPostProcessor;
import hiyouka.seedframework.beans.metadata.InjectionMetadata;
import hiyouka.seedframework.beans.metadata.PropertyValues;
import hiyouka.seedframework.common.AnnotationAttributes;
import hiyouka.seedframework.exception.SeedCoreException;
import hiyouka.seedframework.util.AnnotatedElementUtils;
import hiyouka.seedframework.util.ReflectionUtils;
import hiyouka.seedframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AutowiredAnnotationBeanPostProcessor implements MergedBeanDefinitionPostProcessor, InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(5);

    private ConfigurableDefinitionBeanFactory beanFactory;

    private final Map<String,InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>();

    public AutowiredAnnotationBeanPostProcessor() {
        autowiredAnnotationTypes.add(Autowired.class);
        autowiredAnnotationTypes.add(Value.class);

        // todo add other annotation type support
    }

    /**
     * 获取bean需要注入的类
     */
    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {

//        Field[] declaredFields = beanType.getDeclaredFields();
//        List<Member> members = new ArrayList<>();
//        for(Field field : declaredFields){
//            Autowired annotation = field.getAnnotation(Autowired.class);
//            if(annotation != null){
//                members.add(field);
//            }
//        }
//        beanDefinition.setMembers(members);
    }



    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        InjectionMetadata autowiredMetadata = findAutowiredMetadata(beanName, bean.getClass(), pvs);
        try {
            autowiredMetadata.inject(bean,beanName,pvs);
        } catch (BeanCreatedException e) {
            throw e;
        } catch (Throwable throwable) {
            throw new BeanAutowiredException("bean field autowired error, beanName : "+ beanName
                    + throwable);
        }
        return null;
    }

    private InjectionMetadata findAutowiredMetadata(String beanName, Class<?> clazz,PropertyValues pvs){
        String cacheKey = StringUtils.hasLength(beanName) ? beanName : clazz.getName();
        InjectionMetadata injectionMetadata = this.injectionMetadataCache.get(cacheKey);
        if(injectionMetadata == null){

        }
//        List<InjectionMetadata.InjectionElement> elements = new ArrayList<>();
//        BeanDefinition beanDefinition = this.beanFactory.getBeanDefinition(beanName);
//        if(beanDefinition instanceof RootBeanDefinition){
//            List<Member> members = ((RootBeanDefinition) beanDefinition).getMembers();
//        }
        return null;
    }

    private InjectionMetadata buildAutowiredMetadata(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            AnnotationAttributes autowiredAnnotationAttributes = findAutowiredAnnotationAttributes(field);
        }
        return null;
    }

    private AnnotationAttributes findAutowiredAnnotationAttributes(AnnotatedElement element){
        if(element.getAnnotations().length > 0){
            for(Class<? extends Annotation> type : autowiredAnnotationTypes){
                AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getAnnotationAttributes(element, type);
                if(annotationAttributes != null){
                    return annotationAttributes;
                }
            }
        }
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if(!(beanFactory instanceof ConfigurableDefinitionBeanFactory)){
            throw new SeedCoreException("not support this beanFactory : " + beanFactory);
        }
        this.beanFactory = (ConfigurableDefinitionBeanFactory) beanFactory;
    }

    private class AutowiredFieldElement extends InjectionMetadata.InjectionElement{

        private final boolean request;

//        private volatile Object cacheFieldValue;

        public AutowiredFieldElement(Field field, boolean request) {
            super(field);
            this.request = request;
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
            Field field = (Field) this.member;
            Object value = beanFactory.resolveBeanForField(beanName, field);
            if(value == null && request){
                throw new BeanAutowiredException("Resolve bean : " + beanName + "error, Because of " +
                        "not found beanName type is : " + field.getGenericType().getTypeName(),
                        beanName,field.getGenericType().getTypeName());
            }
            ReflectionUtils.makeAccessible(field);
            field.set(bean,value);
        }
    }



}