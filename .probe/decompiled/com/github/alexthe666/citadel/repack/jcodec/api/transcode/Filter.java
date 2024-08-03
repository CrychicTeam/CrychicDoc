package com.github.alexthe666.citadel.repack.jcodec.api.transcode;

import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public interface Filter {

    PixelStore.LoanerPicture filter(Picture var1, PixelStore var2);

    ColorSpace getInputColor();

    ColorSpace getOutputColor();
}