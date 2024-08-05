package com.rekindled.embers.api.tile;

import com.rekindled.embers.api.filter.IFilter;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IOrderDestination {

    void order(BlockEntity var1, IFilter var2, int var3);

    void resetOrder(BlockEntity var1);
}