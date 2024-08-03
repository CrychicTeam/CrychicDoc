package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;

public interface IBitStream {

    void destroy();

    void setData(byte[] var1);

    void byteAlign() throws AACException;

    void reset();

    int getPosition();

    int getBitsLeft();

    int readBits(int var1) throws AACException;

    int readBit() throws AACException;

    boolean readBool() throws AACException;

    int peekBits(int var1) throws AACException;

    int peekBit() throws AACException;

    void skipBits(int var1) throws AACException;

    void skipBit() throws AACException;

    int maskBits(int var1);
}