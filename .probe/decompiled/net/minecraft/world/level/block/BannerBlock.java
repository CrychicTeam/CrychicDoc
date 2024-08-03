package net.minecraft.world.level.block;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BannerBlock extends AbstractBannerBlock {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    private static final Map<DyeColor, Block> BY_COLOR = Maps.newHashMap();

    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    public BannerBlock(DyeColor dyeColor0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(dyeColor0, blockBehaviourProperties1);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(ROTATION, 0));
        BY_COLOR.put(dyeColor0, this);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return levelReader1.m_8055_(blockPos2.below()).m_280296_();
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(ROTATION, RotationSegment.convertToSegment(blockPlaceContext0.m_7074_() + 180.0F));
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1 == Direction.DOWN && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(ROTATION, rotation1.rotate((Integer) blockState0.m_61143_(ROTATION), 16));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return (BlockState) blockState0.m_61124_(ROTATION, mirror1.mirror((Integer) blockState0.m_61143_(ROTATION), 16));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(ROTATION);
    }

    public static Block byColor(DyeColor dyeColor0) {
        return (Block) BY_COLOR.getOrDefault(dyeColor0, Blocks.WHITE_BANNER);
    }
}