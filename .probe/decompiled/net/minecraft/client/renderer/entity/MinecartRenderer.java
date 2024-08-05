package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MinecartRenderer<T extends AbstractMinecart> extends EntityRenderer<T> {

    private static final ResourceLocation MINECART_LOCATION = new ResourceLocation("textures/entity/minecart.png");

    protected final EntityModel<T> model;

    private final BlockRenderDispatcher blockRenderer;

    public MinecartRenderer(EntityRendererProvider.Context entityRendererProviderContext0, ModelLayerLocation modelLayerLocation1) {
        super(entityRendererProviderContext0);
        this.f_114477_ = 0.7F;
        this.model = new MinecartModel<>(entityRendererProviderContext0.bakeLayer(modelLayerLocation1));
        this.blockRenderer = entityRendererProviderContext0.getBlockRenderDispatcher();
    }

    public void render(T t0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        super.render(t0, float1, float2, poseStack3, multiBufferSource4, int5);
        poseStack3.pushPose();
        long $$6 = (long) t0.m_19879_() * 493286711L;
        $$6 = $$6 * $$6 * 4392167121L + $$6 * 98761L;
        float $$7 = (((float) ($$6 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float $$8 = (((float) ($$6 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float $$9 = (((float) ($$6 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        poseStack3.translate($$7, $$8, $$9);
        double $$10 = Mth.lerp((double) float2, t0.f_19790_, t0.m_20185_());
        double $$11 = Mth.lerp((double) float2, t0.f_19791_, t0.m_20186_());
        double $$12 = Mth.lerp((double) float2, t0.f_19792_, t0.m_20189_());
        double $$13 = 0.3F;
        Vec3 $$14 = t0.getPos($$10, $$11, $$12);
        float $$15 = Mth.lerp(float2, t0.f_19860_, t0.m_146909_());
        if ($$14 != null) {
            Vec3 $$16 = t0.getPosOffs($$10, $$11, $$12, 0.3F);
            Vec3 $$17 = t0.getPosOffs($$10, $$11, $$12, -0.3F);
            if ($$16 == null) {
                $$16 = $$14;
            }
            if ($$17 == null) {
                $$17 = $$14;
            }
            poseStack3.translate($$14.x - $$10, ($$16.y + $$17.y) / 2.0 - $$11, $$14.z - $$12);
            Vec3 $$18 = $$17.add(-$$16.x, -$$16.y, -$$16.z);
            if ($$18.length() != 0.0) {
                $$18 = $$18.normalize();
                float1 = (float) (Math.atan2($$18.z, $$18.x) * 180.0 / Math.PI);
                $$15 = (float) (Math.atan($$18.y) * 73.0);
            }
        }
        poseStack3.translate(0.0F, 0.375F, 0.0F);
        poseStack3.mulPose(Axis.YP.rotationDegrees(180.0F - float1));
        poseStack3.mulPose(Axis.ZP.rotationDegrees(-$$15));
        float $$19 = (float) t0.getHurtTime() - float2;
        float $$20 = t0.getDamage() - float2;
        if ($$20 < 0.0F) {
            $$20 = 0.0F;
        }
        if ($$19 > 0.0F) {
            poseStack3.mulPose(Axis.XP.rotationDegrees(Mth.sin($$19) * $$19 * $$20 / 10.0F * (float) t0.getHurtDir()));
        }
        int $$21 = t0.getDisplayOffset();
        BlockState $$22 = t0.getDisplayBlockState();
        if ($$22.m_60799_() != RenderShape.INVISIBLE) {
            poseStack3.pushPose();
            float $$23 = 0.75F;
            poseStack3.scale(0.75F, 0.75F, 0.75F);
            poseStack3.translate(-0.5F, (float) ($$21 - 8) / 16.0F, 0.5F);
            poseStack3.mulPose(Axis.YP.rotationDegrees(90.0F));
            this.renderMinecartContents(t0, float2, $$22, poseStack3, multiBufferSource4, int5);
            poseStack3.popPose();
        }
        poseStack3.scale(-1.0F, -1.0F, 1.0F);
        this.model.setupAnim(t0, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer $$24 = multiBufferSource4.getBuffer(this.model.m_103119_(this.getTextureLocation(t0)));
        this.model.m_7695_(poseStack3, $$24, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack3.popPose();
    }

    public ResourceLocation getTextureLocation(T t0) {
        return MINECART_LOCATION;
    }

    protected void renderMinecartContents(T t0, float float1, BlockState blockState2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        this.blockRenderer.renderSingleBlock(blockState2, poseStack3, multiBufferSource4, int5, OverlayTexture.NO_OVERLAY);
    }
}