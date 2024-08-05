package com.simibubi.create.content.kinetics.waterwheel;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class LargeWaterWheelBlock extends RotatedPillarKineticBlock implements IBE<LargeWaterWheelBlockEntity> {

    public static final BooleanProperty EXTENSION = BooleanProperty.create("extension");

    public LargeWaterWheelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(EXTENSION, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(EXTENSION));
    }

    public Direction.Axis getAxisForPlacement(BlockPlaceContext context) {
        return (Direction.Axis) super.getStateForPlacement(context).m_61143_(AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        BlockPos pos = context.getClickedPos();
        Direction.Axis axis = (Direction.Axis) stateForPlacement.m_61143_(AXIS);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (axis.choose(x, y, z) == 0) {
                        BlockPos offset = new BlockPos(x, y, z);
                        if (!offset.equals(BlockPos.ZERO)) {
                            BlockState occupiedState = context.m_43725_().getBlockState(pos.offset(offset));
                            if (!occupiedState.m_247087_()) {
                                return null;
                            }
                        }
                    }
                }
            }
        }
        if (context.m_43725_().getBlockState(pos.relative(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.NEGATIVE))).m_60713_(this)) {
            stateForPlacement = (BlockState) stateForPlacement.m_61124_(EXTENSION, true);
        }
        return stateForPlacement;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return this.onBlockEntityUse(pLevel, pPos, wwt -> wwt.applyMaterialIfValid(pPlayer.m_21120_(pHand)));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return pDirection != Direction.fromAxisAndDirection((Direction.Axis) pState.m_61143_(AXIS), Direction.AxisDirection.NEGATIVE) ? pState : (BlockState) pState.m_61124_(EXTENSION, pNeighborState.m_60713_(this));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.m_6807_(state, level, pos, oldState, isMoving);
        if (!level.m_183326_().m_183582_(pos, this)) {
            level.m_186460_(pos, this, 1);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Direction.Axis axis = (Direction.Axis) pState.m_61143_(AXIS);
        for (Direction side : Iterate.directions) {
            if (side.getAxis() != axis) {
                for (boolean secondary : Iterate.falseAndTrue) {
                    Direction targetSide = secondary ? side.getClockWise(axis) : side;
                    BlockPos structurePos = (secondary ? pPos.relative(side) : pPos).relative(targetSide);
                    BlockState occupiedState = pLevel.m_8055_(structurePos);
                    BlockState requiredStructure = (BlockState) AllBlocks.WATER_WHEEL_STRUCTURAL.getDefaultState().m_61124_(WaterWheelStructuralBlock.f_52588_, targetSide.getOpposite());
                    if (occupiedState != requiredStructure) {
                        if (!occupiedState.m_247087_()) {
                            pLevel.m_46961_(pPos, false);
                            return;
                        }
                        pLevel.m_46597_(structurePos, requiredStructure);
                    }
                }
            }
        }
        this.withBlockEntityDo(pLevel, pPos, WaterWheelBlockEntity::determineAndApplyFlowScore);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntityType<? extends LargeWaterWheelBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends LargeWaterWheelBlockEntity>) AllBlockEntityTypes.LARGE_WATER_WHEEL.get();
    }

    @Override
    public Class<LargeWaterWheelBlockEntity> getBlockEntityClass() {
        return LargeWaterWheelBlockEntity.class;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == this.getRotationAxis(state);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return (Direction.Axis) state.m_61143_(AXIS);
    }

    @Override
    public float getParticleTargetRadius() {
        return 2.5F;
    }

    @Override
    public float getParticleInitialRadius() {
        return 2.25F;
    }

    public static Couple<Integer> getSpeedRange() {
        return Couple.create(4, 4);
    }
}