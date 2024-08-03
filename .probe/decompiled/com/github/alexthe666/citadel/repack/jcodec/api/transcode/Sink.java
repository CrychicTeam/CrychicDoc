package com.github.alexthe666.citadel.repack.jcodec.api.transcode;

import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import java.io.IOException;

public interface Sink {

    void init() throws IOException;

    void outputVideoFrame(VideoFrameWithPacket var1) throws IOException;

    void outputAudioFrame(AudioFrameWithPacket var1) throws IOException;

    void finish() throws IOException;

    ColorSpace getInputColor();

    void setOption(Options var1, Object var2);

    boolean isVideo();

    boolean isAudio();
}