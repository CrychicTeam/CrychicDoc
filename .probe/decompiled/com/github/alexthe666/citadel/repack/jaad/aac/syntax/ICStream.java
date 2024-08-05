package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.ChannelConfiguration;
import com.github.alexthe666.citadel.repack.jaad.aac.DecoderConfig;
import com.github.alexthe666.citadel.repack.jaad.aac.error.RVLC;
import com.github.alexthe666.citadel.repack.jaad.aac.gain.GainControl;
import com.github.alexthe666.citadel.repack.jaad.aac.huffman.HCB;
import com.github.alexthe666.citadel.repack.jaad.aac.huffman.Huffman;
import com.github.alexthe666.citadel.repack.jaad.aac.tools.TNS;
import java.util.Arrays;
import java.util.logging.Level;

public class ICStream implements Constants, HCB, ScaleFactorTable, IQTable {

    private static final int SF_DELTA = 60;

    private static final int SF_OFFSET = 200;

    private static int randomState = 523124044;

    private final int frameLength;

    private final ICSInfo info;

    private final int[] sfbCB;

    private final int[] sectEnd;

    private final float[] data;

    private final float[] scaleFactors;

    private int globalGain;

    private boolean pulseDataPresent;

    private boolean tnsDataPresent;

    private boolean gainControlPresent;

    private TNS tns;

    private GainControl gainControl;

    private int[] pulseOffset;

    private int[] pulseAmp;

    private int pulseCount;

    private int pulseStartSWB;

    private boolean noiseUsed;

    private int reorderedSpectralDataLen;

    private int longestCodewordLen;

    private RVLC rvlc;

    public ICStream(DecoderConfig config) {
        this.frameLength = config.getFrameLength();
        this.info = new ICSInfo(config);
        this.sfbCB = new int[120];
        this.sectEnd = new int[120];
        this.data = new float[this.frameLength];
        this.scaleFactors = new float[120];
    }

    public void decode(BitStream in, boolean commonWindow, DecoderConfig conf) throws AACException {
        if (conf.isScalefactorResilienceUsed() && this.rvlc == null) {
            this.rvlc = new RVLC();
        }
        boolean er = conf.getProfile().isErrorResilientProfile();
        this.globalGain = in.readBits(8);
        if (!commonWindow) {
            this.info.decode(in, conf, commonWindow);
        }
        this.decodeSectionData(in, conf.isSectionDataResilienceUsed());
        this.decodeScaleFactors(in);
        this.pulseDataPresent = in.readBool();
        if (this.pulseDataPresent) {
            if (this.info.isEightShortFrame()) {
                throw new AACException("pulse data not allowed for short frames");
            }
            LOGGER.log(Level.FINE, "PULSE");
            this.decodePulseData(in);
        }
        this.tnsDataPresent = in.readBool();
        if (this.tnsDataPresent && !er) {
            if (this.tns == null) {
                this.tns = new TNS();
            }
            this.tns.decode(in, this.info);
        }
        this.gainControlPresent = in.readBool();
        if (this.gainControlPresent) {
            if (this.gainControl == null) {
                this.gainControl = new GainControl(this.frameLength);
            }
            LOGGER.log(Level.FINE, "GAIN");
            this.gainControl.decode(in, this.info.getWindowSequence());
        }
        if (conf.isSpectralDataResilienceUsed()) {
            int max = conf.getChannelConfiguration() == ChannelConfiguration.CHANNEL_CONFIG_STEREO ? 6144 : 12288;
            this.reorderedSpectralDataLen = Math.max(in.readBits(14), max);
            this.longestCodewordLen = Math.max(in.readBits(6), 49);
        } else {
            this.decodeSpectralData(in);
        }
    }

    public void decodeSectionData(BitStream in, boolean sectionDataResilienceUsed) throws AACException {
        Arrays.fill(this.sfbCB, 0);
        Arrays.fill(this.sectEnd, 0);
        int bits = this.info.isEightShortFrame() ? 3 : 5;
        int escVal = (1 << bits) - 1;
        int windowGroupCount = this.info.getWindowGroupCount();
        int maxSFB = this.info.getMaxSFB();
        int idx = 0;
        for (int g = 0; g < windowGroupCount; g++) {
            int k = 0;
            while (k < maxSFB) {
                int end = k;
                int cb = in.readBits(4);
                if (cb == 12) {
                    throw new AACException("invalid huffman codebook: 12");
                }
                int incr;
                while ((incr = in.readBits(bits)) == escVal) {
                    end += incr;
                }
                end += incr;
                if (end > maxSFB) {
                    throw new AACException("too many bands: " + end + ", allowed: " + maxSFB);
                }
                while (k < end) {
                    this.sfbCB[idx] = cb;
                    this.sectEnd[idx++] = end;
                    k++;
                }
            }
        }
    }

    private void decodePulseData(BitStream in) throws AACException {
        this.pulseCount = in.readBits(2) + 1;
        this.pulseStartSWB = in.readBits(6);
        if (this.pulseStartSWB >= this.info.getSWBCount()) {
            throw new AACException("pulse SWB out of range: " + this.pulseStartSWB + " > " + this.info.getSWBCount());
        } else {
            if (this.pulseOffset == null || this.pulseCount != this.pulseOffset.length) {
                this.pulseOffset = new int[this.pulseCount];
                this.pulseAmp = new int[this.pulseCount];
            }
            this.pulseOffset[0] = this.info.getSWBOffsets()[this.pulseStartSWB];
            this.pulseOffset[0] = this.pulseOffset[0] + in.readBits(5);
            this.pulseAmp[0] = in.readBits(4);
            for (int i = 1; i < this.pulseCount; i++) {
                this.pulseOffset[i] = in.readBits(5) + this.pulseOffset[i - 1];
                if (this.pulseOffset[i] > 1023) {
                    throw new AACException("pulse offset out of range: " + this.pulseOffset[0]);
                }
                this.pulseAmp[i] = in.readBits(4);
            }
        }
    }

