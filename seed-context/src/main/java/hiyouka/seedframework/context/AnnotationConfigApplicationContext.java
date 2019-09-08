package hiyouka.seedframework.context;

import hiyouka.seedframework.beans.factory.DefaultBenFactory;
import hiyouka.seedframework.beans.factory.paser.SpelExpressionResolver;
import hiyouka.seedframework.context.paser.ClassPathBeanDefinitionScanner;
import hiyouka.seedframework.core.io.resource.Resource;
import hiyouka.seedframework.util.Assert;

import java.io.IOException;

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
        initProjectEnvironment();
    }

    /**
     *  init project environment form seed.yml and seed.properties
     */
    private void initProjectEnvironment(){
        String[] projectProperties = super.projectProperties;
        for(String projectProject : projectProperties){
            Resource resource = null;
            try {
                resource = this.scanner.getResourcePatternResolver().getResource(projectProject);
                if(resource.exists()){
                    getEnvironment().loadResource(resource);
                }
                register(SpelExpressionResolver.class);
            } catch (IOException e) {
                logger.error("read properties : " + projectProject + " error ");
            }
        }
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