package com.github.alexthe666.citadel.repack.jaad.aac.syntax;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.DecoderConfig;
import com.github.alexthe666.citadel.repack.jaad.aac.Profile;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleFrequency;
import com.github.alexthe666.citadel.repack.jaad.aac.tools.ICPrediction;
import com.github.alexthe666.citadel.repack.jaad.aac.tools.LTPrediction;
import java.util.Arrays;

public class ICSInfo implements Constants, ScaleFactorBands {

    public static final int WINDOW_SHAPE_SINE = 0;

    public static final int WINDOW_SHAPE_KAISER = 1;

    public static final int PREVIOUS = 0;

    public static final int CURRENT = 1;

    private final int frameLength;

    private ICSInfo.WindowSequence windowSequence;

    private int[] windowShape;

    private int maxSFB;

    private boolean predictionDataPresent;

    private ICPrediction icPredict;

    private final LTPrediction ltPredict;

    private int windowCount;

    private int windowGroupCount;

    private int[] windowGroupLength;

    private int swbCount;

    private int[] swbOffsets;

    public ICSInfo(DecoderConfig config) {
        this.frameLength = config.getFrameLength();
        this.windowShape = new int[2];
        this.windowSequence = ICSInfo.WindowSequence.ONLY_LONG_SEQUENCE;
        this.windowGroupLength = new int[8];
        if (LTPrediction.isLTPProfile(config.getProfile())) {
            this.ltPredict = new LTPrediction(this.frameLength);
        } else {
            this.ltPredict = null;
        }
    }

    public void decode(BitStream in, DecoderConfig conf, boolean commonWindow) throws AACException {
        SampleFrequency sf = conf.getSampleFrequency();
        if (sf.equals(SampleFrequency.SAMPLE_FREQUENCY_NONE)) {
            throw new AACException("invalid sample frequency");
        } else {
            in.skipBit();
            this.windowSequence = ICSInfo.WindowSequence.forInt(in.readBits(2));
            this.windowShape[0] = this.windowShape[1];
            this.windowShape[1] = in.readBit();
            this.windowGroupCount = 1;
            this.windowGroupLength[0] = 1;
            if (this.windowSequence.equals(ICSInfo.WindowSequence.EIGHT_SHORT_SEQUENCE)) {
                this.maxSFB = in.readBits(4);
                for (int i = 0; i < 7; i++) {
                    if (in.readBool()) {
                        this.windowGroupLength[this.windowGroupCount - 1]++;
                    } else {
                        this.windowGroupCount++;
                        this.windowGroupLength[this.windowGroupCount - 1] = 1;
                    }
                }
                this.windowCount = 8;
                this.swbOffsets = SWB_OFFSET_SHORT_WINDOW[sf.getIndex()];
                this.swbCount = SWB_SHORT_WINDOW_COUNT[sf.getIndex()];
            } else {
                this.maxSFB = in.readBits(6);
                this.windowCount = 1;
                this.swbOffsets = SWB_OFFSET_LONG_WINDOW[sf.getIndex()];
                this.swbCount = SWB_LONG_WINDOW_COUNT[sf.getIndex()];
                this.predictionDataPresent = in.readBool();
                if (this.predictionDataPresent) {
                    this.readPredictionData(in, conf.getProfile(), sf, commonWindow);
                }
            }
        }
    }

    private void readPredictionData(BitStream in, Profile profile, SampleFrequency sf, boolean commonWindow) throws AACException {
        switch(profile) {
            case AAC_MAIN:
                if (this.icPredict == null) {
                    this.icPredict = new ICPrediction();
                }
                this.icPredict.decode(in, this.maxSFB, sf);
                break;
            case AAC_LTP:
                this.ltPredict.decode(in, this, profile);
                break;
            case ER_AAC_LTP:
                if (!commonWindow) {
                    this.ltPredict.decode(in, this, profile);
                }
                break;
            default:
                throw new AACException("unexpected profile for LTP: " + profile);
        }
    }

    public int getMaxSFB() {
        return this.maxSFB;
    }

    public int getSWBCount() {
        return this.swbCount;
    }

    public int[] getSWBOffsets() {
        return this.swbOffsets;
    }

    public int getSWBOffsetMax() {
        return this.swbOffsets[this.swbCount];
    }

    public int getWindowCount() {
        return this.windowCount;
    }

    public int getWindowGroupCount() {
        return this.windowGroupCount;
    }

    public int getWindowGroupLength(int g) {
        return this.windowGroupLength[g];
    }

    public ICSInfo.WindowSequence getWindowSequence() {
        return this.windowSequence;
    }

    public boolean isEightShortFrame() {
        return this.windowSequence.equals(ICSInfo.WindowSequence.EIGHT_SHORT_SEQUENCE);
    }

    public int getWindowShape(int index) {
        return this.windowShape[index];
    }

    public boolean isICPredictionPresent() {
        return this.predictionDataPresent;
    }

    public ICPrediction getICPrediction() {
        return this.icPredict;
    }

    public LTPrediction getLTPrediction() {
        return this.ltPredict;
    }

    public void unsetPredictionSFB(int sfb) {
        if (this.predictionDataPresent) {
            this.icPredict.setPredictionUnused(sfb);
        }
        if (this.ltPredict != null) {
            this.ltPredict.setPredictionUnused(sfb);
        }
    }

    public void setData(BitStream in, DecoderConfig conf, ICSInfo info) throws AACException {
        this.windowSequence = ICSInfo.WindowSequence.valueOf(info.windowSequence.name());
        this.windowShape[0] = this.windowShape[1];
        this.windowShape[1] = info.windowShape[1];
        this.maxSFB = info.maxSFB;
        this.predictionDataPresent = info.predictionDataPresent;
        if (this.predictionDataPresent) {
            this.icPredict = info.icPredict;
        }
        this.windowCount = info.windowCount;
        this.windowGroupCount = info.windowGroupCount;
        this.windowGroupLength = Arrays.copyOf(info.windowGroupLength, info.windowGroupLength.length);
        this.swbCount = info.swbCount;
        this.swbOffsets = Arrays.copyOf(info.swbOffsets, info.swbOffsets.length);
        if (this.predictionDataPresent) {
            this.ltPredict.decode(in, this, conf.getProfile());
        }
    }

    public static enum WindowSequence {

        ONLY_LONG_SEQUENCE, LONG_START_SEQUENCE, EIGHT_SHORT_SEQUENCE, LONG_STOP_SEQUENCE;

        public static ICSInfo.WindowSequence forInt(int i) throws AACException {
            return switch(i) {
                case 0 ->
                    ONLY_LONG_SEQUENCE;
                case 1 ->
                    LONG_START_SEQUENCE;
                case 2 ->
                    EIGHT_SHORT_SEQUENCE;
                case 3 ->
                    LONG_STOP_SEQUENCE;
                default ->
                    throw new AACException("unknown window sequence type");
            };
        }
    }
}