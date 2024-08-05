package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SkullBlock extends AbstractSkullBlock {

    public static final int MAX = RotationSegment.getMaxSegmentIndex();

    private static final int ROTATIONS = MAX + 1;

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);

    protected static final VoxelShape PIGLIN_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);

    protected SkullBlock(SkullBlock.Type skullBlockType0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(skullBlockType0, blockBehaviourProperties1);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(ROTATION, 0));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.m_48754_() == SkullBlock.Types.PIGLIN ? PIGLIN_SHAPE : SHAPE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(ROTATION, RotationSegment.convertToSegment(blockPlaceContext0.m_7074_()));
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(ROTATION, rotation1.rotate((Integer) blockState0.m_61143_(ROTATION), ROTATIONS));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return (BlockState) blockState0.m_61124_(ROTATION, mirror1.mirror((Integer) blockState0.m_61143_(ROTATION), ROTATIONS));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(ROTATION);
    }

    public interface Type {
    }

    public static enum Types implements SkullBlock.Type {

        SKELETON,
        WITHER_SKELETON,
        PLAYER,
        ZOMBIE,
        CREEPER,
        PIGLIN,
        DRAGON
    }
}