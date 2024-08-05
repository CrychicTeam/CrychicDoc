package me.lucko.spark.lib.bytesocks.ws.handshake;

import java.util.Iterator;

public interface Handshakedata {

    Iterator<String> iterateHttpFields();

    String getFieldValue(String var1);

    boolean hasFieldValue(String var1);

    byte[] getContent();
}