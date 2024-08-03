package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.FluidExtractorBlockEntity;
import com.rekindled.embers.blockentity.FluidPipeBlockEntityBase;
import com.rekindled.embers.datagen.EmbersBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidExtractorBlock extends ExtractorBlockBase {

    public FluidExtractorBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.FLUID_EXTRACTOR_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.FLUID_EXTRACTOR_ENTITY.get(), FluidPipeBlockEntityBase::clientTick) : m_152132_(pBlockEntityType, RegistryManager.FLUID_EXTRACTOR_ENTITY.get(), FluidExtractorBlockEntity::serverTick);
    }

    @Override
    public TagKey<Block> getConnectionTag() {
        return EmbersBlockTags.FLUID_PIPE_CONNECTION;
    }

    @Override
    public TagKey<Block> getToggleConnectionTag() {
        return EmbersBlockTags.FLUID_PIPE_CONNECTION_TOGGLEABLE;
    }

    @Override
    public boolean connectToTile(BlockEntity blockEntity, Direction direction) {
        return blockEntity != null && blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite()).isPresent();
    }

    @Override
    public boolean unclog(BlockEntity blockEntity, Level level, BlockPos pos) {
        if (blockEntity instanceof FluidPipeBlockEntityBase pipeEntity && pipeEntity.clogged) {
            IFluidHandler handler = (IFluidHandler) blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
            handler.drain(handler.getTankCapacity(0), IFluidHandler.FluidAction.EXECUTE);
            level.updateNeighbourForOutputSignal(pos, this);
            return true;
        }
        return false;
    }
}