package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.factory.DefaultBenFactory;
import hiyouka.seedframework.context.paser.ClassPathBeanDefinitionScanner;
import hiyouka.seedframework.util.Assert;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class AnnotationConfigApplicationContext extends GenericApplicationContext {

    private final ClassPathBeanDefinitionScanner scanner;

    private final AnnotatedBeanDefinitionReader reader;

    public AnnotationConfigApplicationContext() {
        this.scanner = new ClassPathBeanDefinitionScanner(this);
        this.reader = new AnnotatedBeanDefinitionReader(this);
        getEnvironment();
    }

    public AnnotationConfigApplicationContext(DefaultBenFactory beanFactory){
        super(beanFactory);
        this.scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        this.reader = new AnnotatedBeanDefinitionReader(beanFactory);
    }

    public AnnotationConfigApplicationContext(Class<?>... configClass) {
        this();
        register(configClass);
        refresh();
    }

    public void register(Class<?>... annotatedClasses) {
        Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
        this.reader.register(annotatedClasses);
    }


}