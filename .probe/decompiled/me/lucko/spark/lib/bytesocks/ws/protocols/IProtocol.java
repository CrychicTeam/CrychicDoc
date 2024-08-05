package me.lucko.spark.lib.bytesocks.ws.protocols;

public interface IProtocol {

    boolean acceptProvidedProtocol(String var1);

    String getProvidedProtocol();

    IProtocol copyInstance();

    String toString();
}