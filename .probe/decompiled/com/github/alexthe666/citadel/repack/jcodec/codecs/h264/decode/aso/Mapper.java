package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso;

public interface Mapper {

    boolean leftAvailable(int var1);

    boolean topAvailable(int var1);

    int getAddress(int var1);

    int getMbX(int var1);

    int getMbY(int var1);

    boolean topRightAvailable(int var1);

    boolean topLeftAvailable(int var1);
}