package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.joml.Quaternionf;

public class EndCrystalRenderer extends EntityRenderer<EndCrystal> {

    private static final ResourceLocation END_CRYSTAL_LOCATION = new ResourceLocation("textures/entity/end_crystal/end_crystal.png");

    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(END_CRYSTAL_LOCATION);

    private static final float SIN_45 = (float) Math.sin(Math.PI / 4);

    private static final String GLASS = "glass";

    private static final String BASE = "base";

    private final ModelPart cube;

    private final ModelPart glass;

    private final ModelPart base;

    public EndCrystalRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.f_114477_ = 0.5F;
        ModelPart $$1 = entityRendererProviderContext0.bakeLayer(ModelLayers.END_CRYSTAL);
        this.glass = $$1.getChild("glass");
        this.cube = $$1.getChild("cube");
        this.base = $$1.getChild("base");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("glass", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 16).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 4.0F, 12.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 32);
    }

    public void render(EndCrystal endCrystal0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        float $$6 = getY(endCrystal0, float2);
        float $$7 = ((float) endCrystal0.time + float2) * 3.0F;
        VertexConsumer $$8 = multiBufferSource4.getBuffer(RENDER_TYPE);
        poseStack3.pushPose();
        poseStack3.scale(2.0F, 2.0F, 2.0F);
        poseStack3.translate(0.0F, -0.5F, 0.0F);
        int $$9 = OverlayTexture.NO_OVERLAY;
        if (endCrystal0.showsBottom()) {
            this.base.render(poseStack3, $$8, int5, $$9);
        }
        poseStack3.mulPose(Axis.YP.rotationDegrees($$7));
        poseStack3.translate(0.0F, 1.5F + $$6 / 2.0F, 0.0F);
        poseStack3.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0.0F, SIN_45));
        this.glass.render(poseStack3, $$8, int5, $$9);
        float $$10 = 0.875F;
        poseStack3.scale(0.875F, 0.875F, 0.875F);
        poseStack3.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0.0F, SIN_45));
        poseStack3.mulPose(Axis.YP.rotationDegrees($$7));
        this.glass.render(poseStack3, $$8, int5, $$9);
        poseStack3.scale(0.875F, 0.875F, 0.875F);
        poseStack3.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0.0F, SIN_45));
        poseStack3.mulPose(Axis.YP.rotationDegrees($$7));
        this.cube.render(poseStack3, $$8, int5, $$9);
        poseStack3.popPose();
        poseStack3.popPose();
        BlockPos $$11 = endCrystal0.getBeamTarget();
        if ($$11 != null) {
            float $$12 = (float) $$11.m_123341_() + 0.5F;
            float $$13 = (float) $$11.m_123342_() + 0.5F;
            float $$14 = (float) $$11.m_123343_() + 0.5F;
            float $$15 = (float) ((double) $$12 - endCrystal0.m_20185_());
            float $$16 = (float) ((double) $$13 - endCrystal0.m_20186_());
            float $$17 = (float) ((double) $$14 - endCrystal0.m_20189_());
            poseStack3.translate($$15, $$16, $$17);
            EnderDragonRenderer.renderCrystalBeams(-$$15, -$$16 + $$6, -$$17, float2, endCrystal0.time, poseStack3, multiBufferSource4, int5);
        }
        super.render(endCrystal0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public static float getY(EndCrystal endCrystal0, float float1) {
        float $$2 = (float) endCrystal0.time + float1;
        float $$3 = Mth.sin($$2 * 0.2F) / 2.0F + 0.5F;
        $$3 = ($$3 * $$3 + $$3) * 0.4F;
        return $$3 - 1.4F;
    }

    public ResourceLocation getTextureLocation(EndCrystal endCrystal0) {
        return END_CRYSTAL_LOCATION;
    }

    public boolean shouldRender(EndCrystal endCrystal0, Frustum frustum1, double double2, double double3, double double4) {
        return super.shouldRender(endCrystal0, frustum1, double2, double3, double4) || endCrystal0.getBeamTarget() != null;
    }
}