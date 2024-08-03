package info.journeymap.shaded.kotlin.spark.resource;

import info.journeymap.shaded.kotlin.spark.utils.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ExternalResource extends AbstractFileResolvingResource {

    private final File file;

    public ExternalResource(String path) {
        this.file = new File(StringUtils.cleanPath(path));
    }

    public boolean isDirectory() {
        return this.file.isDirectory();
    }

    @Override
    public boolean exists() {
        return this.file.exists();
    }

    @Override
    public String getDescription() {
        return "external resource [" + this.file.getAbsolutePath() + "]";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override
    public URL getURL() throws IOException {
        return this.file.toURI().toURL();
    }

    public String getPath() {
        return this.file.getPath();
    }

    @Override
    public String getFilename() {
        return StringUtils.getFilename(this.getPath());
    }
}