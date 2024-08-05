package com.github.alexthe666.citadel.repack.jcodec.audio;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import java.nio.FloatBuffer;

public class ChannelSplit implements AudioFilter {

    private AudioFormat format;

    public ChannelSplit(AudioFormat format) {
        this.format = format;
    }

    @Override
    public void filter(FloatBuffer[] _in, long[] inPos, FloatBuffer[] out) {
        if (_in.length != 1) {
            throw new IllegalArgumentException("Channel split invoked on more then one input");
        } else if (out.length != this.format.getChannels()) {
            throw new IllegalArgumentException("Channel split must be supplied with " + this.format.getChannels() + " output buffers to hold the channels.");
        } else {
            FloatBuffer in0 = _in[0];
            int outSampleCount = in0.remaining() / out.length;
            for (int i = 0; i < out.length; i++) {
                if (out[i].remaining() < outSampleCount) {
                    throw new IllegalArgumentException("Supplied buffer for " + i + "th channel doesn't have sufficient space to put the samples ( required: " + outSampleCount + ", actual: " + out[i].remaining() + ")");
                }
            }
            while (in0.remaining() >= this.format.getChannels()) {
                for (int ix = 0; ix < out.length; ix++) {
                    out[ix].put(in0.get());
                }
            }
        }
    }

    @Override
    public int getDelay() {
        return 0;
    }

    @Override
    public int getNInputs() {
        return 1;
    }

    @Override
    public int getNOutputs() {
        return this.format.getChannels();
    }
}