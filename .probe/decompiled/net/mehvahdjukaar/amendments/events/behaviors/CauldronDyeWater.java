package net.mehvahdjukaar.amendments.events.behaviors;

import net.mehvahdjukaar.amendments.common.block.DyeCauldronBlock;
import net.mehvahdjukaar.amendments.common.item.DyeBottleItem;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CauldronDyeWater implements BlockUse {

    @Override
    public boolean isEnabled() {
        return (Boolean) CommonConfigs.DYE_WATER.get();
    }

    @Override
    public boolean appliesToBlock(Block block) {
        return Blocks.WATER_CAULDRON == block;
    }

    @Override
    public InteractionResult tryPerformingAction(BlockState state, BlockPos pos, Level level, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (state.m_60713_(Blocks.WATER_CAULDRON) && stack.getItem() instanceof DyeItem dye) {
            Integer l = (Integer) state.m_61143_(LayeredCauldronBlock.LEVEL);
            level.setBlockAndUpdate(pos, (BlockState) ((Block) ModRegistry.DYE_CAULDRON.get()).defaultBlockState().m_61124_(DyeCauldronBlock.LEVEL, l));
            if (level.getBlockEntity(pos) instanceof LiquidCauldronBlockTile te) {
                te.getSoftFluidTank().setFluid(DyeBottleItem.toFluidStack(dye.getDyeColor(), l));
            }
            DyeCauldronBlock.playDyeSoundAndConsume(state, pos, level, player, stack);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}