package com.rekindled.embers.block;

import com.rekindled.embers.blockentity.PipeBlockEntityBase;
import com.rekindled.embers.datagen.EmbersBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class ExtractorBlockBase extends PipeBlockBase {

    public static final VoxelShape EXTRACTOR_AABB = Block.box(5.0, 5.0, 5.0, 11.0, 11.0, 11.0);

    public static final VoxelShape[] EXTRACTOR_SHAPES = new VoxelShape[729];

    public ExtractorBlockBase(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getCenterShape() {
        return EXTRACTOR_AABB;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return level.getBlockEntity(pos) instanceof PipeBlockEntityBase pipe ? EXTRACTOR_SHAPES[getShapeIndex(pipe.connections[0], pipe.connections[1], pipe.connections[2], pipe.connections[3], pipe.connections[4], pipe.connections[5])] : CENTER_AABB;
    }

    @Override
    public boolean connected(Direction direction, BlockState state) {
        if (!state.m_204336_(EmbersBlockTags.EMITTER_CONNECTION)) {
            return false;
        } else if (state.m_204336_(EmbersBlockTags.EMITTER_CONNECTION_FLOOR)) {
            return direction == Direction.DOWN && state.m_204336_(EmbersBlockTags.EMITTER_CONNECTION_CEILING) ? true : direction == Direction.UP;
        } else {
            BooleanProperty sideProp = EmberEmitterBlock.ALL_DIRECTIONS[direction.getOpposite().get3DDataValue()];
            if (state.m_61138_(sideProp) && (Boolean) state.m_61143_(sideProp)) {
                return true;
            } else if (state.m_61138_(BlockStateProperties.BELL_ATTACHMENT) && state.m_61143_(BlockStateProperties.BELL_ATTACHMENT) == BellAttachType.CEILING && direction == Direction.DOWN) {
                return true;
            } else if (state.m_61138_(BlockStateProperties.HANGING)) {
                return direction == Direction.DOWN && state.m_61143_(BlockStateProperties.HANGING) ? true : direction == Direction.UP && !(Boolean) state.m_61143_(BlockStateProperties.HANGING);
            } else if (state.m_61138_(BlockStateProperties.AXIS)) {
                return state.m_61143_(BlockStateProperties.AXIS) == direction.getAxis();
            } else if (!state.m_61138_(BlockStateProperties.ATTACH_FACE) || state.m_61143_(BlockStateProperties.ATTACH_FACE) == AttachFace.WALL) {
                return facingConnected(direction, state, BlockStateProperties.HORIZONTAL_FACING) && facingConnected(direction, state, BlockStateProperties.FACING) && facingConnected(direction, state, BlockStateProperties.FACING_HOPPER);
            } else {
                return direction == Direction.DOWN && state.m_61143_(BlockStateProperties.ATTACH_FACE) == AttachFace.CEILING ? true : direction == Direction.UP && state.m_61143_(BlockStateProperties.ATTACH_FACE) == AttachFace.FLOOR;
            }
        }
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return Shapes.block();
    }

    static {
        makeShapes(EXTRACTOR_AABB, EXTRACTOR_SHAPES);
    }
}