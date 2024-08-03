package com.rekindled.embers.render;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.rekindled.embers.item.AlchemyHintItem;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class AlchemicalNoteItemRenderer extends BlockEntityWithoutLevelRenderer {

    public Font font;

    public Minecraft minecraft;

    public ItemInHandRenderer itemInHandRenderer;

    public ItemRenderer itemRenderer;

    public AlchemicalNoteItemRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
        this.font = pBlockEntityRenderDispatcher.font;
        this.minecraft = Minecraft.getInstance();
        this.itemInHandRenderer = this.minecraft.getEntityRenderDispatcher().getItemInHandRenderer();
        this.itemRenderer = this.minecraft.getItemRenderer();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
            boolean flag = displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND == (this.minecraft.player.m_5737_() == HumanoidArm.RIGHT);
            HumanoidArm humanoidarm = flag ? this.minecraft.player.m_5737_() : this.minecraft.player.m_5737_().getOpposite();
            InteractionHand interactionhand = (InteractionHand) MoreObjects.firstNonNull(this.minecraft.player.f_20912_, InteractionHand.MAIN_HAND);
            float pitch = Mth.lerp(this.minecraft.getPartialTick(), this.minecraft.player.f_19860_, this.minecraft.player.m_146909_());
            float swingProgress = interactionhand == InteractionHand.MAIN_HAND ? this.minecraft.player.m_21324_(this.minecraft.getPartialTick()) : 0.0F;
            float equippedProgress = interactionhand == InteractionHand.MAIN_HAND ? 1.0F - Mth.lerp(this.minecraft.getPartialTick(), this.itemInHandRenderer.oMainHandHeight, this.itemInHandRenderer.mainHandHeight) : 0.0F;
            poseStack.pushPose();
            poseStack.scale(64.0F, 64.0F, 64.0F);
            if (!flag || !this.minecraft.player.m_21206_().isEmpty() && !ItemStack.isSameItemSameTags(this.minecraft.player.m_21206_(), stack)) {
                this.renderOneHandedNote(poseStack, buffer, packedLight, packedOverlay, equippedProgress, humanoidarm, swingProgress, stack);
            } else {
                this.renderTwoHandedNote(poseStack, buffer, packedLight, packedOverlay, pitch, equippedProgress, swingProgress, stack);
            }
            poseStack.popPose();
        } else if (displayContext == ItemDisplayContext.FIXED) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.49);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.scale(4.0F, 4.0F, 4.0F);
            this.renderNote(poseStack, buffer, packedLight, packedOverlay, stack);
            poseStack.popPose();
        }
    }

    private void renderOneHandedNote(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float equippedProgress, HumanoidArm hand, float swingProgress, ItemStack stack) {
        float f = hand == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        poseStack.translate(f * 0.125F, -0.125F, 0.0F);
        if (!this.minecraft.player.m_20145_()) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.ZP.rotationDegrees(f * 10.0F));
            this.itemInHandRenderer.renderPlayerArm(poseStack, buffer, packedLight, equippedProgress, swingProgress, hand);
            poseStack.popPose();
        }
        poseStack.pushPose();
        poseStack.translate(f * 0.51F, -0.08F + equippedProgress * -1.2F, -0.75F);
        float f1 = Mth.sqrt(swingProgress);
        float f2 = Mth.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * Mth.sin(f1 * (float) (Math.PI * 2));
        float f5 = -0.3F * Mth.sin(swingProgress * (float) Math.PI);
        poseStack.translate(f * f3, f4 - 0.3F * f2, f5);
        poseStack.mulPose(Axis.XP.rotationDegrees(f2 * -45.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(f * f2 * -30.0F));
        this.renderNote(poseStack, buffer, packedLight, packedOverlay, stack);
        poseStack.popPose();
    }

    private void renderTwoHandedNote(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float pitch, float equippedProgress, float swingProgress, ItemStack stack) {
        float f = Mth.sqrt(swingProgress);
        float f1 = -0.2F * Mth.sin(swingProgress * (float) Math.PI);
        float f2 = -0.4F * Mth.sin(f * (float) Math.PI);
        poseStack.translate(0.0F, -f1 / 2.0F, f2);
        float f3 = this.itemInHandRenderer.calculateMapTilt(pitch);
        poseStack.translate(0.0F, 0.04F + equippedProgress * -1.2F + f3 * -0.5F, -0.72F);
        poseStack.mulPose(Axis.XP.rotationDegrees(f3 * -85.0F));
        if (!this.minecraft.player.m_20145_()) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            this.itemInHandRenderer.renderMapHand(poseStack, buffer, packedLight, HumanoidArm.RIGHT);
            this.itemInHandRenderer.renderMapHand(poseStack, buffer, packedLight, HumanoidArm.LEFT);
            poseStack.popPose();
        }
        float f4 = Mth.sin(f * (float) Math.PI);
        poseStack.mulPose(Axis.XP.rotationDegrees(f4 * 20.0F));
        poseStack.scale(2.0F, 2.0F, 2.0F);
        this.renderNote(poseStack, buffer, packedLight, packedOverlay, stack);
    }

    public void renderNote(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, ItemStack stack) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.scale(0.38F, 0.38F, 0.38F);
        poseStack.translate(-0.5F, -0.5F, 0.0F);
        poseStack.scale(0.015625F, 0.015625F, 0.015625F);
        VertexConsumer vertexconsumer = buffer.getBuffer(EmbersRenderTypes.NOTE_BACKGROUND);
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        vertexconsumer.vertex(matrix4f, 0.0F, 64.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, 0.0F, 0.5F, -1.0F).endVertex();
        vertexconsumer.vertex(matrix4f, 64.0F, 64.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, 0.0F, 0.5F, -1.0F).endVertex();
        vertexconsumer.vertex(matrix4f, 64.0F, 0.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, 0.0F, 0.5F, -1.0F).endVertex();
        vertexconsumer.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, 0.0F, 0.5F, -1.0F).endVertex();
        ArrayList<ItemStack> inputs = AlchemyHintItem.getInputs(stack);
        ArrayList<ItemStack> aspects = AlchemyHintItem.getAspects(stack);
        ItemStack result = AlchemyHintItem.getResult(stack);
        if (inputs.size() == aspects.size()) {
            for (int j = 0; j < inputs.size(); j++) {
                int pedestal = 10;
                int margin = 4;
                int width = (64 - pedestal - 2 * margin) / inputs.size();
                poseStack.translate(0.0, 0.0, -0.01);
                this.renderPedestal(poseStack, margin + width * j + width / 2 + 1, 10, buffer, packedLight, packedOverlay, (ItemStack) aspects.get(j), (ItemStack) inputs.get(j));
            }
        }
        poseStack.translate(32.0, 44.0, -0.01);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.last().pose().scale(16.0F, 16.0F, 0.01F);
        poseStack.last().normal().rotate(Axis.XP.rotationDegrees(-22.5F));
        this.itemRenderer.renderStatic(result, ItemDisplayContext.GUI, packedLight, packedOverlay, poseStack, buffer, this.minecraft.level, 0);
    }

    public void renderPedestal(PoseStack poseStack, int x, int y, MultiBufferSource buffer, int combinedLight, int packedOverlay, ItemStack aspect, ItemStack ingredient) {
        VertexConsumer vertexconsumer = buffer.getBuffer(EmbersRenderTypes.NOTE_PEDESTAL);
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        vertexconsumer.vertex(matrix4f, (float) x, (float) (y + 14), 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).overlayCoords(packedOverlay).uv2(combinedLight).normal(normal, 0.0F, 0.5F, -1.0F).endVertex();
        vertexconsumer.vertex(matrix4f, (float) (x + 10), (float) (y + 14), 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).overlayCoords(packedOverlay).uv2(combinedLight).normal(normal, 0.0F, 0.5F, -1.0F).endVertex();
        vertexconsumer.vertex(matrix4f, (float) (x + 10), (float) y, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).overlayCoords(packedOverlay).uv2(combinedLight).normal(normal, 0.0F, 0.5F, -1.0F).endVertex();
        vertexconsumer.vertex(matrix4f, (float) x, (float) y, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).overlayCoords(packedOverlay).uv2(combinedLight).normal(normal, 0.0F, 0.5F, -1.0F).endVertex();
        poseStack.pushPose();
        poseStack.translate((double) (x + 5), (double) (y + 7), -0.01);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.last().pose().scale(8.0F, 8.0F, 0.01F);
        poseStack.last().normal().rotate(Axis.XP.rotationDegrees(-22.5F));
        this.itemRenderer.renderStatic(aspect, ItemDisplayContext.GUI, combinedLight, packedOverlay, poseStack, buffer, this.minecraft.level, 0);
        poseStack.translate(0.0, 1.25, 0.0);
        this.itemRenderer.renderStatic(ingredient, ItemDisplayContext.GUI, combinedLight, packedOverlay, poseStack, buffer, this.minecraft.level, 0);
        poseStack.popPose();
    }
}