package com.simibubi.create.content.contraptions.actors.contraptionControls;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.elevator.ElevatorContraption;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

public class ContraptionControlsMovement implements MovementBehaviour {

    @Override
    public ItemStack canBeDisabledVia(MovementContext context) {
        return null;
    }

    @Override
    public void startMoving(MovementContext context) {
        if (context.contraption instanceof ElevatorContraption && context.blockEntityData != null) {
            context.blockEntityData.remove("Filter");
        }
    }

    @Override
    public void stopMoving(MovementContext context) {
        ItemStack filter = getFilter(context);
        if (filter != null) {
            context.blockEntityData.putBoolean("Disabled", context.contraption.isActorTypeDisabled(filter) || context.contraption.isActorTypeDisabled(ItemStack.EMPTY));
        }
    }

    public static boolean isSameFilter(ItemStack stack1, ItemStack stack2) {
        return stack1.isEmpty() && stack2.isEmpty() ? true : ItemHandlerHelper.canItemStacksStack(stack1, stack2);
    }

    public static ItemStack getFilter(MovementContext ctx) {
        CompoundTag blockEntityData = ctx.blockEntityData;
        return blockEntityData == null ? null : ItemStack.of(blockEntityData.getCompound("Filter"));
    }

    public static boolean isDisabledInitially(MovementContext ctx) {
        return ctx.blockEntityData != null && ctx.blockEntityData.getBoolean("Disabled");
    }

    @Override
    public void tick(MovementContext ctx) {
        if (ctx.world.isClientSide()) {
            Contraption contraption = ctx.contraption;
            if (contraption instanceof ElevatorContraption ec) {
                if (!(ctx.temporaryData instanceof ContraptionControlsMovement.ElevatorFloorSelection)) {
                    ctx.temporaryData = new ContraptionControlsMovement.ElevatorFloorSelection();
                }
                ContraptionControlsMovement.ElevatorFloorSelection efs = (ContraptionControlsMovement.ElevatorFloorSelection) ctx.temporaryData;
                tickFloorSelection(efs, ec);
                if (contraption.presentBlockEntities.get(ctx.localPos) instanceof ContraptionControlsBlockEntity cbe) {
                    cbe.tickAnimations();
                    int var17 = (int) Math.round(contraption.entity.m_20186_() + (double) ec.getContactYOffset());
                    boolean atTargetY = ec.clientYTarget == var17;
                    LerpedFloat indicator = cbe.indicator;
                    float currentIndicator = indicator.getChaseTarget();
                    boolean below = atTargetY ? currentIndicator > 0.0F : ec.clientYTarget <= var17;
                    if (currentIndicator == 0.0F && !atTargetY) {
                        int startingPoint = below ? 181 : -181;
                        indicator.setValue((double) startingPoint);
                        indicator.updateChaseTarget((float) startingPoint);
                        cbe.tickAnimations();
                    } else {
                        int currentStage = Mth.floor((currentIndicator % 360.0F + 360.0F) % 360.0F);
                        if (atTargetY && currentStage / 45 == 0) {
                            indicator.setValue(0.0);
                            indicator.updateChaseTarget(0.0F);
                        } else {
                            float increment = currentStage / 45 == (below ? 4 : 3) ? 2.25F : 33.75F;
                            indicator.chase((double) (currentIndicator + (below ? increment : -increment)), 45.0, LerpedFloat.Chaser.LINEAR);
                        }
                    }
                }
            } else if (contraption.presentBlockEntities.get(ctx.localPos) instanceof ContraptionControlsBlockEntity cbex) {
                ItemStack var14 = getFilter(ctx);
                int value = !contraption.isActorTypeDisabled(var14) && !contraption.isActorTypeDisabled(ItemStack.EMPTY) ? 0 : 180;
                cbex.indicator.setValue((double) value);
                cbex.indicator.updateChaseTarget((float) value);
                cbex.tickAnimations();
            }
        }
    }

    public static void tickFloorSelection(ContraptionControlsMovement.ElevatorFloorSelection efs, ElevatorContraption ec) {
        if (ec.namesList.isEmpty()) {
            efs.currentShortName = "X";
            efs.currentLongName = "No Floors";
            efs.currentIndex = 0;
            efs.targetYEqualsSelection = true;
        } else {
            efs.currentIndex = Mth.clamp(efs.currentIndex, 0, ec.namesList.size() - 1);
            IntAttached<Couple<String>> entry = (IntAttached<Couple<String>>) ec.namesList.get(efs.currentIndex);
            efs.currentTargetY = entry.getFirst();
            efs.currentShortName = entry.getSecond().getFirst();
            efs.currentLongName = entry.getSecond().getSecond();
            efs.targetYEqualsSelection = efs.currentTargetY == ec.clientYTarget;
            if (ec.isTargetUnreachable(efs.currentTargetY)) {
                efs.currentLongName = Lang.translate("contraption.controls.floor_unreachable").string();
            }
        }
    }

    @Override
    public boolean renderAsNormalBlockEntity() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderInContraption(MovementContext ctx, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        ContraptionControlsRenderer.renderInContraption(ctx, renderWorld, matrices, buffer);
    }

    public static class ElevatorFloorSelection {

        public int currentIndex = 0;

        public int currentTargetY = 0;

        public boolean targetYEqualsSelection = true;

        public String currentShortName = "";

        public String currentLongName = "";
    }
}