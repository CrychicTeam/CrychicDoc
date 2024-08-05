package com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari;

import java.io.IOException;
import java.io.OutputStream;

public class MQEncoder {

    public static final int CARRY_MASK = 134217728;

    private int range = 32768;

    private int offset = 0;

    private int bitsToCode = 12;

    private long bytesOutput;

    private int byteToGo;

    private OutputStream out;

    public MQEncoder(OutputStream out) {
        this.out = out;
    }

    public void encode(int symbol, Context cm) throws IOException {
        int rangeLps = MQConst.pLps[cm.getState()];
        if (symbol == cm.getMps()) {
            this.range -= rangeLps;
            this.offset += rangeLps;
            if (this.range < 32768) {
                while (this.range < 32768) {
                    this.renormalize();
                }
                cm.setState(MQConst.transitMPS[cm.getState()]);
            }
        } else {
            this.range = rangeLps;
            while (this.range < 32768) {
                this.renormalize();
            }
            if (MQConst.mpsSwitch[cm.getState()] != 0) {
                cm.setMps(1 - cm.getMps());
            }
            cm.setState(MQConst.transitLPS[cm.getState()]);
        }
    }

    public void finish() throws IOException {
        this.finalizeValue();
        this.offset = this.offset << this.bitsToCode;
        int bitsToOutput = 12 - this.bitsToCode;
        this.outputByte();
        bitsToOutput -= this.bitsToCode;
        if (bitsToOutput > 0) {
            this.offset = this.offset << this.bitsToCode;
            this.outputByte();
        }
        this.out.write(this.byteToGo);
    }

    private void finalizeValue() {
        int halfBit = this.offset & 32768;
        this.offset &= -65536;
        if (halfBit == 0) {
            this.offset |= 32768;
        } else {
            this.offset += 65536;
        }
    }

    private void renormalize() throws IOException {
        this.offset <<= 1;
        this.range <<= 1;
        this.range = (int) ((long) this.range & 65535L);
        this.bitsToCode--;
        if (this.bitsToCode == 0) {
            this.outputByte();
        }
    }

    private void outputByte() throws IOException {
        if (this.bytesOutput == 0L) {
            this.outputByteNoStuffing();
        } else if (this.byteToGo == 255) {
            this.outputByteWithStuffing();
        } else if ((this.offset & 134217728) != 0) {
            this.byteToGo++;
            this.offset &= 134217727;
            if (this.byteToGo == 255) {
                this.outputByteWithStuffing();
            } else {
                this.outputByteNoStuffing();
            }
        } else {
            this.outputByteNoStuffing();
        }
    }

    private void outputByteWithStuffing() throws IOException {
        this.bitsToCode = 7;
        if (this.bytesOutput > 0L) {
            this.out.write(this.byteToGo);
        }
        this.byteToGo = this.offset >> 20 & 0xFF;
        this.offset &= 1048575;
        this.bytesOutput++;
    }

    private void outputByteNoStuffing() throws IOException {
        this.bitsToCode = 8;
        if (this.bytesOutput > 0L) {
            this.out.write(this.byteToGo);
        }
        this.byteToGo = this.offset >> 19 & 0xFF;
        this.offset &= 524287;
        this.bytesOutput++;
    }
}