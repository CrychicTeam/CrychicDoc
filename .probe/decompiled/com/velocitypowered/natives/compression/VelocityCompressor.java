package com.velocitypowered.natives.compression;

import com.velocitypowered.natives.Disposable;
import com.velocitypowered.natives.Native;
import io.netty.buffer.ByteBuf;
import java.util.zip.DataFormatException;

public interface VelocityCompressor extends Disposable, Native {

    void inflate(ByteBuf var1, ByteBuf var2, int var3) throws DataFormatException;

    void deflate(ByteBuf var1, ByteBuf var2) throws DataFormatException;
}