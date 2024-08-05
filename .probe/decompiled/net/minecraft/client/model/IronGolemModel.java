package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.IronGolem;

public class IronGolemModel<T extends IronGolem> extends HierarchicalModel<T> {

    private final ModelPart root;

    private final ModelPart head;

    private final ModelPart rightArm;

    private final ModelPart leftArm;

    private final ModelPart rightLeg;

    private final ModelPart leftLeg;

    public IronGolemModel(ModelPart modelPart0) {
        this.root = modelPart0;
        this.head = modelPart0.getChild("head");
        this.rightArm = modelPart0.getChild("right_arm");
        this.leftArm = modelPart0.getChild("left_arm");
        this.rightLeg = modelPart0.getChild("right_leg");
        this.leftLeg = modelPart0.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F).texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, -7.0F, -2.0F));
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F).texOffs(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -7.0F, 0.0F));
        $$1.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F), PartPose.offset(0.0F, -7.0F, 0.0F));
        $$1.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F), PartPose.offset(0.0F, -7.0F, 0.0F));
        $$1.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F), PartPose.offset(-4.0F, 11.0F, 0.0F));
        $$1.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(60, 0).mirror().addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F), PartPose.offset(5.0F, 11.0F, 0.0F));
        return LayerDefinition.create($$0, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
        this.rightLeg.xRot = -1.5F * Mth.triangleWave(float1, 13.0F) * float2;
        this.leftLeg.xRot = 1.5F * Mth.triangleWave(float1, 13.0F) * float2;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        int $$4 = t0.getAttackAnimationTick();
        if ($$4 > 0) {
            this.rightArm.xRot = -2.0F + 1.5F * Mth.triangleWave((float) $$4 - float3, 10.0F);
            this.leftArm.xRot = -2.0F + 1.5F * Mth.triangleWave((float) $$4 - float3, 10.0F);
        } else {
            int $$5 = t0.getOfferFlowerTick();
            if ($$5 > 0) {
                this.rightArm.xRot = -0.8F + 0.025F * Mth.triangleWave((float) $$5, 70.0F);
                this.leftArm.xRot = 0.0F;
            } else {
                this.rightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(float1, 13.0F)) * float2;
                this.leftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(float1, 13.0F)) * float2;
            }
        }
    }

    public ModelPart getFlowerHoldingArm() {
        return this.rightArm;
    }
}