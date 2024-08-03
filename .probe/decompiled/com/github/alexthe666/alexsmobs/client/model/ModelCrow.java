package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelCrow extends AdvancedEntityModel<EntityCrow> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox leg_left;

    public final AdvancedModelBox leg_right;

    public final AdvancedModelBox wing_left;

    public final AdvancedModelBox wing_right;

    public final AdvancedModelBox tail;

    public final AdvancedModelBox head;

    public final AdvancedModelBox beak;

    public ModelCrow() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -2.1F, 0.0F);
        this.root.addChild(this.body);
        this.setRotationAngle(this.body, 1.0036F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.leg_left.setPos(0.9F, 0.0F, 0.0F);
        this.body.addChild(this.leg_left);
        this.setRotationAngle(this.leg_left, 0.5672F, 0.0F, 0.0F);
        this.leg_left.setTextureOffset(0, 17).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setPos(-0.9F, 0.0F, 0.0F);
        this.body.addChild(this.leg_right);
        this.setRotationAngle(this.leg_right, 0.5672F, 0.0F, 0.0F);
        this.leg_right.setTextureOffset(0, 17).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 2.0F, 3.0F, 0.0F, true);
        this.wing_left = new AdvancedModelBox(this, "wing_left");
        this.wing_left.setPos(1.5F, -4.9F, 1.7F);
        this.body.addChild(this.wing_left);
        this.setRotationAngle(this.wing_left, 0.0436F, 0.0F, 0.0F);
        this.wing_left.setTextureOffset(13, 13).addBox(-0.5F, 0.0F, -1.7F, 1.0F, 6.0F, 3.0F, 0.0F, false);
        this.wing_right = new AdvancedModelBox(this, "wing_right");
        this.wing_right.setPos(-1.5F, -4.9F, 1.7F);
        this.body.addChild(this.wing_right);
        this.setRotationAngle(this.wing_right, 0.0436F, 0.0F, 0.0F);
        this.wing_right.setTextureOffset(13, 13).addBox(-0.5F, 0.0F, -1.7F, 1.0F, 6.0F, 3.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, -0.1F, 3.0F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, -0.1309F, 0.0F, 0.0F);
        this.tail.setTextureOffset(13, 0).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 2.0F, -0.1F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -4.8F, 1.7F);
        this.body.addChild(this.head);
        this.setRotationAngle(this.head, -0.7418F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 9).addBox(-1.5F, -2.8F, -1.5F, 3.0F, 4.0F, 3.0F, -0.2F, false);
        this.beak = new AdvancedModelBox(this, "beak");
        this.beak.setPos(0.0F, -1.4F, -1.9F);
        this.head.addChild(this.beak);
        this.beak.setTextureOffset(13, 7).addBox(-0.5F, -1.0F, -1.8F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.head, this.beak, this.leg_left, this.leg_right, this.tail, this.body, this.wing_left, this.wing_right);
    }

    public void setupAnim(EntityCrow entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float flapSpeed = 0.8F;
        float flapDegree = 0.2F;
        float walkSpeed = 1.2F;
        float walkDegree = 0.78F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTick;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        float runProgress = Math.max(0.0F, limbSwingAmount * 5.0F - flyProgress);
        float biteProgress = entity.prevAttackProgress + (entity.attackProgress - entity.prevAttackProgress) * partialTick;
        this.progressRotationPrev(this.head, biteProgress, Maths.rad(60.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, biteProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, biteProgress, Maths.rad(-25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, biteProgress, Maths.rad(-25.0), 0.0F, 0.0F, 5.0F);
        this.walk(this.head, idleSpeed * 0.7F, idleDegree, false, -1.0F, 0.05F, ageInTicks, 1.0F);
        this.walk(this.tail, idleSpeed * 0.7F, idleDegree, false, 1.0F, 0.05F, ageInTicks, 1.0F);
        this.progressRotationPrev(this.body, flyProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, flyProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, flyProgress, Maths.rad(55.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, flyProgress, Maths.rad(55.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.wing_right, flyProgress, Maths.rad(-90.0), Maths.rad(90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.wing_left, flyProgress, Maths.rad(-90.0), Maths.rad(-90.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.wing_right, flyProgress, 0.0F, 2.0F, 1.0F, 5.0F);
        this.progressPositionPrev(this.wing_left, flyProgress, 0.0F, 2.0F, 1.0F, 5.0F);
        this.progressRotationPrev(this.body, runProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, runProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, runProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, runProgress, Maths.rad(-15.0), 0.0F, 0.0F, 5.0F);
        if (flyProgress > 0.0F) {
            this.swing(this.wing_right, flapSpeed, flapDegree * 5.0F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.swing(this.wing_left, flapSpeed, flapDegree * 5.0F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.bob(this.body, flapSpeed * 0.5F, flapDegree * 4.0F, true, ageInTicks, 1.0F);
            this.walk(this.head, flapSpeed, flapDegree * 0.2F, true, 2.0F, -0.1F, ageInTicks, 1.0F);
        } else {
            this.bob(this.body, walkSpeed * 1.0F, walkDegree * 1.3F, true, limbSwing, limbSwingAmount);
            this.walk(this.leg_right, walkSpeed, walkDegree * 1.85F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.leg_left, walkSpeed, walkDegree * 1.85F, true, 0.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.head, walkSpeed, walkDegree * 0.4F, false, 2.0F, -0.01F, limbSwing, limbSwingAmount);
            this.flap(this.tail, walkSpeed, walkDegree * 0.2F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        }
        this.progressRotationPrev(this.body, sitProgress, Maths.rad(-25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, sitProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, sitProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, sitProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.head.rotateAngleY += netHeadYaw / (180.0F / (float) Math.PI);
        this.head.rotateAngleZ += headPitch / (180.0F / (float) Math.PI);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.45F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(0.9F, 0.9F, 0.9F);
        } else {
            this.head.setScale(0.9F, 0.9F, 0.9F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}