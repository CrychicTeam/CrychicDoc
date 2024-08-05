package net.minecraft.world.level;

import net.minecraft.world.level.dimension.DimensionType;

public interface LevelTimeAccess extends LevelReader {

    long dayTime();

    default float getMoonBrightness() {
        return DimensionType.MOON_BRIGHTNESS_PER_PHASE[this.m_6042_().moonPhase(this.dayTime())];
    }

    default float getTimeOfDay(float float0) {
        return this.m_6042_().timeOfDay(this.dayTime());
    }

    default int getMoonPhase() {
        return this.m_6042_().moonPhase(this.dayTime());
    }
}