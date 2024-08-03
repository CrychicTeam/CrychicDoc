package me.lucko.spark.lib.bytesocks.ws;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collection;
import javax.net.ssl.SSLSession;
import me.lucko.spark.lib.bytesocks.ws.drafts.Draft;
import me.lucko.spark.lib.bytesocks.ws.enums.Opcode;
import me.lucko.spark.lib.bytesocks.ws.enums.ReadyState;
import me.lucko.spark.lib.bytesocks.ws.framing.Framedata;
import me.lucko.spark.lib.bytesocks.ws.protocols.IProtocol;

public interface WebSocket {

    void close(int var1, String var2);

    void close(int var1);

    void close();

    void closeConnection(int var1, String var2);

    void send(String var1);

    void send(ByteBuffer var1);

    void send(byte[] var1);

    void sendFrame(Framedata var1);

    void sendFrame(Collection<Framedata> var1);

    void sendPing();

    void sendFragmentedFrame(Opcode var1, ByteBuffer var2, boolean var3);

    boolean hasBufferedData();

    InetSocketAddress getRemoteSocketAddress();

    InetSocketAddress getLocalSocketAddress();

    boolean isOpen();

    boolean isClosing();

    boolean isFlushAndClose();

    boolean isClosed();

    Draft getDraft();

    ReadyState getReadyState();

    String getResourceDescriptor();

    <T> void setAttachment(T var1);

    <T> T getAttachment();

    boolean hasSSLSupport();

    SSLSession getSSLSession() throws IllegalArgumentException;

    IProtocol getProtocol();
}