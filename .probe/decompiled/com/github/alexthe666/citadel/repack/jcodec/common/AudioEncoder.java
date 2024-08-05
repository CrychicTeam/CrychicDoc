package com.github.alexthe666.citadel.repack.jcodec.common;

import java.nio.ByteBuffer;

public interface AudioEncoder {

    ByteBuffer encode(ByteBuffer var1, ByteBuffer var2);
}