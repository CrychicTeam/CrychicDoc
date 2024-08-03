package info.journeymap.shaded.org.eclipse.jetty.util.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class BadResource extends URLResource {

    private String _message = null;

    BadResource(URL url, String message) {
        super(url, null);
        this._message = message;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public long lastModified() {
        return -1L;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public long length() {
        return -1L;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new FileNotFoundException(this._message);
    }

    @Override
    public boolean delete() throws SecurityException {
        throw new SecurityException(this._message);
    }

    @Override
    public boolean renameTo(Resource dest) throws SecurityException {
        throw new SecurityException(this._message);
    }

    @Override
    public String[] list() {
        return null;
    }

    @Override
    public void copyTo(File destination) throws IOException {
        throw new SecurityException(this._message);
    }

    @Override
    public String toString() {
        return super.toString() + "; BadResource=" + this._message;
    }
}