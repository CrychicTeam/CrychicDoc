package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityRattlesnake;
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

public class ModelRattlesnake extends AdvancedEntityModel<EntityRattlesnake> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox neck1;

    private final AdvancedModelBox neck2;

    private final AdvancedModelBox head;

    private final AdvancedModelBox tongue;

    private ModelAnimator animator;

    public ModelRattlesnake() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, 24.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-2.0F, -3.0F, -4.0F, 4.0F, 3.0F, 7.0F, 0.0F, false);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setPos(0.0F, -1.75F, 2.95F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(0, 11).addBox(-1.5F, -1.25F, 0.05F, 3.0F, 3.0F, 7.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setPos(0.0F, 0.45F, 7.05F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(15, 16).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.neck1 = new AdvancedModelBox(this, "neck1");
        this.neck1.setPos(0.0F, -1.5F, -4.0F);
        this.body.addChild(this.neck1);
        this.neck1.setTextureOffset(18, 6).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F, 0.0F, false);
        this.neck2 = new AdvancedModelBox(this, "neck2");
        this.neck2.setPos(0.0F, 0.0F, -4.9F);
        this.neck1.addChild(this.neck2);
        this.neck2.setTextureOffset(12, 25).addBox(-1.0F, -1.5F, -5.1F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, 0.0F, -5.0F);
        this.neck2.addChild(this.head);
        this.head.setTextureOffset(0, 22).addBox(-2.0F, -1.0F, -3.8F, 4.0F, 2.0F, 4.0F, 0.0F, false);
        this.tongue = new AdvancedModelBox(this, "tongue");
        this.tongue.setPos(0.0F, 0.0F, -3.8F);
        this.head.addChild(this.tongue);
        this.tongue.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityRattlesnake.ANIMATION_BITE);
        this.animator.startKeyframe(7);
        this.animator.move(this.body, 0.0F, 0.0F, 2.0F);
        this.animator.rotate(this.neck1, Maths.rad(-45.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck2, Maths.rad(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, 0.0F, Maths.rad(15.0), 0.0F);
        this.animator.rotate(this.tail2, 0.0F, Maths.rad(-15.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, -2.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.75F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.35F, 0.35F, 0.35F);
            matrixStackIn.translate(0.0, 2.75, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setupAnim(EntityRattlesnake entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 1.0F;
        float walkDegree = 0.4F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[] { this.neck1, this.neck2, this.body, this.tail1, this.tail2 };
        float curlProgress = entity.prevCurlProgress + (entity.curlProgress - entity.prevCurlProgress) * partialTick;
        this.progressPositionPrev(this.body, curlProgress, 0.0F, 0.0F, 3.0F, 5.0F);
        this.progressRotationPrev(this.body, curlProgress, 0.0F, Maths.rad(-90.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail1, curlProgress, Maths.rad(-10.0), Maths.rad(-70.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.neck1, curlProgress, Maths.rad(-20.0), Maths.rad(60.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.neck2, curlProgress, Maths.rad(-20.0), Maths.rad(60.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.head, curlProgress, Maths.rad(20.0), Maths.rad(-30.0), Maths.rad(10.0), 5.0F);
        if (entity.randomToungeTick > 0) {
            this.tongue.showModel = true;
        } else {
            this.tongue.showModel = false;
        }
        this.walk(this.tongue, 1.0F, 0.5F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        if (entity.isRattling()) {
            this.progressRotationPrev(this.tail2, curlProgress, Maths.rad(70.0), Maths.rad(-60.0), 0.0F, 5.0F);
            this.walk(this.tail2, 18.0F, 0.1F, false, 1.0F, 0.2F, ageInTicks, 1.0F);
            this.swing(this.tail2, 18.0F, 0.1F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        } else {
            this.progressRotationPrev(this.tail2, curlProgress, Maths.rad(10.0), Maths.rad(-90.0), 0.0F, 5.0F);
        }
        this.faceTarget(netHeadYaw, headPitch, 2.0F, new AdvancedModelBox[] { this.neck2, this.head });
        this.chainSwing(bodyParts, walkSpeed, walkDegree, -5.0, limbSwing, limbSwingAmount);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.tail1, this.tail2, this.neck1, this.neck2, this.head, this.tongue);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}