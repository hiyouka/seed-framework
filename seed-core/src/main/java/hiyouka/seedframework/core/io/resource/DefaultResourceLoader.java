package hiyouka.seedframework.core.io.resource;

import hiyouka.seedframework.util.ClassUtils;
import hiyouka.seedframework.util.FileUtils;
import hiyouka.seedframework.util.ResourceUtils;
import hiyouka.seedframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class DefaultResourceLoader extends AbstractResourceLoader{


    protected DefaultResourceLoader(){};

    public DefaultResourceLoader(ClassLoader classLoader){
        this();
        super.setClassLoader(classLoader);
    }

    @Override
    public Resource getResource(String location) throws IOException {
        Resource resource = null;
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        String uPath = location;
        if (uPath.startsWith("/")) {
            uPath = uPath.substring(1);
        }
        uPath = StringUtils.replace(uPath, ResourceUtils.CLASSPATH_URL_PREFIX,"");
        Enumeration<URL> resourceUrls = (cl != null ? cl.getResources(uPath) : ClassLoader.getSystemResources(uPath));
        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            resource = convertClassLoaderURL(url);
        }
        if(resource == null){
            File file = FileUtils.getFile(location);
            if(ResourceUtils.isJarFileURL(file.toURI().toURL())){
                resource = new UrlResource(file.toURI());
            }else{
                resource = new FileSystemResource(file);
            }
        }
        return resource;
    }




}