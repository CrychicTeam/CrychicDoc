package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallSkullBlock extends AbstractSkullBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(4.0, 4.0, 8.0, 12.0, 12.0, 16.0), Direction.SOUTH, Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 8.0), Direction.EAST, Block.box(0.0, 4.0, 4.0, 8.0, 12.0, 12.0), Direction.WEST, Block.box(8.0, 4.0, 4.0, 16.0, 12.0, 12.0)));

    protected WallSkullBlock(SkullBlock.Type skullBlockType0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(skullBlockType0, blockBehaviourProperties1);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public String getDescriptionId() {
        return this.m_5456_().getDescriptionId();
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (VoxelShape) AABBS.get(blockState0.m_61143_(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = this.m_49966_();
        BlockGetter $$2 = blockPlaceContext0.m_43725_();
        BlockPos $$3 = blockPlaceContext0.getClickedPos();
        Direction[] $$4 = blockPlaceContext0.getNearestLookingDirections();
        for (Direction $$5 : $$4) {
            if ($$5.getAxis().isHorizontal()) {
                Direction $$6 = $$5.getOpposite();
                $$1 = (BlockState) $$1.m_61124_(FACING, $$6);
                if (!$$2.getBlockState($$3.relative($$5)).m_60629_(blockPlaceContext0)) {
                    return $$1;
                }
            }
        }
        return null;
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