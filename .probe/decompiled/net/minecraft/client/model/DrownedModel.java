package net.minecraft.client.model;

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
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DrownedModel<T extends Zombie> extends ZombieModel<T> {

    public DrownedModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation0) {
        MeshDefinition $$1 = HumanoidModel.createMesh(cubeDeformation0, 0.0F);
        PartDefinition $$2 = $$1.getRoot();
        $$2.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0), PartPose.offset(5.0F, 2.0F, 0.0F));
        $$2.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation0), PartPose.offset(1.9F, 12.0F, 0.0F));
        return LayerDefinition.create($$1, 64, 64);
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        this.f_102816_ = HumanoidModel.ArmPose.EMPTY;
        this.f_102815_ = HumanoidModel.ArmPose.EMPTY;
        ItemStack $$4 = t0.m_21120_(InteractionHand.MAIN_HAND);
        if ($$4.is(Items.TRIDENT) && t0.m_5912_()) {
            if (t0.m_5737_() == HumanoidArm.RIGHT) {
                this.f_102816_ = HumanoidModel.ArmPose.THROW_SPEAR;
            } else {
                this.f_102815_ = HumanoidModel.ArmPose.THROW_SPEAR;
            }
        }
        super.m_6839_(t0, float1, float2, float3);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.m_6973_(t0, float1, float2, float3, float4, float5);
        if (this.f_102815_ == HumanoidModel.ArmPose.THROW_SPEAR) {
            this.f_102812_.xRot = this.f_102812_.xRot * 0.5F - (float) Math.PI;
            this.f_102812_.yRot = 0.0F;
        }
        if (this.f_102816_ == HumanoidModel.ArmPose.THROW_SPEAR) {
            this.f_102811_.xRot = this.f_102811_.xRot * 0.5F - (float) Math.PI;
            this.f_102811_.yRot = 0.0F;
        }
        if (this.f_102818_ > 0.0F) {
            this.f_102811_.xRot = this.m_102835_(this.f_102818_, this.f_102811_.xRot, (float) (-Math.PI * 4.0 / 5.0)) + this.f_102818_ * 0.35F * Mth.sin(0.1F * float3);
            this.f_102812_.xRot = this.m_102835_(this.f_102818_, this.f_102812_.xRot, (float) (-Math.PI * 4.0 / 5.0)) - this.f_102818_ * 0.35F * Mth.sin(0.1F * float3);
            this.f_102811_.zRot = this.m_102835_(this.f_102818_, this.f_102811_.zRot, -0.15F);
            this.f_102812_.zRot = this.m_102835_(this.f_102818_, this.f_102812_.zRot, 0.15F);
            this.f_102814_.xRot = this.f_102814_.xRot - this.f_102818_ * 0.55F * Mth.sin(0.1F * float3);
            this.f_102813_.xRot = this.f_102813_.xRot + this.f_102818_ * 0.55F * Mth.sin(0.1F * float3);
            this.f_102808_.xRot = 0.0F;
        }
    }
}