package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallTorchBlock extends TorchBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final float AABB_OFFSET = 2.5F;

    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(5.5, 3.0, 11.0, 10.5, 13.0, 16.0), Direction.SOUTH, Block.box(5.5, 3.0, 0.0, 10.5, 13.0, 5.0), Direction.WEST, Block.box(11.0, 3.0, 5.5, 16.0, 13.0, 10.5), Direction.EAST, Block.box(0.0, 3.0, 5.5, 5.0, 13.0, 10.5)));

    protected WallTorchBlock(BlockBehaviour.Properties blockBehaviourProperties0, ParticleOptions particleOptions1) {
        super(blockBehaviourProperties0, particleOptions1);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public String getDescriptionId() {
        return this.m_5456_().getDescriptionId();
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return getShape(blockState0);
    }

    public static VoxelShape getShape(BlockState blockState0) {
        return (VoxelShape) AABBS.get(blockState0.m_61143_(FACING));
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        Direction $$3 = (Direction) blockState0.m_61143_(FACING);
        BlockPos $$4 = blockPos2.relative($$3.getOpposite());
        BlockState $$5 = levelReader1.m_8055_($$4);
        return $$5.m_60783_(levelReader1, $$4, $$3);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = this.m_49966_();
        LevelReader $$2 = blockPlaceContext0.m_43725_();
        BlockPos $$3 = blockPlaceContext0.getClickedPos();
        Direction[] $$4 = blockPlaceContext0.getNearestLookingDirections();
        for (Direction $$5 : $$4) {
            if ($$5.getAxis().isHorizontal()) {
                Direction $$6 = $$5.getOpposite();
                $$1 = (BlockState) $$1.m_61124_(FACING, $$6);
                if ($$1.m_60710_($$2, $$3)) {
                    return $$1;
                }
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1.getOpposite() == blockState0.m_61143_(FACING) && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : blockState0;
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        Direction $$4 = (Direction) blockState0.m_61143_(FACING);
        double $$5 = (double) blockPos2.m_123341_() + 0.5;
        double $$6 = (double) blockPos2.m_123342_() + 0.7;
        double $$7 = (double) blockPos2.m_123343_() + 0.5;
        double $$8 = 0.22;
        double $$9 = 0.27;
        Direction $$10 = $$4.getOpposite();
        level1.addParticle(ParticleTypes.SMOKE, $$5 + 0.27 * (double) $$10.getStepX(), $$6 + 0.22, $$7 + 0.27 * (double) $$10.getStepZ(), 0.0, 0.0, 0.0);
        level1.addParticle(this.f_57488_, $$5 + 0.27 * (double) $$10.getStepX(), $$6 + 0.22, $$7 + 0.27 * (double) $$10.getStepZ(), 0.0, 0.0, 0.0);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING);
    }
}