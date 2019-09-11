package seed.seedframework.util;

import seed.seedframework.constant.EncodeConstant;
import seed.seedframework.exception.FileReadException;
import seed.seedframework.exception.FileSizeOutException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author hiyouka
 * Date: 2019/2/12
 * @since JDK 1.8
 */
public class FileUtils implements EncodeConstant {

    private final static int CACHE_SIZE = 1024;

    private final static int MAX_TO_CACHE_SIZE = 20240;

    private final static String REPLACE_SPACE = "%20";



    public static StringBuffer getFileContent(File file){
        StringBuffer buffer = new StringBuffer();
        if(!file.exists()){
            return buffer;
        }
        long length = file.length();
        if(length > MAX_TO_CACHE_SIZE) {
            throw new FileSizeOutException();
        }
        try {
            BufferedInputStream bufferIn = new BufferedInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(bufferIn, EncodeConstant.UTF_8),CACHE_SIZE);
            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line);
            }
        }catch (IOException e){
            throw new FileReadException(file.getPath() + "read error !!");
        }
        return buffer;
    }


    public static File getFile(String path) throws FileNotFoundException {
        if(path.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)){
            path = path.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length());
            if(path.startsWith(StringUtils.FOLDER_SEPARATOR)){
                path = path.substring(1,path.length());
            }
            String description = "class path resource [" + path + "]";
            ClassLoader cl = ClassUtils.getDefaultClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                throw new FileNotFoundException(description +
                        " cannot be resolved to absolute file path because it does not exist");
            }
            return getFile(url);
        }
        if(path.startsWith(ResourceUtils.FILE_URL_PREFIX) && path.indexOf(REPLACE_SPACE) > 0){
            path = path.substring(ResourceUtils.FILE_URL_PREFIX.length());
        }
        try {
            return getFile(new URL(path));
        }
        catch (MalformedURLException ex) {
            return new File(path);
        }
    }

    public static File getFile(URL resourceUrl) throws FileNotFoundException {
        return getFile(resourceUrl, "URL");
    }

    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        Assert.notNull(resourceUrl, "resource URL must not be null");
        if (!ResourceUtils.URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUrl);
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        }
        catch (URISyntaxException ex) {
            return new File(resourceUrl.getFile());
        }
    }

    public static File getFile(URI resourceUri) throws FileNotFoundException {
        return getFile(resourceUri, "URI");
    }

    public static File getFile(URI resourceUri, String description) throws FileNotFoundException {
        Assert.notNull(resourceUri, "Resource URI must not be null");
        if (!ResourceUtils.URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUri);
        }
        return new File(resourceUri.getSchemeSpecificPart());
    }

    private static URI toURI(URL url) throws URISyntaxException {
        return new URI(url.toString().replace(" ",REPLACE_SPACE));
    }

}