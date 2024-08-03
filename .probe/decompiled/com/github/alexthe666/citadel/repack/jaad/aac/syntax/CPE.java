package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.DecoderConfig;
import com.github.alexthe666.citadel.repack.jaad.aac.Profile;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleFrequency;
import com.github.alexthe666.citadel.repack.jaad.aac.tools.LTPrediction;
import com.github.alexthe666.citadel.repack.jaad.aac.tools.MSMask;
import java.util.Arrays;

public class CPE extends Element implements Constants {

    private MSMask msMask;

    private boolean[] msUsed = new boolean[128];

    private boolean commonWindow;

    ICStream icsL;

    ICStream icsR;

    CPE(DecoderConfig config) {
        this.icsL = new ICStream(config);
        this.icsR = new ICStream(config);
    }

    void decode(BitStream in, DecoderConfig conf) throws AACException {
        Profile profile = conf.getProfile();
        SampleFrequency sf = conf.getSampleFrequency();
        if (sf.equals(SampleFrequency.SAMPLE_FREQUENCY_NONE)) {
            throw new AACException("invalid sample frequency");
        } else {
            this.readElementInstanceTag(in);
            this.commonWindow = in.readBool();
            ICSInfo info = this.icsL.getInfo();
            if (this.commonWindow) {
                info.decode(in, conf, this.commonWindow);
                this.icsR.getInfo().setData(in, conf, info);
                this.msMask = MSMask.forInt(in.readBits(2));
                if (this.msMask.equals(MSMask.TYPE_USED)) {
                    int maxSFB = info.getMaxSFB();
                    int windowGroupCount = info.getWindowGroupCount();
                    for (int idx = 0; idx < windowGroupCount * maxSFB; idx++) {
                        this.msUsed[idx] = in.readBool();
                    }
                } else if (this.msMask.equals(MSMask.TYPE_ALL_1)) {
                    Arrays.fill(this.msUsed, true);
                } else {
                    if (!this.msMask.equals(MSMask.TYPE_ALL_0)) {
                        throw new AACException("reserved MS mask type used");
                    }
                    Arrays.fill(this.msUsed, false);
                }
            } else {
                this.msMask = MSMask.TYPE_ALL_0;
                Arrays.fill(this.msUsed, false);
            }
            if (profile.isErrorResilientProfile()) {
                LTPrediction ltp = this.icsR.getInfo().getLTPrediction();
                if (ltp != null) {
                    ltp.decode(in, info, profile);
                }
            }
            this.icsL.decode(in, this.commonWindow, conf);
            this.icsR.decode(in, this.commonWindow, conf);
        }
    }

    public ICStream getLeftChannel() {
        return this.icsL;
    }

    public ICStream getRightChannel() {
        return this.icsR;
    }

    public MSMask getMSMask() {
        return this.msMask;
    }

    public boolean isMSUsed(int off) {
        return this.msUsed[off];
    }

    public boolean isMSMaskPresent() {
        return !this.msMask.equals(MSMask.TYPE_ALL_0);
    }

    public boolean isCommonWindow() {
        return this.commonWindow;
    }
}