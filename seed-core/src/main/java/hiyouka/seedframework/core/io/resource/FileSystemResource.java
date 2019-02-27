package hiyouka.seedframework.core.io.resource;

import hiyouka.seedframework.util.Assert;
import hiyouka.seedframework.util.StringUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

/**
 * @author hiyouka
 * @since JDK 1.8
 */
public class FileSystemResource extends AbstractResource implements WritableResource{

    private final File file;

    private final String path;

    public FileSystemResource(File file) {
        Assert.notNull(file, "File must not be null");
        this.file = file;
        this.path = StringUtils.cleanPath(file.getPath());
    }

    public FileSystemResource(String path) {
        Assert.notNull(path, "Path must not be null");
        this.file = new File(path);
        this.path = StringUtils.cleanPath(path);
    }

    public final String getPath() {
        return this.path;
    }

    @Override
    public boolean exists() {
        return this.file.exists();
    }

    @Override
    public boolean isReadable() {
        return (this.file.canRead() && !this.file.isDirectory());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return Files.newInputStream(this.file.toPath());
        }
        catch (NoSuchFileException ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }


    @Override
    public boolean isWritable() {
        return (this.file.canWrite() && !this.file.isDirectory());
    }

    /**
     * 用这个文件新开一个流
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        return Files.newOutputStream(this.file.toPath());
    }

    @Override
    public URL getURL() throws IOException {
        return this.file.toURI().toURL();
    }

    @Override
    public URI getURI() throws IOException {
        return this.file.toURI();
    }

    @Override
    public Resource createRelative(String relativePath) {
        String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
        return new FileSystemResource(pathToUse);
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public File getFile() {
        return this.file;
    }


    @Override
    public long contentLength() throws IOException {
        return this.file.length();
    }

    @Override
    public String getFilename() {
        return this.file.getName();
    }

    @Override
    public String getDescription() {
        return "file [" + this.file.getAbsolutePath() + "]";
    }


    @Override
    public boolean equals(Object obj) {
        return (obj == this ||
                (obj instanceof FileSystemResource && this.path.equals(((FileSystemResource) obj).path)));
    }

    /**
     * This implementation returns the hash code of the underlying File reference.
     */
    @Override
    public int hashCode() {
        return this.path.hashCode();
    }

}