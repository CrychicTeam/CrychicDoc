package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;

class DSE extends Element {

    private byte[] dataStreamBytes;

    void decode(BitStream in) throws AACException {
        this.readElementInstanceTag(in);
        boolean byteAlign = in.readBool();
        int count = in.readBits(8);
        if (count == 255) {
            count += in.readBits(8);
        }
        if (byteAlign) {
            in.byteAlign();
        }
        this.dataStreamBytes = new byte[count];
        for (int i = 0; i < count; i++) {
            this.dataStreamBytes[i] = (byte) in.readBits(8);
        }
    }
}