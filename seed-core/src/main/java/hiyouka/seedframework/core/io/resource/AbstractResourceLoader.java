package hiyouka.seedframework.core.io.resource;

import hiyouka.seedframework.util.ClassUtils;
import hiyouka.seedframework.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public abstract class AbstractResourceLoader implements ResourceLoader {

    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();


    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Resource getResource(String location) throws IOException {
        File file = FileUtils.getFile(location);
        return new FileSystemResource(file);
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }


    protected Resource convertClassLoaderURL(URL url) {
        return new UrlResource(url);
    }

}