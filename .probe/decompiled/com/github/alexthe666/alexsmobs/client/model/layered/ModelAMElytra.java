package com.github.alexthe666.alexsmobs.client.model.layered;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ModelAMElytra extends HumanoidModel {

    private final ModelPart rightWing;

    private final ModelPart leftWing;

    public ModelAMElytra(ModelPart part) {
        super(part);
        this.leftWing = part.getChild("body").getChild("left_wing");
        this.rightWing = part.getChild("body").getChild("right_wing");
    }

    public static LayerDefinition createLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot().getChild("body");
        CubeDeformation cubedeformation = new CubeDeformation(1.0F);
        partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(32, 32).addBox(-10.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, cubedeformation), PartPose.offsetAndRotation(5.0F, 0.0F, 0.0F, (float) (Math.PI / 12), 0.0F, (float) (-Math.PI / 12)));
        partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(32, 32).mirror().addBox(0.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, cubedeformation), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, (float) (Math.PI / 12), 0.0F, (float) (Math.PI / 12)));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public ModelAMElytra withAnimations(LivingEntity entity) {
        if (entity != null) {
            float partialTick = Minecraft.getInstance().getFrameTime();
            float limbSwingAmount = entity.walkAnimation.speed(partialTick);
            float limbSwing = entity.walkAnimation.position() + partialTick;
            this.setupAnim(entity, limbSwing, limbSwingAmount, (float) entity.f_19797_ + partialTick, 0.0F, 0.0F);
        }
        return this;
    }

    @Override
    public void setupAnim(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = (float) (Math.PI / 12);
        float f1 = (float) (-Math.PI / 12);
        float f2 = 0.0F;
        float f3 = 0.0F;
        if (entityIn.isFallFlying()) {
            float f4 = 1.0F;
            Vec3 vector3d = entityIn.m_20184_();
            if (vector3d.y < 0.0) {
                Vec3 vector3d1 = vector3d.normalize();
                f4 = 1.0F - (float) Math.pow(-vector3d1.y, 1.5);
            }
            f = f4 * (float) (Math.PI / 9) + (1.0F - f4) * f;
            f1 = f4 * (float) (-Math.PI / 2) + (1.0F - f4) * f1;
        } else if (entityIn.m_6047_()) {
            f = (float) (Math.PI * 2.0 / 9.0);
            f1 = (float) (-Math.PI / 4);
            f2 = -1.0F;
            f3 = 0.08726646F;
        }
        this.leftWing.x = 5.0F;
        this.leftWing.y = f2;
        if (entityIn instanceof AbstractClientPlayer abstractclientplayerentity) {
            abstractclientplayerentity.elytraRotX = (float) ((double) abstractclientplayerentity.elytraRotX + (double) (f - abstractclientplayerentity.elytraRotX) * 0.1);
            abstractclientplayerentity.elytraRotY = (float) ((double) abstractclientplayerentity.elytraRotY + (double) (f3 - abstractclientplayerentity.elytraRotY) * 0.1);
            abstractclientplayerentity.elytraRotZ = (float) ((double) abstractclientplayerentity.elytraRotZ + (double) (f1 - abstractclientplayerentity.elytraRotZ) * 0.1);
            this.leftWing.xRot = abstractclientplayerentity.elytraRotX;
            this.leftWing.yRot = abstractclientplayerentity.elytraRotY;
            this.leftWing.zRot = abstractclientplayerentity.elytraRotZ;
        } else {
            this.leftWing.xRot = f;
            this.leftWing.zRot = f1;
            this.leftWing.yRot = f3;
        }
        this.rightWing.x = -this.leftWing.x;
        this.rightWing.yRot = -this.leftWing.yRot;
        this.rightWing.y = this.leftWing.y;
        this.rightWing.xRot = this.leftWing.xRot;
        this.rightWing.zRot = -this.leftWing.zRot;
    }
}