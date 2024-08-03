package fr.frinn.custommachinery.common.util;

import net.minecraft.world.entity.player.Player;

public class ExperienceUtils {

    public static int getXpNeededForNextLevel(int currentLevel) {
        if (currentLevel >= 30) {
            return 112 + (currentLevel - 30) * 9;
        } else {
            return currentLevel >= 15 ? 37 + (currentLevel - 15) * 5 : 7 + currentLevel * 2;
        }
    }

    public static int getXpFromLevel(int level) {
        if (level >= 32) {
            return (int) (4.5 * Math.pow((double) level, 2.0) - 162.5 * (double) level + 2220.0);
        } else {
            return level >= 17 ? (int) (2.5 * Math.pow((double) level, 2.0) - 40.5 * (double) level + 360.0) : (int) (Math.pow((double) level, 2.0) + (double) (6L * (long) level));
        }
    }

    public static int getLevelFromXp(long experience) {
        if (experience >= 1508L) {
            return (int) (18.055555555555557 + Math.sqrt(0.2222222222222222 * ((double) experience - 752.9861111111111)));
        } else {
            return experience >= 353L ? (int) (8.1 + Math.sqrt(0.4 * ((double) experience - 195.975))) : (int) (Math.sqrt((double) (experience + 9L)) - 3.0);
        }
    }

    public static int getPlayerTotalXp(Player player) {
        return getXpFromLevel(player.experienceLevel) + (int) Math.floor((double) (player.experienceProgress * (float) getXpNeededForNextLevel(player.experienceLevel)));
    }
}