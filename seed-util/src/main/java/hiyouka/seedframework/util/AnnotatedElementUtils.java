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

    private static final AnnotationAttributesProcessor annotationAttributesProcessor = new AnnotationAttributesProcessor();

    private static final AlwaysTrueProcessor alwaysTrueProcessor = new AlwaysTrueProcessor();

    /**
     * 判断某个注解是否包含type注解
     */
    public static boolean isAnnotated(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        return Boolean.TRUE.equals(searchWithFindSemantics(element,annotationType,new HashSet<>(),alwaysTrueProcessor));
    }

    public static Object getAttribute(AnnotatedElement element,Class<? extends Annotation> annotationType, String attributeName){
        return getAttributes(element, annotationType).getFirst(attributeName);

    }


    public static MultiValueMap<String, Object> getAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType){
        final MultiValueMap<String, Object> attributesMap = new DefaultMultiValueMap<>();
        Assert.notNull(annotationType,"annotation type must not null");
        searchWithFindSemantics(element, annotationType, new HashSet<>(), new Processor<Object>() {
            @Override
            public boolean getList() {
                return false;
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

    public static Set<AnnotationAttributes> getAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType){
        Set<AnnotationAttributes> result = new LinkedHashSet<>();
        searchWithFindSemantics(element, annotationType, new HashSet<>(), new Processor<Object>() {
            @Override
            public boolean getList() {
                return false;
            }
            @Override
            public Object process(AnnotatedElement annotatedElement, Annotation annotation) {
                result.add(new AnnotationAttributes(annotation.annotationType(),AnnotationUtils.getAttributes(annotation)));
                return null;
            }
        });
        return result;
    }



    private static <T> T searchWithFindSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType,
                                                 Set<AnnotatedElement> visited, Processor<T> processor) {
        Assert.notNull(element, "AnnotatedElement must not be null");
        if (visited.add(element)) {
            Annotation[] annotations = element.getDeclaredAnnotations();
            List<T> aggregatedResults = (processor.getList() ? new ArrayList<>() : null);
            for (Annotation annotation : annotations) {
                if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation)) {
                    if (annotation.annotationType() == annotationType) {
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
//                        // Repeatable annotations in container?
//                        else if (annotation.annotationType() == containerType) {
//                            for (Annotation contained : getRawAnnotationsFromContainer(element, annotation)) {
//                                T result = processor.process(element, contained, metaDepth);
//                                if (result != null) {
//                                    // No need to post-process since repeatable annotations within a
//                                    // container cannot be composed annotations.
//                                    aggregatedResults.add(result);
//                                }
//                            }
//                        }
                }
            }
            for (Annotation annotation : annotations) {
                if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation)) {
                    T result = searchWithFindSemantics(annotation.annotationType(), annotationType,
                            visited, processor);
                    if(result != null)
                        return result;
                }
            }
        }
        return null;
    }



//    public static boolean isAnnotated(AnnotatedElement element, String annotationName) {
//        return Boolean.TRUE.equals(searchWithGetSemantics(element, null, annotationName, alwaysTrueAnnotationProcessor));
//    }
//
//    public static <T> T searchWithGetSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType,
//                                                            String annotationName,Set<AnnotatedElement> visited){
//        if(visited.add(element)){
//            List<Annotation> declaredAnnotations = Arrays.asList(element.getDeclaredAnnotations());
//            if(element instanceof Class){
//
//            }
//        }
//    }
//
//
//    private static  <T> T  TsearchWithGetSemanticsInAnnotations (AnnotatedElement element,List<Annotation> annotations,
//                         Class<? extends Annotation> annotationType,String annotationName,Set<AnnotatedElement> visited){
//        // Search in annotations
//        for (Annotation annotation : annotations) {
//            Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
//            if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
//                if (currentAnnotationType == annotationType ||
//                        currentAnnotationType.getName().equals(annotationName) ||
//                        processor.alwaysProcesses()) {
//                    T result = processor.process(element, annotation, metaDepth);
//                    if (result != null) {
//                        if (processor.aggregates() && metaDepth == 0) {
//                            processor.getAggregatedResults().add(result);
//                        }
//                        else {
//                            return result;
//                        }
//                    }
//                }
//                // Repeatable annotations in container?
//                else if (currentAnnotationType == containerType) {
//                    for (Annotation contained : getRawAnnotationsFromContainer(element, annotation)) {
//                        T result = processor.process(element, contained, metaDepth);
//                        if (result != null) {
//                            // No need to post-process since repeatable annotations within a
//                            // container cannot be composed annotations.
//                            processor.getAggregatedResults().add(result);
//                        }
//                    }
//                }
//            }
//        }
//    }



    private interface Processor<T>{
        T process(AnnotatedElement element, Annotation annotation);

        boolean getList();


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
    }




}