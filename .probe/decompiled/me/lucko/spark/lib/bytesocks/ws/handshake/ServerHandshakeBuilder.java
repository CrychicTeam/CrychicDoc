package me.lucko.spark.lib.bytesocks.ws.handshake;

public interface ServerHandshakeBuilder extends HandshakeBuilder, ServerHandshake {

    void setHttpStatus(short var1);

    void setHttpStatusMessage(String var1);
}