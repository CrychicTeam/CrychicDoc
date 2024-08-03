package com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;
import java.nio.ByteBuffer;

public class JPEGBitStream {

    private VLC[] huff;

    private BitReader _in;

    private int[] dcPredictor = new int[3];

    private int lumaLen;

    public JPEGBitStream(ByteBuffer b, VLC[] huff, int lumaLen) {
        this._in = BitReader.createBitReader(b);
        this.huff = huff;
        this.lumaLen = lumaLen;
    }

    public void readMCU(int[][] buf) {
        int blk = 0;
        for (int i = 0; i < this.lumaLen; blk++) {
            this.dcPredictor[0] = buf[blk][0] = this.readDCValue(this.dcPredictor[0], this.huff[0]);
            this.readACValues(buf[blk], this.huff[2]);
            i++;
        }
        this.dcPredictor[1] = buf[blk][0] = this.readDCValue(this.dcPredictor[1], this.huff[1]);
        this.readACValues(buf[blk], this.huff[3]);
        blk++;
        this.dcPredictor[2] = buf[blk][0] = this.readDCValue(this.dcPredictor[2], this.huff[1]);
        this.readACValues(buf[blk], this.huff[3]);
        blk++;
    }

    public int readDCValue(int prevDC, VLC table) {
        int code = table.readVLC(this._in);
        return code != 0 ? this.toValue(this._in.readNBit(code), code) + prevDC : prevDC;
    }

    public void readACValues(int[] target, VLC table) {
        int curOff = 1;
        int code;
        do {
            code = table.readVLC(this._in);
            if (code == 240) {
                curOff += 16;
            } else if (code > 0) {
                int rle = code >> 4;
                curOff += rle;
                int len = code & 15;
                target[curOff] = this.toValue(this._in.readNBit(len), len);
                curOff++;
            }
        } while (code != 0 && curOff < 64);
    }

    public final int toValue(int raw, int length) {
        return length >= 1 && raw < 1 << length - 1 ? -(1 << length) + 1 + raw : raw;
    }
}