package me.lucko.spark.lib.bytesocks.ws;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public interface WrappedByteChannel extends ByteChannel {

    boolean isNeedWrite();

    void writeMore() throws IOException;

    boolean isNeedRead();

    int readMore(ByteBuffer var1) throws IOException;

    boolean isBlocking();
}