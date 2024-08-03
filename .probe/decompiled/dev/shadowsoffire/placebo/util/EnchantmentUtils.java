package dev.shadowsoffire.placebo.util;

import net.minecraft.world.entity.player.Player;

public class EnchantmentUtils {

    public static boolean chargeExperience(Player player, int cost) {
        int playerExperience = getExperience(player);
        if (playerExperience >= cost) {
            player.giveExperiencePoints(-cost);
            if (getExperience(player) <= 0) {
                player.experienceProgress = 0.0F;
            }
            return true;
        } else {
            return false;
        }
    }

    public static int getExperience(Player player) {
        int exp = getTotalExperienceForLevel(player.experienceLevel);
        return (int) ((float) exp + player.experienceProgress * (float) getTotalExperienceForLevel(player.experienceLevel + 1));
    }

    public static int getExperienceForLevel(int level) {
        if (level == 0) {
            return 0;
        } else if (level > 30) {
            return 112 + (level - 31) * 9;
        } else {
            return level > 15 ? 37 + (level - 16) * 5 : 7 + (level - 1) * 2;
        }
    }

    public static int getExperienceDifference(int start, int target) {
        if (target < start || start < 0) {
            throw new IllegalArgumentException("Invalid start/target");
        } else if (target == start) {
            return 0;
        } else {
            int expReq = 0;
            for (int lvl = start + 1; lvl <= target; lvl++) {
                expReq += getExperienceForLevel(lvl);
            }
            return expReq;
        }
    }

    public static int getTotalExperienceForLevel(int level) {
        return getExperienceDifference(0, level);
    }

    public static int getLevelForExperience(int experience) {
        int level = 0;
        while (true) {
            int xpToNextLevel = getExperienceForLevel(level + 1);
            if (experience < xpToNextLevel) {
                return level;
            }
            level++;
            experience -= xpToNextLevel;
        }
    }
}