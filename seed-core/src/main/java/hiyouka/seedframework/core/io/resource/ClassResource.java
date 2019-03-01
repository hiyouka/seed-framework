package hiyouka.seedframework.core.io.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class ClassResource extends AbstractResource {

    private final String beanName;

    private final Class<?> clazz;

    public ClassResource(String className){
        this.beanName = className;
        this.clazz = null;
    }


    public ClassResource(Class<?> clazz){
        this.clazz = clazz;
        this.beanName = clazz.getName();
    }

    public ClassResource(String className, Class<?> clazz){
        this.clazz = clazz;
        this.beanName = className;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        throw new FileNotFoundException(
                getDescription() + " cannot be opened because it does not point to a readable resource");
    }


    public String getBeanName() {
        return beanName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this ||
                (obj instanceof ClassResource && ((ClassResource) obj).clazz.equals(this.clazz)
                                && ((ClassResource) obj).beanName.equals(this.beanName)));
    }



}