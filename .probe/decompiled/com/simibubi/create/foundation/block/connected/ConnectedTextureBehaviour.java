package com.simibubi.create.foundation.block.connected;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConnectedTextureBehaviour {

    @Nullable
    public abstract CTSpriteShiftEntry getShift(BlockState var1, Direction var2, @NotNull TextureAtlasSprite var3);

    @Nullable
    public abstract CTType getDataType(BlockAndTintGetter var1, BlockPos var2, BlockState var3, Direction var4);

    public boolean buildContextForOccludedDirections() {
        return false;
    }

    protected boolean isBeingBlocked(BlockState state, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        BlockPos blockingPos = otherPos.relative(face);
        BlockState blockState = reader.m_8055_(pos);
        BlockState blockingState = reader.m_8055_(blockingPos);
        if (!Block.isFaceFull(blockingState.m_60808_(reader, blockingPos), face.getOpposite())) {
            return false;
        } else {
            return face.getAxis().choose(pos.m_123341_(), pos.m_123342_(), pos.m_123343_()) != face.getAxis().choose(otherPos.m_123341_(), otherPos.m_123342_(), otherPos.m_123343_()) ? false : this.connectsTo(state, this.getCTBlockState(reader, blockState, face.getOpposite(), pos.relative(face), blockingPos), reader, pos, blockingPos, face);
        }
    }

    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, Direction primaryOffset, Direction secondaryOffset) {
        return this.connectsTo(state, other, reader, pos, otherPos, face);
    }

    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        return !this.isBeingBlocked(state, reader, pos, otherPos, face) && state.m_60734_() == other.m_60734_();
    }

    private boolean testConnection(BlockAndTintGetter reader, BlockPos currentPos, BlockState connectiveCurrentState, Direction textureSide, Direction horizontal, Direction vertical, int sh, int sv) {
        BlockState trueCurrentState = reader.m_8055_(currentPos);
        BlockPos targetPos = currentPos.relative(horizontal, sh).relative(vertical, sv);
        BlockState connectiveTargetState = this.getCTBlockState(reader, trueCurrentState, textureSide, currentPos, targetPos);
        return this.connectsTo(connectiveCurrentState, connectiveTargetState, reader, currentPos, targetPos, textureSide, sh == 0 ? null : (sh == -1 ? horizontal.getOpposite() : horizontal), sv == 0 ? null : (sv == -1 ? vertical.getOpposite() : vertical));
    }

    public BlockState getCTBlockState(BlockAndTintGetter reader, BlockState reference, Direction face, BlockPos fromPos, BlockPos toPos) {
        BlockState blockState = reader.m_8055_(toPos);
        return blockState.getAppearance(reader, toPos, face, reference, fromPos);
    }

    protected boolean reverseUVs(BlockState state, Direction face) {
        return false;
    }

    protected boolean reverseUVsHorizontally(BlockState state, Direction face) {
        return this.reverseUVs(state, face);
    }

    protected boolean reverseUVsVertically(BlockState state, Direction face) {
        return this.reverseUVs(state, face);
    }

    protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        Direction.Axis axis = face.getAxis();
        return axis.isHorizontal() ? Direction.UP : Direction.NORTH;
    }

    protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        Direction.Axis axis = face.getAxis();
        return axis == Direction.Axis.X ? Direction.SOUTH : Direction.WEST;
    }

    public ConnectedTextureBehaviour.CTContext buildContext(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face, ConnectedTextureBehaviour.ContextRequirement requirement) {
        boolean positive = face.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        Direction h = this.getRightDirection(reader, pos, state, face);
        Direction v = this.getUpDirection(reader, pos, state, face);
        h = positive ? h.getOpposite() : h;
        if (face == Direction.DOWN) {
            v = v.getOpposite();
            h = h.getOpposite();
        }
        boolean flipH = this.reverseUVsHorizontally(state, face);
        boolean flipV = this.reverseUVsVertically(state, face);
        int sh = flipH ? -1 : 1;
        int sv = flipV ? -1 : 1;
        ConnectedTextureBehaviour.CTContext context = new ConnectedTextureBehaviour.CTContext();
        if (requirement.up) {
            context.up = this.testConnection(reader, pos, state, face, h, v, 0, sv);
        }
        if (requirement.down) {
            context.down = this.testConnection(reader, pos, state, face, h, v, 0, -sv);
        }
        if (requirement.left) {
            context.left = this.testConnection(reader, pos, state, face, h, v, -sh, 0);
        }
        if (requirement.right) {
            context.right = this.testConnection(reader, pos, state, face, h, v, sh, 0);
        }
        if (requirement.topLeft) {
            context.topLeft = context.up && context.left && this.testConnection(reader, pos, state, face, h, v, -sh, sv);
        }
        if (requirement.topRight) {
            context.topRight = context.up && context.right && this.testConnection(reader, pos, state, face, h, v, sh, sv);
        }
        if (requirement.bottomLeft) {
            context.bottomLeft = context.down && context.left && this.testConnection(reader, pos, state, face, h, v, -sh, -sv);
        }
        if (requirement.bottomRight) {
            context.bottomRight = context.down && context.right && this.testConnection(reader, pos, state, face, h, v, sh, -sv);
        }
        return context;
    }

    public abstract static class Base extends ConnectedTextureBehaviour {

        @Nullable
        @Override
        public abstract CTSpriteShiftEntry getShift(BlockState var1, Direction var2, @Nullable TextureAtlasSprite var3);

        @Nullable
        @Override
        public CTType getDataType(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
            CTSpriteShiftEntry shift = this.getShift(state, direction, null);
            return shift == null ? null : shift.getType();
        }
    }

    public static class CTContext {

        public static final ConnectedTextureBehaviour.CTContext EMPTY = new ConnectedTextureBehaviour.CTContext();

        public boolean up;

        public boolean down;

        public boolean left;

        public boolean right;

        public boolean topLeft;

        public boolean topRight;

        public boolean bottomLeft;

        public boolean bottomRight;
    }

    public static class ContextRequirement {

        public final boolean up;

        public final boolean down;

        public final boolean left;

        public final boolean right;

        public final boolean topLeft;

        public final boolean topRight;

        public final boolean bottomLeft;

        public final boolean bottomRight;

        public ContextRequirement(boolean up, boolean down, boolean left, boolean right, boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
            this.up = up;
            this.down = down;
            this.left = left;
            this.right = right;
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
        }

        public static ConnectedTextureBehaviour.ContextRequirement.Builder builder() {
            return new ConnectedTextureBehaviour.ContextRequirement.Builder();
        }

        public static class Builder {

            private boolean up;

            private boolean down;

            private boolean left;

            private boolean right;

            private boolean topLeft;

            private boolean topRight;

            private boolean bottomLeft;

            private boolean bottomRight;

            public ConnectedTextureBehaviour.ContextRequirement.Builder up() {
                this.up = true;
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder down() {
                this.down = true;
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder left() {
                this.left = true;
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder right() {
                this.right = true;
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder topLeft() {
                this.topLeft = true;
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder topRight() {
                this.topRight = true;
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder bottomLeft() {
                this.bottomLeft = true;
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder bottomRight() {
                this.bottomRight = true;
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder horizontal() {
                this.left();
                this.right();
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder vertical() {
                this.up();
                this.down();
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder axisAligned() {
                this.horizontal();
                this.vertical();
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder corners() {
                this.topLeft();
                this.topRight();
                this.bottomLeft();
                this.bottomRight();
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement.Builder all() {
                this.axisAligned();
                this.corners();
                return this;
            }

            public ConnectedTextureBehaviour.ContextRequirement build() {
                return new ConnectedTextureBehaviour.ContextRequirement(this.up, this.down, this.left, this.right, this.topLeft, this.topRight, this.bottomLeft, this.bottomRight);
            }
        }
    }
}