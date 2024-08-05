package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.data.LHConfig;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PlayerFinder {

    @Nullable
    public static Player getNearestPlayer(Level level, LivingEntity le) {
        int safeZone = LHConfig.COMMON.newPlayerProtectRange.get();
        int sr = safeZone * safeZone;
        int lowLv = 0;
        Player lowPl = null;
        double nearDist = 0.0;
        Player nearPl = null;
        for (Player pl : level.m_6907_()) {
            double dist = pl.m_20280_(le);
            if (!(dist > 16384.0) && pl.m_6084_()) {
                Optional<PlayerDifficulty> plOpt = pl.getCapability(PlayerDifficulty.CAPABILITY).resolve();
                if (!plOpt.isEmpty()) {
                    int lv = ((PlayerDifficulty) plOpt.get()).getLevel().getLevel();
                    if (dist < (double) sr) {
                        if (lowPl == null || lv < lowLv) {
                            lowPl = pl;
                            lowLv = lv;
                        }
                    } else if (nearPl == null || dist < nearDist) {
                        nearPl = pl;
                        nearDist = dist;
                    }
                }
            }
        }
        return lowPl != null ? lowPl : nearPl;
    }
}