package com.simibubi.create.foundation.block.render;

import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface MultiPosDestructionHandler {

    @Nullable
    Set<BlockPos> getExtraPositions(ClientLevel var1, BlockPos var2, BlockState var3, int var4);
}