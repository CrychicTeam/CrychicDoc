package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Panda;

public class PandaModel<T extends Panda> extends QuadrupedModel<T> {

    private float sitAmount;

    private float lieOnBackAmount;

    private float rollAmount;

    public PandaModel(ModelPart modelPart0) {
        super(modelPart0, true, 23.0F, 4.8F, 2.7F, 3.0F, 49);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 6).addBox(-6.5F, -5.0F, -4.0F, 13.0F, 10.0F, 9.0F).texOffs(45, 16).addBox("nose", -3.5F, 0.0F, -6.0F, 7.0F, 5.0F, 2.0F).texOffs(52, 25).addBox("left_ear", 3.5F, -8.0F, -1.0F, 5.0F, 4.0F, 1.0F).texOffs(52, 25).addBox("right_ear", -8.5F, -8.0F, -1.0F, 5.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 11.5F, -17.0F));
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 25).addBox(-9.5F, -13.0F, -6.5F, 19.0F, 26.0F, 13.0F), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        int $$2 = 9;
        int $$3 = 6;
        CubeListBuilder $$4 = CubeListBuilder.create().texOffs(40, 0).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F);
        $$1.addOrReplaceChild("right_hind_leg", $$4, PartPose.offset(-5.5F, 15.0F, 9.0F));
        $$1.addOrReplaceChild("left_hind_leg", $$4, PartPose.offset(5.5F, 15.0F, 9.0F));
        $$1.addOrReplaceChild("right_front_leg", $$4, PartPose.offset(-5.5F, 15.0F, -9.0F));
        $$1.addOrReplaceChild("left_front_leg", $$4, PartPose.offset(5.5F, 15.0F, -9.0F));
        return LayerDefinition.create($$0, 64, 64);
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        super.m_6839_(t0, float1, float2, float3);
        this.sitAmount = t0.getSitAmount(float3);
        this.lieOnBackAmount = t0.getLieOnBackAmount(float3);
        this.rollAmount = t0.m_6162_() ? 0.0F : t0.getRollAmount(float3);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        boolean $$6 = t0.getUnhappyCounter() > 0;
        boolean $$7 = t0.isSneezing();
        int $$8 = t0.getSneezeCounter();
        boolean $$9 = t0.isEating();
        boolean $$10 = t0.isScared();
        if ($$6) {
            this.f_103492_.yRot = 0.35F * Mth.sin(0.6F * float3);
            this.f_103492_.zRot = 0.35F * Mth.sin(0.6F * float3);
            this.f_170854_.xRot = -0.75F * Mth.sin(0.3F * float3);
            this.f_170855_.xRot = 0.75F * Mth.sin(0.3F * float3);
        } else {
            this.f_103492_.zRot = 0.0F;
        }
        if ($$7) {
            if ($$8 < 15) {
                this.f_103492_.xRot = (float) (-Math.PI / 4) * (float) $$8 / 14.0F;
            } else if ($$8 < 20) {
                float $$11 = (float) (($$8 - 15) / 5);
                this.f_103492_.xRot = (float) (-Math.PI / 4) + (float) (Math.PI / 4) * $$11;
            }
        }
        if (this.sitAmount > 0.0F) {
            this.f_103493_.xRot = ModelUtils.rotlerpRad(this.f_103493_.xRot, 1.7407963F, this.sitAmount);
            this.f_103492_.xRot = ModelUtils.rotlerpRad(this.f_103492_.xRot, (float) (Math.PI / 2), this.sitAmount);
            this.f_170854_.zRot = -0.27079642F;
            this.f_170855_.zRot = 0.27079642F;
            this.f_170852_.zRot = 0.5707964F;
            this.f_170853_.zRot = -0.5707964F;
            if ($$9) {
                this.f_103492_.xRot = (float) (Math.PI / 2) + 0.2F * Mth.sin(float3 * 0.6F);
                this.f_170854_.xRot = -0.4F - 0.2F * Mth.sin(float3 * 0.6F);
                this.f_170855_.xRot = -0.4F - 0.2F * Mth.sin(float3 * 0.6F);
            }
            if ($$10) {
                this.f_103492_.xRot = 2.1707964F;
                this.f_170854_.xRot = -0.9F;
                this.f_170855_.xRot = -0.9F;
            }
        } else {
            this.f_170852_.zRot = 0.0F;
            this.f_170853_.zRot = 0.0F;
            this.f_170854_.zRot = 0.0F;
            this.f_170855_.zRot = 0.0F;
        }
        if (this.lieOnBackAmount > 0.0F) {
            this.f_170852_.xRot = -0.6F * Mth.sin(float3 * 0.15F);
            this.f_170853_.xRot = 0.6F * Mth.sin(float3 * 0.15F);
            this.f_170854_.xRot = 0.3F * Mth.sin(float3 * 0.25F);
            this.f_170855_.xRot = -0.3F * Mth.sin(float3 * 0.25F);
            this.f_103492_.xRot = ModelUtils.rotlerpRad(this.f_103492_.xRot, (float) (Math.PI / 2), this.lieOnBackAmount);
        }
        if (this.rollAmount > 0.0F) {
            this.f_103492_.xRot = ModelUtils.rotlerpRad(this.f_103492_.xRot, 2.0561945F, this.rollAmount);
            this.f_170852_.xRot = -0.5F * Mth.sin(float3 * 0.5F);
            this.f_170853_.xRot = 0.5F * Mth.sin(float3 * 0.5F);
            this.f_170854_.xRot = 0.5F * Mth.sin(float3 * 0.5F);
            this.f_170855_.xRot = -0.5F * Mth.sin(float3 * 0.5F);
        }
    }
}