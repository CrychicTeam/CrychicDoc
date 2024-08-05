package com.github.alexthe666.citadel.repack.jcodec.common.io;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public interface SeekableByteChannel extends ByteChannel, Channel, Closeable, ReadableByteChannel, WritableByteChannel {

    long position() throws IOException;

    SeekableByteChannel setPosition(long var1) throws IOException;

    long size() throws IOException;

    SeekableByteChannel truncate(long var1) throws IOException;
}