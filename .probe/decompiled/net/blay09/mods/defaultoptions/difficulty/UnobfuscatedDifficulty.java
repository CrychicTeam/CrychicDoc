package net.blay09.mods.defaultoptions.difficulty;

import net.minecraft.world.Difficulty;

public enum UnobfuscatedDifficulty {

    PEACEFUL, EASY, NORMAL, HARD;

    public Difficulty toDifficulty() {
        return switch(this) {
            case PEACEFUL ->
                Difficulty.PEACEFUL;
            case EASY ->
                Difficulty.EASY;
            case NORMAL ->
                Difficulty.NORMAL;
            case HARD ->
                Difficulty.HARD;
        };
    }
}