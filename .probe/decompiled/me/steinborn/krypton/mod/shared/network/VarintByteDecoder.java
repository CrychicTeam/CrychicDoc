package me.steinborn.krypton.mod.shared.network;

import io.netty.util.ByteProcessor;

public class VarintByteDecoder implements ByteProcessor {

    private int readVarint;

    private int bytesRead;

    private VarintByteDecoder.DecodeResult result = VarintByteDecoder.DecodeResult.TOO_SHORT;

    public boolean process(byte k) {
        if (k == 0 && this.bytesRead == 0) {
            this.result = VarintByteDecoder.DecodeResult.RUN_OF_ZEROES;
            return true;
        } else if (this.result == VarintByteDecoder.DecodeResult.RUN_OF_ZEROES) {
            return false;
        } else {
            this.readVarint = this.readVarint | (k & 127) << this.bytesRead++ * 7;
            if (this.bytesRead > 3) {
                this.result = VarintByteDecoder.DecodeResult.TOO_BIG;
                return false;
            } else if ((k & 128) != 128) {
                this.result = VarintByteDecoder.DecodeResult.SUCCESS;
                return false;
            } else {
                return true;
            }
        }
    }

    public int getReadVarint() {
        return this.readVarint;
    }

    public int getBytesRead() {
        return this.bytesRead;
    }

    public VarintByteDecoder.DecodeResult getResult() {
        return this.result;
    }

    public void reset() {
        this.readVarint = 0;
        this.bytesRead = 0;
        this.result = VarintByteDecoder.DecodeResult.TOO_SHORT;
    }

    public static enum DecodeResult {

        SUCCESS, TOO_SHORT, TOO_BIG, RUN_OF_ZEROES
    }
}