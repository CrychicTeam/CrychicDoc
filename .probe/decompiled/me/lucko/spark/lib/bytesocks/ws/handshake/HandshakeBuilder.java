package me.lucko.spark.lib.bytesocks.ws.handshake;

public interface HandshakeBuilder extends Handshakedata {

    void setContent(byte[] var1);

    void put(String var1, String var2);
}