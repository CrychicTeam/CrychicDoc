package com.simibubi.create.content.kinetics.waterwheel;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WaterWheelBlock extends DirectionalKineticBlock implements IBE<WaterWheelBlockEntity> {

    public WaterWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        for (Direction direction : Iterate.directions) {
            BlockPos neighbourPos = pos.relative(direction);
            BlockState neighbourState = worldIn.m_8055_(neighbourPos);
            if (AllBlocks.WATER_WHEEL.has(neighbourState)) {
                Direction.Axis axis = ((Direction) state.m_61143_(FACING)).getAxis();
                if (((Direction) neighbourState.m_61143_(FACING)).getAxis() != axis || axis != direction.getAxis()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return this.onBlockEntityUse(pLevel, pPos, wwt -> wwt.applyMaterialIfValid(pPlayer.m_21120_(pHand)));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (worldIn instanceof WrappedWorld) {
            return stateIn;
        } else if (worldIn.m_5776_()) {
            return stateIn;
        } else {
            if (!worldIn.getBlockTicks().m_183582_(currentPos, this)) {
                worldIn.scheduleTick(currentPos, this, 1);
            }
            return stateIn;
        }
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.m_6807_(state, worldIn, pos, oldState, isMoving);
        if (!worldIn.isClientSide()) {
            if (!worldIn.m_183326_().m_183582_(pos, this)) {
                worldIn.m_186460_(pos, this, 1);
            }
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        this.withBlockEntityDo(pLevel, pPos, WaterWheelBlockEntity::determineAndApplyFlowScore);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        state.m_61124_(FACING, Direction.get(Direction.AxisDirection.POSITIVE, ((Direction) state.m_61143_(FACING)).getAxis()));
        return state;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return ((Direction) state.m_61143_(FACING)).getAxis() == face.getAxis();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(FACING)).getAxis();
    }

    @Override
    public float getParticleTargetRadius() {
        return 1.125F;
    }

    @Override
    public float getParticleInitialRadius() {
        return 1.0F;
    }

    @Override
    public boolean hideStressImpact() {
        return true;
    }

    @Override
    public Class<WaterWheelBlockEntity> getBlockEntityClass() {
        return WaterWheelBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends WaterWheelBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends WaterWheelBlockEntity>) AllBlockEntityTypes.WATER_WHEEL.get();
    }

    public static Couple<Integer> getSpeedRange() {
        return Couple.create(8, 8);
    }
}