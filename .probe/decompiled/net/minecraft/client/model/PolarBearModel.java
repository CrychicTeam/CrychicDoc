package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.PolarBear;

public class PolarBearModel<T extends PolarBear> extends QuadrupedModel<T> {

    public PolarBearModel(ModelPart modelPart0) {
        super(modelPart0, true, 16.0F, 4.0F, 2.25F, 2.0F, 24);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.0F, -3.0F, 7.0F, 7.0F, 7.0F).texOffs(0, 44).addBox("mouth", -2.5F, 1.0F, -6.0F, 5.0F, 3.0F, 3.0F).texOffs(26, 0).addBox("right_ear", -4.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F).texOffs(26, 0).mirror().addBox("left_ear", 2.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 10.0F, -16.0F));
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-5.0F, -13.0F, -7.0F, 14.0F, 14.0F, 11.0F).texOffs(39, 0).addBox(-4.0F, -25.0F, -7.0F, 12.0F, 12.0F, 10.0F), PartPose.offsetAndRotation(-2.0F, 9.0F, 12.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        int $$2 = 10;
        CubeListBuilder $$3 = CubeListBuilder.create().texOffs(50, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 8.0F);
        $$1.addOrReplaceChild("right_hind_leg", $$3, PartPose.offset(-4.5F, 14.0F, 6.0F));
        $$1.addOrReplaceChild("left_hind_leg", $$3, PartPose.offset(4.5F, 14.0F, 6.0F));
        CubeListBuilder $$4 = CubeListBuilder.create().texOffs(50, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 6.0F);
        $$1.addOrReplaceChild("right_front_leg", $$4, PartPose.offset(-3.5F, 14.0F, -8.0F));
        $$1.addOrReplaceChild("left_front_leg", $$4, PartPose.offset(3.5F, 14.0F, -8.0F));
        return LayerDefinition.create($$0, 128, 64);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        float $$6 = float3 - (float) t0.f_19797_;
        float $$7 = t0.getStandingAnimationScale($$6);
        $$7 *= $$7;
        float $$8 = 1.0F - $$7;
        this.f_103493_.xRot = (float) (Math.PI / 2) - $$7 * (float) Math.PI * 0.35F;
        this.f_103493_.y = 9.0F * $$8 + 11.0F * $$7;
        this.f_170854_.y = 14.0F * $$8 - 6.0F * $$7;
        this.f_170854_.z = -8.0F * $$8 - 4.0F * $$7;
        this.f_170854_.xRot -= $$7 * (float) Math.PI * 0.45F;
        this.f_170855_.y = this.f_170854_.y;
        this.f_170855_.z = this.f_170854_.z;
        this.f_170855_.xRot -= $$7 * (float) Math.PI * 0.45F;
        if (this.f_102610_) {
            this.f_103492_.y = 10.0F * $$8 - 9.0F * $$7;
            this.f_103492_.z = -16.0F * $$8 - 7.0F * $$7;
        } else {
            this.f_103492_.y = 10.0F * $$8 - 14.0F * $$7;
            this.f_103492_.z = -16.0F * $$8 - 3.0F * $$7;
        }
        this.f_103492_.xRot += $$7 * (float) Math.PI * 0.15F;
    }
}