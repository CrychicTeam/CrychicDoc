package com.simibubi.create.content.contraptions.actors.contraptionControls;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.DyeHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ContraptionControlsBlockEntity extends SmartBlockEntity {

    public FilteringBehaviour filtering;

    public boolean disabled;

    public boolean powered;

    public LerpedFloat indicator = LerpedFloat.angular().startWithValue(0.0);

    public LerpedFloat button = LerpedFloat.linear().startWithValue(0.0).chase(0.0, 0.125, LerpedFloat.Chaser.EXP);

    public ContraptionControlsBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.filtering = new FilteringBehaviour(this, new ContraptionControlsBlockEntity.ControlsSlot()));
        this.filtering.setLabel(Lang.translateDirect("contraptions.contoller.target"));
        this.filtering.withPredicate(AllTags.AllItemTags.CONTRAPTION_CONTROLLED::matches);
    }

    public void pressButton() {
        this.button.setValue(1.0);
    }

    public void updatePoweredState() {
        if (!this.f_58857_.isClientSide()) {
            boolean powered = this.f_58857_.m_276867_(this.f_58858_);
            if (this.powered != powered) {
                this.powered = powered;
                this.disabled = powered;
                this.notifyUpdate();
            }
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        this.updatePoweredState();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_58857_.isClientSide()) {
            this.tickAnimations();
            int value = this.disabled ? 180 : 0;
            this.indicator.setValue((double) value);
            this.indicator.updateChaseTarget((float) value);
        }
    }

    public void tickAnimations() {
        this.button.tickChaser();
        this.indicator.tickChaser();
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.disabled = tag.getBoolean("Disabled");
        this.powered = tag.getBoolean("Powered");
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putBoolean("Disabled", this.disabled);
        tag.putBoolean("Powered", this.powered);
    }

    public static void sendStatus(Player player, ItemStack filter, boolean enabled) {
        MutableComponent state = Lang.translate("contraption.controls.actor_toggle." + (enabled ? "on" : "off")).color((Integer) ((Couple) DyeHelper.DYE_TABLE.get(enabled ? DyeColor.LIME : DyeColor.ORANGE)).getFirst()).component();
        if (filter.isEmpty()) {
            Lang.translate("contraption.controls.all_actor_toggle", state).sendStatus(player);
        } else {
            Lang.translate("contraption.controls.specific_actor_toggle", filter.getHoverName().getString(), state).sendStatus(player);
        }
    }

    public static class ControlsSlot extends ValueBoxTransform.Sided {

        @Override
        public Vec3 getLocalOffset(BlockState state) {
            Direction facing = (Direction) state.m_61143_(ControlsBlock.f_54117_);
            float yRot = AngleHelper.horizontalAngle(facing);
            return VecHelper.rotateCentered(VecHelper.voxelSpace(8.0, 12.0, 5.5), (double) yRot, Direction.Axis.Y);
        }

        @Override
        public void rotate(BlockState state, PoseStack ms) {
            Direction facing = (Direction) state.m_61143_(ControlsBlock.f_54117_);
            float yRot = AngleHelper.horizontalAngle(facing);
            ((TransformStack) TransformStack.cast(ms).rotateY((double) (yRot + 180.0F))).rotateX(67.5);
        }

        @Override
        public float getScale() {
            return 0.508F;
        }

        @Override
        protected Vec3 getSouthLocation() {
            return Vec3.ZERO;
        }
    }
}