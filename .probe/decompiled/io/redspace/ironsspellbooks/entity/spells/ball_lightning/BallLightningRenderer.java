package io.redspace.ironsspellbooks.entity.spells.ball_lightning;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BallLightningRenderer extends EntityRenderer<BallLightning> {

    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("irons_spellbooks", "ball_lightning_model"), "main");

    private static final ResourceLocation[] SWIRL_TEXTURES = new ResourceLocation[] { IronsSpellbooks.id("textures/entity/ball_lightning/ball_lightning_0.png"), IronsSpellbooks.id("textures/entity/ball_lightning/ball_lightning_1.png"), IronsSpellbooks.id("textures/entity/ball_lightning/ball_lightning_2.png"), IronsSpellbooks.id("textures/entity/ball_lightning/ball_lightning_3.png"), IronsSpellbooks.id("textures/entity/ball_lightning/ball_lightning_4.png") };

    private final ModelPart orb;

    public BallLightningRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
        this.orb = modelpart.getChild("orb");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("orb", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 8, 8);
    }

    public void render(BallLightning entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.translate(0.0, entity.m_20191_().getYsize() * 0.5, 0.0);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
        for (int i = 0; i < 3; i++) {
            poseStack.pushPose();
            float r = 0.25F;
            float g = 0.8F;
            float b = 1.0F;
            r = Mth.clamp(r + r * (float) i, 0.0F, 1.0F);
            g = Mth.clamp(g + g * (float) i, 0.0F, 1.0F);
            b = Mth.clamp(b + b * (float) i, 0.0F, 1.0F);
            float f = (float) entity.f_19797_ + partialTicks + (float) (i * 777);
            float swirlX = Mth.cos(0.065F * f) * 180.0F;
            float swirlY = Mth.sin(0.065F * f) * 180.0F;
            float swirlZ = Mth.cos(0.065F * f + 5464.0F) * 180.0F;
            float scalePerLayer = 0.2F;
            poseStack.mulPose(Axis.XP.rotationDegrees(swirlX * (float) ((int) Math.pow(-1.0, (double) i))));
            poseStack.mulPose(Axis.YP.rotationDegrees(swirlY * (float) ((int) Math.pow(-1.0, (double) i))));
            poseStack.mulPose(Axis.ZP.rotationDegrees(swirlZ * (float) ((int) Math.pow(-1.0, (double) i))));
            consumer = bufferSource.getBuffer(MagicArrowRenderer.CustomRenderType.magic(this.getSwirlTextureLocation(entity, i * i)));
            float scale = 2.0F - (float) i * scalePerLayer;
            if (entity.f_19797_ > 70) {
                float f2 = ((float) entity.f_19797_ + partialTicks - 75.0F) * 0.4F;
                scale += i == 0 ? f2 : -f2;
            }
            poseStack.scale(scale, scale, scale);
            this.orb.render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
            poseStack.popPose();
        }
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(BallLightning entity) {
        return SWIRL_TEXTURES[0];
    }

    private ResourceLocation getSwirlTextureLocation(BallLightning entity, int offset) {
        int frame = (entity.f_19797_ + offset) % SWIRL_TEXTURES.length;
        return SWIRL_TEXTURES[frame];
    }
}