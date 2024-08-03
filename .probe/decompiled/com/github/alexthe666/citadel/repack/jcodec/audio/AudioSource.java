package com.github.alexthe666.citadel.repack.jcodec.audio;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import java.io.IOException;
import java.nio.FloatBuffer;

public interface AudioSource {

    AudioFormat getFormat();

    int readFloat(FloatBuffer var1) throws IOException;
}