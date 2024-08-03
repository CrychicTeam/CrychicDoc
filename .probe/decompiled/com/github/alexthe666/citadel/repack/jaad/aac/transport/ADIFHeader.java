package com.github.alexthe666.citadel.repack.jaad.aac.transport;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.PCE;

public final class ADIFHeader {

    private static final long ADIF_ID = 1094994246L;

    private long id;

    private boolean copyrightIDPresent;

    private byte[] copyrightID = new byte[9];

    private boolean originalCopy;

    private boolean home;

    private boolean bitstreamType;

    private int bitrate;

    private int pceCount;

    private int[] adifBufferFullness;

    private PCE[] pces;

    public static boolean isPresent(BitStream in) throws AACException {
        return (long) in.peekBits(32) == 1094994246L;
    }

    private ADIFHeader() {
    }

    public static ADIFHeader readHeader(BitStream in) throws AACException {
        ADIFHeader h = new ADIFHeader();
        h.decode(in);
        return h;
    }

    private void decode(BitStream in) throws AACException {
        this.id = (long) in.readBits(32);
        this.copyrightIDPresent = in.readBool();
        if (this.copyrightIDPresent) {
            for (int i = 0; i < 9; i++) {
                this.copyrightID[i] = (byte) in.readBits(8);
            }
        }
        this.originalCopy = in.readBool();
        this.home = in.readBool();
        this.bitstreamType = in.readBool();
        this.bitrate = in.readBits(23);
        this.pceCount = in.readBits(4) + 1;
        this.pces = new PCE[this.pceCount];
        this.adifBufferFullness = new int[this.pceCount];
        for (int i = 0; i < this.pceCount; i++) {
            if (this.bitstreamType) {
                this.adifBufferFullness[i] = -1;
            } else {
                this.adifBufferFullness[i] = in.readBits(20);
            }
            this.pces[i] = new PCE();
            this.pces[i].decode(in);
        }
    }

    public PCE getFirstPCE() {
        return this.pces[0];
    }
}