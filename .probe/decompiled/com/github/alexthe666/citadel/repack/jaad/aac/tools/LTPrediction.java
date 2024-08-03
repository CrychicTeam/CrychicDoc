package com.github.alexthe666.citadel.repack.jaad.aac.tools;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.Profile;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleFrequency;
import com.github.alexthe666.citadel.repack.jaad.aac.filterbank.FilterBank;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.Constants;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICSInfo;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICStream;
import java.util.Arrays;

public class LTPrediction implements Constants {

    private static final float[] CODEBOOK = new float[] { 0.570829F, 0.696616F, 0.813004F, 0.911304F, 0.9849F, 1.067894F, 1.194601F, 1.369533F };

    private boolean isPresent = false;

    private final int frameLength;

    private final int[] states;

    private int coef;

    private int lag;

    private int lastBand;

    private boolean lagUpdate;

    private boolean[] shortUsed;

    private boolean[] shortLagPresent;

    private boolean[] longUsed;

    private int[] shortLag;

    public LTPrediction(int frameLength) {
        this.frameLength = frameLength;
        this.states = new int[4 * frameLength];
    }

    public boolean isPresent() {
        return this.isPresent;
    }

    public void decode(BitStream in, ICSInfo info, Profile profile) throws AACException {
        this.lag = 0;
        this.isPresent = in.readBool();
        if (this.isPresent) {
            if (profile.equals(Profile.AAC_LD)) {
                this.lagUpdate = in.readBool();
                if (this.lagUpdate) {
                    this.lag = in.readBits(10);
                }
            } else {
                this.lag = in.readBits(11);
            }
            if (this.lag > this.frameLength << 1) {
                throw new AACException("LTP lag too large: " + this.lag);
            } else {
                this.coef = in.readBits(3);
                int windowCount = info.getWindowCount();
                if (info.isEightShortFrame()) {
                    this.shortUsed = new boolean[windowCount];
                    this.shortLagPresent = new boolean[windowCount];
                    this.shortLag = new int[windowCount];
                    for (int w = 0; w < windowCount; w++) {
                        if (this.shortUsed[w] = in.readBool()) {
                            this.shortLagPresent[w] = in.readBool();
                            if (this.shortLagPresent[w]) {
                                this.shortLag[w] = in.readBits(4);
                            }
                        }
                    }
                } else {
                    this.lastBand = Math.min(info.getMaxSFB(), 40);
                    this.longUsed = new boolean[this.lastBand];
                    for (int i = 0; i < this.lastBand; i++) {
                        this.longUsed[i] = in.readBool();
                    }
                }
            }
        }
    }

    public void setPredictionUnused(int sfb) {
        if (this.longUsed != null) {
            this.longUsed[sfb] = false;
        }
    }

    public void process(ICStream ics, float[] data, FilterBank filterBank, SampleFrequency sf) {
        if (this.isPresent) {
            ICSInfo info = ics.getInfo();
            if (!info.isEightShortFrame()) {
                int samples = this.frameLength << 1;
                float[] in = new float[2048];
                float[] out = new float[2048];
                for (int i = 0; i < samples; i++) {
                    in[i] = (float) this.states[samples + i - this.lag] * CODEBOOK[this.coef];
                }
                filterBank.processLTP(info.getWindowSequence(), info.getWindowShape(1), info.getWindowShape(0), in, out);
                if (ics.isTNSDataPresent()) {
                    ics.getTNS().process(ics, out, sf, true);
                }
                int[] swbOffsets = info.getSWBOffsets();
                int swbOffsetMax = info.getSWBOffsetMax();
                for (int sfb = 0; sfb < this.lastBand; sfb++) {
                    if (this.longUsed[sfb]) {
                        int low = swbOffsets[sfb];
                        int high = Math.min(swbOffsets[sfb + 1], swbOffsetMax);
                        for (int bin = low; bin < high; bin++) {
                            data[bin] += out[bin];
                        }
                    }
                }
            }
        }
    }

    public void updateState(float[] time, float[] overlap, Profile profile) {
        if (profile.equals(Profile.AAC_LD)) {
            for (int i = 0; i < this.frameLength; i++) {
                this.states[i] = this.states[i + this.frameLength];
                this.states[this.frameLength + i] = this.states[i + this.frameLength * 2];
                this.states[this.frameLength * 2 + i] = Math.round(time[i]);
                this.states[this.frameLength * 3 + i] = Math.round(overlap[i]);
            }
        } else {
            for (int i = 0; i < this.frameLength; i++) {
                this.states[i] = this.states[i + this.frameLength];
                this.states[this.frameLength + i] = Math.round(time[i]);
                this.states[this.frameLength * 2 + i] = Math.round(overlap[i]);
            }
        }
        this.isPresent = false;
    }

    public static boolean isLTPProfile(Profile profile) {
        return profile.equals(Profile.AAC_LTP) || profile.equals(Profile.ER_AAC_LTP) || profile.equals(Profile.AAC_LD);
    }

    public void copy(LTPrediction ltp) {
        System.arraycopy(ltp.states, 0, this.states, 0, this.states.length);
        this.coef = ltp.coef;
        this.lag = ltp.lag;
        this.lastBand = ltp.lastBand;
        this.lagUpdate = ltp.lagUpdate;
        this.shortUsed = Arrays.copyOf(ltp.shortUsed, ltp.shortUsed.length);
        this.shortLagPresent = Arrays.copyOf(ltp.shortLagPresent, ltp.shortLagPresent.length);
        this.shortLag = Arrays.copyOf(ltp.shortLag, ltp.shortLag.length);
        this.longUsed = Arrays.copyOf(ltp.longUsed, ltp.longUsed.length);
    }
}