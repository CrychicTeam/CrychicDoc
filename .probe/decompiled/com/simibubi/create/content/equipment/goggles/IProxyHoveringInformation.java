package com.simibubi.create.content.equipment.goggles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IProxyHoveringInformation {

    BlockPos getInformationSource(Level var1, BlockPos var2, BlockState var3);
}