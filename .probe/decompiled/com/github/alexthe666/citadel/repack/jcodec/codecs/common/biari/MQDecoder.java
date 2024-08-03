package com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari;

import java.io.IOException;
import java.io.InputStream;

public class MQDecoder {

    private int range;

    private int value;

    private int availableBits;

    private int lastByte;

    private int decodedBytes;

    private InputStream is;

    public MQDecoder(InputStream is) throws IOException {
        this.is = is;
        this.range = 32768;
        this.value = 0;
        this.fetchByte();
        this.value <<= 8;
        this.fetchByte();
        this.value = this.value << this.availableBits - 1;
        this.availableBits = 1;
    }

    public int decode(Context cm) throws IOException {
        int rangeLps = MQConst.pLps[cm.getState()];
        int decoded;
        if (this.value > rangeLps) {
            this.range -= rangeLps;
            this.value -= rangeLps;
            if (this.range < 32768) {
                while (this.range < 32768) {
                    this.renormalize();
                }
                cm.setState(MQConst.transitMPS[cm.getState()]);
            }
            decoded = cm.getMps();
        } else {
            this.range = rangeLps;
            while (this.range < 32768) {
                this.renormalize();
            }
            if (MQConst.mpsSwitch[cm.getState()] != 0) {
                cm.setMps(1 - cm.getMps());
            }
            cm.setState(MQConst.transitLPS[cm.getState()]);
            decoded = 1 - cm.getMps();
        }
        return decoded;
    }

    private void fetchByte() throws IOException {
        this.availableBits = 8;
        if (this.decodedBytes > 0 && this.lastByte == 255) {
            this.availableBits = 7;
        }
        this.lastByte = this.is.read();
        int shiftCarry = 8 - this.availableBits;
        this.value = this.value + (this.lastByte << shiftCarry);
        this.decodedBytes++;
    }

    private void renormalize() throws IOException {
        this.value <<= 1;
        this.range <<= 1;
        this.range &= 65535;
        this.availableBits--;
        if (this.availableBits == 0) {
            this.fetchByte();
        }
    }
}