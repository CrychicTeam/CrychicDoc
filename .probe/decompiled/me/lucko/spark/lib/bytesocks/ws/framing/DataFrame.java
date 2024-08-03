package me.lucko.spark.lib.bytesocks.ws.framing;

import me.lucko.spark.lib.bytesocks.ws.enums.Opcode;
import me.lucko.spark.lib.bytesocks.ws.exceptions.InvalidDataException;

public abstract class DataFrame extends FramedataImpl1 {

    public DataFrame(Opcode opcode) {
        super(opcode);
    }

    @Override
    public void isValid() throws InvalidDataException {
    }
}