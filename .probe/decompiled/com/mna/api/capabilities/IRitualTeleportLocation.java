package com.mna.api.capabilities;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public interface IRitualTeleportLocation {

    boolean matches(List<ResourceLocation> var1);

    boolean matches(BlockPos var1);

    ImmutableList<ResourceLocation> getReagents();

    Direction getDirection();

    BlockPos getPos();

    ResourceKey<Level> getWorldType();

    void tryCorrectWorldKey(ResourceKey<Level> var1);
}