package com.corosus.mobtimizations.config;

import com.corosus.modconfig.ConfigComment;
import com.corosus.modconfig.IConfigCategory;
import java.io.File;

public class ConfigFeatures implements IConfigCategory {

    @ConfigComment({ "If true, optimizes the expensive zombie village raid task that scans around a lot for villagers, by default cuts the chance to 1/3" })
    public static boolean optimizationZombieVillageRaid = true;

    @ConfigComment({ "If true, optimizes the expensive mob targeting tasks" })
    public static boolean optimizationMobEnemyTargeting = true;

    @ConfigComment({ "If true, optimizes the expensive mob wander pathfinding, even more so if far from players" })
    public static boolean optimizationMobWandering = true;

    @ConfigComment({ "If true, disables the frequent pathfinds done while already following a path" })
    public static boolean optimizationMobRepathfinding = true;

    @ConfigComment({ "If true, optimizes the expensive searching done to find a turtle egg to crush, for zombies/husks, by default fully disabling it" })
    public static boolean optimizationZombieSearchAndDestroyTurtleEgg = true;

    @ConfigComment({ "If true, disables the expensive 3x3x3 hazard scanning that is constantly running every tick while path following, for monsters only, your pets are safe" })
    public static boolean optimizationMonsterHazardAvoidingPathfollowing = true;

    @ConfigComment({ "If true, wander pathfinding and enemy targeting attempts will be further reduced when far away from players" })
    public static boolean playerProximityReducedRate = true;

    @Override
    public String getName() {
        return "features";
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