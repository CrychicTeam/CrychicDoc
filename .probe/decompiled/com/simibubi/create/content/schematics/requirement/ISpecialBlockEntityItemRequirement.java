package com.simibubi.create.content.schematics.requirement;

import net.minecraft.world.level.block.state.BlockState;

public interface ISpecialBlockEntityItemRequirement {

    ItemRequirement getRequiredItems(BlockState var1);
}