    public void decodeScaleFactors(BitStream in) throws AACException {
        int windowGroups = this.info.getWindowGroupCount();
        int maxSFB = this.info.getMaxSFB();
        int[] offset = new int[] { this.globalGain, this.globalGain - 90, 0 };
        boolean noiseFlag = true;
        int idx = 0;
        for (int g = 0; g < windowGroups; g++) {
            int sfb = 0;
            while (sfb < maxSFB) {
                int end = this.sectEnd[idx];
                switch(this.sfbCB[idx]) {
                    case 0:
                        while (sfb < end) {
                            this.scaleFactors[idx] = 0.0F;
                            sfb++;
                            idx++;
                        }
                        break;
                    case 13:
                        while (sfb < end) {
                            if (noiseFlag) {
                                offset[1] += in.readBits(9) - 256;
                                noiseFlag = false;
                            } else {
                                offset[1] += Huffman.decodeScaleFactor(in) - 60;
                            }
                            int tmp = Math.min(Math.max(offset[1], -100), 155);
                            this.scaleFactors[idx] = -SCALEFACTOR_TABLE[tmp + 200];
                            sfb++;
                            idx++;
                        }
                        break;
                    case 14:
                    case 15:
                        while (sfb < end) {
                            offset[2] += Huffman.decodeScaleFactor(in) - 60;
                            int tmp = Math.min(Math.max(offset[2], -155), 100);
                            this.scaleFactors[idx] = SCALEFACTOR_TABLE[-tmp + 200];
                            sfb++;
                            idx++;
                        }
                        break;
                    default:
                        while (sfb < end) {
                            offset[0] += Huffman.decodeScaleFactor(in) - 60;
                            if (offset[0] > 255) {
                                throw new AACException("scalefactor out of range: " + offset[0]);
                            }
                            this.scaleFactors[idx] = SCALEFACTOR_TABLE[offset[0] - 100 + 200];
                            sfb++;
                            idx++;
                        }
                }
            }
        }
    }

    private void decodeSpectralData(BitStream in) throws AACException {
        Arrays.fill(this.data, 0.0F);
        int maxSFB = this.info.getMaxSFB();
        int windowGroups = this.info.getWindowGroupCount();
        int[] offsets = this.info.getSWBOffsets();
        int[] buf = new int[4];
        int groupOff = 0;
        int idx = 0;
        for (int g = 0; g < windowGroups; g++) {
            int groupLen = this.info.getWindowGroupLength(g);
            for (int sfb = 0; sfb < maxSFB; idx++) {
                int hcb = this.sfbCB[idx];
                int off = groupOff + offsets[sfb];
                int width = offsets[sfb + 1] - offsets[sfb];
                if (hcb != 0 && hcb != 15 && hcb != 14) {
                    if (hcb == 13) {
                        for (int w = 0; w < groupLen; off += 128) {
                            float energy = 0.0F;
                            for (int k = 0; k < width; k++) {
                                randomState = 1664525 * randomState + 1013904223;
                                this.data[off + k] = (float) randomState;
                                energy += this.data[off + k] * this.data[off + k];
                            }
                            float scale = (float) ((double) this.scaleFactors[idx] / Math.sqrt((double) energy));
                            for (int var21 = 0; var21 < width; var21++) {
                                this.data[off + var21] = this.data[off + var21] * scale;
                            }
                            w++;
                        }
                    } else {
                        for (int w = 0; w < groupLen; off += 128) {
                            int num = hcb >= 5 ? 2 : 4;
                            for (int k = 0; k < width; k += num) {
                                Huffman.decodeSpectralData(in, hcb, buf, 0);
                                for (int j = 0; j < num; j++) {
                                    this.data[off + k + j] = buf[j] > 0 ? IQ_TABLE[buf[j]] : -IQ_TABLE[-buf[j]];
                                    this.data[off + k + j] = this.data[off + k + j] * this.scaleFactors[idx];
                                }
                            }
                            w++;
                        }
                    }
                } else {
                    for (int w = 0; w < groupLen; off += 128) {
                        Arrays.fill(this.data, off, off + width, 0.0F);
                        w++;
                    }
                }
                sfb++;
            }
            groupOff += groupLen << 7;
        }
    }

    public float[] getInvQuantData() {
        return this.data;
    }

    public ICSInfo getInfo() {
        return this.info;
    }

    public int[] getSectEnd() {
        return this.sectEnd;
    }

    public int[] getSfbCB() {
        return this.sfbCB;
    }

    public float[] getScaleFactors() {
        return this.scaleFactors;
    }

    public boolean isTNSDataPresent() {
        return this.tnsDataPresent;
    }

    public TNS getTNS() {
        return this.tns;
    }

    public int getGlobalGain() {
        return this.globalGain;
    }

    public boolean isNoiseUsed() {
        return this.noiseUsed;
    }

    public int getLongestCodewordLength() {
        return this.longestCodewordLen;
    }

    public int getReorderedSpectralDataLength() {
        return this.reorderedSpectralDataLen;
    }

    public boolean isGainControlPresent() {
        return this.gainControlPresent;
    }

    public GainControl getGainControl() {
        return this.gainControl;
    }
}