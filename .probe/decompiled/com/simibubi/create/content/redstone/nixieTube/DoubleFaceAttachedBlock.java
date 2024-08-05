package com.simibubi.create.content.redstone.nixieTube;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;

public class DoubleFaceAttachedBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<DoubleFaceAttachedBlock.DoubleAttachFace> FACE = EnumProperty.create("double_face", DoubleFaceAttachedBlock.DoubleAttachFace.class);

    public DoubleFaceAttachedBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        for (Direction direction : pContext.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = (BlockState) ((BlockState) this.m_49966_().m_61124_(FACE, direction == Direction.UP ? DoubleFaceAttachedBlock.DoubleAttachFace.CEILING : DoubleFaceAttachedBlock.DoubleAttachFace.FLOOR)).m_61124_(f_54117_, pContext.m_8125_());
            } else {
                Vec3 n = Vec3.atLowerCornerOf(direction.getClockWise().getNormal());
                DoubleFaceAttachedBlock.DoubleAttachFace face = DoubleFaceAttachedBlock.DoubleAttachFace.WALL;
                if (pContext.m_43723_() != null) {
                    Vec3 lookAngle = pContext.m_43723_().m_20154_();
                    if (lookAngle.dot(n) < 0.0) {
                        face = DoubleFaceAttachedBlock.DoubleAttachFace.WALL_REVERSED;
                    }
                }
                blockstate = (BlockState) ((BlockState) this.m_49966_().m_61124_(FACE, face)).m_61124_(f_54117_, direction.getOpposite());
            }
            if (blockstate.m_60710_(pContext.m_43725_(), pContext.getClickedPos())) {
                return blockstate;
            }
        }
        return null;
    }

    protected static Direction getConnectedDirection(BlockState pState) {
        switch((DoubleFaceAttachedBlock.DoubleAttachFace) pState.m_61143_(FACE)) {
            case CEILING:
                return Direction.DOWN;
            case FLOOR:
                return Direction.UP;
            default:
                return (Direction) pState.m_61143_(f_54117_);
        }
    }

    public static enum DoubleAttachFace implements StringRepresentable {

        FLOOR("floor"), WALL("wall"), WALL_REVERSED("wall_reversed"), CEILING("ceiling");

        private final String name;

        private DoubleAttachFace(String p_61311_) {
            this.name = p_61311_;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public int xRot() {
            return this == FLOOR ? 0 : (this == CEILING ? 180 : 90);
        }
    }
}