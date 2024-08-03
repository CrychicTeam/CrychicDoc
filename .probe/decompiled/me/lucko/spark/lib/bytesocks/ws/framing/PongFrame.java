package me.lucko.spark.lib.bytesocks.ws.framing;

import me.lucko.spark.lib.bytesocks.ws.enums.Opcode;

public class PongFrame extends ControlFrame {

    public PongFrame() {
        super(Opcode.PONG);
    }

    public PongFrame(PingFrame pingFrame) {
        super(Opcode.PONG);
        this.setPayload(pingFrame.getPayloadData());
    }
}