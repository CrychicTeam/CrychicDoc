package com.github.alexmodguy.alexscaves.server.block;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CycadBlock extends BushBlock implements BonemealableBlock {

    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    public static final VoxelShape SHAPE_TOP = buildShape(Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0), Block.box(5.0, 8.0, 5.0, 11.0, 16.0, 11.0), Block.box(0.0, 8.0, 0.0, 16.0, 10.0, 16.0));

    public CycadBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).dynamicShape().strength(1.0F, 2.0F).sound(SoundType.WOOD).offsetType(BlockBehaviour.OffsetType.XZ));
        this.m_49959_((BlockState) this.m_49966_().m_61124_(TOP, true));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos blockPos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos blockPos) {
        return 1.0F;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.m_60824_(getter, pos);
        VoxelShape shape = state.m_61143_(TOP) ? SHAPE_TOP : SHAPE;
        return shape.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public long getSeed(BlockState blockState, BlockPos pos) {
        return Mth.getSeed(pos.m_123341_(), 0, pos.m_123343_());
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.m_60734_() == this;
    }

    @Override
    public float getMaxHorizontalOffset() {
        return 0.2F;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        BlockState prev = super.updateShape(state, direction, state1, levelAccessor, blockPos, blockPos1);
        if (prev.m_60734_() == this) {
            if (levelAccessor.m_8055_(blockPos.above()).m_60734_() == this) {
                prev = (BlockState) prev.m_61124_(TOP, false);
            } else {
                prev = (BlockState) prev.m_61124_(TOP, true);
            }
        }
        return prev;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        BlockState above = levelaccessor.m_8055_(blockpos.above());
        return (BlockState) this.m_49966_().m_61124_(TOP, above.m_60734_() != this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(TOP);
    }

    private static VoxelShape buildShape(VoxelShape... from) {
        return (VoxelShape) Stream.of(from).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos blockPos, BlockState blockState, boolean idk) {
        if (!(Boolean) blockState.m_61143_(TOP)) {
            return false;
        } else {
            int size = 0;
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            mutable.set(blockPos);
            while (level.m_8055_(mutable).m_60713_(this) && mutable.m_123342_() > level.getMinBuildHeight()) {
                mutable.move(0, -1, 0);
                size++;
            }
            return size < 4;
        }
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState state) {
        return randomSource.nextBoolean();
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource randomSource, BlockPos blockPos, BlockState state) {
        if (level.m_8055_(blockPos.above()).m_247087_()) {
            level.m_46597_(blockPos, (BlockState) state.m_61124_(TOP, false));
            level.m_46597_(blockPos.above(), (BlockState) state.m_61124_(TOP, true));
        }
    }
}