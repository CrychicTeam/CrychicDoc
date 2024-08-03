package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.MetaData;
import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import java.nio.ByteBuffer;

public interface HttpTransport {

    void send(MetaData.Response var1, boolean var2, ByteBuffer var3, boolean var4, Callback var5);

    boolean isPushSupported();

    void push(MetaData.Request var1);

    void onCompleted();

    void abort(Throwable var1);

    boolean isOptimizedForDirectBuffers();
}