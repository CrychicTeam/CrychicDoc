package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public interface RemoteEndpoint {

    void sendBytes(ByteBuffer var1) throws IOException;

    Future<Void> sendBytesByFuture(ByteBuffer var1);

    void sendBytes(ByteBuffer var1, WriteCallback var2);

    void sendPartialBytes(ByteBuffer var1, boolean var2) throws IOException;

    void sendPartialString(String var1, boolean var2) throws IOException;

    void sendPing(ByteBuffer var1) throws IOException;

    void sendPong(ByteBuffer var1) throws IOException;

    void sendString(String var1) throws IOException;

    Future<Void> sendStringByFuture(String var1);

    void sendString(String var1, WriteCallback var2);

    BatchMode getBatchMode();

    void setBatchMode(BatchMode var1);

    InetSocketAddress getInetSocketAddress();

    void flush() throws IOException;
}