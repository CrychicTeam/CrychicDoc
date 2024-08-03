package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityUnderminer;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class ModelUnderminerDwarf extends AdvancedEntityModel<EntityUnderminer> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox helmet;

    private final AdvancedModelBox beard;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox rightArm;

    private final AdvancedModelBox leftLeg;

    private final AdvancedModelBox rightLeg;

    public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;

    public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;

    public boolean crouching;

    public float swimAmount;

    public ModelUnderminerDwarf() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.body.setTextureOffset(0, 36).addBox(-5.0F, -10.0F, -3.0F, 10.0F, 11.0F, 6.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -10.02F, 0.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(30, 24).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 8.0F, 9.0F, 0.0F, false);
        this.head.setTextureOffset(0, 15).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 8.0F, 9.0F, 0.1F, false);
        this.helmet = new AdvancedModelBox(this, "helmet");
        this.helmet.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addChild(this.helmet);
        this.helmet.setTextureOffset(0, 0).addBox(-6.0F, -10.0F, -5.5F, 12.0F, 4.0F, 10.0F, 0.1F, false);
        this.beard = new AdvancedModelBox(this, "beard");
        this.beard.setRotationPoint(0.0F, 0.1F, -4.1F);
        this.head.addChild(this.beard);
        this.beard.setTextureOffset(0, 54).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 9.0F, 2.0F, 0.0F, false);
        this.leftArm = new AdvancedModelBox(this, "leftArm");
        this.leftArm.setRotationPoint(7.0F, -9.0F, 0.0F);
        this.body.addChild(this.leftArm);
        this.leftArm.setTextureOffset(45, 0).addBox(-2.0F, -1.0F, -2.5F, 4.0F, 13.0F, 5.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this, "rightArm");
        this.rightArm.setRotationPoint(-7.0F, -9.0F, 0.0F);
        this.body.addChild(this.rightArm);
        this.rightArm.setTextureOffset(45, 0).addBox(-2.0F, -1.0F, -2.5F, 4.0F, 13.0F, 5.0F, 0.0F, true);
        this.leftLeg = new AdvancedModelBox(this, "leftLeg");
        this.leftLeg.setRotationPoint(2.0F, 2.0F, 0.0F);
        this.body.addChild(this.leftLeg);
        this.leftLeg.setTextureOffset(33, 42).addBox(-2.0F, -1.0F, -3.0F, 5.0F, 11.0F, 6.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this, "rightLeg");
        this.rightLeg.setRotationPoint(-2.0F, 2.0F, 0.0F);
        this.body.addChild(this.rightLeg);
        this.rightLeg.setTextureOffset(33, 42).addBox(-3.0F, -1.0F, -3.0F, 5.0F, 11.0F, 6.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    public void setupAnim(EntityUnderminer entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.setupHumanoidAnims(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.head, this.beard, this.helmet, this.rightLeg, this.leftLeg, this.rightArm, this.leftArm);
    }

    public void setupHumanoidAnims(EntityUnderminer entityIn, float float0, float float1, float float2, float float3, float float4) {
        boolean flag = entityIn.m_21256_() > 4;
        boolean flag1 = entityIn.m_6067_();
        this.head.rotateAngleY = float3 * (float) (Math.PI / 180.0);
        if (flag) {
            this.head.rotateAngleX = (float) (-Math.PI / 4);
        } else if (this.swimAmount > 0.0F) {
            if (flag1) {
                this.head.rotateAngleX = this.rotlerpRad(this.swimAmount, this.head.rotateAngleX, (float) (-Math.PI / 4));
            } else {
                this.head.rotateAngleX = this.rotlerpRad(this.swimAmount, this.head.rotateAngleX, float4 * (float) (Math.PI / 180.0));
            }
        } else {
            this.head.rotateAngleX = float4 * (float) (Math.PI / 180.0);
        }
        float f = 1.0F;
        if (flag) {
            f = (float) entityIn.m_20184_().lengthSqr();
            f /= 0.2F;
            f *= f * f;
        }
        if (f < 1.0F) {
            f = 1.0F;
        }
        this.rightArm.rotateAngleX = Mth.cos(float0 * 0.6662F + (float) Math.PI) * 2.0F * float1 * 0.5F / f;
        this.leftArm.rotateAngleX = Mth.cos(float0 * 0.6662F) * 2.0F * float1 * 0.5F / f;
        this.rightArm.rotateAngleZ = 0.0F;
        this.leftArm.rotateAngleZ = 0.0F;
        this.rightLeg.rotateAngleX = Mth.cos(float0 * 0.6662F) * 1.4F * float1 / f;
        this.leftLeg.rotateAngleX = Mth.cos(float0 * 0.6662F + (float) Math.PI) * 1.4F * float1 / f;
        this.rightLeg.rotateAngleY = 0.0F;
        this.leftLeg.rotateAngleY = 0.0F;
        this.rightLeg.rotateAngleZ = 0.0F;
        this.leftLeg.rotateAngleZ = 0.0F;
        if (this.f_102609_) {
            this.rightArm.rotateAngleX += (float) (-Math.PI / 5);
            this.leftArm.rotateAngleX += (float) (-Math.PI / 5);
            this.rightLeg.rotateAngleX = -1.4137167F;
            this.rightLeg.rotateAngleY = (float) (Math.PI / 10);
            this.rightLeg.rotateAngleZ = 0.07853982F;
            this.leftLeg.rotateAngleX = -1.4137167F;
            this.leftLeg.rotateAngleY = (float) (-Math.PI / 10);
            this.leftLeg.rotateAngleZ = -0.07853982F;
        }
        this.rightArm.rotateAngleY = 0.0F;
        this.leftArm.rotateAngleY = 0.0F;
        boolean flag2 = entityIn.m_5737_() == HumanoidArm.RIGHT;
        if (entityIn.m_6117_()) {
            boolean flag3 = entityIn.m_7655_() == InteractionHand.MAIN_HAND;
            if (flag3 == flag2) {
                this.poseRightArm(entityIn);
            } else {
                this.poseLeftArm(entityIn);
            }
        } else {
            boolean flag4 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
            if (flag2 != flag4) {
                this.poseLeftArm(entityIn);
                this.poseRightArm(entityIn);
            } else {
                this.poseRightArm(entityIn);
                this.poseLeftArm(entityIn);
            }
        }
        this.setupAttackAnimation(entityIn, float2);
        if (this.crouching) {
            this.body.rotateAngleX = 0.5F;
            this.rightArm.rotateAngleX += 0.4F;
            this.leftArm.rotateAngleX += 0.4F;
        }
        if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS) {
            this.rightArm.rotateAngleZ = this.rightArm.rotateAngleZ + 1.0F * (Mth.cos(float2 * 0.09F) * 0.05F + 0.05F);
            this.rightArm.rotateAngleX = this.rightArm.rotateAngleX + 1.0F * Mth.sin(float2 * 0.067F) * 0.05F;
        }
        if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS) {
            this.leftArm.rotateAngleZ = this.leftArm.rotateAngleZ + -1.0F * (Mth.cos(float2 * 0.09F) * 0.05F + 0.05F);
            this.leftArm.rotateAngleX = this.leftArm.rotateAngleX + -1.0F * Mth.sin(float2 * 0.067F) * 0.05F;
        }
        if (this.swimAmount > 0.0F) {
            float f5 = float0 % 26.0F;
            HumanoidArm humanoidarm = this.getAttackArm(entityIn);
            float f1 = humanoidarm == HumanoidArm.RIGHT && this.f_102608_ > 0.0F ? 0.0F : this.swimAmount;
            float f2 = humanoidarm == HumanoidArm.LEFT && this.f_102608_ > 0.0F ? 0.0F : this.swimAmount;
            if (!entityIn.m_6117_()) {
                if (f5 < 14.0F) {
                    this.leftArm.rotateAngleX = this.rotlerpRad(f2, this.leftArm.rotateAngleX, 0.0F);
                    this.rightArm.rotateAngleX = Mth.lerp(f1, this.rightArm.rotateAngleX, 0.0F);
                    this.leftArm.rotateAngleY = this.rotlerpRad(f2, this.leftArm.rotateAngleY, (float) Math.PI);
                    this.rightArm.rotateAngleY = Mth.lerp(f1, this.rightArm.rotateAngleY, (float) Math.PI);
                    this.leftArm.rotateAngleZ = this.rotlerpRad(f2, this.leftArm.rotateAngleZ, (float) Math.PI + 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F));
                    this.rightArm.rotateAngleZ = Mth.lerp(f1, this.rightArm.rotateAngleZ, (float) Math.PI - 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F));
                } else if (f5 >= 14.0F && f5 < 22.0F) {
                    float f6 = (f5 - 14.0F) / 8.0F;
                    this.leftArm.rotateAngleX = this.rotlerpRad(f2, this.leftArm.rotateAngleX, (float) (Math.PI / 2) * f6);
                    this.rightArm.rotateAngleX = Mth.lerp(f1, this.rightArm.rotateAngleX, (float) (Math.PI / 2) * f6);
                    this.leftArm.rotateAngleY = this.rotlerpRad(f2, this.leftArm.rotateAngleY, (float) Math.PI);
                    this.rightArm.rotateAngleY = Mth.lerp(f1, this.rightArm.rotateAngleY, (float) Math.PI);
                    this.leftArm.rotateAngleZ = this.rotlerpRad(f2, this.leftArm.rotateAngleZ, 5.012389F - 1.8707964F * f6);
                    this.rightArm.rotateAngleZ = Mth.lerp(f1, this.rightArm.rotateAngleZ, 1.2707963F + 1.8707964F * f6);
                } else if (f5 >= 22.0F && f5 < 26.0F) {
                    float f3 = (f5 - 22.0F) / 4.0F;
                    this.leftArm.rotateAngleX = this.rotlerpRad(f2, this.leftArm.rotateAngleX, (float) (Math.PI / 2) - (float) (Math.PI / 2) * f3);
                    this.rightArm.rotateAngleX = Mth.lerp(f1, this.rightArm.rotateAngleX, (float) (Math.PI / 2) - (float) (Math.PI / 2) * f3);
                    this.leftArm.rotateAngleY = this.rotlerpRad(f2, this.leftArm.rotateAngleY, (float) Math.PI);
                    this.rightArm.rotateAngleY = Mth.lerp(f1, this.rightArm.rotateAngleY, (float) Math.PI);
                    this.leftArm.rotateAngleZ = this.rotlerpRad(f2, this.leftArm.rotateAngleZ, (float) Math.PI);
                    this.rightArm.rotateAngleZ = Mth.lerp(f1, this.rightArm.rotateAngleZ, (float) Math.PI);
                }
            }
            this.leftLeg.rotateAngleX = Mth.lerp(this.swimAmount, this.leftLeg.rotateAngleX, 0.3F * Mth.cos(float0 * 0.33333334F + (float) Math.PI));
            this.rightLeg.rotateAngleX = Mth.lerp(this.swimAmount, this.rightLeg.rotateAngleX, 0.3F * Mth.cos(float0 * 0.33333334F));
        }
    }

    private void poseRightArm(EntityUnderminer entityUnderminer0) {
        switch(this.rightArmPose) {
            case EMPTY:
                this.rightArm.rotateAngleY = 0.0F;
                break;
            case BLOCK:
                this.rightArm.rotateAngleX = this.rightArm.rotateAngleX * 0.5F - 0.9424779F;
                this.rightArm.rotateAngleY = (float) (-Math.PI / 6);
                break;
            case ITEM:
                this.rightArm.rotateAngleX = this.rightArm.rotateAngleX * 0.5F - (float) (Math.PI / 10);
                this.rightArm.rotateAngleY = 0.0F;
                break;
            case THROW_SPEAR:
                this.rightArm.rotateAngleX = this.rightArm.rotateAngleX * 0.5F - (float) Math.PI;
                this.rightArm.rotateAngleY = 0.0F;
                break;
            case BOW_AND_ARROW:
                this.rightArm.rotateAngleY = -0.1F + this.head.rotateAngleY;
                this.leftArm.rotateAngleY = 0.1F + this.head.rotateAngleY + 0.4F;
                this.rightArm.rotateAngleX = (float) (-Math.PI / 2) + this.head.rotateAngleX;
                this.leftArm.rotateAngleX = (float) (-Math.PI / 2) + this.head.rotateAngleX;
            case CROSSBOW_CHARGE:
            case CROSSBOW_HOLD:
            default:
                break;
            case SPYGLASS:
                this.rightArm.rotateAngleX = Mth.clamp(this.head.rotateAngleX - 1.9198622F - (entityUnderminer0.m_6047_() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                this.rightArm.rotateAngleY = this.head.rotateAngleY - (float) (Math.PI / 12);
                break;
            case TOOT_HORN:
                this.rightArm.rotateAngleX = Mth.clamp(this.head.rotateAngleX, -1.2F, 1.2F) - 1.4835298F;
                this.rightArm.rotateAngleY = this.head.rotateAngleY - (float) (Math.PI / 6);
        }
    }

    private void poseLeftArm(EntityUnderminer entityUnderminer0) {
        switch(this.leftArmPose) {
            case EMPTY:
                this.leftArm.rotateAngleY = 0.0F;
                break;
            case BLOCK:
                this.leftArm.rotateAngleX = this.leftArm.rotateAngleX * 0.5F - 0.9424779F;
                this.leftArm.rotateAngleY = (float) (Math.PI / 6);
                break;
            case ITEM:
                this.leftArm.rotateAngleX = this.leftArm.rotateAngleX * 0.5F - (float) (Math.PI / 10);
                this.leftArm.rotateAngleY = 0.0F;
                break;
            case THROW_SPEAR:
                this.leftArm.rotateAngleX = this.leftArm.rotateAngleX * 0.5F - (float) Math.PI;
                this.leftArm.rotateAngleY = 0.0F;
                break;
            case BOW_AND_ARROW:
                this.rightArm.rotateAngleY = -0.1F + this.head.rotateAngleY - 0.4F;
                this.leftArm.rotateAngleY = 0.1F + this.head.rotateAngleY;
                this.rightArm.rotateAngleX = (float) (-Math.PI / 2) + this.head.rotateAngleX;
                this.leftArm.rotateAngleX = (float) (-Math.PI / 2) + this.head.rotateAngleX;
            case CROSSBOW_CHARGE:
            case CROSSBOW_HOLD:
            default:
                break;
            case SPYGLASS:
                this.leftArm.rotateAngleX = Mth.clamp(this.head.rotateAngleX - 1.9198622F - (entityUnderminer0.m_6047_() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                this.leftArm.rotateAngleY = this.head.rotateAngleY + (float) (Math.PI / 12);
                break;
            case TOOT_HORN:
                this.leftArm.rotateAngleX = Mth.clamp(this.head.rotateAngleX, -1.2F, 1.2F) - 1.4835298F;
                this.leftArm.rotateAngleY = this.head.rotateAngleY + (float) (Math.PI / 6);
        }
    }

    protected void setupAttackAnimation(EntityUnderminer entityUnderminer0, float float1) {
        if (!(this.f_102608_ <= 0.0F)) {
            HumanoidArm humanoidarm = this.getAttackArm(entityUnderminer0);
            AdvancedModelBox modelpart = this.getArm(humanoidarm);
            float f = this.f_102608_;
            this.body.rotateAngleY = Mth.sin(Mth.sqrt(f) * (float) (Math.PI * 2)) * 0.2F;
            if (humanoidarm == HumanoidArm.LEFT) {
                this.body.rotateAngleY *= -1.0F;
            }
            this.head.rotateAngleY = this.head.rotateAngleY - this.body.rotateAngleY;
            this.leftLeg.rotateAngleY = this.leftLeg.rotateAngleY - this.body.rotateAngleY;
            this.rightLeg.rotateAngleY = this.rightLeg.rotateAngleY - this.body.rotateAngleY;
            this.leftArm.rotateAngleX = this.leftArm.rotateAngleX + this.body.rotateAngleY;
            f = 1.0F - this.f_102608_;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float) Math.PI);
            float f2 = Mth.sin(this.f_102608_ * (float) Math.PI) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
            modelpart.rotateAngleX -= f1 * 1.2F + f2;
            modelpart.rotateAngleY = modelpart.rotateAngleY + this.body.rotateAngleY * 2.0F;
            modelpart.rotateAngleZ = modelpart.rotateAngleZ + Mth.sin(this.f_102608_ * (float) Math.PI) * -0.4F;
        }
    }

    protected float rotlerpRad(float float0, float float1, float float2) {
        float f = (float2 - float1) % (float) (Math.PI * 2);
        if (f < (float) -Math.PI) {
            f += (float) (Math.PI * 2);
        }
        if (f >= (float) Math.PI) {
            f -= (float) (Math.PI * 2);
        }
        return float1 + float0 * f;
    }

    private float quadraticArmUpdate(float float0) {
        return -65.0F * float0 + float0 * float0;
    }

    public void translateToHand(HumanoidArm humanoidArm0, PoseStack poseStack1) {
        this.getArm(humanoidArm0).translateAndRotate(poseStack1);
    }

    protected AdvancedModelBox getArm(HumanoidArm humanoidArm0) {
        return humanoidArm0 == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }

    public AdvancedModelBox getHead() {
        return this.head;
    }

    private HumanoidArm getAttackArm(EntityUnderminer entityUnderminer0) {
        HumanoidArm humanoidarm = entityUnderminer0.m_5737_();
        return entityUnderminer0.f_20912_ == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
    }
}