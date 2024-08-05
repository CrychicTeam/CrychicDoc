package me.lucko.spark.lib.bytesocks.ws.framing;

import me.lucko.spark.lib.bytesocks.ws.enums.Opcode;

public class BinaryFrame extends DataFrame {

    public BinaryFrame() {
        super(Opcode.BINARY);
    }
}