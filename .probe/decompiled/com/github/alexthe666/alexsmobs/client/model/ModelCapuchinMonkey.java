package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCapuchinMonkey;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

public class ModelCapuchinMonkey extends AdvancedEntityModel<EntityCapuchinMonkey> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox body;

    public final AdvancedModelBox arm_left;

    public final AdvancedModelBox arm_right;

    public final AdvancedModelBox leg_left;

    public final AdvancedModelBox leg_right;

    public final AdvancedModelBox tail1;

    public final AdvancedModelBox tail2;

    public final AdvancedModelBox tail2_r1;

    public final AdvancedModelBox head;

    public final AdvancedModelBox hair;

    public final AdvancedModelBox snout;

    public ModelAnimator animator;

    public ModelCapuchinMonkey() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -9.9F, 3.9F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-3.0F, -2.1F, -9.9F, 6.0F, 5.0F, 11.0F, 0.0F, false);
        this.arm_left = new AdvancedModelBox(this, "arm_left");
        this.arm_left.setPos(2.1F, 2.4F, -8.8F);
        this.body.addChild(this.arm_left);
        this.arm_left.setTextureOffset(28, 17).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.arm_right = new AdvancedModelBox(this, "arm_right");
        this.arm_right.setPos(-2.1F, 2.4F, -8.8F);
        this.body.addChild(this.arm_right);
        this.arm_right.setTextureOffset(28, 17).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, true);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.leg_left.setPos(2.0F, 2.9F, 0.1F);
        this.body.addChild(this.leg_left);
        this.leg_left.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setPos(-2.0F, 2.9F, 0.1F);
        this.body.addChild(this.leg_right);
        this.leg_right.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setPos(0.0F, -1.1F, 0.5F);
        this.body.addChild(this.tail1);
        this.setRotationAngle(this.tail1, 0.6981F, 0.0F, 0.0F);
        this.tail1.setTextureOffset(15, 22).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setPos(0.0F, -0.2F, 7.7F);
        this.tail1.addChild(this.tail2);
        this.setRotationAngle(this.tail2, 0.6981F, 0.0F, 0.0F);
        this.tail2_r1 = new AdvancedModelBox(this, "tail2_r1");
        this.tail2_r1.setPos(0.0F, -0.1875F, -0.0791F);
        this.tail2.addChild(this.tail2_r1);
        this.setRotationAngle(this.tail2_r1, -1.4399F, 0.0F, 0.0F);
        this.tail2_r1.setTextureOffset(0, 17).addBox(-1.0F, -0.8125F, -0.2209F, 2.0F, 3.0F, 9.0F, -0.1F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -1.1F, -9.9F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(24, 0).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 4.0F, 3.0F, 0.0F, false);
        this.hair = new AdvancedModelBox(this, "hair");
        this.hair.setPos(0.0F, -1.0F, -2.0F);
        this.head.addChild(this.hair);
        this.hair.setTextureOffset(6, 38).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, 0.0F, false);
        this.snout = new AdvancedModelBox(this, "snout");
        this.snout.setPos(0.0F, 1.0F, -3.0F);
        this.head.addChild(this.snout);
        this.snout.setTextureOffset(0, 17).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityCapuchinMonkey.ANIMATION_THROW);
        this.animator.startKeyframe(3);
        this.animator.move(this.body, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-70.0), 0.0F, Maths.rad(25.0));
        this.animator.rotate(this.arm_left, Maths.rad(-70.0), 0.0F, Maths.rad(-25.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.move(this.body, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-45.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(45.0), 0.0F, 0.0F);
        this.animator.move(this.arm_right, -1.0F, -2.0F, 1.0F);
        this.animator.move(this.arm_left, 1.0F, -2.0F, 1.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-160.0), 0.0F, Maths.rad(5.0));
        this.animator.rotate(this.arm_left, Maths.rad(-160.0), 0.0F, Maths.rad(-5.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.move(this.body, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.move(this.arm_right, -1.0F, -2.0F, -1.0F);
        this.animator.move(this.arm_left, 1.0F, -2.0F, -1.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-50.0), 0.0F, Maths.rad(5.0));
        this.animator.rotate(this.arm_left, Maths.rad(-50.0), 0.0F, Maths.rad(-5.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(4);
        this.animator.setAnimation(EntityCapuchinMonkey.ANIMATION_SCRATCH);
        this.animator.startKeyframe(3);
        this.animator.move(this.body, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-50.0), 0.0F, Maths.rad(15.0));
        this.animator.rotate(this.arm_left, Maths.rad(10.0), 0.0F, Maths.rad(-15.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.move(this.body, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(10.0), 0.0F, Maths.rad(15.0));
        this.animator.rotate(this.arm_left, Maths.rad(-50.0), 0.0F, Maths.rad(-15.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.move(this.body, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(-50.0), 0.0F, Maths.rad(15.0));
        this.animator.rotate(this.arm_left, Maths.rad(10.0), 0.0F, Maths.rad(-15.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(3);
        this.animator.move(this.body, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.body, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.arm_right, Maths.rad(10.0), 0.0F, Maths.rad(15.0));
        this.animator.rotate(this.arm_left, Maths.rad(-50.0), 0.0F, Maths.rad(-15.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityCapuchinMonkey.ANIMATION_HEADTILT);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(25.0));
        this.animator.move(this.head, 0.0F, 1.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.startKeyframe(5);
        this.animator.move(this.head, 0.0F, 1.0F, 0.0F);
        this.animator.rotate(this.head, 0.0F, 0.0F, Maths.rad(-25.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.resetKeyframe(5);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.leg_left, this.leg_right, this.tail1, this.tail2, this.tail2_r1, this.arm_left, this.arm_right, this.head, this.snout, this.hair, new AdvancedModelBox[0]);
    }

    public void setupAnim(EntityCapuchinMonkey entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float idleSpeed = 0.2F;
        float idleDegree = 0.4F;
        float walkSpeed = 0.8F;
        float walkDegree = 0.7F;
        float stillProgress = 5.0F * (1.0F - limbSwingAmount);
        float partialTick = Minecraft.getInstance().getFrameTime();
        float sitProgress = entity.m_20159_() ? 0.0F : entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        float rideProgress = entity.m_20159_() && entity.m_20202_() instanceof LivingEntity && entity.m_21830_((LivingEntity) entity.m_20202_()) ? 10.0F : 0.0F;
        this.progressPositionPrev(this.body, rideProgress, 3.0F, 12.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.body, rideProgress, 0.0F, Maths.rad(90.0), 0.0F, 10.0F);
        this.progressRotationPrev(this.head, rideProgress, 0.0F, Maths.rad(-90.0), 0.0F, 10.0F);
        this.progressRotationPrev(this.leg_right, rideProgress, 0.0F, 0.0F, Maths.rad(-15.0), 10.0F);
        this.progressRotationPrev(this.arm_right, rideProgress, 0.0F, 0.0F, Maths.rad(-15.0), 10.0F);
        this.progressRotationPrev(this.tail1, stillProgress, Maths.rad(-55.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail2, stillProgress, Maths.rad(15.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, 6.0F, 0.0F, 10.0F);
        this.progressRotationPrev(this.tail1, sitProgress, Maths.rad(-15.0), Maths.rad(15.0), Maths.rad(90.0), 10.0F);
        this.progressRotationPrev(this.arm_left, sitProgress, Maths.rad(-85.0), Maths.rad(-15.0), 0.0F, 10.0F);
        this.progressRotationPrev(this.arm_right, sitProgress, Maths.rad(-85.0), Maths.rad(15.0), 0.0F, 10.0F);
        this.progressRotationPrev(this.leg_left, sitProgress, Maths.rad(85.0), Maths.rad(-15.0), 0.0F, 10.0F);
        this.progressRotationPrev(this.leg_right, sitProgress, Maths.rad(85.0), Maths.rad(-15.0), 0.0F, 10.0F);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
        this.swing(this.tail1, idleSpeed, idleDegree * 0.2F, false, 0.3F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail2, idleSpeed, idleDegree * 0.2F, false, 0.3F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.tail1, walkSpeed, walkDegree * 0.2F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.tail2, walkSpeed, walkDegree * 0.2F, false, 1.3F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.tail2_r1, walkSpeed, walkDegree * 0.2F, false, 1.5F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.body, walkSpeed, walkDegree * 0.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed, walkDegree * 2.0F, false, limbSwing, limbSwingAmount);
        this.walk(this.arm_left, walkSpeed, walkDegree, false, 1.4F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.arm_right, walkSpeed, walkDegree, false, 1.4F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leg_left, walkSpeed, walkDegree, false, -2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.leg_right, walkSpeed, walkDegree, false, -2.0F, 0.0F, limbSwing, limbSwingAmount);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.75F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}