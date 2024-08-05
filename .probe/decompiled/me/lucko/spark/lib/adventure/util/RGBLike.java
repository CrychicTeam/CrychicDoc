package me.lucko.spark.lib.adventure.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface RGBLike {

    @Range(from = 0L, to = 255L)
    int red();

    @Range(from = 0L, to = 255L)
    int green();

    @Range(from = 0L, to = 255L)
    int blue();

    @NotNull
    default HSVLike asHSV() {
        return HSVLike.fromRGB(this.red(), this.green(), this.blue());
    }
}