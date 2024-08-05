package me.lucko.spark.lib.bytesocks.ws.framing;

import java.nio.ByteBuffer;
import me.lucko.spark.lib.bytesocks.ws.enums.Opcode;

public interface Framedata {

    boolean isFin();

    boolean isRSV1();

    boolean isRSV2();

    boolean isRSV3();

    boolean getTransfereMasked();

    Opcode getOpcode();

    ByteBuffer getPayloadData();

    void append(Framedata var1);
}