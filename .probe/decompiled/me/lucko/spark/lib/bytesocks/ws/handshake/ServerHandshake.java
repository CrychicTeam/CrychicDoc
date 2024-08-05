package me.lucko.spark.lib.bytesocks.ws.handshake;

public interface ServerHandshake extends Handshakedata {

    short getHttpStatus();

    String getHttpStatusMessage();
}