package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class EndermanModel<T extends LivingEntity> extends HumanoidModel<T> {

    public boolean carrying;

    public boolean creepy;

    public EndermanModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    public static LayerDefinition createBodyLayer() {
        float $$0 = -14.0F;
        MeshDefinition $$1 = HumanoidModel.createMesh(CubeDeformation.NONE, -14.0F);
        PartDefinition $$2 = $$1.getRoot();
        PartPose $$3 = PartPose.offset(0.0F, -13.0F, 0.0F);
        $$2.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), $$3);
        $$2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), $$3);
        $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.offset(0.0F, -14.0F, 0.0F));
        $$2.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(-5.0F, -12.0F, 0.0F));
        $$2.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(5.0F, -12.0F, 0.0F));
        $$2.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(-2.0F, -5.0F, 0.0F));
        $$2.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F), PartPose.offset(2.0F, -5.0F, 0.0F));
        return LayerDefinition.create($$1, 64, 32);
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        this.f_102808_.visible = true;
        int $$6 = -14;
        this.f_102810_.xRot = 0.0F;
        this.f_102810_.y = -14.0F;
        this.f_102810_.z = -0.0F;
        this.f_102813_.xRot -= 0.0F;
        this.f_102814_.xRot -= 0.0F;
        this.f_102811_.xRot *= 0.5F;
        this.f_102812_.xRot *= 0.5F;
        this.f_102813_.xRot *= 0.5F;
        this.f_102814_.xRot *= 0.5F;
        float $$7 = 0.4F;
        if (this.f_102811_.xRot > 0.4F) {
            this.f_102811_.xRot = 0.4F;
        }
        if (this.f_102812_.xRot > 0.4F) {
            this.f_102812_.xRot = 0.4F;
        }
        if (this.f_102811_.xRot < -0.4F) {
            this.f_102811_.xRot = -0.4F;
        }
        if (this.f_102812_.xRot < -0.4F) {
            this.f_102812_.xRot = -0.4F;
        }
        if (this.f_102813_.xRot > 0.4F) {
            this.f_102813_.xRot = 0.4F;
        }
        if (this.f_102814_.xRot > 0.4F) {
            this.f_102814_.xRot = 0.4F;
        }
        if (this.f_102813_.xRot < -0.4F) {
            this.f_102813_.xRot = -0.4F;
        }
        if (this.f_102814_.xRot < -0.4F) {
            this.f_102814_.xRot = -0.4F;
        }
        if (this.carrying) {
            this.f_102811_.xRot = -0.5F;
            this.f_102812_.xRot = -0.5F;
            this.f_102811_.zRot = 0.05F;
            this.f_102812_.zRot = -0.05F;
        }
        this.f_102813_.z = 0.0F;
        this.f_102814_.z = 0.0F;
        this.f_102813_.y = -5.0F;
        this.f_102814_.y = -5.0F;
        this.f_102808_.z = -0.0F;
        this.f_102808_.y = -13.0F;
        this.f_102809_.x = this.f_102808_.x;
        this.f_102809_.y = this.f_102808_.y;
        this.f_102809_.z = this.f_102808_.z;
        this.f_102809_.xRot = this.f_102808_.xRot;
        this.f_102809_.yRot = this.f_102808_.yRot;
        this.f_102809_.zRot = this.f_102808_.zRot;
        if (this.creepy) {
            float $$8 = 1.0F;
            this.f_102808_.y -= 5.0F;
        }
        int $$9 = -14;
        this.f_102811_.setPos(-5.0F, -12.0F, 0.0F);
        this.f_102812_.setPos(5.0F, -12.0F, 0.0F);
    }
}