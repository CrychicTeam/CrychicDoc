package net.mehvahdjukaar.supplementaries.mixins.forge.self;

import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FrameBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ FrameBlock.class })
public abstract class SelfFrameMixin extends Block {

    protected SelfFrameMixin(BlockBehaviour.Properties arg) {
        super(arg);
    }

    public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
        return world.m_7702_(pos) instanceof FrameBlockTile tile ? tile.getHeldBlock().getEnchantPowerBonus(world, pos) : 0.0F;
    }
}