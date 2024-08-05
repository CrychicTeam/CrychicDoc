package com.simibubi.create.content.contraptions.actors.roller;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RollerBlockEntity extends SmartBlockEntity {

    private float manuallyAnimatedSpeed;

    public FilteringBehaviour filtering;

    public ScrollOptionBehaviour<RollerBlockEntity.RollingMode> mode;

    private boolean dontPropagate = false;

    public RollerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.filtering = new FilteringBehaviour(this, new RollerBlockEntity.RollerValueBox(3)));
        behaviours.add(this.mode = new ScrollOptionBehaviour(RollerBlockEntity.RollingMode.class, Lang.translateDirect("contraptions.roller_mode"), this, new RollerBlockEntity.RollerValueBox(-3)));
        this.filtering.setLabel(Lang.translateDirect("contraptions.mechanical_roller.pave_material"));
        this.filtering.withCallback(this::onFilterChanged);
        this.filtering.withPredicate(this::isValidMaterial);
        this.mode.withCallback(this::onModeChanged);
    }

    protected void onModeChanged(int mode) {
        this.shareValuesToAdjacent();
    }

    protected void onFilterChanged(ItemStack newFilter) {
        this.shareValuesToAdjacent();
    }

    protected boolean isValidMaterial(ItemStack newFilter) {
        if (newFilter.isEmpty()) {
            return true;
        } else {
            BlockState appliedState = RollerMovementBehaviour.getStateToPaveWith(newFilter);
            if (appliedState.m_60795_()) {
                return false;
            } else if (appliedState.m_60734_() instanceof EntityBlock) {
                return false;
            } else if (appliedState.m_60734_() instanceof StairBlock) {
                return false;
            } else {
                VoxelShape shape = appliedState.m_60808_(this.f_58857_, this.f_58858_);
                if (!shape.isEmpty() && shape.bounds().equals(Shapes.block().bounds())) {
                    VoxelShape collisionShape = appliedState.m_60812_(this.f_58857_, this.f_58858_);
                    return !collisionShape.isEmpty();
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_).inflate(1.0);
    }

    public float getAnimatedSpeed() {
        return this.manuallyAnimatedSpeed;
    }

    public void setAnimatedSpeed(float speed) {
        this.manuallyAnimatedSpeed = speed;
    }

    public void searchForSharedValues() {
        BlockState blockState = this.m_58900_();
        Direction facing = (Direction) blockState.m_61145_(RollerBlock.f_54117_).orElse(Direction.SOUTH);
        for (int side : Iterate.positiveAndNegative) {
            BlockPos pos = this.f_58858_.relative(facing.getClockWise(), side);
            if (this.f_58857_.getBlockState(pos) == blockState && this.f_58857_.getBlockEntity(pos) instanceof RollerBlockEntity otherRoller) {
                this.acceptSharedValues(otherRoller.mode.getValue(), otherRoller.filtering.getFilter());
                this.shareValuesToAdjacent();
                break;
            }
        }
    }

    protected void acceptSharedValues(int mode, ItemStack filter) {
        this.dontPropagate = true;
        this.filtering.setFilter(filter.copy());
        this.mode.setValue(mode);
        this.dontPropagate = false;
        this.notifyUpdate();
    }

    public void shareValuesToAdjacent() {
        if (!this.dontPropagate && !this.f_58857_.isClientSide()) {
            BlockState blockState = this.m_58900_();
            Direction facing = (Direction) blockState.m_61145_(RollerBlock.f_54117_).orElse(Direction.SOUTH);
            for (int side : Iterate.positiveAndNegative) {
                for (int i = 1; i < 100; i++) {
                    BlockPos pos = this.f_58858_.relative(facing.getClockWise(), side * i);
                    if (this.f_58857_.getBlockState(pos) != blockState || !(this.f_58857_.getBlockEntity(pos) instanceof RollerBlockEntity otherRoller)) {
                        break;
                    }
                    otherRoller.acceptSharedValues(this.mode.getValue(), this.filtering.getFilter());
                }
            }
        }
    }

    private final class RollerValueBox extends ValueBoxTransform {

        private int hOffset;

        public RollerValueBox(int hOffset) {
            this.hOffset = hOffset;
        }

        @Override
        public void rotate(BlockState state, PoseStack ms) {
            Direction facing = (Direction) state.m_61143_(RollerBlock.f_54117_);
            float yRot = AngleHelper.horizontalAngle(facing) + 180.0F;
            ((TransformStack) TransformStack.cast(ms).rotateY((double) yRot)).rotateX(90.0);
        }

        @Override
        public boolean testHit(BlockState state, Vec3 localHit) {
            Vec3 offset = this.getLocalOffset(state);
            return offset == null ? false : localHit.distanceTo(offset) < (double) (this.scale / 3.0F);
        }

        @Override
        public Vec3 getLocalOffset(BlockState state) {
            Direction facing = (Direction) state.m_61143_(RollerBlock.f_54117_);
            float stateAngle = AngleHelper.horizontalAngle(facing) + 180.0F;
            return VecHelper.rotateCentered(VecHelper.voxelSpace((double) (8 + this.hOffset), 15.5, 11.0), (double) stateAngle, Direction.Axis.Y);
        }
    }

    static enum RollingMode implements INamedIconOptions {

        TUNNEL_PAVE(AllIcons.I_ROLLER_PAVE), STRAIGHT_FILL(AllIcons.I_ROLLER_FILL), WIDE_FILL(AllIcons.I_ROLLER_WIDE_FILL);

        private String translationKey;

        private AllIcons icon;

        private RollingMode(AllIcons icon) {
            this.icon = icon;
            this.translationKey = "contraptions.roller_mode." + Lang.asId(this.name());
        }

        @Override
        public AllIcons getIcon() {
            return this.icon;
        }

        @Override
        public String getTranslationKey() {
            return this.translationKey;
        }
    }
}