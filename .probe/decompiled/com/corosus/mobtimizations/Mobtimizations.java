package com.corosus.mobtimizations;

import com.corosus.coroutil.util.CU;
import com.corosus.mobtimizations.config.ConfigFeatures;
import com.corosus.mobtimizations.config.ConfigFeaturesCustomization;
import com.corosus.modconfig.ConfigMod;
import com.mojang.logging.LogUtils;
import java.io.File;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.slf4j.Logger;

public class Mobtimizations {

    public static final String MODID = "mobtimizations";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean modActive = true;

    public static boolean testSpawningActive = false;

    private static int cancels = 0;

    public Mobtimizations() {
        new File("./config/mobtimizations").mkdirs();
        ConfigMod.addConfigFile("mobtimizations", new ConfigFeatures());
        ConfigMod.addConfigFile("mobtimizations", new ConfigFeaturesCustomization());
    }

    public static int getCancels() {
        return cancels;
    }

    public static void incCancel() {
        cancels++;
        if (cancels == Integer.MAX_VALUE) {
            cancels = 0;
        }
    }

    public static boolean rollPercentChance(float percent) {
        return percent == 0.0F ? false : CU.rand().nextFloat() <= percent / 100.0F;
    }

    public static boolean canAvoidHazards(Mob mob) {
        if (!modActive) {
            return true;
        } else {
            return ConfigFeatures.optimizationMonsterHazardAvoidingPathfollowing ? !(mob instanceof Monster) : true;
        }
    }

    public static boolean canCrushEggs() {
        if (!modActive) {
            return true;
        } else {
            return ConfigFeatures.optimizationZombieSearchAndDestroyTurtleEgg ? rollPercentChance((float) ConfigFeaturesCustomization.zombieSearchAndDestroyTurtleEggPercentChance) : true;
        }
    }

    public static boolean canRecomputePath() {
        return !modActive ? true : !ConfigFeatures.optimizationMobRepathfinding;
    }

    public static boolean canVillageRaid() {
        if (!modActive) {
            return true;
        } else {
            return ConfigFeatures.optimizationZombieVillageRaid ? rollPercentChance((float) ConfigFeaturesCustomization.zombieVillageRaidPercentChance) : true;
        }
    }

    public static boolean canTarget(Mob mob) {
        if (!modActive) {
            return true;
        } else if (ConfigFeatures.optimizationMobEnemyTargeting) {
            return useReducedRates(mob) ? rollPercentChance((float) ConfigFeaturesCustomization.mobEnemyTargetingReducedRatePercentChance) : true;
        } else {
            return true;
        }
    }

    public static boolean canWander(Mob mob) {
        if (!modActive) {
            return true;
        } else if (ConfigFeatures.optimizationMobWandering) {
            if (!rollPercentChance((float) ConfigFeaturesCustomization.mobWanderingPercentChance)) {
                return false;
            } else {
                float multiplier = useReducedRates(mob) ? (float) ConfigFeaturesCustomization.mobWanderingReducedRateMultiplier : 1.0F;
                long lastWander = ((MobtimizationEntityFields) mob).getlastWanderTime();
                if ((float) lastWander + (float) ConfigFeaturesCustomization.mobWanderingDelay * multiplier > (float) mob.m_9236_().getGameTime()) {
                    return false;
                } else {
                    ((MobtimizationEntityFields) mob).setlastWanderTime(mob.m_9236_().getGameTime());
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    public static boolean useReducedRates(Mob mob) {
        if (!ConfigFeatures.playerProximityReducedRate) {
            return false;
        } else {
            long lastPlayerScan = ((MobtimizationEntityFields) mob).getlastPlayerScanTime();
            if (lastPlayerScan + (long) ConfigFeaturesCustomization.playerProximityReducedRatePlayerScanRate > mob.m_9236_().getGameTime()) {
                return ((MobtimizationEntityFields) mob).isplayerInRange();
            } else {
                boolean playerInRangeBool = checkIfPlayerInRange(mob);
                ((MobtimizationEntityFields) mob).setplayerInRange(playerInRangeBool);
                ((MobtimizationEntityFields) mob).setlastPlayerScanTime(mob.m_9236_().getGameTime());
                return !playerInRangeBool;
            }
        }
    }

    private static boolean checkIfPlayerInRange(Mob mob) {
        for (Player player : mob.m_9236_().m_6907_()) {
            if (player.m_20280_(mob) < (double) (ConfigFeaturesCustomization.playerProximityReducedRateRangeCutoff * ConfigFeaturesCustomization.playerProximityReducedRateRangeCutoff)) {
                return true;
            }
        }
        return false;
    }

    public static BlockPathTypes getBlockPathTypeStatic(BlockGetter blockGetter0, BlockPos.MutableBlockPos blockPosMutableBlockPos1) {
        int i = blockPosMutableBlockPos1.m_123341_();
        int j = blockPosMutableBlockPos1.m_123342_();
        int k = blockPosMutableBlockPos1.m_123343_();
        BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeRaw(blockGetter0, blockPosMutableBlockPos1);
        if (blockpathtypes == BlockPathTypes.OPEN && j >= blockGetter0.m_141937_() + 1) {
            BlockPathTypes blockpathtypes1 = WalkNodeEvaluator.getBlockPathTypeRaw(blockGetter0, blockPosMutableBlockPos1.set(i, j - 1, k));
            blockpathtypes = blockpathtypes1 != BlockPathTypes.WALKABLE && blockpathtypes1 != BlockPathTypes.OPEN && blockpathtypes1 != BlockPathTypes.WATER && blockpathtypes1 != BlockPathTypes.LAVA ? BlockPathTypes.WALKABLE : BlockPathTypes.OPEN;
            if (blockpathtypes1 == BlockPathTypes.DAMAGE_FIRE) {
                blockpathtypes = BlockPathTypes.DAMAGE_FIRE;
            }
            if (blockpathtypes1 == BlockPathTypes.DAMAGE_OTHER) {
                blockpathtypes = BlockPathTypes.DAMAGE_OTHER;
            }
            if (blockpathtypes1 == BlockPathTypes.STICKY_HONEY) {
                blockpathtypes = BlockPathTypes.STICKY_HONEY;
            }
            if (blockpathtypes1 == BlockPathTypes.POWDER_SNOW) {
                blockpathtypes = BlockPathTypes.DANGER_POWDER_SNOW;
            }
            if (blockpathtypes1 == BlockPathTypes.DAMAGE_CAUTIOUS) {
                blockpathtypes = BlockPathTypes.DAMAGE_CAUTIOUS;
            }
        }
        return blockpathtypes;
    }
}