package com.almostreliable.summoningrituals.altar;

import com.almostreliable.summoningrituals.util.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class AltarRenderer implements BlockEntityRenderer<AltarBlockEntity> {

    private static final int MAX_RENDER_DISTANCE = 32;

    private static final int MAX_ITEM_HEIGHT = 2;

    private static final int MAX_RESET = 60;

    private static final float MAX_PROGRESS_HEIGHT = 2.5F;

    private static final float HALF = 0.5F;

    private static final float ITEM_OFFSET = 1.5F;

    private final Minecraft mc = Minecraft.getInstance();

    private final ItemRenderer itemRenderer = this.mc.getItemRenderer();

    private float resetTimer;

    private double oldCircleOffset;

    public AltarRenderer(BlockEntityRendererProvider.Context ignoredContext) {
    }

    public void render(AltarBlockEntity entity, float partial, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        if (this.mc.player != null && entity.m_58904_() != null && !(entity.m_58899_().m_123331_(this.mc.player.m_20183_()) > Math.pow(32.0, 2.0))) {
            stack.pushPose();
            stack.translate(0.5F, 0.8F, 0.5F);
            stack.scale(0.5F, 0.5F, 0.5F);
            int lightAbove = LevelRenderer.getLightColor(entity.m_58904_(), entity.m_58899_().above());
            Vec3 altarPos = MathUtils.shiftToCenter(MathUtils.vectorFromPos(entity.m_58899_()));
            Vec3 playerPos = this.mc.player.m_20182_();
            double playerAngle = Math.toDegrees(Math.atan2(altarPos.x - playerPos.x, playerPos.z - altarPos.z)) + 180.0;
            int progress = entity.getProgress();
            int processTime = entity.getProcessTime();
            stack.translate(0.0F, 2.5F * MathUtils.modifier((float) progress, (float) processTime, 0.0F), 0.0F);
            if (!entity.getInventory().getCatalyst().isEmpty()) {
                stack.pushPose();
                stack.translate(0.0F, 1.0F - 0.75F * MathUtils.modifier((float) progress, (float) processTime, 0.0F), 0.0F);
                stack.scale(0.75F, 0.75F, 0.75F);
                stack.mulPose(Axis.YN.rotationDegrees((float) playerAngle));
                this.itemRenderer.renderStatic(entity.getInventory().getCatalyst(), ItemDisplayContext.FIXED, lightAbove, overlay, stack, buffer, entity.m_58904_(), (int) entity.m_58899_().asLong());
                stack.popPose();
            }
            float axisRotation = MathUtils.singleRotation(entity.m_58904_().getGameTime());
            float scale = 1.0F - MathUtils.modifier((float) progress, (float) processTime, 0.0F);
            if (progress == 0 && this.resetTimer > 0.0F) {
                scale = 1.0F - MathUtils.modifier(this.resetTimer, 60.0F, 0.0F);
                this.resetTimer = Math.max(0.0F, this.resetTimer - partial);
            }
            stack.scale(scale, scale, scale);
            List<ItemStack> inputs = entity.getInventory().getNoneEmptyItems();
            for (int i = 0; i < inputs.size(); i++) {
                stack.pushPose();
                int itemRotation = MathUtils.flipCircle((float) i * 360.0F / (float) inputs.size());
                double circleOffset = 0.0;
                if (progress > 0) {
                    circleOffset = (double) (MathUtils.modifier((float) progress, (float) processTime, 1.0F) * 360.0F * 3.0F) + this.oldCircleOffset;
                } else {
                    circleOffset = playerAngle;
                    this.oldCircleOffset = playerAngle;
                }
                float rotationDiff = MathUtils.singleRotation((double) (axisRotation + (float) itemRotation) - circleOffset);
                if (rotationDiff > 180.0F) {
                    rotationDiff = 360.0F - rotationDiff;
                }
                float newHeight = rotationDiff / 180.0F * 2.0F;
                double playerOffset = Math.max(1.0 - altarPos.distanceTo(playerPos) / 8.0, 0.0);
                newHeight *= (float) playerOffset;
                stack.mulPose(Axis.YN.rotationDegrees(MathUtils.singleRotation((float) itemRotation + axisRotation)));
                stack.translate(0.0F, newHeight, -1.5F);
                ItemStack item = (ItemStack) inputs.get(i);
                if (!item.isEmpty()) {
                    this.mc.getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, lightAbove, overlay, stack, buffer, entity.m_58904_(), (int) entity.m_58899_().asLong());
                }
                stack.popPose();
            }
            if (processTime > 0 && progress >= processTime) {
                this.resetTimer = 60.0F;
            }
            stack.popPose();
        }
    }
}