package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;

public class RealmsWorldResetDto extends ValueObject implements ReflectionBasedSerialization {

    @SerializedName("seed")
    private final String seed;

    @SerializedName("worldTemplateId")
    private final long worldTemplateId;

    @SerializedName("levelType")
    private final int levelType;

    @SerializedName("generateStructures")
    private final boolean generateStructures;

    public RealmsWorldResetDto(String string0, long long1, int int2, boolean boolean3) {
        this.seed = string0;
        this.worldTemplateId = long1;
        this.levelType = int2;
        this.generateStructures = boolean3;
    }
}