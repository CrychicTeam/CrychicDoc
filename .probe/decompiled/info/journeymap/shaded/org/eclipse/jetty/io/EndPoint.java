package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.Callback;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadPendingException;
import java.nio.channels.WritePendingException;

public interface EndPoint extends Closeable {

    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

    boolean isOpen();

    long getCreatedTimeStamp();

    void shutdownOutput();

    boolean isOutputShutdown();

    boolean isInputShutdown();

    void close();

    int fill(ByteBuffer var1) throws IOException;

    boolean flush(ByteBuffer... var1) throws IOException;

    Object getTransport();

    long getIdleTimeout();

    void setIdleTimeout(long var1);

    void fillInterested(Callback var1) throws ReadPendingException;

    boolean tryFillInterested(Callback var1);

    boolean isFillInterested();

    void write(Callback var1, ByteBuffer... var2) throws WritePendingException;

    Connection getConnection();

    void setConnection(Connection var1);

    void onOpen();

    void onClose();

    boolean isOptimizedForDirectBuffers();

    void upgrade(Connection var1);
}