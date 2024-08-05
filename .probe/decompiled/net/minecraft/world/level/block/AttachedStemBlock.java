package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AttachedStemBlock extends BushBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final float AABB_OFFSET = 2.0F;

    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.SOUTH, Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 16.0), Direction.WEST, Block.box(0.0, 0.0, 6.0, 10.0, 10.0, 10.0), Direction.NORTH, Block.box(6.0, 0.0, 0.0, 10.0, 10.0, 10.0), Direction.EAST, Block.box(6.0, 0.0, 6.0, 16.0, 10.0, 10.0)));

    private final StemGrownBlock fruit;

    private final Supplier<Item> seedSupplier;

    protected AttachedStemBlock(StemGrownBlock stemGrownBlock0, Supplier<Item> supplierItem1, BlockBehaviour.Properties blockBehaviourProperties2) {
        super(blockBehaviourProperties2);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH));
        this.fruit = stemGrownBlock0;
        this.seedSupplier = supplierItem1;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (VoxelShape) AABBS.get(blockState0.m_61143_(FACING));
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return !blockState2.m_60713_(this.fruit) && direction1 == blockState0.m_61143_(FACING) ? (BlockState) this.fruit.getStem().m_49966_().m_61124_(StemBlock.AGE, 7) : super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_60713_(Blocks.FARMLAND);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return new ItemStack((ItemLike) this.seedSupplier.get());
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