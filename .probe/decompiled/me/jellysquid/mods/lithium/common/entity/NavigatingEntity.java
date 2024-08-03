package me.jellysquid.mods.lithium.common.entity;

import net.minecraft.world.entity.ai.navigation.PathNavigation;

public interface NavigatingEntity {

    boolean isRegisteredToWorld();

    void setRegisteredToWorld(PathNavigation var1);

    PathNavigation getRegisteredNavigation();

    void updateNavigationRegistration();
}