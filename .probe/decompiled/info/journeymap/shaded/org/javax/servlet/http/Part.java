package info.journeymap.shaded.org.javax.servlet.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface Part {

    InputStream getInputStream() throws IOException;

    String getContentType();

    String getName();

    String getSubmittedFileName();

    long getSize();

    void write(String var1) throws IOException;

    void delete() throws IOException;

    String getHeader(String var1);

    Collection<String> getHeaders(String var1);

    Collection<String> getHeaderNames();
}