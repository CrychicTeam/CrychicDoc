package com.rekindled.embers.api.block;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IDial {

    List<Component> getDisplayInfo(Level var1, BlockPos var2, BlockState var3, int var4);

    void updateBEData(BlockPos var1, int var2);

    String getDialType();
}