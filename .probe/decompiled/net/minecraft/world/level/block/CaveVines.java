package net.minecraft.world.level.block;

import java.util.function.ToIntFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface CaveVines {

    VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    BooleanProperty BERRIES = BlockStateProperties.BERRIES;

    static InteractionResult use(@Nullable Entity entity0, BlockState blockState1, Level level2, BlockPos blockPos3) {
        if ((Boolean) blockState1.m_61143_(BERRIES)) {
            Block.popResource(level2, blockPos3, new ItemStack(Items.GLOW_BERRIES, 1));
            float $$4 = Mth.randomBetween(level2.random, 0.8F, 1.2F);
            level2.playSound(null, blockPos3, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, $$4);
            BlockState $$5 = (BlockState) blockState1.m_61124_(BERRIES, false);
            level2.setBlock(blockPos3, $$5, 2);
            level2.m_220407_(GameEvent.BLOCK_CHANGE, blockPos3, GameEvent.Context.of(entity0, $$5));
            return InteractionResult.sidedSuccess(level2.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    static boolean hasGlowBerries(BlockState blockState0) {
        return blockState0.m_61138_(BERRIES) && (Boolean) blockState0.m_61143_(BERRIES);
    }

    static ToIntFunction<BlockState> emission(int int0) {
        return p_181216_ -> p_181216_.m_61143_(BlockStateProperties.BERRIES) ? int0 : 0;
    }
}