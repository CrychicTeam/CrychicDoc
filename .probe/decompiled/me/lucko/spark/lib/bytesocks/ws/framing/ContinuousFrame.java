package me.lucko.spark.lib.bytesocks.ws.framing;

import me.lucko.spark.lib.bytesocks.ws.enums.Opcode;

public class ContinuousFrame extends DataFrame {

    public ContinuousFrame() {
        super(Opcode.CONTINUOUS);
    }
}