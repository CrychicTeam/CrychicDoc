package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.WitherSkull;

public class WitherSkullRenderer extends EntityRenderer<WitherSkull> {

    private static final ResourceLocation WITHER_INVULNERABLE_LOCATION = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");

    private static final ResourceLocation WITHER_LOCATION = new ResourceLocation("textures/entity/wither/wither.png");

    private final SkullModel model;

    public WitherSkullRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.model = new SkullModel(entityRendererProviderContext0.bakeLayer(ModelLayers.WITHER_SKULL));
    }

    public static LayerDefinition createSkullLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 35).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 64);
    }

    protected int getBlockLightLevel(WitherSkull witherSkull0, BlockPos blockPos1) {
        return 15;
    }

    public void render(WitherSkull witherSkull0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        poseStack3.scale(-1.0F, -1.0F, 1.0F);
        float $$6 = Mth.rotLerp(float2, witherSkull0.f_19859_, witherSkull0.m_146908_());
        float $$7 = Mth.lerp(float2, witherSkull0.f_19860_, witherSkull0.m_146909_());
        VertexConsumer $$8 = multiBufferSource4.getBuffer(this.model.m_103119_(this.getTextureLocation(witherSkull0)));
        this.model.setupAnim(0.0F, $$6, $$7);
        this.model.renderToBuffer(poseStack3, $$8, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack3.popPose();
        super.render(witherSkull0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(WitherSkull witherSkull0) {
        return witherSkull0.isDangerous() ? WITHER_INVULNERABLE_LOCATION : WITHER_LOCATION;
    }
}