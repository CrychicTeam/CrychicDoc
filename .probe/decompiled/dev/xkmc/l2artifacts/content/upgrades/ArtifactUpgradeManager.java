package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.StatEntry;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import javax.annotation.Nullable;
import net.minecraft.util.RandomSource;

public class ArtifactUpgradeManager {

    public static int getExpForLevel(int rank, int level) {
        double rank_factor = ArtifactConfig.COMMON.expConsumptionRankFactor.get();
        double level_factor = ArtifactConfig.COMMON.expLevelFactor.get();
        double base = (double) ArtifactConfig.COMMON.baseExpConsumption.get().intValue();
        return (int) Math.round(base * Math.pow(level_factor, (double) level) * Math.pow(rank_factor, (double) (rank - 1)));
    }

    public static int getExpForConversion(int rank, @Nullable ArtifactStats stat) {
        int base = ArtifactConfig.COMMON.baseExpConversion.get();
        double base_factor = ArtifactConfig.COMMON.expConversionRankFactor.get();
        double retention = ArtifactConfig.COMMON.expRetention.get();
        double base_exp = (double) base * Math.pow(base_factor, (double) (rank - 1));
        if (stat == null) {
            return (int) Math.round(base_exp);
        } else {
            double used_exp = (double) stat.exp;
            for (int i = 0; i < stat.level; i++) {
                used_exp += (double) getExpForLevel(stat.rank, i);
            }
            return (int) Math.round(base_exp + used_exp * retention);
        }
    }

    public static int getMaxLevel(int rank) {
        return rank * ArtifactConfig.COMMON.maxLevelPerRank.get();
    }

    public static void onUpgrade(ArtifactStats stats, int lv, Upgrade upgrade, RandomSource random) {
        int gate = ArtifactConfig.COMMON.levelPerSubStat.get();
        stats.add(stats.main_stat.type, stats.main_stat.getType().getMainValue(random, upgrade.removeMain()));
        if (lv % gate == 0 && stats.sub_stats.size() > 0) {
            StatEntry substat = null;
            if (upgrade.stats.size() > 0) {
                for (StatEntry entry : stats.sub_stats) {
                    if (entry.type.equals(upgrade.stats.get(0))) {
                        upgrade.stats.remove(0);
                        substat = entry;
                        break;
                    }
                }
            }
            if (substat == null) {
                substat = (StatEntry) stats.sub_stats.get(random.nextInt(stats.sub_stats.size()));
            }
            stats.add(substat.type, substat.getType().getSubValue(random, upgrade.removeSub()));
        }
    }
}