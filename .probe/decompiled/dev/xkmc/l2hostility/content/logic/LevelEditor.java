package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.init.data.LHConfig;

public record LevelEditor(DifficultyLevel difficulty, int extra) {

    public boolean setBase(int level) {
        int old = this.difficulty().level;
        this.difficulty().level = level;
        this.difficulty().experience = 0L;
        return level != old;
    }

    public boolean addBase(int level) {
        int old = this.difficulty().level;
        this.difficulty().level = Math.min(LHConfig.COMMON.maxPlayerLevel.get(), Math.max(0, this.difficulty().level + level));
        if (level < 0) {
            this.difficulty().experience = 0L;
        }
        return this.difficulty().level != old;
    }

    public int getBase() {
        return this.difficulty().level;
    }

    public boolean setTotal(int level) {
        return this.addTotal(level - this.getTotal());
    }

    public boolean addTotal(int level) {
        int old = this.difficulty().extraLevel;
        this.difficulty().extraLevel += level;
        return old != this.difficulty().extraLevel;
    }

    public int getTotal() {
        return this.difficulty().getLevel() + this.extra;
    }
}