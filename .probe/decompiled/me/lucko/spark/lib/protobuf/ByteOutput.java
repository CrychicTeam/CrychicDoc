package me.lucko.spark.lib.protobuf;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class ByteOutput {

    public abstract void write(byte var1) throws IOException;

    public abstract void write(byte[] var1, int var2, int var3) throws IOException;

    public abstract void writeLazy(byte[] var1, int var2, int var3) throws IOException;

    public abstract void write(ByteBuffer var1) throws IOException;

    public abstract void writeLazy(ByteBuffer var1) throws IOException;
}