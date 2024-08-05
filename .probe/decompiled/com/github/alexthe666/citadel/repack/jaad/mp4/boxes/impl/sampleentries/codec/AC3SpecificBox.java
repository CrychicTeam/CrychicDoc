package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class AC3SpecificBox extends CodecSpecificBox {

    private int fscod;

    private int bsid;

    private int bsmod;

    private int acmod;

    private int bitRateCode;

    private boolean lfeon;

    public AC3SpecificBox() {
        super("AC-3 Specific Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        long l = in.readBytes(3);
        this.fscod = (int) (l >> 22 & 3L);
        this.bsid = (int) (l >> 17 & 31L);
        this.bsmod = (int) (l >> 14 & 7L);
        this.acmod = (int) (l >> 11 & 7L);
        this.lfeon = (l >> 10 & 1L) == 1L;
        this.bitRateCode = (int) (l >> 5 & 31L);
    }

    public int getFscod() {
        return this.fscod;
    }

    public int getBsid() {
        return this.bsid;
    }

    public int getBsmod() {
        return this.bsmod;
    }

    public int getAcmod() {
        return this.acmod;
    }

    public boolean isLfeon() {
        return this.lfeon;
    }

    public int getBitRateCode() {
        return this.bitRateCode;
    }
}