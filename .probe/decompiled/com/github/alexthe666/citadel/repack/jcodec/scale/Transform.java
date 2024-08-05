package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public interface Transform {

    void transform(Picture var1, Picture var2);

    public static enum Levels {

        STUDIO, PC
    }
}