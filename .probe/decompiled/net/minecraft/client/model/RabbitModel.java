package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Rabbit;

public class RabbitModel<T extends Rabbit> extends EntityModel<T> {

    private static final float REAR_JUMP_ANGLE = 50.0F;

    private static final float FRONT_JUMP_ANGLE = -40.0F;

    private static final String LEFT_HAUNCH = "left_haunch";

    private static final String RIGHT_HAUNCH = "right_haunch";

    private final ModelPart leftRearFoot;

    private final ModelPart rightRearFoot;

    private final ModelPart leftHaunch;

    private final ModelPart rightHaunch;

    private final ModelPart body;

    private final ModelPart leftFrontLeg;

    private final ModelPart rightFrontLeg;

    private final ModelPart head;

    private final ModelPart rightEar;

    private final ModelPart leftEar;

    private final ModelPart tail;

    private final ModelPart nose;

    private float jumpRotation;

    private static final float NEW_SCALE = 0.6F;

    public RabbitModel(ModelPart modelPart0) {
        this.leftRearFoot = modelPart0.getChild("left_hind_foot");
        this.rightRearFoot = modelPart0.getChild("right_hind_foot");
        this.leftHaunch = modelPart0.getChild("left_haunch");
        this.rightHaunch = modelPart0.getChild("right_haunch");
        this.body = modelPart0.getChild("body");
        this.leftFrontLeg = modelPart0.getChild("left_front_leg");
        this.rightFrontLeg = modelPart0.getChild("right_front_leg");
        this.head = modelPart0.getChild("head");
        this.rightEar = modelPart0.getChild("right_ear");
        this.leftEar = modelPart0.getChild("left_ear");
        this.tail = modelPart0.getChild("tail");
        this.nose = modelPart0.getChild("nose");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("left_hind_foot", CubeListBuilder.create().texOffs(26, 24).addBox(-1.0F, 5.5F, -3.7F, 2.0F, 1.0F, 7.0F), PartPose.offset(3.0F, 17.5F, 3.7F));
        $$1.addOrReplaceChild("right_hind_foot", CubeListBuilder.create().texOffs(8, 24).addBox(-1.0F, 5.5F, -3.7F, 2.0F, 1.0F, 7.0F), PartPose.offset(-3.0F, 17.5F, 3.7F));
        $$1.addOrReplaceChild("left_haunch", CubeListBuilder.create().texOffs(30, 15).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 5.0F), PartPose.offsetAndRotation(3.0F, 17.5F, 3.7F, (float) (-Math.PI / 9), 0.0F, 0.0F));
        $$1.addOrReplaceChild("right_haunch", CubeListBuilder.create().texOffs(16, 15).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 5.0F), PartPose.offsetAndRotation(-3.0F, 17.5F, 3.7F, (float) (-Math.PI / 9), 0.0F, 0.0F));
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -10.0F, 6.0F, 5.0F, 10.0F), PartPose.offsetAndRotation(0.0F, 19.0F, 8.0F, (float) (-Math.PI / 9), 0.0F, 0.0F));
        $$1.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(8, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F), PartPose.offsetAndRotation(3.0F, 17.0F, -1.0F, (float) (-Math.PI / 18), 0.0F, 0.0F));
        $$1.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F), PartPose.offsetAndRotation(-3.0F, 17.0F, -1.0F, (float) (-Math.PI / 18), 0.0F, 0.0F));
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0).addBox(-2.5F, -4.0F, -5.0F, 5.0F, 4.0F, 5.0F), PartPose.offset(0.0F, 16.0F, -1.0F));
        $$1.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(52, 0).addBox(-2.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 16.0F, -1.0F, 0.0F, (float) (-Math.PI / 12), 0.0F));
        $$1.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(58, 0).addBox(0.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 16.0F, -1.0F, 0.0F, (float) (Math.PI / 12), 0.0F));
        $$1.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(52, 6).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 20.0F, 7.0F, -0.3490659F, 0.0F, 0.0F));
        $$1.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(32, 9).addBox(-0.5F, -2.5F, -5.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.0F, 16.0F, -1.0F));
        return LayerDefinition.create($$0, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        if (this.f_102610_) {
            float $$8 = 1.5F;
            poseStack0.pushPose();
            poseStack0.scale(0.56666666F, 0.56666666F, 0.56666666F);
            poseStack0.translate(0.0F, 1.375F, 0.125F);
            ImmutableList.of(this.head, this.leftEar, this.rightEar, this.nose).forEach(p_103597_ -> p_103597_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
            poseStack0.popPose();
            poseStack0.pushPose();
            poseStack0.scale(0.4F, 0.4F, 0.4F);
            poseStack0.translate(0.0F, 2.25F, 0.0F);
            ImmutableList.of(this.leftRearFoot, this.rightRearFoot, this.leftHaunch, this.rightHaunch, this.body, this.leftFrontLeg, this.rightFrontLeg, this.tail).forEach(p_103587_ -> p_103587_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
            poseStack0.popPose();
        } else {
            poseStack0.pushPose();
            poseStack0.scale(0.6F, 0.6F, 0.6F);
            poseStack0.translate(0.0F, 1.0F, 0.0F);
            ImmutableList.of(this.leftRearFoot, this.rightRearFoot, this.leftHaunch, this.rightHaunch, this.body, this.leftFrontLeg, this.rightFrontLeg, this.head, this.rightEar, this.leftEar, this.tail, this.nose, new ModelPart[0]).forEach(p_103572_ -> p_103572_.render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7));
            poseStack0.popPose();
        }
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        float $$6 = float3 - (float) t0.f_19797_;
        this.nose.xRot = float5 * (float) (Math.PI / 180.0);
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
        this.rightEar.xRot = float5 * (float) (Math.PI / 180.0);
        this.leftEar.xRot = float5 * (float) (Math.PI / 180.0);
        this.nose.yRot = float4 * (float) (Math.PI / 180.0);
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        this.rightEar.yRot = this.nose.yRot - (float) (Math.PI / 12);
        this.leftEar.yRot = this.nose.yRot + (float) (Math.PI / 12);
        this.jumpRotation = Mth.sin(t0.getJumpCompletion($$6) * (float) Math.PI);
        this.leftHaunch.xRot = (this.jumpRotation * 50.0F - 21.0F) * (float) (Math.PI / 180.0);
        this.rightHaunch.xRot = (this.jumpRotation * 50.0F - 21.0F) * (float) (Math.PI / 180.0);
        this.leftRearFoot.xRot = this.jumpRotation * 50.0F * (float) (Math.PI / 180.0);
        this.rightRearFoot.xRot = this.jumpRotation * 50.0F * (float) (Math.PI / 180.0);
        this.leftFrontLeg.xRot = (this.jumpRotation * -40.0F - 11.0F) * (float) (Math.PI / 180.0);
        this.rightFrontLeg.xRot = (this.jumpRotation * -40.0F - 11.0F) * (float) (Math.PI / 180.0);
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        super.prepareMobModel(t0, float1, float2, float3);
        this.jumpRotation = Mth.sin(t0.getJumpCompletion(float3) * (float) Math.PI);
    }
}