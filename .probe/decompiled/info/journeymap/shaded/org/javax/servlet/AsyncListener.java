package info.journeymap.shaded.org.javax.servlet;

import java.io.IOException;
import java.util.EventListener;

public interface AsyncListener extends EventListener {

    void onComplete(AsyncEvent var1) throws IOException;

    void onTimeout(AsyncEvent var1) throws IOException;

    void onError(AsyncEvent var1) throws IOException;

    void onStartAsync(AsyncEvent var1) throws IOException;
}