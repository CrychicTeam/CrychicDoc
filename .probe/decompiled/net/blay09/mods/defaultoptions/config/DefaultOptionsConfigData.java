package net.blay09.mods.defaultoptions.config;

import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.Config;
import net.blay09.mods.defaultoptions.difficulty.UnobfuscatedDifficulty;

@Config("defaultoptions")
public class DefaultOptionsConfigData implements BalmConfigData {

    @Comment("The default difficulty selected for newly created worlds.")
    public UnobfuscatedDifficulty defaultDifficulty = UnobfuscatedDifficulty.NORMAL;

    @Comment("Set to true if the difficulty for new world's should be locked to the specific default. This cannot be unlocked by players without external tools! Probably a bad idea. I don't recommend. Why am I adding this option?")
    public boolean lockDifficulty = false;
}