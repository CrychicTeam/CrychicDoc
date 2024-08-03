package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class NotorModel extends AdvancedEntityModel<NotorEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox propeller;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox blades;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox lleg;

    public NotorModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.body.setTextureOffset(0, 4).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        this.propeller = new AdvancedModelBox(this);
        this.propeller.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.body.addChild(this.propeller);
        this.propeller.setTextureOffset(0, 0).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.propeller.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, -1.5708F, 0.0F);
        this.cube_r1.setTextureOffset(0, 0).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        this.blades = new AdvancedModelBox(this);
        this.blades.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.propeller.addChild(this.blades);
        this.blades.setTextureOffset(0, 0).addBox(-9.0F, 0.0F, -2.0F, 18.0F, 0.0F, 4.0F, 0.0F, false);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(-1.5F, 2.0F, 2.0F);
        this.body.addChild(this.larm);
        this.larm.setTextureOffset(4, 12).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(1.5F, 2.0F, 2.0F);
        this.body.addChild(this.rarm);
        this.rarm.setTextureOffset(4, 12).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(1.5F, 2.0F, -2.0F);
        this.body.addChild(this.rleg);
        this.rleg.setTextureOffset(4, 12).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(-1.5F, 2.0F, -2.0F);
        this.body.addChild(this.lleg);
        this.lleg.setTextureOffset(4, 12).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.propeller, this.cube_r1, this.blades, this.rarm, this.larm, this.rleg, this.lleg);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    public void setupAnim(NotorEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float landProgress = entity.getGroundProgress(partialTick);
        float flyProgress = 1.0F - landProgress;
        float bodyYaw = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTick;
        float bodyIdleBob = Math.abs(ACMath.walkValue(ageInTicks, landProgress, 0.1F, 0.0F, 1.0F, false));
        float bodyFlyBob = ACMath.walkValue(ageInTicks, flyProgress, 0.2F, 1.0F, 1.2F, false) - 1.2F * flyProgress;
        this.body.rotationPointY += bodyIdleBob + bodyFlyBob;
        this.larm.rotationPointY -= bodyIdleBob * 0.8F;
        this.rarm.rotationPointY -= bodyIdleBob * 0.8F;
        this.lleg.rotationPointY -= bodyIdleBob * 0.8F;
        this.rleg.rotationPointY -= bodyIdleBob * 0.8F;
        this.flap(this.propeller, 0.2F, 0.1F, false, 1.0F, 0.0F, ageInTicks, landProgress);
        this.walk(this.larm, 0.2F, 0.3F, false, 0.0F, 0.2F, ageInTicks, landProgress);
        this.walk(this.rarm, 0.2F, 0.3F, false, 0.0F, 0.2F, ageInTicks, landProgress);
        this.walk(this.lleg, 0.2F, 0.3F, true, 0.0F, 0.2F, ageInTicks, landProgress);
        this.walk(this.rleg, 0.2F, 0.3F, true, 0.0F, 0.2F, ageInTicks, landProgress);
        this.walk(this.larm, 0.2F, 0.3F, false, 1.0F, 0.2F, ageInTicks, flyProgress);
        this.walk(this.rarm, 0.2F, 0.3F, false, 1.0F, 0.2F, ageInTicks, flyProgress);
        this.walk(this.lleg, 0.2F, 0.3F, false, 2.0F, 0.2F, ageInTicks, flyProgress);
        this.walk(this.rleg, 0.2F, 0.3F, false, 2.0F, 0.2F, ageInTicks, flyProgress);
        this.propeller.rotateAngleY = this.propeller.rotateAngleY + (float) Math.toRadians((double) (entity.getPropellerAngle(partialTick) - bodyYaw * flyProgress));
        float flyForwards = limbSwingAmount * flyProgress;
        this.walk(this.body, 0.2F, 0.1F, false, 2.0F, 0.2F, limbSwing, flyForwards);
        this.flap(this.body, 0.4F, 0.05F, false, -1.0F, 0.0F, ageInTicks, flyProgress);
        this.walk(this.propeller, 0.2F, 0.1F, true, 2.0F, 0.2F, limbSwing, flyForwards);
    }

    public Vec3 getChainPosition(Vec3 offsetIn) {
        PoseStack armStack = new PoseStack();
        armStack.pushPose();
        this.body.translateAndRotate(armStack);
        Vector4f armOffsetVec = new Vector4f((float) offsetIn.x, (float) offsetIn.y, (float) offsetIn.z, 1.0F);
        armOffsetVec.mul(armStack.last().pose());
        Vec3 vec3 = new Vec3((double) armOffsetVec.x(), (double) (-armOffsetVec.y()), (double) armOffsetVec.z());
        armStack.popPose();
        return vec3.add(0.0, 1.5, 0.0);
    }
}