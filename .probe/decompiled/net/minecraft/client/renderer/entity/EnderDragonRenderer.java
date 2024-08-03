package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class EnderDragonRenderer extends EntityRenderer<EnderDragon> {

    public static final ResourceLocation CRYSTAL_BEAM_LOCATION = new ResourceLocation("textures/entity/end_crystal/end_crystal_beam.png");

    private static final ResourceLocation DRAGON_EXPLODING_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");

    private static final ResourceLocation DRAGON_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon.png");

    private static final ResourceLocation DRAGON_EYES_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");

    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(DRAGON_LOCATION);

    private static final RenderType DECAL = RenderType.entityDecal(DRAGON_LOCATION);

    private static final RenderType EYES = RenderType.eyes(DRAGON_EYES_LOCATION);

    private static final RenderType BEAM = RenderType.entitySmoothCutout(CRYSTAL_BEAM_LOCATION);

    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    private final EnderDragonRenderer.DragonModel model;

    public EnderDragonRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.f_114477_ = 0.5F;
        this.model = new EnderDragonRenderer.DragonModel(entityRendererProviderContext0.bakeLayer(ModelLayers.ENDER_DRAGON));
    }

    public void render(EnderDragon enderDragon0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        float $$6 = (float) enderDragon0.getLatencyPos(7, float2)[0];
        float $$7 = (float) (enderDragon0.getLatencyPos(5, float2)[1] - enderDragon0.getLatencyPos(10, float2)[1]);
        poseStack3.mulPose(Axis.YP.rotationDegrees(-$$6));
        poseStack3.mulPose(Axis.XP.rotationDegrees($$7 * 10.0F));
        poseStack3.translate(0.0F, 0.0F, 1.0F);
        poseStack3.scale(-1.0F, -1.0F, 1.0F);
        poseStack3.translate(0.0F, -1.501F, 0.0F);
        boolean $$8 = enderDragon0.f_20916_ > 0;
        this.model.prepareMobModel(enderDragon0, 0.0F, 0.0F, float2);
        if (enderDragon0.dragonDeathTime > 0) {
            float $$9 = (float) enderDragon0.dragonDeathTime / 200.0F;
            VertexConsumer $$10 = multiBufferSource4.getBuffer(RenderType.dragonExplosionAlpha(DRAGON_EXPLODING_LOCATION));
            this.model.renderToBuffer(poseStack3, $$10, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, $$9);
            VertexConsumer $$11 = multiBufferSource4.getBuffer(DECAL);
            this.model.renderToBuffer(poseStack3, $$11, int5, OverlayTexture.pack(0.0F, $$8), 1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            VertexConsumer $$12 = multiBufferSource4.getBuffer(RENDER_TYPE);
            this.model.renderToBuffer(poseStack3, $$12, int5, OverlayTexture.pack(0.0F, $$8), 1.0F, 1.0F, 1.0F, 1.0F);
        }
        VertexConsumer $$13 = multiBufferSource4.getBuffer(EYES);
        this.model.renderToBuffer(poseStack3, $$13, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (enderDragon0.dragonDeathTime > 0) {
            float $$14 = ((float) enderDragon0.dragonDeathTime + float2) / 200.0F;
            float $$15 = Math.min($$14 > 0.8F ? ($$14 - 0.8F) / 0.2F : 0.0F, 1.0F);
            RandomSource $$16 = RandomSource.create(432L);
            VertexConsumer $$17 = multiBufferSource4.getBuffer(RenderType.lightning());
            poseStack3.pushPose();
            poseStack3.translate(0.0F, -1.0F, -2.0F);
            for (int $$18 = 0; (float) $$18 < ($$14 + $$14 * $$14) / 2.0F * 60.0F; $$18++) {
                poseStack3.mulPose(Axis.XP.rotationDegrees($$16.nextFloat() * 360.0F));
                poseStack3.mulPose(Axis.YP.rotationDegrees($$16.nextFloat() * 360.0F));
                poseStack3.mulPose(Axis.ZP.rotationDegrees($$16.nextFloat() * 360.0F));
                poseStack3.mulPose(Axis.XP.rotationDegrees($$16.nextFloat() * 360.0F));
                poseStack3.mulPose(Axis.YP.rotationDegrees($$16.nextFloat() * 360.0F));
                poseStack3.mulPose(Axis.ZP.rotationDegrees($$16.nextFloat() * 360.0F + $$14 * 90.0F));
                float $$19 = $$16.nextFloat() * 20.0F + 5.0F + $$15 * 10.0F;
                float $$20 = $$16.nextFloat() * 2.0F + 1.0F + $$15 * 2.0F;
                Matrix4f $$21 = poseStack3.last().pose();
                int $$22 = (int) (255.0F * (1.0F - $$15));
                vertex01($$17, $$21, $$22);
                vertex2($$17, $$21, $$19, $$20);
                vertex3($$17, $$21, $$19, $$20);
                vertex01($$17, $$21, $$22);
                vertex3($$17, $$21, $$19, $$20);
                vertex4($$17, $$21, $$19, $$20);
                vertex01($$17, $$21, $$22);
                vertex4($$17, $$21, $$19, $$20);
                vertex2($$17, $$21, $$19, $$20);
            }
            poseStack3.popPose();
        }
        poseStack3.popPose();
        if (enderDragon0.nearestCrystal != null) {
            poseStack3.pushPose();
            float $$23 = (float) (enderDragon0.nearestCrystal.m_20185_() - Mth.lerp((double) float2, enderDragon0.f_19854_, enderDragon0.m_20185_()));
            float $$24 = (float) (enderDragon0.nearestCrystal.m_20186_() - Mth.lerp((double) float2, enderDragon0.f_19855_, enderDragon0.m_20186_()));
            float $$25 = (float) (enderDragon0.nearestCrystal.m_20189_() - Mth.lerp((double) float2, enderDragon0.f_19856_, enderDragon0.m_20189_()));
            renderCrystalBeams($$23, $$24 + EndCrystalRenderer.getY(enderDragon0.nearestCrystal, float2), $$25, float2, enderDragon0.f_19797_, poseStack3, multiBufferSource4, int5);
            poseStack3.popPose();
        }
        super.render(enderDragon0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    private static void vertex01(VertexConsumer vertexConsumer0, Matrix4f matrixF1, int int2) {
        vertexConsumer0.vertex(matrixF1, 0.0F, 0.0F, 0.0F).color(255, 255, 255, int2).endVertex();
    }

    private static void vertex2(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3) {
        vertexConsumer0.vertex(matrixF1, -HALF_SQRT_3 * float3, float2, -0.5F * float3).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex3(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3) {
        vertexConsumer0.vertex(matrixF1, HALF_SQRT_3 * float3, float2, -0.5F * float3).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex4(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3) {
        vertexConsumer0.vertex(matrixF1, 0.0F, float2, 1.0F * float3).color(255, 0, 255, 0).endVertex();
    }

    public static void renderCrystalBeams(float float0, float float1, float float2, float float3, int int4, PoseStack poseStack5, MultiBufferSource multiBufferSource6, int int7) {
        float $$8 = Mth.sqrt(float0 * float0 + float2 * float2);
        float $$9 = Mth.sqrt(float0 * float0 + float1 * float1 + float2 * float2);
        poseStack5.pushPose();
        poseStack5.translate(0.0F, 2.0F, 0.0F);
        poseStack5.mulPose(Axis.YP.rotation((float) (-Math.atan2((double) float2, (double) float0)) - (float) (Math.PI / 2)));
        poseStack5.mulPose(Axis.XP.rotation((float) (-Math.atan2((double) $$8, (double) float1)) - (float) (Math.PI / 2)));
        VertexConsumer $$10 = multiBufferSource6.getBuffer(BEAM);
        float $$11 = 0.0F - ((float) int4 + float3) * 0.01F;
        float $$12 = Mth.sqrt(float0 * float0 + float1 * float1 + float2 * float2) / 32.0F - ((float) int4 + float3) * 0.01F;
        int $$13 = 8;
        float $$14 = 0.0F;
        float $$15 = 0.75F;
        float $$16 = 0.0F;
        PoseStack.Pose $$17 = poseStack5.last();
        Matrix4f $$18 = $$17.pose();
        Matrix3f $$19 = $$17.normal();
        for (int $$20 = 1; $$20 <= 8; $$20++) {
            float $$21 = Mth.sin((float) $$20 * (float) (Math.PI * 2) / 8.0F) * 0.75F;
            float $$22 = Mth.cos((float) $$20 * (float) (Math.PI * 2) / 8.0F) * 0.75F;
            float $$23 = (float) $$20 / 8.0F;
            $$10.vertex($$18, $$14 * 0.2F, $$15 * 0.2F, 0.0F).color(0, 0, 0, 255).uv($$16, $$11).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int7).normal($$19, 0.0F, -1.0F, 0.0F).endVertex();
            $$10.vertex($$18, $$14, $$15, $$9).color(255, 255, 255, 255).uv($$16, $$12).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int7).normal($$19, 0.0F, -1.0F, 0.0F).endVertex();
            $$10.vertex($$18, $$21, $$22, $$9).color(255, 255, 255, 255).uv($$23, $$12).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int7).normal($$19, 0.0F, -1.0F, 0.0F).endVertex();
            $$10.vertex($$18, $$21 * 0.2F, $$22 * 0.2F, 0.0F).color(0, 0, 0, 255).uv($$23, $$11).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int7).normal($$19, 0.0F, -1.0F, 0.0F).endVertex();
            $$14 = $$21;
            $$15 = $$22;
            $$16 = $$23;
        }
        poseStack5.popPose();
    }

    public ResourceLocation getTextureLocation(EnderDragon enderDragon0) {
        return DRAGON_LOCATION;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        float $$2 = -16.0F;
        PartDefinition $$3 = $$1.addOrReplaceChild("head", CubeListBuilder.create().addBox("upperlip", -6.0F, -1.0F, -24.0F, 12, 5, 16, 176, 44).addBox("upperhead", -8.0F, -8.0F, -10.0F, 16, 16, 16, 112, 30).mirror().addBox("scale", -5.0F, -12.0F, -4.0F, 2, 4, 6, 0, 0).addBox("nostril", -5.0F, -3.0F, -22.0F, 2, 2, 4, 112, 0).mirror().addBox("scale", 3.0F, -12.0F, -4.0F, 2, 4, 6, 0, 0).addBox("nostril", 3.0F, -3.0F, -22.0F, 2, 2, 4, 112, 0), PartPose.ZERO);
        $$3.addOrReplaceChild("jaw", CubeListBuilder.create().addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16, 176, 65), PartPose.offset(0.0F, 4.0F, -8.0F));
        $$1.addOrReplaceChild("neck", CubeListBuilder.create().addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10, 192, 104).addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6, 48, 0), PartPose.ZERO);
        $$1.addOrReplaceChild("body", CubeListBuilder.create().addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64, 0, 0).addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12, 220, 53).addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12, 220, 53).addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12, 220, 53), PartPose.offset(0.0F, 4.0F, 8.0F));
        PartDefinition $$4 = $$1.addOrReplaceChild("left_wing", CubeListBuilder.create().mirror().addBox("bone", 0.0F, -4.0F, -4.0F, 56, 8, 8, 112, 88).addBox("skin", 0.0F, 0.0F, 2.0F, 56, 0, 56, -56, 88), PartPose.offset(12.0F, 5.0F, 2.0F));
        $$4.addOrReplaceChild("left_wing_tip", CubeListBuilder.create().mirror().addBox("bone", 0.0F, -2.0F, -2.0F, 56, 4, 4, 112, 136).addBox("skin", 0.0F, 0.0F, 2.0F, 56, 0, 56, -56, 144), PartPose.offset(56.0F, 0.0F, 0.0F));
        PartDefinition $$5 = $$1.addOrReplaceChild("left_front_leg", CubeListBuilder.create().addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8, 112, 104), PartPose.offset(12.0F, 20.0F, 2.0F));
        PartDefinition $$6 = $$5.addOrReplaceChild("left_front_leg_tip", CubeListBuilder.create().addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6, 226, 138), PartPose.offset(0.0F, 20.0F, -1.0F));
        $$6.addOrReplaceChild("left_front_foot", CubeListBuilder.create().addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16, 144, 104), PartPose.offset(0.0F, 23.0F, 0.0F));
        PartDefinition $$7 = $$1.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16, 0, 0), PartPose.offset(16.0F, 16.0F, 42.0F));
        PartDefinition $$8 = $$7.addOrReplaceChild("left_hind_leg_tip", CubeListBuilder.create().addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12, 196, 0), PartPose.offset(0.0F, 32.0F, -4.0F));
        $$8.addOrReplaceChild("left_hind_foot", CubeListBuilder.create().addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24, 112, 0), PartPose.offset(0.0F, 31.0F, 4.0F));
        PartDefinition $$9 = $$1.addOrReplaceChild("right_wing", CubeListBuilder.create().addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8, 112, 88).addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56, -56, 88), PartPose.offset(-12.0F, 5.0F, 2.0F));
        $$9.addOrReplaceChild("right_wing_tip", CubeListBuilder.create().addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4, 112, 136).addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56, -56, 144), PartPose.offset(-56.0F, 0.0F, 0.0F));
        PartDefinition $$10 = $$1.addOrReplaceChild("right_front_leg", CubeListBuilder.create().addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8, 112, 104), PartPose.offset(-12.0F, 20.0F, 2.0F));
        PartDefinition $$11 = $$10.addOrReplaceChild("right_front_leg_tip", CubeListBuilder.create().addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6, 226, 138), PartPose.offset(0.0F, 20.0F, -1.0F));
        $$11.addOrReplaceChild("right_front_foot", CubeListBuilder.create().addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16, 144, 104), PartPose.offset(0.0F, 23.0F, 0.0F));
        PartDefinition $$12 = $$1.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16, 0, 0), PartPose.offset(-16.0F, 16.0F, 42.0F));
        PartDefinition $$13 = $$12.addOrReplaceChild("right_hind_leg_tip", CubeListBuilder.create().addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12, 196, 0), PartPose.offset(0.0F, 32.0F, -4.0F));
        $$13.addOrReplaceChild("right_hind_foot", CubeListBuilder.create().addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24, 112, 0), PartPose.offset(0.0F, 31.0F, 4.0F));
        return LayerDefinition.create($$0, 256, 256);
    }

    public static class DragonModel extends EntityModel<EnderDragon> {

        private final ModelPart head;

        private final ModelPart neck;

        private final ModelPart jaw;

        private final ModelPart body;

        private final ModelPart leftWing;

        private final ModelPart leftWingTip;

        private final ModelPart leftFrontLeg;

        private final ModelPart leftFrontLegTip;

        private final ModelPart leftFrontFoot;

        private final ModelPart leftRearLeg;

        private final ModelPart leftRearLegTip;

        private final ModelPart leftRearFoot;

        private final ModelPart rightWing;

        private final ModelPart rightWingTip;

        private final ModelPart rightFrontLeg;

        private final ModelPart rightFrontLegTip;

        private final ModelPart rightFrontFoot;

        private final ModelPart rightRearLeg;

        private final ModelPart rightRearLegTip;

        private final ModelPart rightRearFoot;

        @Nullable
        private EnderDragon entity;

        private float a;

        public DragonModel(ModelPart modelPart0) {
            this.head = modelPart0.getChild("head");
            this.jaw = this.head.getChild("jaw");
            this.neck = modelPart0.getChild("neck");
            this.body = modelPart0.getChild("body");
            this.leftWing = modelPart0.getChild("left_wing");
            this.leftWingTip = this.leftWing.getChild("left_wing_tip");
            this.leftFrontLeg = modelPart0.getChild("left_front_leg");
            this.leftFrontLegTip = this.leftFrontLeg.getChild("left_front_leg_tip");
            this.leftFrontFoot = this.leftFrontLegTip.getChild("left_front_foot");
            this.leftRearLeg = modelPart0.getChild("left_hind_leg");
            this.leftRearLegTip = this.leftRearLeg.getChild("left_hind_leg_tip");
            this.leftRearFoot = this.leftRearLegTip.getChild("left_hind_foot");
            this.rightWing = modelPart0.getChild("right_wing");
            this.rightWingTip = this.rightWing.getChild("right_wing_tip");
            this.rightFrontLeg = modelPart0.getChild("right_front_leg");
            this.rightFrontLegTip = this.rightFrontLeg.getChild("right_front_leg_tip");
            this.rightFrontFoot = this.rightFrontLegTip.getChild("right_front_foot");
            this.rightRearLeg = modelPart0.getChild("right_hind_leg");
            this.rightRearLegTip = this.rightRearLeg.getChild("right_hind_leg_tip");
            this.rightRearFoot = this.rightRearLegTip.getChild("right_hind_foot");
        }

        public void prepareMobModel(EnderDragon enderDragon0, float float1, float float2, float float3) {
            this.entity = enderDragon0;
            this.a = float3;
        }

        public void setupAnim(EnderDragon enderDragon0, float float1, float float2, float float3, float float4, float float5) {
        }

        @Override
        public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
            poseStack0.pushPose();
            float $$8 = Mth.lerp(this.a, this.entity.oFlapTime, this.entity.flapTime);
            this.jaw.xRot = (float) (Math.sin((double) ($$8 * (float) (Math.PI * 2))) + 1.0) * 0.2F;
            float $$9 = (float) (Math.sin((double) ($$8 * (float) (Math.PI * 2) - 1.0F)) + 1.0);
            $$9 = ($$9 * $$9 + $$9 * 2.0F) * 0.05F;
            poseStack0.translate(0.0F, $$9 - 2.0F, -3.0F);
            poseStack0.mulPose(Axis.XP.rotationDegrees($$9 * 2.0F));
            float $$10 = 0.0F;
            float $$11 = 20.0F;
            float $$12 = -12.0F;
            float $$13 = 1.5F;
            double[] $$14 = this.entity.getLatencyPos(6, this.a);
            float $$15 = Mth.wrapDegrees((float) (this.entity.getLatencyPos(5, this.a)[0] - this.entity.getLatencyPos(10, this.a)[0]));
            float $$16 = Mth.wrapDegrees((float) (this.entity.getLatencyPos(5, this.a)[0] + (double) ($$15 / 2.0F)));
            float $$17 = $$8 * (float) (Math.PI * 2);
            for (int $$18 = 0; $$18 < 5; $$18++) {
                double[] $$19 = this.entity.getLatencyPos(5 - $$18, this.a);
                float $$20 = (float) Math.cos((double) ((float) $$18 * 0.45F + $$17)) * 0.15F;
                this.neck.yRot = Mth.wrapDegrees((float) ($$19[0] - $$14[0])) * (float) (Math.PI / 180.0) * 1.5F;
                this.neck.xRot = $$20 + this.entity.getHeadPartYOffset($$18, $$14, $$19) * (float) (Math.PI / 180.0) * 1.5F * 5.0F;
                this.neck.zRot = -Mth.wrapDegrees((float) ($$19[0] - (double) $$16)) * (float) (Math.PI / 180.0) * 1.5F;
                this.neck.y = $$11;
                this.neck.z = $$12;
                this.neck.x = $$10;
                $$11 += Mth.sin(this.neck.xRot) * 10.0F;
                $$12 -= Mth.cos(this.neck.yRot) * Mth.cos(this.neck.xRot) * 10.0F;
                $$10 -= Mth.sin(this.neck.yRot) * Mth.cos(this.neck.xRot) * 10.0F;
                this.neck.render(poseStack0, vertexConsumer1, int2, int3, 1.0F, 1.0F, 1.0F, float7);
            }
            this.head.y = $$11;
            this.head.z = $$12;
            this.head.x = $$10;
            double[] $$21 = this.entity.getLatencyPos(0, this.a);
            this.head.yRot = Mth.wrapDegrees((float) ($$21[0] - $$14[0])) * (float) (Math.PI / 180.0);
            this.head.xRot = Mth.wrapDegrees(this.entity.getHeadPartYOffset(6, $$14, $$21)) * (float) (Math.PI / 180.0) * 1.5F * 5.0F;
            this.head.zRot = -Mth.wrapDegrees((float) ($$21[0] - (double) $$16)) * (float) (Math.PI / 180.0);
            this.head.render(poseStack0, vertexConsumer1, int2, int3, 1.0F, 1.0F, 1.0F, float7);
            poseStack0.pushPose();
            poseStack0.translate(0.0F, 1.0F, 0.0F);
            poseStack0.mulPose(Axis.ZP.rotationDegrees(-$$15 * 1.5F));
            poseStack0.translate(0.0F, -1.0F, 0.0F);
            this.body.zRot = 0.0F;
            this.body.render(poseStack0, vertexConsumer1, int2, int3, 1.0F, 1.0F, 1.0F, float7);
            float $$22 = $$8 * (float) (Math.PI * 2);
            this.leftWing.xRot = 0.125F - (float) Math.cos((double) $$22) * 0.2F;
            this.leftWing.yRot = -0.25F;
            this.leftWing.zRot = -((float) (Math.sin((double) $$22) + 0.125)) * 0.8F;
            this.leftWingTip.zRot = (float) (Math.sin((double) ($$22 + 2.0F)) + 0.5) * 0.75F;
            this.rightWing.xRot = this.leftWing.xRot;
            this.rightWing.yRot = -this.leftWing.yRot;
            this.rightWing.zRot = -this.leftWing.zRot;
            this.rightWingTip.zRot = -this.leftWingTip.zRot;
            this.renderSide(poseStack0, vertexConsumer1, int2, int3, $$9, this.leftWing, this.leftFrontLeg, this.leftFrontLegTip, this.leftFrontFoot, this.leftRearLeg, this.leftRearLegTip, this.leftRearFoot, float7);
            this.renderSide(poseStack0, vertexConsumer1, int2, int3, $$9, this.rightWing, this.rightFrontLeg, this.rightFrontLegTip, this.rightFrontFoot, this.rightRearLeg, this.rightRearLegTip, this.rightRearFoot, float7);
            poseStack0.popPose();
            float $$23 = -Mth.sin($$8 * (float) (Math.PI * 2)) * 0.0F;
            $$17 = $$8 * (float) (Math.PI * 2);
            $$11 = 10.0F;
            $$12 = 60.0F;
            $$10 = 0.0F;
            $$14 = this.entity.getLatencyPos(11, this.a);
            for (int $$24 = 0; $$24 < 12; $$24++) {
                $$21 = this.entity.getLatencyPos(12 + $$24, this.a);
                $$23 += Mth.sin((float) $$24 * 0.45F + $$17) * 0.05F;
                this.neck.yRot = (Mth.wrapDegrees((float) ($$21[0] - $$14[0])) * 1.5F + 180.0F) * (float) (Math.PI / 180.0);
                this.neck.xRot = $$23 + (float) ($$21[1] - $$14[1]) * (float) (Math.PI / 180.0) * 1.5F * 5.0F;
                this.neck.zRot = Mth.wrapDegrees((float) ($$21[0] - (double) $$16)) * (float) (Math.PI / 180.0) * 1.5F;
                this.neck.y = $$11;
                this.neck.z = $$12;
                this.neck.x = $$10;
                $$11 += Mth.sin(this.neck.xRot) * 10.0F;
                $$12 -= Mth.cos(this.neck.yRot) * Mth.cos(this.neck.xRot) * 10.0F;
                $$10 -= Mth.sin(this.neck.yRot) * Mth.cos(this.neck.xRot) * 10.0F;
                this.neck.render(poseStack0, vertexConsumer1, int2, int3, 1.0F, 1.0F, 1.0F, float7);
            }
            poseStack0.popPose();
        }

        private void renderSide(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, ModelPart modelPart5, ModelPart modelPart6, ModelPart modelPart7, ModelPart modelPart8, ModelPart modelPart9, ModelPart modelPart10, ModelPart modelPart11, float float12) {
            modelPart9.xRot = 1.0F + float4 * 0.1F;
            modelPart10.xRot = 0.5F + float4 * 0.1F;
            modelPart11.xRot = 0.75F + float4 * 0.1F;
            modelPart6.xRot = 1.3F + float4 * 0.1F;
            modelPart7.xRot = -0.5F - float4 * 0.1F;
            modelPart8.xRot = 0.75F + float4 * 0.1F;
            modelPart5.render(poseStack0, vertexConsumer1, int2, int3, 1.0F, 1.0F, 1.0F, float12);
            modelPart6.render(poseStack0, vertexConsumer1, int2, int3, 1.0F, 1.0F, 1.0F, float12);
            modelPart9.render(poseStack0, vertexConsumer1, int2, int3, 1.0F, 1.0F, 1.0F, float12);
        }
    }
}