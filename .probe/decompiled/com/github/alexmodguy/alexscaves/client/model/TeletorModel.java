package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.TeletorEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class TeletorModel extends AdvancedEntityModel<TeletorEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox rarmPivot;

    private final AdvancedModelBox larmPivot;

    private final AdvancedModelBox rlegcrossed;

    private final AdvancedModelBox llegcrossed;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox lleg;

    public TeletorModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body.setTextureOffset(40, 37).addBox(-3.0F, -16.0F, -2.0F, 6.0F, 6.0F, 4.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -16.0F, 0.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-6.0F, -18.0F, -6.0F, 12.0F, 17.0F, 12.0F, 0.0F, false);
        this.head.setTextureOffset(40, 29).addBox(6.0F, -9.0F, -2.0F, 9.0F, 4.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(20, 29).addBox(9.0F, -23.0F, -2.0F, 6.0F, 14.0F, 4.0F, 0.0F, false);
        this.head.setTextureOffset(40, 29).addBox(-15.0F, -9.0F, -2.0F, 9.0F, 4.0F, 4.0F, 0.0F, true);
        this.head.setTextureOffset(0, 29).addBox(-15.0F, -23.0F, -2.0F, 6.0F, 14.0F, 4.0F, 0.0F, false);
        this.rarmPivot = new AdvancedModelBox(this);
        this.rarmPivot.setRotationPoint(-3.0F, -16.0F, 0.0F);
        this.body.addChild(this.rarmPivot);
        this.rarm = new AdvancedModelBox(this);
        this.rarmPivot.addChild(this.rarm);
        this.rarm.setTextureOffset(34, 8).addBox(-9.0F, 0.0F, -1.0F, 9.0F, 0.0F, 2.0F, 0.0F, true);
        this.larmPivot = new AdvancedModelBox(this);
        this.larmPivot.setRotationPoint(3.0F, -16.0F, 0.0F);
        this.body.addChild(this.larmPivot);
        this.larm = new AdvancedModelBox(this);
        this.larmPivot.addChild(this.larm);
        this.larm.setTextureOffset(34, 8).addBox(0.0F, 0.0F, -1.0F, 9.0F, 0.0F, 2.0F, 0.0F, false);
        this.rlegcrossed = new AdvancedModelBox(this);
        this.rlegcrossed.setRotationPoint(-1.5F, -10.0F, 0.0F);
        this.body.addChild(this.rlegcrossed);
        this.rlegcrossed.setTextureOffset(0, 47).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 2.0F, 5.0F, 0.0F, true);
        this.llegcrossed = new AdvancedModelBox(this);
        this.llegcrossed.setRotationPoint(1.5F, -9.0F, -3.0F);
        this.body.addChild(this.llegcrossed);
        this.llegcrossed.setTextureOffset(0, 47).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 5.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-2.0F, -10.0F, 0.0F);
        this.body.addChild(this.rleg);
        this.rleg.setTextureOffset(4, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 6.0F, 0.0F, 0.0F, false);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(2.0F, -10.0F, 0.0F);
        this.body.addChild(this.lleg);
        this.lleg.setTextureOffset(4, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 6.0F, 0.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.head, this.larm, this.lleg, this.llegcrossed, this.rarm, this.rleg, this.rlegcrossed, this.rarmPivot, this.larmPivot);
    }

    public void setupAnim(TeletorEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        if (entity.areLegsCrossed(limbSwingAmount)) {
            this.lleg.showModel = false;
            this.rleg.showModel = false;
            this.llegcrossed.showModel = true;
            this.rlegcrossed.showModel = true;
        } else {
            this.lleg.showModel = true;
            this.rleg.showModel = true;
            this.llegcrossed.showModel = false;
            this.rlegcrossed.showModel = false;
        }
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float controlProgress = entity.getControlProgress(partialTick);
        this.progressRotationPrev(this.body, limbSwingAmount, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, limbSwingAmount, (float) Math.toRadians(-10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, controlProgress * limbSwingAmount, (float) Math.toRadians(-10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, limbSwingAmount, (float) Math.toRadians(40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, limbSwingAmount, (float) Math.toRadians(40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, limbSwingAmount, (float) Math.toRadians(40.0), (float) Math.toRadians(40.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.larm, limbSwingAmount, (float) Math.toRadians(40.0), (float) Math.toRadians(-40.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, controlProgress * limbSwingAmount, (float) Math.toRadians(-60.0), (float) Math.toRadians(-40.0), (float) Math.toRadians(60.0), 1.0F);
        this.progressRotationPrev(this.larm, controlProgress * limbSwingAmount, (float) Math.toRadians(-60.0), (float) Math.toRadians(40.0), (float) Math.toRadians(-60.0), 1.0F);
        this.progressPositionPrev(this.body, limbSwingAmount, 0.0F, 1.0F, 2.0F, 1.0F);
        this.progressPositionPrev(this.head, limbSwingAmount, 0.0F, -1.0F, -2.0F, 1.0F);
        this.progressPositionPrev(this.rarm, limbSwingAmount, -1.0F, 1.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.larm, limbSwingAmount, 1.0F, 1.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.rarm, controlProgress, -1.0F, 1.0F, -2.0F, 1.0F);
        this.progressPositionPrev(this.larm, controlProgress, 1.0F, 1.0F, -2.0F, 1.0F);
        this.progressRotationPrev(this.rarm, controlProgress, (float) Math.toRadians(-10.0), (float) Math.toRadians(-90.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.larm, controlProgress, (float) Math.toRadians(-10.0), (float) Math.toRadians(90.0), 0.0F, 1.0F);
        this.bob(this.body, 0.1F, 2.0F, false, ageInTicks, 1.0F);
        this.bob(this.head, 0.1F, 1.0F, false, ageInTicks, 1.0F);
        this.flap(this.larm, 0.1F, 0.2F, true, -1.0F, -0.4F, ageInTicks, 1.0F);
        this.flap(this.rarm, 0.1F, 0.2F, false, -1.0F, -0.4F, ageInTicks, 1.0F);
        this.swing(this.llegcrossed, 0.1F, 0.2F, false, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.rlegcrossed, 0.1F, 0.2F, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.rarm, 0.5F, 0.2F, false, 2.0F, 0.0F, ageInTicks, controlProgress);
        this.swing(this.larm, 0.5F, 0.2F, true, 2.0F, 0.0F, ageInTicks, controlProgress);
        this.rarm.rotationPointZ = this.rarm.rotationPointZ - (float) (Math.sin((double) (ageInTicks * 0.5F)) * (double) controlProgress);
        this.rarm.rotationPointX = this.rarm.rotationPointX - (float) (Math.sin((double) (ageInTicks * 0.5F + 2.0F)) * (double) controlProgress * 0.5);
        this.larm.rotationPointZ = this.larm.rotationPointZ - (float) (Math.sin((double) (ageInTicks * 0.5F)) * (double) controlProgress);
        this.larm.rotationPointX = this.larm.rotationPointX + (float) (Math.sin((double) (ageInTicks * 0.5F + 2.0F)) * (double) controlProgress * 0.5);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
        Entity look = entity.getWeapon();
        if (look != null) {
            Vec3 vector3d = look.getEyePosition(partialTick);
            Vec3 vector3d1 = entity.m_20299_(partialTick);
            double d0 = Mth.clamp((vector3d.y - vector3d1.y) * 0.5, -1.0, 1.0) * Math.PI / 2.0;
            Vec3 vector3d2 = entity.m_20252_(0.0F);
            vector3d2 = new Vec3(vector3d2.x, 0.0, vector3d2.z);
            Vec3 vector3d3 = new Vec3(vector3d.x - vector3d1.x, 0.0, vector3d.z - vector3d1.z).normalize().yRot((float) (Math.PI / 2));
            double d1 = vector3d2.dot(vector3d3);
            this.rarmPivot.rotateAngleX = (float) ((double) this.rarmPivot.rotateAngleX - d0 * (double) controlProgress);
            this.larmPivot.rotateAngleX = (float) ((double) this.larmPivot.rotateAngleX - d0 * (double) controlProgress);
            this.rarmPivot.rotateAngleY = (float) ((double) this.rarmPivot.rotateAngleY + d1 * (double) controlProgress);
            this.larmPivot.rotateAngleY = (float) ((double) this.larmPivot.rotateAngleY + d1 * (double) controlProgress);
            if (d0 > 0.0) {
                this.head.rotationPointY = (float) ((double) this.head.rotationPointY - d0 * (double) controlProgress * 5.0);
            }
        }
    }

    public Vec3 translateToHead(Vec3 in, float yawIn) {
        PoseStack modelTranslateStack = new PoseStack();
        modelTranslateStack.mulPose(Axis.YP.rotationDegrees(180.0F - yawIn));
        modelTranslateStack.translate((double) (this.body.rotationPointX / 16.0F), (double) (this.body.rotationPointY / 16.0F), (double) (this.body.rotationPointZ / 16.0F));
        modelTranslateStack.mulPose(Axis.ZN.rotation(this.body.rotateAngleZ));
        modelTranslateStack.mulPose(Axis.YN.rotation(this.body.rotateAngleY));
        modelTranslateStack.mulPose(Axis.XN.rotation(this.body.rotateAngleX));
        modelTranslateStack.translate((double) (this.head.rotationPointX / 16.0F), (double) (this.head.rotationPointY / 16.0F), (double) (this.head.rotationPointZ / 16.0F));
        modelTranslateStack.mulPose(Axis.ZN.rotation(this.head.rotateAngleZ));
        modelTranslateStack.mulPose(Axis.YN.rotation(this.head.rotateAngleY));
        modelTranslateStack.mulPose(Axis.XN.rotation(this.head.rotateAngleX));
        Vector4f bodyOffsetVec = new Vector4f((float) in.x, (float) in.y, (float) in.z, 1.0F);
        bodyOffsetVec.mul(modelTranslateStack.last().pose());
        Vec3 offset = new Vec3((double) bodyOffsetVec.x(), (double) bodyOffsetVec.y(), (double) bodyOffsetVec.z());
        modelTranslateStack.popPose();
        return offset;
    }
}