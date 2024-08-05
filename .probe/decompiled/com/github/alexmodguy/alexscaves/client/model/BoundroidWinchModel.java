package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidWinchEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class BoundroidWinchModel extends AdvancedEntityModel<BoundroidWinchEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox coil;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox rarm;

    public BoundroidWinchModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 18.0F, 0.0F);
        this.body.setTextureOffset(20, 41).addBox(4.0F, -1.5F, -3.0F, 2.0F, 7.0F, 6.0F, 0.0F, false);
        this.body.setTextureOffset(36, 41).addBox(-6.0F, -1.5F, -3.0F, 2.0F, 7.0F, 6.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-6.0F, -10.5F, -6.0F, 12.0F, 9.0F, 12.0F, 0.0F, false);
        this.coil = new AdvancedModelBox(this);
        this.coil.setRotationPoint(0.0F, 2.5F, 0.0F);
        this.body.addChild(this.coil);
        this.coil.setTextureOffset(0, 21).addBox(-4.5F, -3.5F, -3.5F, 9.0F, 7.0F, 7.0F, 0.0F, false);
        this.coil.setTextureOffset(26, 29).addBox(-4.0F, -3.0F, -3.0F, 8.0F, 6.0F, 6.0F, 0.0F, false);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(5.0F, -7.5F, 5.0F);
        this.body.addChild(this.lleg);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(1.5F, -4.0F, 1.5F);
        this.lleg.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, -1.5708F, 0.0F);
        this.cube_r1.setTextureOffset(0, 35).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 10.0F, 5.0F, 0.0F, false);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(5.0F, -7.5F, -5.0F);
        this.body.addChild(this.larm);
        this.larm.setTextureOffset(0, 35).addBox(-1.0F, -9.0F, -4.0F, 5.0F, 10.0F, 5.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-5.0F, -7.5F, 5.0F);
        this.body.addChild(this.rleg);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(-1.5F, -4.0F, 1.5F);
        this.rleg.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.0F, 1.5708F, 0.0F);
        this.cube_r2.setTextureOffset(0, 35).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 10.0F, 5.0F, 0.0F, true);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-5.0F, -7.5F, -5.0F);
        this.body.addChild(this.rarm);
        this.rarm.setTextureOffset(0, 35).addBox(-4.0F, -9.0F, -4.0F, 5.0F, 10.0F, 5.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    public Vec3 getChainPosition(Vec3 offsetIn) {
        PoseStack armStack = new PoseStack();
        armStack.pushPose();
        this.body.translateAndRotate(armStack);
        this.coil.translateAndRotate(armStack);
        Vector4f armOffsetVec = new Vector4f((float) offsetIn.x, (float) offsetIn.y, (float) offsetIn.z, 1.0F);
        armOffsetVec.mul(armStack.last().pose());
        Vec3 vec3 = new Vec3((double) armOffsetVec.x(), (double) (-armOffsetVec.y()), (double) armOffsetVec.z());
        armStack.popPose();
        return vec3.add(0.0, 1.5, 0.0);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.coil, this.rarm, this.rleg, this.larm, this.lleg, this.cube_r1, this.cube_r2);
    }

    public void setupAnim(BoundroidWinchEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float chainLength = entity.getChainLength(partialTick);
        float bodyYaw = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTick;
        float walkSpeed = 0.8F;
        float walkDegree = 1.3F;
        float moveSideways = (float) (entity.m_20185_() - entity.f_19854_) * 3.0F;
        float moveForwards = (float) (entity.m_20189_() - entity.f_19856_) * 3.0F;
        float onGroundAmount = 1.0F - entity.getLatchProgress(partialTick);
        this.coil.rotateAngleX = (float) Math.toRadians((double) (chainLength * 260.0F));
        this.body.rotateAngleX = (float) ((double) this.body.rotateAngleX + (double) onGroundAmount * Math.PI);
        this.body.rotationPointY -= onGroundAmount * 11.0F;
        this.walk(this.larm, walkSpeed, walkDegree, true, 1.0F, 0.0F, ageInTicks, moveForwards);
        this.walk(this.rarm, walkSpeed, walkDegree, false, 1.0F, 0.0F, ageInTicks, moveForwards);
        this.walk(this.lleg, walkSpeed, walkDegree, true, -1.0F, 0.0F, ageInTicks, moveForwards);
        this.walk(this.rleg, walkSpeed, walkDegree, false, -1.0F, 0.0F, ageInTicks, moveForwards);
        this.flap(this.larm, walkSpeed, walkDegree, true, 1.0F, 0.0F, ageInTicks, moveSideways);
        this.flap(this.rarm, walkSpeed, walkDegree, false, 1.0F, 0.0F, ageInTicks, moveSideways);
        this.flap(this.lleg, walkSpeed, walkDegree, true, -1.0F, 0.0F, ageInTicks, moveSideways);
        this.flap(this.rleg, walkSpeed, walkDegree, false, -1.0F, 0.0F, ageInTicks, moveSideways);
    }
}