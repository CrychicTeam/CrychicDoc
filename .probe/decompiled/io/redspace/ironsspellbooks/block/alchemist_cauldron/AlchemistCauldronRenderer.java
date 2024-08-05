package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class AlchemistCauldronRenderer implements BlockEntityRenderer<AlchemistCauldronTile> {

    ItemRenderer itemRenderer;

    private static final Vec3 ITEM_POS = new Vec3(0.5, 1.5, 0.5);

    public AlchemistCauldronRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(AlchemistCauldronTile cauldron, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float waterOffset = getWaterOffest(cauldron.m_58900_());
        int waterLevel = (Integer) cauldron.m_58900_().m_61143_(AlchemistCauldronBlock.LEVEL);
        if (waterLevel > 0) {
            this.renderWater(cauldron, poseStack, bufferSource, packedLight, waterOffset);
        }
        NonNullList<ItemStack> floatingItems = cauldron.inputItems;
        for (int i = 0; i < floatingItems.size(); i++) {
            ItemStack itemStack = floatingItems.get(i);
            if (!itemStack.isEmpty()) {
                float f = waterLevel > 0 ? (float) cauldron.m_58904_().getGameTime() + partialTick : 15.0F;
                Vec2 floatOffset = this.getFloatingItemOffset(f, i * 587);
                float yRot = (f + (float) (i * 213)) / (float) (i + 1) * 1.5F;
                this.renderItem(itemStack, new Vec3((double) floatOffset.x, (double) (waterOffset + (float) i * 0.01F), (double) floatOffset.y), yRot, cauldron, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
            }
        }
    }

    public Vec2 getFloatingItemOffset(float time, int offset) {
        float xspeed = offset % 2 == 0 ? 0.0075F : 0.025F * (1.0F + (float) (offset % 88) * 0.001F);
        float yspeed = offset % 2 == 0 ? 0.025F : 0.0075F * (1.0F + (float) (offset % 88) * 0.001F);
        float x = (time + (float) offset) * xspeed;
        x = (Math.abs(x % 2.0F - 1.0F) + 1.0F) / 2.0F;
        float y = (time + (float) offset + 4356.0F) * yspeed;
        y = (Math.abs(y % 2.0F - 1.0F) + 1.0F) / 2.0F;
        x = Mth.lerp(x, -0.2F, 0.75F);
        y = Mth.lerp(y, -0.2F, 0.75F);
        return new Vec2(x, y);
    }

    public static float getWaterOffest(BlockState blockState) {
        return Mth.lerp((float) AlchemistCauldronBlock.getLevel(blockState) / 4.0F, 0.25F, 0.9F);
    }

    private void renderWater(AlchemistCauldronTile cauldron, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float waterOffset) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.beaconBeam(new ResourceLocation("irons_spellbooks", "textures/block/water_still.png"), true));
        long color = (long) cauldron.getAverageWaterColor();
        Vector3f rgb = this.colorFromLong(color);
        Matrix4f pose = poseStack.last().pose();
        int frames = 32;
        float frameSize = 1.0F / (float) frames;
        long frame = cauldron.m_58904_().getGameTime() / 3L % (long) frames;
        float min_u = 0.0F;
        float max_u = 1.0F;
        float min_v = frameSize * (float) frame;
        float max_v = frameSize * (float) (frame + 1L);
        consumer.vertex(pose, 1.0F, waterOffset, 0.0F).color(rgb.x(), rgb.y(), rgb.z(), 1.0F).uv(max_u, min_v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(pose, 0.0F, waterOffset, 0.0F).color(rgb.x(), rgb.y(), rgb.z(), 1.0F).uv(min_u, min_v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(pose, 0.0F, waterOffset, 1.0F).color(rgb.x(), rgb.y(), rgb.z(), 1.0F).uv(min_u, max_v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(pose, 1.0F, waterOffset, 1.0F).color(rgb.x(), rgb.y(), rgb.z(), 1.0F).uv(max_u, max_v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
    }

    private Vector3f colorFromLong(long color) {
        return new Vector3f((float) (color >> 16 & 255L) / 255.0F, (float) (color >> 8 & 255L) / 255.0F, (float) (color & 255L) / 255.0F);
    }

    private void renderItem(ItemStack itemStack, Vec3 offset, float yRot, AlchemistCauldronTile tile, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        int renderId = (int) tile.m_58899_().asLong();
        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(0.4F, 0.4F, 0.4F);
        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(tile.m_58904_(), tile.m_58899_()), packedOverlay, poseStack, bufferSource, tile.m_58904_(), renderId);
        poseStack.popPose();
    }
}