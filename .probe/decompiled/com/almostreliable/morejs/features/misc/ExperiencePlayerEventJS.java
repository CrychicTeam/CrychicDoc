package com.almostreliable.morejs.features.misc;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.player.Player;

public class ExperiencePlayerEventJS extends PlayerEventJS {

    private final Player player;

    private int amount;

    public ExperiencePlayerEventJS(Player player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public Player getEntity() {
        return this.player;
    }

    public float getExperienceProgress() {
        return this.player.experienceProgress;
    }

    public void setExperienceProgress(float progress) {
        this.player.experienceProgress = progress;
    }

    public int getExperienceLevel() {
        return this.player.experienceLevel;
    }

    public void setExperienceLevel(int level) {
        this.player.experienceLevel = level;
    }

    public int getTotalExperience() {
        return this.player.totalExperience;
    }

    public void setTotalExperience(int experience) {
        this.player.totalExperience = experience;
    }

    public int getXpNeededForNextLevel() {
        return this.player.getXpNeededForNextLevel();
    }

    public int getRemainingExperience() {
        return (int) ((float) this.getXpNeededForNextLevel() - this.getExperienceProgress() * (float) this.getXpNeededForNextLevel());
    }

    public boolean willLevelUp() {
        return this.getExperienceProgress() + (float) this.getAmount() / (float) this.getXpNeededForNextLevel() >= 1.0F;
    }
}