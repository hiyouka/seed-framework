package hiyouka.seedframework.util;

import hiyouka.seedframework.common.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

/**
 * @author hiyouka
 * Date: 2019/2/21
 * @since JDK 1.8
 */
public class AnnotatedElementUtils {


    private static final AlwaysTrueProcessor alwaysTrueProcessor = new AlwaysTrueProcessor();

    /**
     * 判断某个注解是否包含type注解
     */
    public static boolean isAnnotated(AnnotatedElement element, String annotationName) {
        return Boolean.TRUE.equals(searchWithFindSemantics(element,null,annotationName,new HashSet<>(),alwaysTrueProcessor));
    }

    public static Object getAttribute(AnnotatedElement element,Class<? extends Annotation> annotationType, String attributeName){
        return getAttributes(element, annotationType).getFirst(attributeName);
    }

    public static Object getAttribute(AnnotatedElement element,String annotationName, String attributeName){
        return getAttributes(element, getAnnotationType(element,annotationName)).getFirst(attributeName);
    }

    public static MultiValueMap<String, Object> getAttributes(AnnotatedElement element, String annotationName){
        return getAttributes(element,getAnnotationType(element,annotationName));
    }

    public static Class<? extends Annotation> getAnnotationType(AnnotatedElement element, String annotationName){
        Annotation annotation = AnnotationUtils.getAnnotation(element, annotationName);
        return annotation == null ? null : annotation.annotationType();
    }

    public static MultiValueMap<String, Object> getAttributes(AnnotatedElement element,Class<? extends Annotation> annotationType){
        final MultiValueMap<String, Object> attributesMap = new DefaultMultiValueMap<>();
        Assert.notNull(annotationType,"annotation type must not null");
        searchWithFindSemantics(element, annotationType,null, new HashSet<>(), new Processor<Object>() {
            @Override
            public boolean getList() {
                return false;
            }

            @Override
            public boolean matchType() {
                return true;
            }

            @Override
            public Object process(AnnotatedElement annotatedElement, Annotation annotation) {
                Map<String, Object> attributes = AnnotationUtils.getAttributes(annotation);
                for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                    attributesMap.add(entry.getKey(), entry.getValue());
                }
                return null;
            }
        });
        return attributesMap;
    }

    public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement element, String annotationName){
        return searchWithFindSemantics(element, null,annotationName, new HashSet<>(),new AnnotationAttributesProcessor());
    }

    public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType){
        return searchWithFindSemantics(element, annotationType,null, new HashSet<>(),new AnnotationAttributesProcessor());
    }

    public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, String annotationName) {
        return getMetaAnnotationTypes(AnnotationUtils.getAnnotation(element, annotationName));
    }

    private static Set<String> getMetaAnnotationTypes(Annotation composed) {
        if (composed == null) {
            return Collections.emptySet();
        }
        final Set<String> types = new LinkedHashSet<>();
        searchWithFindSemantics(composed.annotationType(), null,null, new HashSet<>(), new Processor<Object>() {
            @Override
            public Object process(AnnotatedElement element, Annotation annotation) {
                types.add(annotation.annotationType().getName());
                return null;
            }

            @Override
            public boolean getList() {
                return false;
            }

            @Override
            public boolean matchType() {
                return false;
            }
        });
        return types;
    }


    private static <T> T searchWithFindSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType,String annotationName,                                                 Set<AnnotatedElement> visited, Processor<T> processor) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        if (visited.add(element)) {
            Annotation[] annotations = element.getDeclaredAnnotations();
            List<T> aggregatedResults = (processor.getList() ? new ArrayList<>() : null);
            for (Annotation annotation : annotations) {
                if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation)) {
                    if (!processor.matchType() || annotation.annotationType() == annotationType
                            || annotation.annotationType().getName().equals(annotationName)) {
                        T result = processor.process(element, annotation);
                        if (result != null) {
                            if (processor.getList()) {
                                aggregatedResults.add(result);
                            }
                            else {
                                return result;
                            }
                            return result;
                        }
                    }
                }
            }
            for (Annotation annotation : annotations) {
                if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation)) {
                    T result = searchWithFindSemantics(annotation.annotationType(), annotationType,annotationName,
                            visited, processor);
                    if(result != null)
                        return result;
                }
            }
        }
        return null;
    }


    private interface Processor<T>{
        T process(AnnotatedElement element, Annotation annotation);

        boolean getList();

        boolean matchType();

    }

    static class AlwaysTrueProcessor implements Processor<Boolean>{
        @Override
        public Boolean process(AnnotatedElement element, Annotation annotation) {
            return Boolean.TRUE;
        }

        @Override
        public boolean getList() {
            return false;
        }

        @Override
        public boolean matchType() {
            return true;
        }
    }

    static class AnnotationAttributesProcessor implements Processor<AnnotationAttributes>{
        @Override
        public AnnotationAttributes process(AnnotatedElement element, Annotation annotation) {
          return new AnnotationAttributes(annotation.annotationType(),AnnotationUtils.getAttributes(annotation));
        }

        @Override
        public boolean getList() {
            return true;
        }

        @Override
        public boolean matchType() {
            return true;
        }
    }


}