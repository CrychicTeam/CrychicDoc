package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityMungus;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ModelMungus extends AdvancedEntityModel<EntityMungus> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox hair;

    public final AdvancedModelBox eye;

    public final AdvancedModelBox leg_left;

    public final AdvancedModelBox leg_right;

    public final AdvancedModelBox nose;

    public final AdvancedModelBox sack;

    public ModelMungus(float f) {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -7.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-6.0F, -16.0F, -4.0F, 12.0F, 16.0F, 8.0F, f, false);
        this.hair = new AdvancedModelBox(this, "hair");
        this.hair.setPos(0.0F, -16.0F, 0.0F);
        this.body.addChild(this.hair);
        this.hair.setTextureOffset(33, 0).addBox(-5.0F, -5.0F, 0.0F, 10.0F, 5.0F, 0.0F, f, false);
        this.eye = new AdvancedModelBox(this, "eye");
        this.eye.setPos(0.0F, -11.0F, -4.1F);
        this.body.addChild(this.eye);
        this.eye.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 1.0F, f, false);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.leg_left.setPos(3.0F, 0.0F, 0.0F);
        this.body.addChild(this.leg_left);
        this.leg_left.setTextureOffset(0, 39).addBox(-2.0F, 0.0F, -3.0F, 5.0F, 7.0F, 6.0F, f, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setPos(-3.0F, 0.0F, 0.0F);
        this.body.addChild(this.leg_right);
        this.leg_right.setTextureOffset(0, 25).addBox(-3.0F, 0.0F, -3.0F, 5.0F, 7.0F, 6.0F, f, false);
        this.nose = new AdvancedModelBox(this, "nose");
        this.nose.setPos(0.0F, -9.0F, -4.0F);
        this.body.addChild(this.nose);
        this.nose.setTextureOffset(35, 43).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 5.0F, 2.0F, f, false);
        this.sack = new AdvancedModelBox(this, "sack");
        this.sack.setPos(0.0F, -7.0F, 4.0F);
        this.body.addChild(this.sack);
        this.sack.setTextureOffset(23, 25).addBox(-4.0F, -8.0F, 0.0F, 8.0F, 10.0F, 3.0F, f, false);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityMungus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 0.7F;
        float walkDegree = 0.6F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float swell = Math.min(entity.prevSwellProgress + (entity.swellProgress - entity.prevSwellProgress) * Minecraft.getInstance().getFrameTime(), 10.0F);
        float glowyBob = swell * 0.22F + 0.95F + (Mth.cos(ageInTicks * (0.1F + swell * 0.2F)) + 1.0F) * (0.05F + swell * 0.02F);
        BlockPos targetPos = entity.getBeamTarget();
        if (targetPos == null) {
            Entity look = Minecraft.getInstance().getCameraEntity();
            if (look != null) {
                Vec3 vector3d = look.getEyePosition(0.0F);
                Vec3 vector3d1 = entity.m_20299_(0.0F);
                double d0 = vector3d.y - vector3d1.y;
                if (d0 > 0.0) {
                    this.eye.rotationPointY = -11.0F;
                } else {
                    this.eye.rotationPointY = -10.0F;
                }
                Vec3 vector3d2 = entity.m_20252_(0.0F);
                vector3d2 = new Vec3(vector3d2.x, 0.0, vector3d2.z);
                Vec3 vector3d3 = new Vec3(vector3d1.x - vector3d.x, 0.0, vector3d1.z - vector3d.z).normalize().yRot((float) (Math.PI / 2));
                double d1 = vector3d2.dot(vector3d3);
                this.eye.rotationPointX = this.eye.rotationPointX + Mth.sqrt((float) Math.abs(d1)) * 2.0F * (float) Math.signum(d1);
            }
        } else {
            Vec3 vector3d = Vec3.atCenterOf(targetPos);
            Vec3 vector3d1 = entity.m_20299_(0.0F);
            double d0 = vector3d.y - vector3d1.y;
            if (d0 > 0.0) {
                this.eye.rotationPointY = -11.0F;
            } else {
                this.eye.rotationPointY = -10.0F;
            }
            Vec3 vector3d2 = entity.m_20252_(0.0F);
            vector3d2 = new Vec3(vector3d2.x, 0.0, vector3d2.z);
            Vec3 vector3d3 = new Vec3(vector3d1.x - vector3d.x, 0.0, vector3d1.z - vector3d.z).normalize().yRot((float) (Math.PI / 2));
            double d1 = vector3d2.dot(vector3d3);
            this.eye.rotationPointX = this.eye.rotationPointX + Mth.sqrt((float) Math.abs(d1)) * 2.0F * (float) Math.signum(d1);
        }
        this.walk(this.hair, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.nose, idleSpeed, idleDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.sack.setScale(glowyBob, glowyBob, glowyBob + swell * 0.2F);
        this.sack.rotationPointZ += swell * 0.02F;
        this.progressRotationPrev(this.hair, limbSwingAmount, Maths.rad(-23.0), 0.0F, 0.0F, 1.0F);
        this.walk(this.leg_right, walkSpeed, walkDegree * 1.1F, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.leg_right, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.walk(this.leg_left, walkSpeed, walkDegree * 1.1F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.leg_left, walkSpeed, walkDegree, false, limbSwing, limbSwingAmount);
        this.flap(this.body, walkSpeed, walkDegree * 0.4F, false, 0.5F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.nose, walkSpeed, walkDegree * 0.2F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree * 3.0F, true, limbSwing, limbSwingAmount);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.hair, this.eye, this.leg_left, this.leg_right, this.sack, this.nose);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            this.eye.setScale(1.5F, 1.5F, 1.5F);
            this.nose.setScale(1.5F, 1.5F, 1.5F);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        } else {
            this.eye.setScale(1.0F, 1.0F, 1.0F);
            this.nose.setScale(1.0F, 1.0F, 1.0F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void renderShoes() {
        this.leg_left.setScale(1.3F, 1.3F, 1.3F);
        this.leg_right.setScale(1.3F, 1.3F, 1.3F);
    }

    public void postRenderShoes() {
        this.leg_left.setScale(1.0F, 1.0F, 1.0F);
        this.leg_right.setScale(1.0F, 1.0F, 1.0F);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}