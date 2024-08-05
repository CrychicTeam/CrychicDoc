package com.mna.api;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IPortalHelper {

    void openPortal(ServerLevel var1, Vec3 var2, DyeColor var3, BlockPos var4, ResourceKey<Level> var5, boolean var6);
}