package info.journeymap.shaded.kotlin.spark.resource;

import info.journeymap.shaded.kotlin.spark.utils.Assert;
import info.journeymap.shaded.kotlin.spark.utils.ResourceUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class AbstractResource implements Resource {

    @Override
    public boolean exists() {
        try {
            return this.getFile().exists();
        } catch (IOException var4) {
            try {
                InputStream is = this.getInputStream();
                is.close();
                return true;
            } catch (Throwable var3) {
                return false;
            }
        }
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public URL getURL() throws IOException {
        throw new FileNotFoundException(this.getDescription() + " cannot be resolved to URL");
    }

    @Override
    public URI getURI() throws IOException {
        URL url = this.getURL();
        try {
            return ResourceUtils.toURI(url);
        } catch (URISyntaxException var3) {
            throw new IOException("Invalid URI [" + url + "]", var3);
        }
    }

    @Override
    public File getFile() throws IOException {
        throw new FileNotFoundException(this.getDescription() + " cannot be resolved to absolute file path");
    }

    @Override
    public long contentLength() throws IOException {
        InputStream is = this.getInputStream();
        Assert.state(is != null, "resource input stream must not be null");
        long var6;
        try {
            long size = 0L;
            byte[] buf = new byte[255];
            int read;
            while ((read = is.read(buf)) != -1) {
                size += (long) read;
            }
            var6 = size;
        } finally {
            try {
                is.close();
            } catch (IOException var14) {
            }
        }
        return var6;
    }

    @Override
    public long lastModified() throws IOException {
        long lastModified = this.getFileForLastModifiedCheck().lastModified();
        if (lastModified == 0L) {
            throw new FileNotFoundException(this.getDescription() + " cannot be resolved in the file system for resolving its last-modified timestamp");
        } else {
            return lastModified;
        }
    }

    protected File getFileForLastModifiedCheck() throws IOException {
        return this.getFile();
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        throw new FileNotFoundException("Cannot create a relative resource for " + this.getDescription());
    }

    @Override
    public String getFilename() {
        return null;
    }

    public String toString() {
        return this.getDescription();
    }

    public boolean equals(Object obj) {
        return obj == this || obj instanceof Resource && ((Resource) obj).getDescription().equals(this.getDescription());
    }

    public int hashCode() {
        return this.getDescription().hashCode();
    }
}