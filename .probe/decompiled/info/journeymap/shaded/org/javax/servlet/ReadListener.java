package info.journeymap.shaded.org.javax.servlet;

import java.io.IOException;
import java.util.EventListener;

public interface ReadListener extends EventListener {

    void onDataAvailable() throws IOException;

    void onAllDataRead() throws IOException;

    void onError(Throwable var1);
}