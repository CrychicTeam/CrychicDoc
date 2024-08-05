package com.github.alexthe666.citadel.repack.jcodec.audio;

import java.nio.FloatBuffer;

public interface AudioFilter {

    void filter(FloatBuffer[] var1, long[] var2, FloatBuffer[] var3);

    int getDelay();

    int getNInputs();

    int getNOutputs();
}