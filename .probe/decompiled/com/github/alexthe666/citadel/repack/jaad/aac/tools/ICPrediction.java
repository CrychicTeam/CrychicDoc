package com.github.alexthe666.citadel.repack.jaad.aac.tools;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleFrequency;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.Constants;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICSInfo;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICStream;
import java.util.logging.Level;

public class ICPrediction {

    private static final float SF_SCALE = -9.765625E-4F;

    private static final float INV_SF_SCALE = -1024.0F;

    private static final int MAX_PREDICTORS = 672;

    private static final float A = 0.953125F;

    private static final float ALPHA = 0.90625F;

    private boolean predictorReset;

    private int predictorResetGroup;

    private boolean[] predictionUsed;

    private ICPrediction.PredictorState[] states = new ICPrediction.PredictorState[672];

    public ICPrediction() {
        this.resetAllPredictors();
    }

    public void decode(BitStream in, int maxSFB, SampleFrequency sf) throws AACException {
        int predictorCount = sf.getPredictorCount();
        if (this.predictorReset = in.readBool()) {
            this.predictorResetGroup = in.readBits(5);
        }
        int maxPredSFB = sf.getMaximalPredictionSFB();
        int length = Math.min(maxSFB, maxPredSFB);
        this.predictionUsed = new boolean[length];
        for (int sfb = 0; sfb < length; sfb++) {
            this.predictionUsed[sfb] = in.readBool();
        }
        Constants.LOGGER.log(Level.WARNING, "ICPrediction: maxSFB={0}, maxPredSFB={1}", new int[] { maxSFB, maxPredSFB });
    }

    public void setPredictionUnused(int sfb) {
        this.predictionUsed[sfb] = false;
    }

    public void process(ICStream ics, float[] data, SampleFrequency sf) {
        ICSInfo info = ics.getInfo();
        if (info.isEightShortFrame()) {
            this.resetAllPredictors();
        } else {
            int len = Math.min(sf.getMaximalPredictionSFB(), info.getMaxSFB());
            int[] swbOffsets = info.getSWBOffsets();
            for (int sfb = 0; sfb < len; sfb++) {
                for (int k = swbOffsets[sfb]; k < swbOffsets[sfb + 1]; k++) {
                    this.predict(data, k, this.predictionUsed[sfb]);
                }
            }
            if (this.predictorReset) {
                this.resetPredictorGroup(this.predictorResetGroup);
            }
        }
    }

    private void resetPredictState(int index) {
        if (this.states[index] == null) {
            this.states[index] = new ICPrediction.PredictorState();
        }
        this.states[index].r0 = 0.0F;
        this.states[index].r1 = 0.0F;
        this.states[index].cor0 = 0.0F;
        this.states[index].cor1 = 0.0F;
        this.states[index].var0 = 16256.0F;
        this.states[index].var1 = 16256.0F;
    }

    private void resetAllPredictors() {
        for (int i = 0; i < this.states.length; i++) {
            this.resetPredictState(i);
        }
    }

    private void resetPredictorGroup(int group) {
        for (int i = group - 1; i < this.states.length; i += 30) {
            this.resetPredictState(i);
        }
    }

    private void predict(float[] data, int off, boolean output) {
        if (this.states[off] == null) {
            this.states[off] = new ICPrediction.PredictorState();
        }
        ICPrediction.PredictorState state = this.states[off];
        float r0 = state.r0;
        float r1 = state.r1;
        float cor0 = state.cor0;
        float cor1 = state.cor1;
        float var0 = state.var0;
        float var1x = state.var1;
        float k1 = var0 > 1.0F ? cor0 * this.even(0.953125F / var0) : 0.0F;
        float k2 = var1x > 1.0F ? cor1 * this.even(0.953125F / var1x) : 0.0F;
        float pv = this.round(k1 * r0 + k2 * r1);
        if (output) {
            data[off] += pv * -9.765625E-4F;
        }
        float e0 = data[off] * -1024.0F;
        float e1 = e0 - k1 * r0;
        state.cor1 = this.trunc(0.90625F * cor1 + r1 * e1);
        state.var1 = this.trunc(0.90625F * var1x + 0.5F * (r1 * r1 + e1 * e1));
        state.cor0 = this.trunc(0.90625F * cor0 + r0 * e0);
        state.var0 = this.trunc(0.90625F * var0 + 0.5F * (r0 * r0 + e0 * e0));
        state.r1 = this.trunc(0.953125F * (r0 - k1 * e0));
        state.r0 = this.trunc(0.953125F * e0);
    }

    private float round(float pf) {
        return Float.intBitsToFloat(Float.floatToIntBits(pf) + 32768 & -65536);
    }

    private float even(float pf) {
        int i = Float.floatToIntBits(pf);
        i = i + 32767 + (i & 1) & -65536;
        return Float.intBitsToFloat(i);
    }

    private float trunc(float pf) {
        return Float.intBitsToFloat(Float.floatToIntBits(pf) & -65536);
    }

    private static final class PredictorState {

        float cor0 = 0.0F;

        float cor1 = 0.0F;

        float var0 = 0.0F;

        float var1 = 0.0F;

        float r0 = 1.0F;

        float r1 = 1.0F;
    }
}