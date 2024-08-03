package com.github.alexmodguy.alexscaves.server.block;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PewenBranchBlock extends Block implements SimpleWaterloggedBlock {

    public static final Map<Integer, VoxelShape> SHAPES_BY_ROTATION = Util.make(Maps.newHashMap(), map -> {
        map.put(0, Block.box(6.0, 2.0, 0.0, 10.0, 6.0, 16.0));
        map.put(2, Block.box(0.0, 2.0, 6.0, 16.0, 6.0, 10.0));
        map.put(4, Block.box(6.0, 2.0, 0.0, 10.0, 6.0, 16.0));
        map.put(6, Block.box(0.0, 2.0, 6.0, 16.0, 6.0, 10.0));
        map.put(1, Block.box(0.0, 2.0, 0.0, 16.0, 6.0, 16.0));
        map.put(3, Block.box(0.0, 2.0, 0.0, 16.0, 6.0, 16.0));
        map.put(5, Block.box(0.0, 2.0, 0.0, 16.0, 6.0, 16.0));
        map.put(7, Block.box(0.0, 2.0, 0.0, 16.0, 6.0, 16.0));
    });

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty PINES = BooleanProperty.create("pines");

    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 7);

    public PewenBranchBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN).strength(1.0F).sound(ACSoundTypes.PEWEN_BRANCH).randomTicks());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(ROTATION, 0)).m_61124_(PINES, true));
    }

    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return state.m_61143_(PINES) ? SoundType.GRASS : super.getSoundType(state, level, pos, entity);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return (VoxelShape) SHAPES_BY_ROTATION.getOrDefault(state.m_61143_(ROTATION), Shapes.empty());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
        this.tick(state, serverLevel, pos, randomSource);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        int rot = Mth.floor((double) (context.m_7074_() * 8.0F / 360.0F) + 0.5) & 7;
        BlockPos checkPos = blockpos.offset(getOffsetConnectToPos(rot));
        for (int loops = 0; !this.isGoodBase(levelaccessor.m_8055_(checkPos), levelaccessor, checkPos) && loops < 7; loops++) {
            if (++rot > 7) {
                rot = 0;
            }
            checkPos = blockpos.offset(getOffsetConnectToPos(rot));
        }
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(ROTATION, rot)).m_61124_(PINES, this.hasPines(rot, levelaccessor, blockpos))).m_61124_(WATERLOGGED, levelaccessor.m_6425_(blockpos).getType() == Fluids.WATER);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(ROTATION, rotation.rotate((Integer) state.m_61143_(ROTATION), 8));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return (BlockState) state.m_61124_(ROTATION, mirror.mirror((Integer) state.m_61143_(ROTATION), 8));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.offset(getOffsetConnectToPos((Integer) state.m_61143_(ROTATION)));
        BlockState state1 = level.m_8055_(blockpos);
        return this.isGoodBase(state1, level, blockpos);
    }

    public boolean isGoodBase(BlockState state, LevelReader level, BlockPos pos) {
        return state.m_60734_() == this || state.m_60838_(level, pos);
    }

    public boolean hasPines(int rot, LevelReader levelReader, BlockPos pos) {
        BlockPos checkAt = pos.subtract(getOffsetConnectToPos(rot));
        return levelReader.m_8055_(checkAt).m_60734_() != this;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (!state.m_60710_(level, pos)) {
            for (BlockPos tick : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 1, 1))) {
                level.m_186460_(tick, this, 1);
            }
            level.m_46961_(pos, true);
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if (!state.m_60710_(levelAccessor, blockPos)) {
            levelAccessor.scheduleTick(blockPos, this, 1);
        }
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        boolean shouldHavePines = this.hasPines((Integer) state.m_61143_(ROTATION), levelAccessor, blockPos);
        return shouldHavePines != state.m_61143_(PINES) ? (BlockState) state.m_61124_(PINES, shouldHavePines) : super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION, WATERLOGGED, PINES);
    }

    @Override
    public float getMaxHorizontalOffset() {
        return 0.0F;
    }

    @Override
    public float getMaxVerticalOffset() {
        return 0.75F;
    }

    public static Vec3i getOffsetConnectToPos(int rotationValue) {
        switch(rotationValue) {
            case 0:
                return new Vec3i(0, 0, 1);
            case 1:
                return new Vec3i(-1, 0, 1);
            case 2:
                return new Vec3i(-1, 0, 0);
            case 3:
                return new Vec3i(-1, 0, -1);
            case 4:
                return new Vec3i(0, 0, -1);
            case 5:
                return new Vec3i(1, 0, -1);
            case 6:
                return new Vec3i(1, 0, 0);
            case 7:
                return new Vec3i(1, 0, 1);
            default:
                return Vec3i.ZERO;
        }
    }
}