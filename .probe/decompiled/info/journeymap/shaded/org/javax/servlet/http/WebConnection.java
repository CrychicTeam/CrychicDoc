package info.journeymap.shaded.org.javax.servlet.http;

import info.journeymap.shaded.org.javax.servlet.ServletInputStream;
import info.journeymap.shaded.org.javax.servlet.ServletOutputStream;
import java.io.IOException;

public interface WebConnection extends AutoCloseable {

    ServletInputStream getInputStream() throws IOException;

    ServletOutputStream getOutputStream() throws IOException;
}