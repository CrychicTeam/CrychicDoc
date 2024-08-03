package fr.frinn.custommachinery.impl.integration.jei;

import net.minecraft.util.Mth;

public class Experience {

    private int xp;

    private final int capacity;

    private final double chance;

    private final boolean isPerTick;

    private final Experience.Form type;

    private int experienceLevel = 0;

    public Experience(int xp, int capacity, double chance, boolean isPerTick, Experience.Form type) {
        this.xp = xp;
        this.capacity = capacity;
        this.chance = chance;
        this.isPerTick = isPerTick;
        this.type = type;
    }

    public Experience(int xp, int capacity, Experience.Form type) {
        this(xp, capacity, 1.0, false, type);
    }

    public Experience(int xp, Experience.Form type) {
        this(xp, xp, 1.0, false, type);
    }

    public Experience(int xp, double chance, boolean isPerTick, Experience.Form type) {
        this(xp, xp, chance, isPerTick, type);
    }

    public Experience(int xp, int capacity, double chance, Experience.Form type) {
        this(xp, capacity, chance, false, type);
    }

    public Experience(int xp, double chance, Experience.Form type) {
        this(xp, xp, chance, false, type);
    }

    public Experience(int xp, int capacity, boolean isPerTick, Experience.Form type) {
        this(xp, capacity, 1.0, isPerTick, type);
    }

    public Experience(int xp, boolean isPerTick, Experience.Form type) {
        this(xp, xp, 1.0, isPerTick, type);
    }

    public int getXp() {
        return this.xp;
    }

    public int getLevels() {
        return this.experienceLevel;
    }

    public void setXp(int xp) {
        this.xp = xp;
        this.experienceLevel = this.getFromExperiencePoints(this.xp);
    }

    public void addXp(int xp) {
        this.xp += xp;
        this.experienceLevel = this.getFromExperiencePoints(this.xp);
    }

    public int getCapacity() {
        return this.capacity;
    }

    public double getChance() {
        return this.chance;
    }

    public boolean isPerTick() {
        return this.isPerTick;
    }

    public boolean isLevels() {
        return this.type == Experience.Form.LEVEL;
    }

    public boolean isPoints() {
        return this.type == Experience.Form.POINT;
    }

    public Experience.Form getForm() {
        return this.type;
    }

    private int getXpNeededForNextLevel(int experienceLevel) {
        if (experienceLevel >= 30) {
            return 112 + (experienceLevel - 30) * 9;
        } else {
            return experienceLevel >= 15 ? 37 + (experienceLevel - 15) * 5 : 7 + experienceLevel * 2;
        }
    }

    private int getFromExperiencePoints(int xp) {
        int experienceLevel = 0;
        xp = Mth.clamp(xp, 0, Integer.MAX_VALUE);
        float experienceProgress = (float) xp / (float) this.getXpNeededForNextLevel(experienceLevel);
        while (experienceProgress < 0.0F) {
            float f = experienceProgress * (float) this.getXpNeededForNextLevel(experienceLevel);
            if (experienceLevel > 0) {
                experienceLevel--;
                experienceProgress = 1.0F + f / (float) this.getXpNeededForNextLevel(experienceLevel);
            } else {
                experienceLevel--;
                experienceProgress = 0.0F;
            }
        }
        while (experienceProgress >= 1.0F) {
            experienceProgress = (experienceProgress - 1.0F) * (float) this.getXpNeededForNextLevel(experienceLevel);
            experienceLevel++;
            experienceProgress /= (float) this.getXpNeededForNextLevel(experienceLevel);
        }
        return experienceLevel;
    }

    public static enum Form {

        LEVEL, POINT;

        public boolean isLevel() {
            return this == LEVEL;
        }

        public boolean isPoint() {
            return this == POINT;
        }
    }
}