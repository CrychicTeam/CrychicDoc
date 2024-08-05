package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;

public class PiglinModel<T extends Mob> extends PlayerModel<T> {

    public final ModelPart rightEar = this.f_102808_.getChild("right_ear");

    private final ModelPart leftEar = this.f_102808_.getChild("left_ear");

    private final PartPose bodyDefault = this.f_102810_.storePose();

    private final PartPose headDefault = this.f_102808_.storePose();

    private final PartPose leftArmDefault = this.f_102812_.storePose();

    private final PartPose rightArmDefault = this.f_102811_.storePose();

    public PiglinModel(ModelPart modelPart0) {
        super(modelPart0, false);
    }

    public static MeshDefinition createMesh(CubeDeformation cubeDeformation0) {
        MeshDefinition $$1 = PlayerModel.createMesh(cubeDeformation0, false);
        PartDefinition $$2 = $$1.getRoot();
        $$2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, cubeDeformation0), PartPose.ZERO);
        addHead(cubeDeformation0, $$1);
        $$2.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        return $$1;
    }

    public static void addHead(CubeDeformation cubeDeformation0, MeshDefinition meshDefinition1) {
        PartDefinition $$2 = meshDefinition1.getRoot();
        PartDefinition $$3 = $$2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, cubeDeformation0).texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, cubeDeformation0).texOffs(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, cubeDeformation0).texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, cubeDeformation0), PartPose.ZERO);
        $$3.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(51, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, cubeDeformation0), PartPose.offsetAndRotation(4.5F, -6.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 6)));
        $$3.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, cubeDeformation0), PartPose.offsetAndRotation(-4.5F, -6.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 6)));
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.f_102810_.loadPose(this.bodyDefault);
        this.f_102808_.loadPose(this.headDefault);
        this.f_102812_.loadPose(this.leftArmDefault);
        this.f_102811_.loadPose(this.rightArmDefault);
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        float $$6 = (float) (Math.PI / 6);
        float $$7 = float3 * 0.1F + float1 * 0.5F;
        float $$8 = 0.08F + float2 * 0.4F;
        this.leftEar.zRot = (float) (-Math.PI / 6) - Mth.cos($$7 * 1.2F) * $$8;
        this.rightEar.zRot = (float) (Math.PI / 6) + Mth.cos($$7) * $$8;
        if (t0 instanceof AbstractPiglin $$9) {
            PiglinArmPose $$10 = $$9.getArmPose();
            if ($$10 == PiglinArmPose.DANCING) {
                float $$11 = float3 / 60.0F;
                this.rightEar.zRot = (float) (Math.PI / 6) + (float) (Math.PI / 180.0) * Mth.sin($$11 * 30.0F) * 10.0F;
                this.leftEar.zRot = (float) (-Math.PI / 6) - (float) (Math.PI / 180.0) * Mth.cos($$11 * 30.0F) * 10.0F;
                this.f_102808_.x = Mth.sin($$11 * 10.0F);
                this.f_102808_.y = Mth.sin($$11 * 40.0F) + 0.4F;
                this.f_102811_.zRot = (float) (Math.PI / 180.0) * (70.0F + Mth.cos($$11 * 40.0F) * 10.0F);
                this.f_102812_.zRot = this.f_102811_.zRot * -1.0F;
                this.f_102811_.y = Mth.sin($$11 * 40.0F) * 0.5F + 1.5F;
                this.f_102812_.y = Mth.sin($$11 * 40.0F) * 0.5F + 1.5F;
                this.f_102810_.y = Mth.sin($$11 * 40.0F) * 0.35F;
            } else if ($$10 == PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON && this.f_102608_ == 0.0F) {
                this.holdWeaponHigh(t0);
            } else if ($$10 == PiglinArmPose.CROSSBOW_HOLD) {
                AnimationUtils.animateCrossbowHold(this.f_102811_, this.f_102812_, this.f_102808_, !t0.isLeftHanded());
            } else if ($$10 == PiglinArmPose.CROSSBOW_CHARGE) {
                AnimationUtils.animateCrossbowCharge(this.f_102811_, this.f_102812_, t0, !t0.isLeftHanded());
            } else if ($$10 == PiglinArmPose.ADMIRING_ITEM) {
                this.f_102808_.xRot = 0.5F;
                this.f_102808_.yRot = 0.0F;
                if (t0.isLeftHanded()) {
                    this.f_102811_.yRot = -0.5F;
                    this.f_102811_.xRot = -0.9F;
                } else {
                    this.f_102812_.yRot = 0.5F;
                    this.f_102812_.xRot = -0.9F;
                }
            }
        } else if (t0.m_6095_() == EntityType.ZOMBIFIED_PIGLIN) {
            AnimationUtils.animateZombieArms(this.f_102812_, this.f_102811_, t0.isAggressive(), this.f_102608_, float3);
        }
        this.f_103376_.copyFrom(this.f_102814_);
        this.f_103377_.copyFrom(this.f_102813_);
        this.f_103374_.copyFrom(this.f_102812_);
        this.f_103375_.copyFrom(this.f_102811_);
        this.f_103378_.copyFrom(this.f_102810_);
        this.f_102809_.copyFrom(this.f_102808_);
    }

    protected void setupAttackAnimation(T t0, float float1) {
        if (this.f_102608_ > 0.0F && t0 instanceof Piglin && ((Piglin) t0).getArmPose() == PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON) {
            AnimationUtils.swingWeaponDown(this.f_102811_, this.f_102812_, t0, this.f_102608_, float1);
        } else {
            super.m_7884_(t0, float1);
        }
    }

    private void holdWeaponHigh(T t0) {
        if (t0.isLeftHanded()) {
            this.f_102812_.xRot = -1.8F;
        } else {
            this.f_102811_.xRot = -1.8F;
        }
    }
}