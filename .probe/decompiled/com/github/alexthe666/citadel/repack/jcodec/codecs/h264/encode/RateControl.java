package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;

public interface RateControl {

    int startPicture(Size var1, int var2, SliceType var3);

    int initialQpDelta();

    int accept(int var1);
}