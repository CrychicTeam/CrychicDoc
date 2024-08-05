package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityFlutter;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ModelFlutter extends AdvancedEntityModel<EntityFlutter> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox eyes;

    private final AdvancedModelBox petals;

    private final AdvancedModelBox front_petal;

    private final AdvancedModelBox left_petal;

    private final AdvancedModelBox right_petal;

    private final AdvancedModelBox back_petal;

    private final AdvancedModelBox left_arm;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox right_leg;

    private final AdvancedModelBox right_arm;

    public ModelFlutter() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -3.9F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 13).addBox(-3.5F, -3.0F, -3.5F, 7.0F, 5.0F, 7.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-3.5F, -3.0F, -3.5F, 7.0F, 5.0F, 7.0F, -0.2F, false);
        this.eyes = new AdvancedModelBox(this, "eyes");
        this.eyes.setRotationPoint(0.0F, -1.0F, -3.0F);
        this.body.addChild(this.eyes);
        this.eyes.setTextureOffset(23, 30).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, 0.0F, false);
        this.petals = new AdvancedModelBox(this, "petals");
        this.petals.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.body.addChild(this.petals);
        this.front_petal = new AdvancedModelBox(this, "front_petal");
        this.front_petal.setRotationPoint(0.0F, 0.0F, -1.5F);
        this.petals.addChild(this.front_petal);
        this.setRotationAngle(this.front_petal, 1.1781F, 0.0F, 0.0F);
        this.front_petal.setTextureOffset(0, 26).addBox(-3.5F, -7.0F, 0.0F, 7.0F, 7.0F, 0.0F, 0.0F, false);
        this.left_petal = new AdvancedModelBox(this, "left_petal");
        this.left_petal.setRotationPoint(1.5F, 0.0F, 0.0F);
        this.petals.addChild(this.left_petal);
        this.setRotationAngle(this.left_petal, 1.1781F, -1.5708F, 0.0F);
        this.left_petal.setTextureOffset(0, 26).addBox(-3.5F, -7.0F, 0.0F, 7.0F, 7.0F, 0.0F, 0.0F, false);
        this.right_petal = new AdvancedModelBox(this, "right_petal");
        this.right_petal.setRotationPoint(-1.5F, 0.0F, 0.0F);
        this.petals.addChild(this.right_petal);
        this.setRotationAngle(this.right_petal, 1.1781F, 1.5708F, 0.0F);
        this.right_petal.setTextureOffset(0, 26).addBox(-3.5F, -7.0F, 0.0F, 7.0F, 7.0F, 0.0F, 0.0F, true);
        this.back_petal = new AdvancedModelBox(this, "back_petal");
        this.back_petal.setRotationPoint(0.0F, 0.0F, 1.5F);
        this.petals.addChild(this.back_petal);
        this.setRotationAngle(this.back_petal, 1.1781F, 3.1416F, 0.0F);
        this.back_petal.setTextureOffset(0, 26).addBox(-3.5F, -7.0F, 0.0F, 7.0F, 7.0F, 0.0F, 0.0F, false);
        this.left_arm = new AdvancedModelBox(this, "left_arm");
        this.left_arm.setRotationPoint(3.0F, 1.9F, -3.0F);
        this.body.addChild(this.left_arm);
        this.setRotationAngle(this.left_arm, 0.0F, -0.7418F, 0.0F);
        this.left_arm.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(3.0F, 1.9F, 3.0F);
        this.body.addChild(this.left_leg);
        this.setRotationAngle(this.left_leg, -3.1416F, -0.7418F, 3.1416F);
        this.left_leg.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-3.0F, 1.9F, 3.0F);
        this.body.addChild(this.right_leg);
        this.setRotationAngle(this.right_leg, -3.1416F, 0.7418F, -3.1416F);
        this.right_leg.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, true);
        this.right_arm = new AdvancedModelBox(this, "right_arm");
        this.right_arm.setRotationPoint(-3.0F, 1.9F, -3.0F);
        this.body.addChild(this.right_arm);
        this.setRotationAngle(this.right_arm, 0.0F, 0.7418F, 0.0F);
        this.right_arm.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.eyes, this.petals, this.front_petal, this.left_petal, this.back_petal, this.right_petal, this.left_arm, this.right_arm, this.left_leg, this.right_leg, new AdvancedModelBox[0]);
    }

    public void setupAnim(EntityFlutter entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.25F;
        float idleDegree = 0.1F;
        float walkSpeed = 1.6F;
        float walkDegree = 1.2F;
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float shootProgress = entity.prevShootProgress + (entity.shootProgress - entity.prevShootProgress) * partialTicks;
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTicks;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTicks;
        float groundProgress = (5.0F - flyProgress) * 0.2F;
        float tentacleProgress = (5.0F - limbSwingAmount * 5.0F) * flyProgress * 0.2F;
        float invertTentacle = (entity.prevTentacleProgress + (entity.tentacleProgress - entity.prevTentacleProgress) * partialTicks) * flyProgress * 0.2F;
        float flutterPitch = Maths.rad((double) Mth.rotLerp(partialTicks, entity.prevFlutterPitch, entity.getFlutterPitch()));
        Entity look = Minecraft.getInstance().getCameraEntity();
        if (entity.isShakingHead()) {
            this.eyes.rotationPointX = (float) ((double) this.eyes.rotationPointX + Math.sin((double) ageInTicks));
            this.body.rotateAngleY = (float) ((double) this.body.rotateAngleY + Math.sin((double) ageInTicks) * 0.1F);
            this.eyes.rotationPointY = -0.5F;
        } else if (look != null) {
            Vec3 vector3d = look.getEyePosition(0.0F);
            Vec3 vector3d1 = entity.m_20299_(0.0F);
            double d0 = vector3d.y - vector3d1.y;
            float f1 = (float) Mth.clamp(-d0 - 0.5, -2.0, 0.0);
            this.eyes.rotationPointY = f1;
            Vec3 vector3d2 = entity.m_20252_(0.0F);
            vector3d2 = new Vec3(vector3d2.x, 0.0, vector3d2.z);
            Vec3 vector3d3 = new Vec3(vector3d1.x - vector3d.x, 0.0, vector3d1.z - vector3d.z).normalize().yRot((float) (Math.PI / 2));
            double d1 = vector3d2.dot(vector3d3);
            this.eyes.rotationPointX = this.eyes.rotationPointX + Mth.sqrt((float) Math.abs(d1)) * 1.5F * (float) Math.signum(d1);
        } else {
            this.eyes.rotationPointY = -1.0F;
        }
        this.walk(this.right_arm, walkSpeed, walkDegree * 1.5F, true, 0.0F, 0.3F, limbSwing, limbSwingAmount * groundProgress);
        this.walk(this.left_arm, walkSpeed, walkDegree * 1.5F, false, 0.0F, -0.3F, limbSwing, limbSwingAmount * groundProgress);
        this.walk(this.right_leg, walkSpeed, walkDegree * 1.5F, false, 0.0F, -0.3F, limbSwing, limbSwingAmount * groundProgress);
        this.walk(this.left_leg, walkSpeed, walkDegree * 1.5F, true, 0.0F, 0.3F, limbSwing, limbSwingAmount * groundProgress);
        this.swing(this.body, walkSpeed, walkDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount * groundProgress);
        this.flap(this.body, walkSpeed, walkDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount * groundProgress);
        this.bob(this.body, walkSpeed * 1.5F, walkDegree, false, limbSwing, limbSwingAmount * groundProgress);
        this.walk(this.front_petal, idleSpeed, idleDegree, true, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.back_petal, idleSpeed, idleDegree, true, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.right_petal, idleSpeed, idleDegree, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.flap(this.left_petal, idleSpeed, idleDegree, true, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.progressRotationPrev(this.front_petal, Math.max(shootProgress, invertTentacle), Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.back_petal, Math.max(shootProgress, invertTentacle), Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_petal, Math.max(shootProgress, invertTentacle), 0.0F, 0.0F, Maths.rad(45.0), 5.0F);
        this.progressRotationPrev(this.left_petal, Math.max(shootProgress, invertTentacle), 0.0F, 0.0F, Maths.rad(-45.0), 5.0F);
        this.progressRotationPrev(this.front_petal, Math.max(invertTentacle - shootProgress, 0.0F), Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.back_petal, Math.max(invertTentacle - shootProgress, 0.0F), Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_petal, Math.max(invertTentacle - shootProgress, 0.0F), 0.0F, 0.0F, Maths.rad(45.0), 5.0F);
        this.progressRotationPrev(this.left_petal, Math.max(invertTentacle - shootProgress, 0.0F), 0.0F, 0.0F, Maths.rad(-45.0), 5.0F);
        this.progressRotationPrev(this.front_petal, flyProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.back_petal, flyProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_petal, flyProgress, 0.0F, 0.0F, Maths.rad(-15.0), 5.0F);
        this.progressRotationPrev(this.left_petal, flyProgress, 0.0F, 0.0F, Maths.rad(15.0), 5.0F);
        this.progressPositionPrev(this.body, tentacleProgress, 0.0F, -3.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, tentacleProgress, Maths.rad(105.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, tentacleProgress, Maths.rad(105.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, tentacleProgress, Maths.rad(105.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, tentacleProgress, Maths.rad(105.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.front_petal, tentacleProgress, Maths.rad(5.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.back_petal, tentacleProgress, Maths.rad(5.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_petal, tentacleProgress, Maths.rad(5.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_petal, tentacleProgress, Maths.rad(5.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 2.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg, sitProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg, sitProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.right_arm, sitProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_arm, sitProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.root.rotateAngleX -= flutterPitch * flyProgress * 0.2F;
        this.body.rotateAngleY = (float) ((double) this.body.rotateAngleY + Math.toRadians((double) Mth.wrapDegrees(shootProgress * 360.0F * 0.2F)));
        float petalScale = 1.0F + invertTentacle * 0.05F;
        this.front_petal.setScale(1.0F, petalScale, 1.0F);
        this.back_petal.setScale(1.0F, petalScale, 1.0F);
        this.left_petal.setScale(1.0F, petalScale, 1.0F);
        this.right_petal.setScale(1.0F, petalScale, 1.0F);
        if (entity.m_6162_()) {
            this.root.rotationPointY++;
            this.body.setScale(0.5F, 0.5F, 0.5F);
            this.body.setShouldScaleChildren(true);
        } else {
            this.body.setScale(1.0F, 1.0F, 1.0F);
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}