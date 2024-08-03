package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;

public class HoglinModel<T extends Mob & HoglinBase> extends AgeableListModel<T> {

    private static final float DEFAULT_HEAD_X_ROT = 0.87266463F;

    private static final float ATTACK_HEAD_X_ROT_END = (float) (-Math.PI / 9);

    private final ModelPart head;

    private final ModelPart rightEar;

    private final ModelPart leftEar;

    private final ModelPart body;

    private final ModelPart rightFrontLeg;

    private final ModelPart leftFrontLeg;

    private final ModelPart rightHindLeg;

    private final ModelPart leftHindLeg;

    private final ModelPart mane;

    public HoglinModel(ModelPart modelPart0) {
        super(true, 8.0F, 6.0F, 1.9F, 2.0F, 24.0F);
        this.body = modelPart0.getChild("body");
        this.mane = this.body.getChild("mane");
        this.head = modelPart0.getChild("head");
        this.rightEar = this.head.getChild("right_ear");
        this.leftEar = this.head.getChild("left_ear");
        this.rightFrontLeg = modelPart0.getChild("right_front_leg");
        this.leftFrontLeg = modelPart0.getChild("left_front_leg");
        this.rightHindLeg = modelPart0.getChild("right_hind_leg");
        this.leftHindLeg = modelPart0.getChild("left_hind_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition $$2 = $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(1, 1).addBox(-8.0F, -7.0F, -13.0F, 16.0F, 14.0F, 26.0F), PartPose.offset(0.0F, 7.0F, 0.0F));
        $$2.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(90, 33).addBox(0.0F, 0.0F, -9.0F, 0.0F, 10.0F, 19.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -14.0F, -5.0F));
        PartDefinition $$3 = $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(61, 1).addBox(-7.0F, -3.0F, -19.0F, 14.0F, 6.0F, 19.0F), PartPose.offsetAndRotation(0.0F, 2.0F, -12.0F, 0.87266463F, 0.0F, 0.0F));
        $$3.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(1, 1).addBox(-6.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(-6.0F, -2.0F, -3.0F, 0.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0)));
        $$3.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(1, 6).addBox(0.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(6.0F, -2.0F, -3.0F, 0.0F, 0.0F, (float) (Math.PI * 2.0 / 9.0)));
        $$3.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(10, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), PartPose.offset(-7.0F, 2.0F, -12.0F));
        $$3.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(1, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F), PartPose.offset(7.0F, 2.0F, -12.0F));
        int $$4 = 14;
        int $$5 = 11;
        $$1.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(66, 42).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F), PartPose.offset(-4.0F, 10.0F, -8.5F));
        $$1.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(41, 42).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F), PartPose.offset(4.0F, 10.0F, -8.5F));
        $$1.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(21, 45).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F), PartPose.offset(-5.0F, 13.0F, 10.0F));
        $$1.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 45).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F), PartPose.offset(5.0F, 13.0F, 10.0F));
        return LayerDefinition.create($$0, 128, 64);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightFrontLeg, this.leftFrontLeg, this.rightHindLeg, this.leftHindLeg);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.rightEar.zRot = (float) (-Math.PI * 2.0 / 9.0) - float2 * Mth.sin(float1);
        this.leftEar.zRot = (float) (Math.PI * 2.0 / 9.0) + float2 * Mth.sin(float1);
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        int $$6 = t0.getAttackAnimationRemainingTicks();
        float $$7 = 1.0F - (float) Mth.abs(10 - 2 * $$6) / 10.0F;
        this.head.xRot = Mth.lerp($$7, 0.87266463F, (float) (-Math.PI / 9));
        if (t0.m_6162_()) {
            this.head.y = Mth.lerp($$7, 2.0F, 5.0F);
            this.mane.z = -3.0F;
        } else {
            this.head.y = 2.0F;
            this.mane.z = -7.0F;
        }
        float $$8 = 1.2F;
        this.rightFrontLeg.xRot = Mth.cos(float1) * 1.2F * float2;
        this.leftFrontLeg.xRot = Mth.cos(float1 + (float) Math.PI) * 1.2F * float2;
        this.rightHindLeg.xRot = this.leftFrontLeg.xRot;
        this.leftHindLeg.xRot = this.rightFrontLeg.xRot;
    }
}