package com.corosus.coroutil.util;

import net.minecraft.world.level.Level;

public class CoroUtilWorldTime {

    public static int getDayLength() {
        return 24000;
    }

    public static boolean isNight(Level world) {
        long timeMod = world.getGameTime() % (long) getDayLength();
        return timeMod >= (long) getNightFirstTick() && timeMod <= (long) getDayFirstTick();
    }

    public static boolean isNightPadded(Level world) {
        return isNightPadded(world, 5);
    }

    public static boolean isNightPadded(Level world, int padding) {
        long timeMod = world.getGameTime() % (long) getDayLength();
        return timeMod >= (long) (getNightFirstTick() + padding) && timeMod <= (long) (getDayFirstTick() - padding);
    }

    public static int getNightFirstTick() {
        return 12542;
    }

    public static int getDayFirstTick() {
        return 23460;
    }
}