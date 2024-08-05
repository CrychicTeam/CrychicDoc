package com.simibubi.create.content.kinetics.fan;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.logistics.chute.AbstractChuteBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class EncasedFanBlock extends DirectionalKineticBlock implements IBE<EncasedFanBlockEntity> {

    public EncasedFanBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.m_6807_(state, worldIn, pos, oldState, isMoving);
        this.blockUpdate(state, worldIn, pos);
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState stateIn, LevelAccessor worldIn, BlockPos pos, int flags, int count) {
        super.m_7742_(stateIn, worldIn, pos, flags, count);
        this.blockUpdate(stateIn, worldIn, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        this.blockUpdate(state, worldIn, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.m_43725_();
        BlockPos pos = context.getClickedPos();
        Direction face = context.m_43719_();
        BlockState placedOn = world.getBlockState(pos.relative(face.getOpposite()));
        BlockState placedOnOpposite = world.getBlockState(pos.relative(face));
        if (AbstractChuteBlock.isChute(placedOn)) {
            return (BlockState) this.m_49966_().m_61124_(FACING, face.getOpposite());
        } else if (AbstractChuteBlock.isChute(placedOnOpposite)) {
            return (BlockState) this.m_49966_().m_61124_(FACING, face);
        } else {
            Direction preferredFacing = this.getPreferredFacing(context);
            if (preferredFacing == null) {
                preferredFacing = context.getNearestLookingDirection();
            }
            return (BlockState) this.m_49966_().m_61124_(FACING, context.m_43723_() != null && context.m_43723_().m_6144_() ? preferredFacing : preferredFacing.getOpposite());
        }
    }

    protected void blockUpdate(BlockState state, LevelAccessor worldIn, BlockPos pos) {
        if (!(worldIn instanceof WrappedWorld)) {
            this.notifyFanBlockEntity(worldIn, pos);
        }
    }

    protected void notifyFanBlockEntity(LevelAccessor world, BlockPos pos) {
        this.withBlockEntityDo(world, pos, EncasedFanBlockEntity::blockInFrontChanged);
    }

    @Override
    public BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
        this.blockUpdate(newState, context.getLevel(), context.getClickedPos());
        return newState;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(FACING)).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == ((Direction) state.m_61143_(FACING)).getOpposite();
    }

    @Override
    public Class<EncasedFanBlockEntity> getBlockEntityClass() {
        return EncasedFanBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends EncasedFanBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends EncasedFanBlockEntity>) AllBlockEntityTypes.ENCASED_FAN.get();
    }
}