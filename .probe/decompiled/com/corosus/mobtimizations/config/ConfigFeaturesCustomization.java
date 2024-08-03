package com.corosus.mobtimizations.config;

import com.corosus.modconfig.ConfigComment;
import com.corosus.modconfig.IConfigCategory;
import java.io.File;

public class ConfigFeaturesCustomization implements IConfigCategory {

    @ConfigComment({ "Percent chance to run for task: zombie village raid task" })
    public static int zombieVillageRaidPercentChance = 33;

    @ConfigComment({ "Percent chance to run for task: zombie seeking turtle eggs to destroy" })
    public static int zombieSearchAndDestroyTurtleEggPercentChance = 0;

    @ConfigComment({ "In addition to the timed delay, an additional percent chance to run for task: mob wander pathfinding" })
    public static int mobWanderingPercentChance = 100;

    @ConfigComment({ "Delay in ticks between allowed mob wander pathfinds" })
    public static int mobWanderingDelay = 100;

    @ConfigComment({ "If not near a player, the delay in ticks between wander pathfinds are multiplied by this amount" })
    public static int mobWanderingReducedRateMultiplier = 6;

    @ConfigComment({ "If not near a player, we only allow give the mob this much of a percent chance to search for a target" })
    public static int mobEnemyTargetingReducedRatePercentChance = 10;

    @ConfigComment({ "If a mob is at least this far away from a player, additional reduced rates are used for the tasks mentioned in playerProximityReducedRate in features.toml" })
    public static int playerProximityReducedRateRangeCutoff = 12;

    @ConfigComment({ "How often in ticks we update our check on how close a mob is to a player" })
    public static int playerProximityReducedRatePlayerScanRate = 40;

    @Override
    public String getName() {
        return "features-customization";
    }

    @Override
    public String getRegistryName() {
        return "mobtimizations" + this.getName();
    }

    @Override
    public String getConfigFileName() {
        return "mobtimizations" + File.separator + this.getName();
    }

    @Override
    public String getCategory() {
        return "mobtimizations: " + this.getName();
    }

    @Override
    public void hookUpdatedValues() {
    }
}