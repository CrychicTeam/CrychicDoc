package noobanidus.mods.lootr.block.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import noobanidus.mods.lootr.init.ModBlockEntities;

public class LootrTrappedChestBlockEntity extends LootrChestBlockEntity {

    protected LootrTrappedChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public LootrTrappedChestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(ModBlockEntities.LOOTR_TRAPPED_CHEST.get(), pWorldPosition, pBlockState);
    }

    @Override
    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int int0, int int1) {
        super.m_142151_(level, pos, state, int0, int1);
        if (int0 != int1) {
            Block block = state.m_60734_();
            level.updateNeighborsAt(pos, block);
            level.updateNeighborsAt(pos.below(), block);
        }
    }
}