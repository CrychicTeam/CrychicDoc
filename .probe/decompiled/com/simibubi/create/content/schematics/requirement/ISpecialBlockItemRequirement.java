package com.simibubi.create.content.schematics.requirement;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface ISpecialBlockItemRequirement {

    ItemRequirement getRequiredItems(BlockState var1, BlockEntity var2);
}