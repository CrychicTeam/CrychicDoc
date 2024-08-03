package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import java.nio.ByteBuffer;

public class NIOBitStream implements IBitStream {

    private BitReader br;

    public NIOBitStream(BitReader br) {
        this.br = br;
    }

    @Override
    public void destroy() {
        this.reset();
        this.br = null;
    }

    @Override
    public void setData(byte[] data) {
        this.br = BitReader.createBitReader(ByteBuffer.wrap(data));
    }

    @Override
    public void byteAlign() throws AACException {
        this.br.align();
    }

    @Override
    public void reset() {
        throw new RuntimeException("todo");
    }

    @Override
    public int getPosition() {
        return this.br.position();
    }

    @Override
    public int getBitsLeft() {
        return this.br.remaining();
    }

    @Override
    public int readBits(int n) throws AACException {
        if (this.br.remaining() >= n) {
            return this.br.readNBit(n);
        } else {
            throw AACException.endOfStream();
        }
    }

    @Override
    public int readBit() throws AACException {
        if (this.br.remaining() >= 1) {
            return this.br.read1Bit();
        } else {
            throw AACException.endOfStream();
        }
    }

    @Override
    public boolean readBool() throws AACException {
        int read1Bit = this.readBit();
        return read1Bit != 0;
    }

    @Override
    public int peekBits(int n) throws AACException {
        return this.br.checkNBit(n);
    }

    @Override
    public int peekBit() throws AACException {
        return this.br.curBit();
    }

    @Override
    public void skipBits(int n) throws AACException {
        this.br.skip(n);
    }

    @Override
    public void skipBit() throws AACException {
        this.skipBits(1);
    }

    @Override
    public int maskBits(int n) {
        int i;
        if (n == 32) {
            i = -1;
        } else {
            i = (1 << n) - 1;
        }
        return i;
    }
}