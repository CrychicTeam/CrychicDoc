package com.simibubi.create.foundation.utility.ghost;

import com.jozufozu.flywheel.core.model.ModelUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;

public abstract class GhostBlockRenderer {

    private static final GhostBlockRenderer STANDARD = new GhostBlockRenderer.DefaultGhostBlockRenderer();

    private static final GhostBlockRenderer TRANSPARENT = new GhostBlockRenderer.TransparentGhostBlockRenderer();

    public static GhostBlockRenderer standard() {
        return STANDARD;
    }

    public static GhostBlockRenderer transparent() {
        return TRANSPARENT;
    }

    public abstract void render(PoseStack var1, SuperRenderTypeBuffer var2, Vec3 var3, GhostBlockParams var4);

    private static class DefaultGhostBlockRenderer extends GhostBlockRenderer {

        @Override
        public void render(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera, GhostBlockParams params) {
            ms.pushPose();
            BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
            ModelBlockRenderer renderer = dispatcher.getModelRenderer();
            BlockState state = params.state;
            BlockPos pos = params.pos;
            BakedModel model = dispatcher.getBlockModel(state);
            ms.pushPose();
            ms.translate((double) pos.m_123341_() - camera.x, (double) pos.m_123342_() - camera.y, (double) pos.m_123343_() - camera.z);
            for (RenderType layer : model.getRenderTypes(state, RandomSource.create(42L), ModelUtil.VIRTUAL_DATA)) {
                VertexConsumer vb = buffer.getEarlyBuffer(layer);
                renderer.renderModel(ms.last(), vb, state, model, 1.0F, 1.0F, 1.0F, 15728880, OverlayTexture.NO_OVERLAY, ModelUtil.VIRTUAL_DATA, layer);
            }
            ms.popPose();
        }
    }

    private static class TransparentGhostBlockRenderer extends GhostBlockRenderer {

        @Override
        public void render(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera, GhostBlockParams params) {
            ms.pushPose();
            Minecraft mc = Minecraft.getInstance();
            BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
            BlockState state = params.state;
            BlockPos pos = params.pos;
            float alpha = (Float) params.alphaSupplier.get() * 0.75F * PlacementHelpers.getCurrentAlpha();
            BakedModel model = dispatcher.getBlockModel(state);
            RenderType layer = RenderType.translucent();
            VertexConsumer vb = buffer.getEarlyBuffer(layer);
            ms.translate((double) pos.m_123341_() - camera.x, (double) pos.m_123342_() - camera.y, (double) pos.m_123343_() - camera.z);
            ms.translate(0.5, 0.5, 0.5);
            ms.scale(0.85F, 0.85F, 0.85F);
            ms.translate(-0.5, -0.5, -0.5);
            this.renderModel(ms.last(), vb, state, model, 1.0F, 1.0F, 1.0F, alpha, LevelRenderer.getLightColor(mc.level, pos), OverlayTexture.NO_OVERLAY, ModelUtil.VIRTUAL_DATA, layer);
            ms.popPose();
        }

        public void renderModel(PoseStack.Pose pose, VertexConsumer consumer, @Nullable BlockState state, BakedModel model, float red, float green, float blue, float alpha, int packedLight, int packedOverlay, ModelData modelData, RenderType renderType) {
            RandomSource random = RandomSource.create();
            for (Direction direction : Direction.values()) {
                random.setSeed(42L);
                renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, direction, random, modelData, null), packedLight, packedOverlay);
            }
            random.setSeed(42L);
            renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, null, random, modelData, null), packedLight, packedOverlay);
        }

        private static void renderQuadList(PoseStack.Pose pose, VertexConsumer consumer, float red, float green, float blue, float alpha, List<BakedQuad> quads, int packedLight, int packedOverlay) {
            for (BakedQuad quad : quads) {
                float f;
                float f1;
                float f2;
                if (quad.isTinted()) {
                    f = Mth.clamp(red, 0.0F, 1.0F);
                    f1 = Mth.clamp(green, 0.0F, 1.0F);
                    f2 = Mth.clamp(blue, 0.0F, 1.0F);
                } else {
                    f = 1.0F;
                    f1 = 1.0F;
                    f2 = 1.0F;
                }
                consumer.putBulkData(pose, quad, f, f1, f2, alpha, packedLight, packedOverlay, true);
            }
        }
    }
}