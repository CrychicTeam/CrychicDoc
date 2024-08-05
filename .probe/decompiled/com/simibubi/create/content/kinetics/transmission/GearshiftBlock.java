package com.simibubi.create.content.kinetics.transmission;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.AbstractEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.ticks.TickPriority;

public class GearshiftBlock extends AbstractEncasedShaftBlock implements IBE<SplitShaftBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public GearshiftBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) super.getStateForPlacement(context).m_61124_(POWERED, context.m_43725_().m_276867_(context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
            if (previouslyPowered != worldIn.m_276867_(pos)) {
                this.detachKinetics(worldIn, pos, true);
                worldIn.setBlock(pos, (BlockState) state.m_61122_(POWERED), 2);
            }
        }
    }

    @Override
    public Class<SplitShaftBlockEntity> getBlockEntityClass() {
        return SplitShaftBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SplitShaftBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends SplitShaftBlockEntity>) AllBlockEntityTypes.GEARSHIFT.get();
    }

    public void detachKinetics(Level worldIn, BlockPos pos, boolean reAttachNextTick) {
        BlockEntity be = worldIn.getBlockEntity(pos);
        if (be != null && be instanceof KineticBlockEntity) {
            RotationPropagator.handleRemoved(worldIn, pos, (KineticBlockEntity) be);
            if (reAttachNextTick) {
                worldIn.m_186464_(pos, this, 0, TickPriority.EXTREMELY_HIGH);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        BlockEntity be = worldIn.m_7702_(pos);
        if (be != null && be instanceof KineticBlockEntity kte) {
            RotationPropagator.handleAdded(worldIn, pos, kte);
        }
    }
}