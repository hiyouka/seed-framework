package hiyouka.seedframework.util;

import hiyouka.seedframework.constant.EncodeConstant;
import hiyouka.seedframework.exception.FileReadException;
import hiyouka.seedframework.exception.FileSizeOutException;

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

    private final static String CLASS_PATH = "classpath:";

    private final static String URL_PROTOCOL_FILE = "file";

    private final static String URL_PREFIX_FILE = "file:";

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
        if(path.startsWith(CLASS_PATH)){
            path = path.substring(CLASS_PATH.length());
            String description = "class path resource [" + path + "]";
            ClassLoader cl = ClassUtils.getDefaultClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                throw new FileNotFoundException(description +
                        " cannot be resolved to absolute file path because it does not exist");
            }
            return getFile(url);
        }
        if(path.startsWith(URL_PREFIX_FILE) && path.indexOf(REPLACE_SPACE) > 0){
            path = path.substring(URL_PREFIX_FILE.length());
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
        Assert.notNull(resourceUrl, "Resource URL must not be null");
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
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

    private static URI toURI(URL url) throws URISyntaxException {
        return new URI(url.toString().replace(" ",REPLACE_SPACE));
    }

}