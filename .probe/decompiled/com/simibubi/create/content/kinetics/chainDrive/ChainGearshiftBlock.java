package com.simibubi.create.content.kinetics.chainDrive;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class ChainGearshiftBlock extends ChainDriveBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ChainGearshiftBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED));
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.m_6807_(state, worldIn, pos, oldState, isMoving);
        if (oldState.m_60734_() != state.m_60734_()) {
            this.withBlockEntityDo(worldIn, pos, kbe -> ((ChainGearshiftBlockEntity) kbe).neighbourChanged());
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) super.getStateForPlacement(context).m_61124_(POWERED, context.m_43725_().m_276867_(context.getClickedPos()));
    }

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return super.areStatesKineticallyEquivalent(oldState, newState) && oldState.m_61143_(POWERED) == newState.m_61143_(POWERED);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            this.withBlockEntityDo(worldIn, pos, kbe -> ((ChainGearshiftBlockEntity) kbe).neighbourChanged());
            boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
            if (previouslyPowered != worldIn.m_276867_(pos)) {
                worldIn.setBlock(pos, (BlockState) state.m_61122_(POWERED), 18);
            }
        }
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends KineticBlockEntity>) AllBlockEntityTypes.ADJUSTABLE_CHAIN_GEARSHIFT.get();
    }
}