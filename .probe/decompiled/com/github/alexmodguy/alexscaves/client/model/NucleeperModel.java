package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class NucleeperModel extends AdvancedEntityModel<NucleeperEntity> {

    private final AdvancedModelBox base;

    private final AdvancedModelBox coreTop;

    private final AdvancedModelBox head;

    private final AdvancedModelBox rpupil;

    private final AdvancedModelBox lpupil;

    private final AdvancedModelBox rleg2;

    private final AdvancedModelBox rfoot2;

    private final AdvancedModelBox lleg2;

    private final AdvancedModelBox lfoot2;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox lfoot1;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox rfoot;

    private final AdvancedModelBox coreBottom;

    public NucleeperModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        float f = 0.0F;
        this.base = new AdvancedModelBox(this);
        this.base.setRotationPoint(0.0F, 15.5F, 0.0F);
        this.base.setTextureOffset(80, 21).addBox(-7.0F, -36.5F, -5.0F, 14.0F, 38.0F, 10.0F, f, false);
        this.coreTop = new AdvancedModelBox(this);
        this.coreTop.setRotationPoint(0.0F, -62.5F, 0.0F);
        this.base.addChild(this.coreTop);
        this.coreTop.setTextureOffset(0, 0).addBox(-8.0F, 3.0F, -8.0F, 16.0F, 8.0F, 16.0F, f, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -37.5F, 0.0F);
        this.base.addChild(this.head);
        this.head.setTextureOffset(0, 48).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, f, false);
        this.head.setTextureOffset(26, 86).addBox(-6.0F, -13.5F, -6.0F, 12.0F, 12.0F, 12.0F, f, false);
        this.rpupil = new AdvancedModelBox(this);
        this.rpupil.setRotationPoint(-3.5F, -9.0F, -5.6F);
        this.head.addChild(this.rpupil);
        this.rpupil.setTextureOffset(26, 86).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, f, false);
        this.lpupil = new AdvancedModelBox(this);
        this.lpupil.setRotationPoint(3.5F, -9.0F, -5.6F);
        this.head.addChild(this.lpupil);
        this.lpupil.setTextureOffset(26, 86).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, f, true);
        this.rleg2 = new AdvancedModelBox(this);
        this.rleg2.setRotationPoint(-7.0F, -1.0F, 3.5F);
        this.base.addChild(this.rleg2);
        this.rleg2.setTextureOffset(80, 69).addBox(-2.0F, -2.5F, -3.5F, 6.0F, 5.0F, 12.0F, f, false);
        this.rfoot2 = new AdvancedModelBox(this);
        this.rfoot2.setRotationPoint(1.0F, -2.5F, 4.5F);
        this.rleg2.addChild(this.rfoot2);
        this.rfoot2.setTextureOffset(0, 93).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 12.0F, 5.0F, f, false);
        this.rfoot2.setTextureOffset(1, 28).addBox(0.0F, 0.0F, 5.0F, 0.0F, 6.0F, 3.0F, f, false);
        this.lleg2 = new AdvancedModelBox(this);
        this.lleg2.setRotationPoint(7.0F, -1.0F, 3.5F);
        this.base.addChild(this.lleg2);
        this.lleg2.setTextureOffset(80, 69).addBox(-4.0F, -2.5F, -3.5F, 6.0F, 5.0F, 12.0F, f, true);
        this.lfoot2 = new AdvancedModelBox(this);
        this.lfoot2.setRotationPoint(-1.0F, -2.5F, 4.5F);
        this.lleg2.addChild(this.lfoot2);
        this.lfoot2.setTextureOffset(0, 93).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 12.0F, 5.0F, f, true);
        this.lfoot2.setTextureOffset(1, 28).addBox(0.0F, 0.0F, 5.0F, 0.0F, 6.0F, 3.0F, f, true);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(7.0F, -1.0F, -3.5F);
        this.base.addChild(this.lleg);
        this.lleg.setTextureOffset(44, 69).addBox(-4.0F, -2.5F, -8.5F, 6.0F, 5.0F, 12.0F, f, true);
        this.lfoot1 = new AdvancedModelBox(this);
        this.lfoot1.setRotationPoint(-1.0F, -2.5F, -4.5F);
        this.lleg.addChild(this.lfoot1);
        this.lfoot1.setTextureOffset(0, 76).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 12.0F, 5.0F, f, true);
        this.lfoot1.setTextureOffset(0, 46).addBox(0.0F, 0.0F, -8.0F, 0.0F, 6.0F, 5.0F, f, true);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-7.0F, -1.0F, -3.5F);
        this.base.addChild(this.rleg);
        this.rleg.setTextureOffset(44, 69).addBox(-2.0F, -2.5F, -8.5F, 6.0F, 5.0F, 12.0F, f, false);
        this.rfoot = new AdvancedModelBox(this);
        this.rfoot.setRotationPoint(1.0F, -2.5F, -4.5F);
        this.rleg.addChild(this.rfoot);
        this.rfoot.setTextureOffset(0, 76).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 12.0F, 5.0F, f, false);
        this.rfoot.setTextureOffset(0, 46).addBox(0.0F, 0.0F, -8.0F, 0.0F, 6.0F, 5.0F, f, false);
        this.coreBottom = new AdvancedModelBox(this);
        this.coreBottom.setRotationPoint(0.0F, -27.5F, 0.0F);
        this.base.addChild(this.coreBottom);
        this.coreBottom.setTextureOffset(0, 24).addBox(-8.0F, -11.0F, -8.0F, 16.0F, 8.0F, 16.0F, f, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.base);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.base, this.coreBottom, this.coreTop, this.head, this.rpupil, this.lpupil, this.lleg, this.lleg2, this.rleg, this.rleg2, this.rfoot, this.rfoot2, new AdvancedModelBox[] { this.lfoot2, this.lfoot1 });
    }

    public void setupAnim(NucleeperEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 0.8F;
        float walkDegree = 1.2F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float closeProgress = entity.getCloseProgress(partialTick);
        float explodeProgress = entity.getExplodeProgress(partialTick);
        float stillProgress = 1.0F - limbSwingAmount;
        this.progressPositionPrev(this.head, closeProgress, 0.0F, 8.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.coreTop, closeProgress, 0.0F, 14.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.coreBottom, closeProgress, 0.0F, 1.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, stillProgress, 0.0F, (float) Math.toRadians(-25.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg2, stillProgress, 0.0F, (float) Math.toRadians(25.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, stillProgress, 0.0F, (float) Math.toRadians(25.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg2, stillProgress, 0.0F, (float) Math.toRadians(-25.0), 0.0F, 1.0F);
        this.base.setScale(1.0F - explodeProgress * 0.15F, 1.0F - explodeProgress * 0.65F, 1.0F - explodeProgress * 0.15F);
        this.base.scaleChildren = true;
        this.flap(this.base, walkSpeed, walkDegree * 0.1F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.lleg, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.rleg, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.lleg2, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.rleg2, walkSpeed, walkDegree * 0.1F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        float bodyBob = this.walkValue(limbSwing, limbSwingAmount, walkSpeed * 1.5F, 0.5F, 2.4F, true);
        this.base.rotationPointY += bodyBob;
        this.walk(this.lleg, walkSpeed, walkDegree * 0.3F, false, 1.0F, -0.2F, limbSwing, limbSwingAmount);
        this.walk(this.lfoot1, walkSpeed, walkDegree * 0.2F, false, 3.0F, 0.2F, limbSwing, limbSwingAmount);
        this.lleg.rotationPointY = this.lleg.rotationPointY + (Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 5.0F, true)) - bodyBob);
        this.lleg.rotationPointZ = this.lleg.rotationPointZ + this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 2.5F, true);
        this.walk(this.rleg, walkSpeed, walkDegree * 0.3F, true, 1.0F, 0.2F, limbSwing, limbSwingAmount);
        this.walk(this.rfoot, walkSpeed, walkDegree * 0.2F, true, 3.0F, -0.2F, limbSwing, limbSwingAmount);
        this.rleg.rotationPointY = this.rleg.rotationPointY + (Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 5.0F, false)) - bodyBob);
        this.rleg.rotationPointZ = this.rleg.rotationPointZ + this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 2.5F, false);
        this.walk(this.rleg2, walkSpeed, walkDegree * 0.3F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
        this.walk(this.rfoot2, walkSpeed, walkDegree * 0.2F, false, 2.0F, -0.2F, limbSwing, limbSwingAmount);
        this.rleg2.rotationPointY = this.rleg2.rotationPointY + (Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 5.0F, true)) - bodyBob);
        this.rleg2.rotationPointZ = this.rleg2.rotationPointZ + this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -1.5F, 2.5F, true);
        this.walk(this.lleg2, walkSpeed, walkDegree * 0.3F, true, 0.0F, -0.2F, limbSwing, limbSwingAmount);
        this.walk(this.lfoot2, walkSpeed, walkDegree * 0.2F, true, 2.0F, 0.2F, limbSwing, limbSwingAmount);
        this.lleg2.rotationPointY = this.lleg2.rotationPointY + (Math.min(0.0F, this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 5.0F, false)) - bodyBob);
        this.lleg2.rotationPointZ = this.lleg2.rotationPointZ + this.walkValue(limbSwing, limbSwingAmount, walkSpeed, -1.5F, 2.5F, false);
        this.flap(this.base, 3.0F, 0.3F, true, 1.0F, 0.0F, ageInTicks, explodeProgress);
        Entity look = Minecraft.getInstance().getCameraEntity();
        if (look != null) {
            Vec3 vector3d = look.getEyePosition(0.0F);
            Vec3 vector3d1 = entity.m_20299_(0.0F);
            double d0 = vector3d.y - vector3d1.y;
            float f1 = (float) Mth.clamp(-d0, -1.0, 1.0);
            Vec3 vector3d2 = entity.m_20252_(0.0F);
            vector3d2 = new Vec3(vector3d2.x, 0.0, vector3d2.z);
            Vec3 vector3d3 = new Vec3(vector3d1.x - vector3d.x, 0.0, vector3d1.z - vector3d.z).normalize().yRot((float) (Math.PI / 2));
            double d1 = vector3d2.dot(vector3d3);
            double d2 = (double) (Mth.sqrt((float) Math.abs(d1)) * (float) Math.signum(d1));
            this.lpupil.rotationPointX = (float) ((double) this.lpupil.rotationPointX + (d2 - (double) this.base.rotateAngleZ));
            this.lpupil.rotationPointY += f1;
            this.rpupil.rotationPointX = (float) ((double) this.rpupil.rotationPointX + (d2 - (double) this.base.rotateAngleZ));
            this.rpupil.rotationPointY += f1;
        }
    }

    public Vec3 getSirenPosition(Vec3 offsetIn) {
        PoseStack armStack = new PoseStack();
        armStack.pushPose();
        this.base.translateAndRotate(armStack);
        this.head.translateAndRotate(armStack);
        Vector4f armOffsetVec = new Vector4f((float) offsetIn.x, (float) offsetIn.y, (float) offsetIn.z, 1.0F);
        armOffsetVec.mul(armStack.last().pose());
        Vec3 vec3 = new Vec3((double) armOffsetVec.x(), (double) (-armOffsetVec.y()), (double) armOffsetVec.z());
        armStack.popPose();
        return vec3.add(0.0, 1.5, 0.0);
    }

    private float walkValue(float limbSwing, float limbSwingAmount, float speed, float offset, float degree, boolean inverse) {
        return (float) (Math.cos((double) (limbSwing * speed + offset)) * (double) degree * (double) limbSwingAmount * (double) (inverse ? -1 : 1));
    }
}