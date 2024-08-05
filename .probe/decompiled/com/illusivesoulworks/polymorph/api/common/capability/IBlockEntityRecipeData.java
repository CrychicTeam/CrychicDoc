package com.illusivesoulworks.polymorph.api.common.capability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IBlockEntityRecipeData extends IRecipeData<BlockEntity> {

    void tick();

    void addListener(ServerPlayer var1);

    void removeListener(ServerPlayer var1);
}