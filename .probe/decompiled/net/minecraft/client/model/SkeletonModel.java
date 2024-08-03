package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SkeletonModel<T extends Mob & RangedAttackMob> extends HumanoidModel<T> {

    public SkeletonModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-5.0F, 2.0F, 0.0F));
        $$1.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(5.0F, 2.0F, 0.0F));
        $$1.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-2.0F, 12.0F, 0.0F));
        $$1.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(2.0F, 12.0F, 0.0F));
        return LayerDefinition.create($$0, 64, 32);
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        this.f_102816_ = HumanoidModel.ArmPose.EMPTY;
        this.f_102815_ = HumanoidModel.ArmPose.EMPTY;
        ItemStack $$4 = t0.m_21120_(InteractionHand.MAIN_HAND);
        if ($$4.is(Items.BOW) && t0.isAggressive()) {
            if (t0.getMainArm() == HumanoidArm.RIGHT) {
                this.f_102816_ = HumanoidModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.f_102815_ = HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        }
        super.prepareMobModel(t0, float1, float2, float3);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        ItemStack $$6 = t0.m_21205_();
        if (t0.isAggressive() && ($$6.isEmpty() || !$$6.is(Items.BOW))) {
            float $$7 = Mth.sin(this.f_102608_ * (float) Math.PI);
            float $$8 = Mth.sin((1.0F - (1.0F - this.f_102608_) * (1.0F - this.f_102608_)) * (float) Math.PI);
            this.f_102811_.zRot = 0.0F;
            this.f_102812_.zRot = 0.0F;
            this.f_102811_.yRot = -(0.1F - $$7 * 0.6F);
            this.f_102812_.yRot = 0.1F - $$7 * 0.6F;
            this.f_102811_.xRot = (float) (-Math.PI / 2);
            this.f_102812_.xRot = (float) (-Math.PI / 2);
            this.f_102811_.xRot -= $$7 * 1.2F - $$8 * 0.4F;
            this.f_102812_.xRot -= $$7 * 1.2F - $$8 * 0.4F;
            AnimationUtils.bobArms(this.f_102811_, this.f_102812_, float3);
        }
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm0, PoseStack poseStack1) {
        float $$2 = humanoidArm0 == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        ModelPart $$3 = this.m_102851_(humanoidArm0);
        $$3.x += $$2;
        $$3.translateAndRotate(poseStack1);
        $$3.x -= $$2;
    }
}