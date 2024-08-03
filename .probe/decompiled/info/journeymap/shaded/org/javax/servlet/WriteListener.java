package info.journeymap.shaded.org.javax.servlet;

import java.io.IOException;
import java.util.EventListener;

public interface WriteListener extends EventListener {

    void onWritePossible() throws IOException;

    void onError(Throwable var1);
}