package net.mehvahdjukaar.supplementaries.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

class PancakeDiscBehavior extends OptionalDispenseItemBehavior {

    @NotNull
    @Override
    protected ItemStack execute(BlockSource source, @NotNull ItemStack stack) {
        Direction dir = (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING);
        BlockPos pos = source.getPos().relative(dir);
        Level level = source.getLevel();
        BlockState state = level.getBlockState(pos);
        if (state.m_60734_() == Blocks.JUKEBOX && level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox) {
            ItemStack currentRecord = jukebox.m_272036_();
            jukebox.m_272287_(stack.copy());
            return currentRecord;
        } else {
            return super.m_7498_(source, stack);
        }
    }
}