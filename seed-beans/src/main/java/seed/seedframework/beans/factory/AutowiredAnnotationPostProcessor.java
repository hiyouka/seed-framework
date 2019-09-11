package seed.seedframework.beans.factory;

import seed.seedframework.beans.annotation.Autowired;
import seed.seedframework.beans.annotation.Value;
import seed.seedframework.beans.definition.BeanDefinition;
import seed.seedframework.beans.exception.BeanAutowiredException;
import seed.seedframework.beans.exception.BeanCreatedException;
import seed.seedframework.beans.exception.BeansException;
import seed.seedframework.beans.factory.aware.BeanFactoryAware;
import seed.seedframework.beans.factory.config.ConfigurableDefinitionBeanFactory;
import seed.seedframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import seed.seedframework.beans.factory.config.MergedBeanDefinitionPostProcessor;
import seed.seedframework.beans.metadata.InjectionMetadata;
import seed.seedframework.beans.metadata.PropertyValues;
import seed.seedframework.common.AnnotationAttributes;
import seed.seedframework.exception.SeedCoreException;
import seed.seedframework.util.AnnotatedElementUtils;
import seed.seedframework.util.ReflectionUtils;
import seed.seedframework.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AutowiredAnnotationPostProcessor implements MergedBeanDefinitionPostProcessor, InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private static final Log logger = LogFactory.getLog(AutowiredAnnotationPostProcessor.class);

    Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(5);

    private static final String REQUIRED_KEY = "required";

    private ConfigurableDefinitionBeanFactory beanFactory;

    private final Map<String,InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>();

    public AutowiredAnnotationPostProcessor() {
        autowiredAnnotationTypes.add(Autowired.class);
        autowiredAnnotationTypes.add(Value.class);

        // todo add other annotation type support
    }

    /**
     * 获取bean需要注入的类
     */
    @Override
    public void postProcessMergedBeanDefinition(BeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        findAutowiredMetadata(beanName,beanType,null);
    }



    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        InjectionMetadata autowiredMetadata = findAutowiredMetadata(beanName, bean.getClass(), pvs);
        try {
            autowiredMetadata.inject(bean,beanName,pvs);
        } catch (BeanCreatedException e) {
            throw e;
        } catch (Throwable throwable) {
            throw new BeanAutowiredException("bean field autowired error, beanName : "+ beanName + " ,"
                    + throwable);
        }
        return pvs;
    }

    private InjectionMetadata findAutowiredMetadata(String beanName, Class<?> clazz,PropertyValues pvs){
        String cacheKey = StringUtils.hasLength(beanName) ? beanName : clazz.getName();
        InjectionMetadata injectionMetadata = this.injectionMetadataCache.get(cacheKey);
        if(injectionMetadata == null){
            injectionMetadata = buildAutowiredMetadata(clazz);
            this.injectionMetadataCache.put(cacheKey,injectionMetadata);
        }
        return injectionMetadata;
    }

    private InjectionMetadata buildAutowiredMetadata(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        List<InjectionMetadata.InjectionElement> elements = new ArrayList<>();
        for(Field field : fields){
            AnnotationAttributes annotationAttributes = findAutowiredAnnotationAttributes(field);
            if(annotationAttributes != null){
                if(Modifier.isStatic(field.getModifiers())){
                    if(logger.isDebugEnabled()){
                        logger.debug("not support static field autowired : " + field);
                    }
                    continue;
                }
                boolean required = determineRequired(annotationAttributes);
                elements.add(new AutowiredFieldElement(field,required));
            }
        }
        return new InjectionMetadata(clazz,elements);
    }

    private boolean determineRequired(AnnotationAttributes attributes){
        return (!attributes.containsKey(REQUIRED_KEY) || attributes.getBoolean(REQUIRED_KEY));
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
            Object value = beanFactory.resolveDepend(new DependencyDescriptor(field,this.request),beanName);
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