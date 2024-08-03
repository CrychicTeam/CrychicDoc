package com.mojang.realmsclient.util;

public class WorldGenerationInfo {

    private final String seed;

    private final LevelType levelType;

    private final boolean generateStructures;

    public WorldGenerationInfo(String string0, LevelType levelType1, boolean boolean2) {
        this.seed = string0;
        this.levelType = levelType1;
        this.generateStructures = boolean2;
    }

    public String getSeed() {
        return this.seed;
    }

    public LevelType getLevelType() {
        return this.levelType;
    }

    public boolean shouldGenerateStructures() {
        return this.generateStructures;
    }
}