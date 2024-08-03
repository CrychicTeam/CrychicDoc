package me.lucko.spark.lib.bytesocks.ws.framing;

import me.lucko.spark.lib.bytesocks.ws.enums.Opcode;

public class PingFrame extends ControlFrame {

    public PingFrame() {
        super(Opcode.PING);
    }
}