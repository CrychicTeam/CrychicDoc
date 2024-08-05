package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityEnderiophage;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ModelEnderiophage extends AdvancedEntityModel<EntityEnderiophage> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox mouth;

    private final AdvancedModelBox sheath;

    private final AdvancedModelBox collar;

    private final AdvancedModelBox capsid;

    private final AdvancedModelBox eye;

    private final AdvancedModelBox tailmid_left;

    private final AdvancedModelBox tailmid_right;

    private final AdvancedModelBox tailback_left;

    private final AdvancedModelBox tailback_right;

    private final AdvancedModelBox tailfront_left;

    private final AdvancedModelBox tailfront_right;

    private final AdvancedModelBox tailmid_leftPivot;

    private final AdvancedModelBox tailmid_rightPivot;

    private final AdvancedModelBox tailback_leftPivot;

    private final AdvancedModelBox tailback_rightPivot;

    private final AdvancedModelBox tailfront_leftPivot;

    private final AdvancedModelBox tailfront_rightPivot;

    public ModelEnderiophage() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -11.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 30).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 3.0F, 8.0F, 0.0F, false);
        this.mouth = new AdvancedModelBox(this, "mouth");
        this.mouth.setPos(0.0F, 1.0F, 0.0F);
        this.body.addChild(this.mouth);
        this.mouth.setTextureOffset(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        this.sheath = new AdvancedModelBox(this, "sheath");
        this.sheath.setPos(0.0F, -2.0F, 0.0F);
        this.body.addChild(this.sheath);
        this.sheath.setTextureOffset(50, 43).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 14.0F, 4.0F, 0.0F, false);
        this.collar = new AdvancedModelBox(this, "collar");
        this.collar.setPos(0.0F, -14.0F, 0.0F);
        this.sheath.addChild(this.collar);
        this.collar.setTextureOffset(0, 55).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);
        this.capsid = new AdvancedModelBox(this, "capsid");
        this.capsid.setPos(0.0F, -1.0F, 0.0F);
        this.collar.addChild(this.capsid);
        this.capsid.setTextureOffset(0, 0).addBox(-7.0F, -15.0F, -7.0F, 14.0F, 15.0F, 14.0F, 0.0F, false);
        this.eye = new AdvancedModelBox(this, "eye");
        this.eye.setPos(0.0F, -8.0F, 0.0F);
        this.capsid.addChild(this.eye);
        this.eye.setTextureOffset(43, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
        this.tailmid_leftPivot = new AdvancedModelBox(this, "tailmid_leftPivot");
        this.tailmid_leftPivot.setPos(4.0F, -1.0F, 0.0F);
        this.body.addChild(this.tailmid_leftPivot);
        this.tailmid_left = new AdvancedModelBox(this, "tailmid_left");
        this.tailmid_leftPivot.addChild(this.tailmid_left);
        this.tailmid_left.setTextureOffset(25, 43).addBox(0.0F, 0.0F, 0.0F, 12.0F, 12.0F, 0.0F, 0.0F, false);
        this.tailmid_rightPivot = new AdvancedModelBox(this, "tailmid_rightPivot");
        this.tailmid_rightPivot.setPos(-4.0F, -1.0F, 0.0F);
        this.body.addChild(this.tailmid_rightPivot);
        this.tailmid_right = new AdvancedModelBox(this, "tailmid_right");
        this.tailmid_rightPivot.addChild(this.tailmid_right);
        this.tailmid_right.setTextureOffset(25, 43).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 12.0F, 0.0F, 0.0F, true);
        this.tailback_leftPivot = new AdvancedModelBox(this, "tailback_leftPivot");
        this.tailback_leftPivot.setPos(4.0F, -1.0F, 4.0F);
        this.body.addChild(this.tailback_leftPivot);
        this.setRotationAngle(this.tailback_leftPivot, 0.0F, -0.7854F, 0.0F);
        this.tailback_left = new AdvancedModelBox(this, "tailback_left");
        this.tailback_leftPivot.addChild(this.tailback_left);
        this.tailback_left.setTextureOffset(33, 30).addBox(0.0F, 0.0F, 0.0F, 12.0F, 12.0F, 0.0F, 0.0F, false);
        this.tailback_rightPivot = new AdvancedModelBox(this, "tailback_rightPivot");
        this.tailback_rightPivot.setPos(-4.0F, -1.0F, 4.0F);
        this.body.addChild(this.tailback_rightPivot);
        this.setRotationAngle(this.tailback_rightPivot, 0.0F, 0.7854F, 0.0F);
        this.tailback_right = new AdvancedModelBox(this, "tailback_right");
        this.tailback_rightPivot.addChild(this.tailback_right);
        this.tailback_right.setTextureOffset(33, 30).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 12.0F, 0.0F, 0.0F, true);
        this.tailfront_leftPivot = new AdvancedModelBox(this, "tailfront_leftPivot");
        this.tailfront_leftPivot.setPos(4.0F, -1.0F, -4.0F);
        this.body.addChild(this.tailfront_leftPivot);
        this.setRotationAngle(this.tailfront_leftPivot, 0.0F, 0.6981F, 0.0F);
        this.tailfront_left = new AdvancedModelBox(this, "tailfront_left");
        this.tailfront_leftPivot.addChild(this.tailfront_left);
        this.tailfront_left.setTextureOffset(0, 42).addBox(0.0F, 0.0F, 0.0F, 12.0F, 12.0F, 0.0F, 0.0F, false);
        this.tailfront_rightPivot = new AdvancedModelBox(this, "tailfront_rightPivot");
        this.tailfront_rightPivot.setPos(-4.0F, -1.0F, -4.0F);
        this.body.addChild(this.tailfront_rightPivot);
        this.setRotationAngle(this.tailfront_rightPivot, 0.0F, -0.6981F, 0.0F);
        this.tailfront_right = new AdvancedModelBox(this, "tailfront_right");
        this.tailfront_rightPivot.addChild(this.tailfront_right);
        this.tailfront_right.setTextureOffset(0, 42).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 12.0F, 0.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.tailback_left, this.tailback_right, this.tailfront_left, this.tailfront_right, this.tailmid_left, this.tailmid_right, this.tailback_leftPivot, this.tailback_rightPivot, this.tailfront_leftPivot, this.tailfront_rightPivot, this.tailmid_leftPivot, new AdvancedModelBox[] { this.tailmid_rightPivot, this.body, this.capsid, this.eye, this.mouth, this.sheath, this.collar });
    }

    public void setupAnim(EntityEnderiophage entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        Entity look = Minecraft.getInstance().getCameraEntity();
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float idleSpeed = 0.25F;
        float idleDegree = 0.1F;
        float walkSpeed = 2.0F;
        float walkDegree = 0.6F;
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTicks;
        float phagePitch = Maths.rad((double) Mth.rotLerp(partialTicks, entity.prevPhagePitch, entity.getPhagePitch()));
        float totalYaw = Maths.rad((double) Mth.rotLerp(partialTicks, entity.f_20884_, entity.f_20883_));
        float tentacleProgress = (5.0F - limbSwingAmount * 10.0F) * flyProgress * 0.2F;
        this.bob(this.eye, idleSpeed, idleDegree * -8.0F, false, ageInTicks, 1.0F);
        this.flap(this.tailback_left, idleSpeed, idleDegree, true, -2.0F, 0.5F, ageInTicks, 1.0F);
        this.flap(this.tailback_right, idleSpeed, idleDegree, false, -2.0F, 0.5F, ageInTicks, 1.0F);
        this.walk(this.tailback_left, idleSpeed, idleDegree, false, -2.0F, 0.25F, ageInTicks, 1.0F);
        this.walk(this.tailback_right, idleSpeed, idleDegree, false, -2.0F, 0.25F, ageInTicks, 1.0F);
        this.flap(this.tailmid_left, idleSpeed, idleDegree, true, -2.0F, 0.5F, ageInTicks, 1.0F);
        this.flap(this.tailmid_right, idleSpeed, idleDegree, false, -2.0F, 0.5F, ageInTicks, 1.0F);
        this.flap(this.tailfront_left, idleSpeed, idleDegree, true, -2.0F, 0.5F, ageInTicks, 1.0F);
        this.flap(this.tailfront_right, idleSpeed, idleDegree, false, -2.0F, 0.5F, ageInTicks, 1.0F);
        this.walk(this.tailfront_left, idleSpeed, idleDegree, true, -2.0F, 0.25F, ageInTicks, 1.0F);
        this.walk(this.tailfront_right, idleSpeed, idleDegree, true, -2.0F, 0.25F, ageInTicks, 1.0F);
        this.bob(this.body, idleSpeed, idleDegree * 8.0F, false, ageInTicks, 1.0F);
        this.body.rotationPointY += 8.0F;
        if (flyProgress != 5.0F) {
            limbSwingAmount *= 1.0F - flyProgress * 0.2F;
            this.walk(this.sheath, walkSpeed, walkDegree * 0.2F, true, 1.0F, 0.05F, limbSwing, limbSwingAmount);
            this.swing(this.tailfront_right, walkSpeed, walkDegree * -1.2F, false, 0.0F, -0.3F, limbSwing, limbSwingAmount);
            this.swing(this.tailfront_left, walkSpeed, walkDegree * -1.2F, false, 0.0F, 0.3F, limbSwing, limbSwingAmount);
            this.flap(this.tailfront_right, walkSpeed, walkDegree * -1.6F, false, 0.0F, -0.2F, limbSwing, limbSwingAmount);
            this.flap(this.tailfront_left, walkSpeed, walkDegree * -1.6F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.tailfront_right, walkSpeed, walkDegree * -1.6F, true, 0.0F, 0.3F, limbSwing, limbSwingAmount);
            this.walk(this.tailfront_left, walkSpeed, walkDegree * -1.6F, false, 0.0F, -0.3F, limbSwing, limbSwingAmount);
            this.swing(this.tailmid_right, walkSpeed, walkDegree * -1.2F, false, -2.5F, 0.2F, limbSwing, limbSwingAmount);
            this.swing(this.tailmid_left, walkSpeed, walkDegree * -1.2F, false, -2.5F, -0.2F, limbSwing, limbSwingAmount);
            this.flap(this.tailmid_right, walkSpeed, walkDegree * -1.6F, false, -2.5F, 0.5F, limbSwing, limbSwingAmount);
            this.flap(this.tailmid_left, walkSpeed, walkDegree * -1.6F, false, -2.5F, -0.5F, limbSwing, limbSwingAmount);
            this.walk(this.tailmid_right, walkSpeed, walkDegree * -1.6F, true, -2.5F, 0.3F, limbSwing, limbSwingAmount);
            this.walk(this.tailmid_left, walkSpeed, walkDegree * -1.6F, false, -2.5F, -0.3F, limbSwing, limbSwingAmount);
            this.swing(this.tailback_right, walkSpeed, walkDegree * -1.2F, false, -5.0F, -0.2F, limbSwing, limbSwingAmount);
            this.swing(this.tailback_left, walkSpeed, walkDegree * -1.2F, false, -5.0F, 0.2F, limbSwing, limbSwingAmount);
            this.flap(this.tailback_right, walkSpeed, walkDegree * -1.6F, false, -5.0F, 0.5F, limbSwing, limbSwingAmount);
            this.flap(this.tailback_left, walkSpeed, walkDegree * -1.6F, false, -5.0F, -0.5F, limbSwing, limbSwingAmount);
            this.walk(this.tailback_right, walkSpeed, walkDegree * -1.6F, true, -5.0F, 0.3F, limbSwing, limbSwingAmount);
            this.walk(this.tailback_left, walkSpeed, walkDegree * -1.6F, false, -5.0F, -0.3F, limbSwing, limbSwingAmount);
            this.bob(this.body, walkSpeed * 1.5F, walkDegree * 6.0F, false, limbSwing, limbSwingAmount);
            this.progressRotationPrev(this.body, limbSwingAmount, Maths.rad(-15.0), 0.0F, 0.0F, 1.0F);
            this.progressRotationPrev(this.sheath, limbSwingAmount, Maths.rad(-15.0), 0.0F, 0.0F, 1.0F);
            this.progressRotationPrev(this.tailfront_left, limbSwingAmount, Maths.rad(15.0), 0.0F, 0.0F, 1.0F);
            this.progressRotationPrev(this.tailfront_right, limbSwingAmount, Maths.rad(15.0), 0.0F, 0.0F, 1.0F);
        }
        if (entity.isMissingEye()) {
            this.eye.showModel = false;
        } else {
            this.eye.showModel = true;
        }
        if (entity.m_20159_()) {
            this.body.rotateAngleX++;
            this.body.rotateAngleY = this.body.rotateAngleY + (float) (Math.PI / 2) * (float) entity.passengerIndex;
            this.sheath.setScale(1.0F, (float) (0.85F + Math.sin((double) ageInTicks) * 0.15F), 1.0F);
            this.collar.rotationPointY = this.collar.rotationPointY - (float) (Math.sin((double) ageInTicks) * 0.15F - 0.15F) * 12.0F;
            this.capsid.setScale((float) (0.85F + Math.sin((double) (ageInTicks + 2.0F)) * 0.15F), (float) (1.0 + Math.sin((double) ageInTicks) * 0.15F), (float) (0.85F + Math.sin((double) (ageInTicks + 2.0F)) * 0.15F));
            this.mouth.rotationPointY = (float) ((double) this.mouth.rotationPointY + (Math.sin((double) ageInTicks) + 1.0) * 2.0);
            tentacleProgress = -2.0F;
        } else {
            this.sheath.setScale(1.0F, 1.0F, 1.0F);
            this.capsid.setScale(1.0F, 1.0F, 1.0F);
            this.body.rotateAngleX -= phagePitch * flyProgress * 0.2F;
        }
        this.progressPositionPrev(this.body, tentacleProgress, 0.0F, -6.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tailfront_left, tentacleProgress, 0.0F, 0.0F, Maths.rad(-45.0), 5.0F);
        this.progressRotationPrev(this.tailmid_left, tentacleProgress, 0.0F, 0.0F, Maths.rad(-45.0), 5.0F);
        this.progressRotationPrev(this.tailback_left, tentacleProgress, 0.0F, 0.0F, Maths.rad(-45.0), 5.0F);
        this.progressRotationPrev(this.tailfront_right, tentacleProgress, 0.0F, 0.0F, Maths.rad(45.0), 5.0F);
        this.progressRotationPrev(this.tailmid_right, tentacleProgress, 0.0F, 0.0F, Maths.rad(45.0), 5.0F);
        this.progressRotationPrev(this.tailback_right, tentacleProgress, 0.0F, 0.0F, Maths.rad(45.0), 5.0F);
        if (look != null) {
            Vec3 vector3d = look.getEyePosition(partialTicks);
            Vec3 vector3d1 = entity.m_20299_(partialTicks);
            Vec3 vector3d2 = vector3d.subtract(vector3d1);
            float f = Mth.sqrt((float) (vector3d2.x * vector3d2.x + vector3d2.z * vector3d2.z)) - totalYaw;
            this.eye.rotateAngleY = this.eye.rotateAngleY + (-((float) Mth.atan2(vector3d2.x, vector3d2.z)) - totalYaw);
            this.eye.rotateAngleX = (float) ((double) this.eye.rotateAngleX + -Mth.clamp(vector3d2.y * 0.5, -Math.PI / 2, Math.PI / 2) + (double) (phagePitch * flyProgress * 0.2F));
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}