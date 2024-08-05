package net.mehvahdjukaar.amendments.integration;

import net.mehvahdjukaar.amendments.common.CakeRegistry;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.tag.ModTags;

public class FarmersDelightCompat {

    public static InteractionResult onCakeInteract(BlockState state, BlockPos pos, Level level, @NotNull ItemStack stack) {
        if (stack.is(ModTags.KNIVES)) {
            int bites = (Integer) state.m_61143_(CakeBlock.BITES);
            if (bites < 6) {
                level.setBlock(pos, (BlockState) state.m_61124_(CakeBlock.BITES, bites + 1), 3);
            } else if (state.m_60713_((Block) ModRegistry.DOUBLE_CAKES.get(CakeRegistry.VANILLA))) {
                level.setBlock(pos, Blocks.CAKE.defaultBlockState(), 3);
            } else {
                level.removeBlock(pos, false);
            }
            Containers.dropItemStack(level, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), new ItemStack((ItemLike) CompatObjects.CAKE_SLICE.get()));
            level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}