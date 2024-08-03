package com.mojang.blaze3d.platform;

import java.util.OptionalInt;

public class DisplayData {

    public final int width;

    public final int height;

    public final OptionalInt fullscreenWidth;

    public final OptionalInt fullscreenHeight;

    public final boolean isFullscreen;

    public DisplayData(int int0, int int1, OptionalInt optionalInt2, OptionalInt optionalInt3, boolean boolean4) {
        this.width = int0;
        this.height = int1;
        this.fullscreenWidth = optionalInt2;
        this.fullscreenHeight = optionalInt3;
        this.isFullscreen = boolean4;
    }
}