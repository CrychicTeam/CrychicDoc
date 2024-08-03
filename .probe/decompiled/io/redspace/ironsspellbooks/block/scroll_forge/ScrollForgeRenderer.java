package io.redspace.ironsspellbooks.block.scroll_forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ScrollForgeRenderer implements BlockEntityRenderer<ScrollForgeTile> {

    private static final ResourceLocation PAPER_TEXTURE = new ResourceLocation("irons_spellbooks", "textures/block/scroll_forge_paper.png");

    private static final ResourceLocation SIGIL_TEXTURE = new ResourceLocation("irons_spellbooks", "textures/block/scroll_forge_sigil.png");

    ItemRenderer itemRenderer;

    private static final Vec3 INK_POS = new Vec3(0.175, 0.876, 0.25);

    private static final Vec3 FOCUS_POS = new Vec3(0.75, 0.876, 0.4);

    private static final Vec3 PAPER_POS = new Vec3(0.5, 0.876, 0.7);

    public ScrollForgeRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(ScrollForgeTile scrollForgeTile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack inkStack = scrollForgeTile.getStackInSlot(0);
        ItemStack paperStack = scrollForgeTile.getStackInSlot(1);
        ItemStack focusStack = scrollForgeTile.getItemHandler().getStackInSlot(2);
        if (!inkStack.isEmpty() && inkStack.getItem() instanceof InkItem) {
            this.renderItem(inkStack, INK_POS, 15.0F, scrollForgeTile, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
        if (!focusStack.isEmpty() && focusStack.is(ModTags.SCHOOL_FOCUS)) {
            this.renderItem(focusStack, FOCUS_POS, 5.0F, scrollForgeTile, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
        if (!paperStack.isEmpty() && paperStack.is(Items.PAPER)) {
            poseStack.pushPose();
            this.rotatePoseWithBlock(poseStack, scrollForgeTile);
            poseStack.translate(PAPER_POS.x, PAPER_POS.y, PAPER_POS.z);
            poseStack.mulPose(Axis.YP.rotationDegrees(85.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(PAPER_TEXTURE));
            int light = LevelRenderer.getLightColor(scrollForgeTile.m_58904_(), scrollForgeTile.m_58899_());
            this.drawQuad(0.45F, poseStack.last(), consumer, light);
            poseStack.popPose();
        }
    }

    private void renderItem(ItemStack itemStack, Vec3 offset, float yRot, ScrollForgeTile scrollForgeTile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        int renderId = (int) scrollForgeTile.m_58899_().asLong();
        this.rotatePoseWithBlock(poseStack, scrollForgeTile);
        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-yRot));
        poseStack.scale(0.45F, 0.45F, 0.45F);
        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(scrollForgeTile.m_58904_(), scrollForgeTile.m_58899_()), packedOverlay, poseStack, bufferSource, scrollForgeTile.m_58904_(), renderId);
        poseStack.popPose();
    }

    private void drawQuad(float width, PoseStack.Pose pose, VertexConsumer consumer, int light) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float halfWidth = width * 0.5F;
        consumer.vertex(poseMatrix, -halfWidth, 0.0F, -halfWidth).color(255, 255, 255, 255).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0.0F, -1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, halfWidth, 0.0F, -halfWidth).color(255, 255, 255, 255).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0.0F, -1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, halfWidth, 0.0F, halfWidth).color(255, 255, 255, 255).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0.0F, -1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, -halfWidth, 0.0F, halfWidth).color(255, 255, 255, 255).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private void rotatePoseWithBlock(PoseStack poseStack, ScrollForgeTile scrollForgeTile) {
        Vec3 center = new Vec3(0.5, 0.5, 0.5);
        poseStack.translate(center.x, center.y, center.z);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) this.getBlockFacingDegrees(scrollForgeTile)));
        poseStack.translate(-center.x, -center.y, -center.z);
    }

    private int getBlockFacingDegrees(ScrollForgeTile tileEntity) {
        BlockState block = tileEntity.m_58904_().getBlockState(tileEntity.m_58899_());
        if (block.m_60734_() instanceof ScrollForgeBlock) {
            Direction facing = (Direction) block.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
            return switch(facing) {
                case NORTH ->
                    180;
                case EAST ->
                    90;
                case WEST ->
                    -90;
                default ->
                    0;
            };
        } else {
            return 0;
        }
    }
}