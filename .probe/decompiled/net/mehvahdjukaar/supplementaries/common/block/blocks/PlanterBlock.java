package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.function.BiConsumer;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlanterBlock extends WaterBlock {

    protected static final VoxelShape SHAPE = Shapes.or(Shapes.box(0.125, 0.0, 0.125, 0.875, 0.687, 0.875), Shapes.box(0.0, 0.687, 0.0, 1.0, 1.0, 1.0));

    protected static final VoxelShape SHAPE_C = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 0.9375, 1.0));

    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;

    public PlanterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(EXTENDED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE_C;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EXTENDED, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) super.getStateForPlacement(context).m_61124_(EXTENDED, this.canConnect(context.m_43725_(), context.getClickedPos()));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        return facing == Direction.UP ? (BlockState) stateIn.m_61124_(EXTENDED, this.canConnect(worldIn, currentPos)) : stateIn;
    }

    private boolean canConnect(LevelAccessor world, BlockPos pos) {
        BlockPos up = pos.above();
        BlockState state = world.m_8055_(up);
        Block b = state.m_60734_();
        VoxelShape shape = state.m_60808_(world, up);
        boolean connect = !shape.isEmpty() && shape.bounds().minY < 0.06;
        return connect && !(b instanceof StemBlock) && !(b instanceof CropBlock);
    }

    public boolean onTreeGrow(BlockState state, LevelReader level, BiConsumer<BlockPos, BlockState> placeFunction, RandomSource randomSource, BlockPos pos, TreeConfiguration config) {
        if ((Boolean) CommonConfigs.Building.PLANTER_BREAKS.get()) {
            placeFunction.accept(pos, Blocks.ROOTED_DIRT.defaultBlockState());
            if (level instanceof Level l) {
                l.m_46796_(2001, pos.below(), Block.getId(state));
                l.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 0.71F);
            }
            return true;
        } else {
            return false;
        }
    }
}