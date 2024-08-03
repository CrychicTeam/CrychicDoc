package net.mehvahdjukaar.supplementaries.mixins.forge.self;

import net.mehvahdjukaar.supplementaries.common.block.blocks.SafeBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SafeBlockTile;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ SafeBlock.class })
public abstract class SelfSafeMixin extends Block {

    protected SelfSafeMixin(BlockBehaviour.Properties arg) {
        super(arg);
    }

    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return CommonConfigs.Functional.SAFE_UNBREAKABLE.get() && world.getBlockEntity(pos) instanceof SafeBlockTile tile && !tile.canPlayerOpen(player, true) ? false : super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }
}