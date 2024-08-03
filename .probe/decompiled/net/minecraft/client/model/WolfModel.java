package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Wolf;

public class WolfModel<T extends Wolf> extends ColorableAgeableListModel<T> {

    private static final String REAL_HEAD = "real_head";

    private static final String UPPER_BODY = "upper_body";

    private static final String REAL_TAIL = "real_tail";

    private final ModelPart head;

    private final ModelPart realHead;

    private final ModelPart body;

    private final ModelPart rightHindLeg;

    private final ModelPart leftHindLeg;

    private final ModelPart rightFrontLeg;

    private final ModelPart leftFrontLeg;

    private final ModelPart tail;

    private final ModelPart realTail;

    private final ModelPart upperBody;

    private static final int LEG_SIZE = 8;

    public WolfModel(ModelPart modelPart0) {
        this.head = modelPart0.getChild("head");
        this.realHead = this.head.getChild("real_head");
        this.body = modelPart0.getChild("body");
        this.upperBody = modelPart0.getChild("upper_body");
        this.rightHindLeg = modelPart0.getChild("right_hind_leg");
        this.leftHindLeg = modelPart0.getChild("left_hind_leg");
        this.rightFrontLeg = modelPart0.getChild("right_front_leg");
        this.leftFrontLeg = modelPart0.getChild("left_front_leg");
        this.tail = modelPart0.getChild("tail");
        this.realTail = this.tail.getChild("real_tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        float $$2 = 13.5F;
        PartDefinition $$3 = $$1.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-1.0F, 13.5F, -7.0F));
        $$3.addOrReplaceChild("real_head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F).texOffs(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F).texOffs(16, 14).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F).texOffs(0, 10).addBox(-0.5F, -0.001F, -5.0F, 3.0F, 3.0F, 4.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        $$1.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        CubeListBuilder $$4 = CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F);
        $$1.addOrReplaceChild("right_hind_leg", $$4, PartPose.offset(-2.5F, 16.0F, 7.0F));
        $$1.addOrReplaceChild("left_hind_leg", $$4, PartPose.offset(0.5F, 16.0F, 7.0F));
        $$1.addOrReplaceChild("right_front_leg", $$4, PartPose.offset(-2.5F, 16.0F, -4.0F));
        $$1.addOrReplaceChild("left_front_leg", $$4, PartPose.offset(0.5F, 16.0F, -4.0F));
        PartDefinition $$5 = $$1.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 12.0F, 8.0F, (float) (Math.PI / 5), 0.0F, 0.0F));
        $$5.addOrReplaceChild("real_tail", CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 32);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg, this.tail, this.upperBody);
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        if (t0.m_21660_()) {
            this.tail.yRot = 0.0F;
        } else {
            this.tail.yRot = Mth.cos(float1 * 0.6662F) * 1.4F * float2;
        }
        if (t0.m_21825_()) {
            this.upperBody.setPos(-1.0F, 16.0F, -3.0F);
            this.upperBody.xRot = (float) (Math.PI * 2.0 / 5.0);
            this.upperBody.yRot = 0.0F;
            this.body.setPos(0.0F, 18.0F, 0.0F);
            this.body.xRot = (float) (Math.PI / 4);
            this.tail.setPos(-1.0F, 21.0F, 6.0F);
            this.rightHindLeg.setPos(-2.5F, 22.7F, 2.0F);
            this.rightHindLeg.xRot = (float) (Math.PI * 3.0 / 2.0);
            this.leftHindLeg.setPos(0.5F, 22.7F, 2.0F);
            this.leftHindLeg.xRot = (float) (Math.PI * 3.0 / 2.0);
            this.rightFrontLeg.xRot = 5.811947F;
            this.rightFrontLeg.setPos(-2.49F, 17.0F, -4.0F);
            this.leftFrontLeg.xRot = 5.811947F;
            this.leftFrontLeg.setPos(0.51F, 17.0F, -4.0F);
        } else {
            this.body.setPos(0.0F, 14.0F, 2.0F);
            this.body.xRot = (float) (Math.PI / 2);
            this.upperBody.setPos(-1.0F, 14.0F, -3.0F);
            this.upperBody.xRot = this.body.xRot;
            this.tail.setPos(-1.0F, 12.0F, 8.0F);
            this.rightHindLeg.setPos(-2.5F, 16.0F, 7.0F);
            this.leftHindLeg.setPos(0.5F, 16.0F, 7.0F);
            this.rightFrontLeg.setPos(-2.5F, 16.0F, -4.0F);
            this.leftFrontLeg.setPos(0.5F, 16.0F, -4.0F);
            this.rightHindLeg.xRot = Mth.cos(float1 * 0.6662F) * 1.4F * float2;
            this.leftHindLeg.xRot = Mth.cos(float1 * 0.6662F + (float) Math.PI) * 1.4F * float2;
            this.rightFrontLeg.xRot = Mth.cos(float1 * 0.6662F + (float) Math.PI) * 1.4F * float2;
            this.leftFrontLeg.xRot = Mth.cos(float1 * 0.6662F) * 1.4F * float2;
        }
        this.realHead.zRot = t0.getHeadRollAngle(float3) + t0.getBodyRollAngle(float3, 0.0F);
        this.upperBody.zRot = t0.getBodyRollAngle(float3, -0.08F);
        this.body.zRot = t0.getBodyRollAngle(float3, -0.16F);
        this.realTail.zRot = t0.getBodyRollAngle(float3, -0.2F);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        this.tail.xRot = float3;
    }
}