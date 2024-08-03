package info.journeymap.shaded.org.eclipse.jetty.server;

import java.io.Closeable;
import java.io.IOException;

public interface NetworkConnector extends Connector, Closeable {

    void open() throws IOException;

    void close();

    boolean isOpen();

    String getHost();

    int getPort();

    int getLocalPort();
}