package net.minecraft.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class ScreenEffectRenderer {

    private static final ResourceLocation UNDERWATER_LOCATION = new ResourceLocation("textures/misc/underwater.png");

    public static void renderScreenEffect(Minecraft minecraft0, PoseStack poseStack1) {
        Player $$2 = minecraft0.player;
        if (!$$2.f_19794_) {
            BlockState $$3 = getViewBlockingState($$2);
            if ($$3 != null) {
                renderTex(minecraft0.getBlockRenderer().getBlockModelShaper().getParticleIcon($$3), poseStack1);
            }
        }
        if (!minecraft0.player.m_5833_()) {
            if (minecraft0.player.m_204029_(FluidTags.WATER)) {
                renderWater(minecraft0, poseStack1);
            }
            if (minecraft0.player.m_6060_()) {
                renderFire(minecraft0, poseStack1);
            }
        }
    }

    @Nullable
    private static BlockState getViewBlockingState(Player player0) {
        BlockPos.MutableBlockPos $$1 = new BlockPos.MutableBlockPos();
        for (int $$2 = 0; $$2 < 8; $$2++) {
            double $$3 = player0.m_20185_() + (double) (((float) (($$2 >> 0) % 2) - 0.5F) * player0.m_20205_() * 0.8F);
            double $$4 = player0.m_20188_() + (double) (((float) (($$2 >> 1) % 2) - 0.5F) * 0.1F);
            double $$5 = player0.m_20189_() + (double) (((float) (($$2 >> 2) % 2) - 0.5F) * player0.m_20205_() * 0.8F);
            $$1.set($$3, $$4, $$5);
            BlockState $$6 = player0.m_9236_().getBlockState($$1);
            if ($$6.m_60799_() != RenderShape.INVISIBLE && $$6.m_60831_(player0.m_9236_(), $$1)) {
                return $$6;
            }
        }
        return null;
    }

    private static void renderTex(TextureAtlasSprite textureAtlasSprite0, PoseStack poseStack1) {
        RenderSystem.setShaderTexture(0, textureAtlasSprite0.atlasLocation());
        RenderSystem.setShader(GameRenderer::m_172814_);
        BufferBuilder $$2 = Tesselator.getInstance().getBuilder();
        float $$3 = 0.1F;
        float $$4 = -1.0F;
        float $$5 = 1.0F;
        float $$6 = -1.0F;
        float $$7 = 1.0F;
        float $$8 = -0.5F;
        float $$9 = textureAtlasSprite0.getU0();
        float $$10 = textureAtlasSprite0.getU1();
        float $$11 = textureAtlasSprite0.getV0();
        float $$12 = textureAtlasSprite0.getV1();
        Matrix4f $$13 = poseStack1.last().pose();
        $$2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        $$2.m_252986_($$13, -1.0F, -1.0F, -0.5F).color(0.1F, 0.1F, 0.1F, 1.0F).uv($$10, $$12).endVertex();
        $$2.m_252986_($$13, 1.0F, -1.0F, -0.5F).color(0.1F, 0.1F, 0.1F, 1.0F).uv($$9, $$12).endVertex();
        $$2.m_252986_($$13, 1.0F, 1.0F, -0.5F).color(0.1F, 0.1F, 0.1F, 1.0F).uv($$9, $$11).endVertex();
        $$2.m_252986_($$13, -1.0F, 1.0F, -0.5F).color(0.1F, 0.1F, 0.1F, 1.0F).uv($$10, $$11).endVertex();
        BufferUploader.drawWithShader($$2.end());
    }

    private static void renderWater(Minecraft minecraft0, PoseStack poseStack1) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, UNDERWATER_LOCATION);
        BufferBuilder $$2 = Tesselator.getInstance().getBuilder();
        BlockPos $$3 = BlockPos.containing(minecraft0.player.m_20185_(), minecraft0.player.m_20188_(), minecraft0.player.m_20189_());
        float $$4 = LightTexture.getBrightness(minecraft0.player.m_9236_().dimensionType(), minecraft0.player.m_9236_().m_46803_($$3));
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor($$4, $$4, $$4, 0.1F);
        float $$5 = 4.0F;
        float $$6 = -1.0F;
        float $$7 = 1.0F;
        float $$8 = -1.0F;
        float $$9 = 1.0F;
        float $$10 = -0.5F;
        float $$11 = -minecraft0.player.m_146908_() / 64.0F;
        float $$12 = minecraft0.player.m_146909_() / 64.0F;
        Matrix4f $$13 = poseStack1.last().pose();
        $$2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        $$2.m_252986_($$13, -1.0F, -1.0F, -0.5F).uv(4.0F + $$11, 4.0F + $$12).endVertex();
        $$2.m_252986_($$13, 1.0F, -1.0F, -0.5F).uv(0.0F + $$11, 4.0F + $$12).endVertex();
        $$2.m_252986_($$13, 1.0F, 1.0F, -0.5F).uv(0.0F + $$11, 0.0F + $$12).endVertex();
        $$2.m_252986_($$13, -1.0F, 1.0F, -0.5F).uv(4.0F + $$11, 0.0F + $$12).endVertex();
        BufferUploader.drawWithShader($$2.end());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    private static void renderFire(Minecraft minecraft0, PoseStack poseStack1) {
        BufferBuilder $$2 = Tesselator.getInstance().getBuilder();
        RenderSystem.setShader(GameRenderer::m_172814_);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        TextureAtlasSprite $$3 = ModelBakery.FIRE_1.sprite();
        RenderSystem.setShaderTexture(0, $$3.atlasLocation());
        float $$4 = $$3.getU0();
        float $$5 = $$3.getU1();
        float $$6 = ($$4 + $$5) / 2.0F;
        float $$7 = $$3.getV0();
        float $$8 = $$3.getV1();
        float $$9 = ($$7 + $$8) / 2.0F;
        float $$10 = $$3.uvShrinkRatio();
        float $$11 = Mth.lerp($$10, $$4, $$6);
        float $$12 = Mth.lerp($$10, $$5, $$6);
        float $$13 = Mth.lerp($$10, $$7, $$9);
        float $$14 = Mth.lerp($$10, $$8, $$9);
        float $$15 = 1.0F;
        for (int $$16 = 0; $$16 < 2; $$16++) {
            poseStack1.pushPose();
            float $$17 = -0.5F;
            float $$18 = 0.5F;
            float $$19 = -0.5F;
            float $$20 = 0.5F;
            float $$21 = -0.5F;
            poseStack1.translate((float) (-($$16 * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            poseStack1.mulPose(Axis.YP.rotationDegrees((float) ($$16 * 2 - 1) * 10.0F));
            Matrix4f $$22 = poseStack1.last().pose();
            $$2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            $$2.m_252986_($$22, -0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv($$12, $$14).endVertex();
            $$2.m_252986_($$22, 0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv($$11, $$14).endVertex();
            $$2.m_252986_($$22, 0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv($$11, $$13).endVertex();
            $$2.m_252986_($$22, -0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv($$12, $$13).endVertex();
            BufferUploader.drawWithShader($$2.end());
            poseStack1.popPose();
        }
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }
}