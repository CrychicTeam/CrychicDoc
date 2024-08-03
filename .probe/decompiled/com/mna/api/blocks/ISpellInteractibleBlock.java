package com.mna.api.blocks;

import com.mna.api.spells.base.ISpellDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public interface ISpellInteractibleBlock<B extends Block> {

    boolean onHitBySpell(Level var1, BlockPos var2, ISpellDefinition var3);
}