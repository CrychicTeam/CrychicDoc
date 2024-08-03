package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityOrca;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ModelOrca extends AdvancedEntityModel<EntityOrca> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox fintop;

    private final AdvancedModelBox fin_left;

    private final AdvancedModelBox fin_right;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox tailend;

    private final AdvancedModelBox head;

    private final AdvancedModelBox jaw;

    private final ModelAnimator animator;

    public ModelOrca() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -1.3333F, -0.0833F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-9.0F, -14.6667F, -16.9167F, 18.0F, 16.0F, 33.0F, 0.0F, false);
        this.fintop = new AdvancedModelBox(this, "fintop");
        this.fintop.setPos(0.0F, -14.6667F, -2.4167F);
        this.body.addChild(this.fintop);
        this.setRotationAngle(this.fintop, -0.2182F, 0.0F, 0.0F);
        this.fintop.setTextureOffset(0, 0).addBox(-1.0F, -16.0F, -1.5F, 2.0F, 18.0F, 8.0F, 0.0F, false);
        this.fin_left = new AdvancedModelBox(this, "fin_left");
        this.fin_left.setPos(8.5F, -0.1667F, -8.9167F);
        this.body.addChild(this.fin_left);
        this.setRotationAngle(this.fin_left, -0.6109F, 1.2217F, 0.0F);
        this.fin_left.setTextureOffset(0, 92).addBox(-7.5F, -1.5F, -3.0F, 12.0F, 2.0F, 17.0F, 0.0F, false);
        this.fin_right = new AdvancedModelBox(this, "fin_right");
        this.fin_right.setPos(-8.5F, -0.1667F, -8.9167F);
        this.body.addChild(this.fin_right);
        this.setRotationAngle(this.fin_right, -0.6109F, -1.2217F, 0.0F);
        this.fin_right.setTextureOffset(0, 92).addBox(-4.5F, -1.5F, -3.0F, 12.0F, 2.0F, 17.0F, 0.0F, true);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setPos(0.0F, -5.9167F, 15.5833F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(70, 0).addBox(-7.0F, -6.75F, 0.5F, 14.0F, 13.0F, 18.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setPos(0.0F, 0.25F, 16.5F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(43, 97).addBox(-5.0F, -4.0F, 2.0F, 10.0F, 9.0F, 16.0F, 0.0F, false);
        this.tailend = new AdvancedModelBox(this, "tailend");
        this.tailend.setPos(0.0F, 0.5F, 16.5F);
        this.tail2.addChild(this.tailend);
        this.tailend.setTextureOffset(0, 50).addBox(-16.0F, -1.0F, -2.5F, 32.0F, 2.0F, 13.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -5.6667F, -16.9167F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(71, 71).addBox(-8.0F, -8.0F, -17.0F, 16.0F, 8.0F, 17.0F, 0.0F, false);
        this.head.setTextureOffset(96, 97).addBox(-7.0F, 0.0F, -15.0F, 14.0F, 1.0F, 15.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this, "jaw");
        this.jaw.setPos(0.0F, 1.0F, 0.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(73, 50).addBox(-7.0F, -2.0F, -15.0F, 14.0F, 1.0F, 18.0F, 0.0F, false);
        this.jaw.setTextureOffset(0, 66).addBox(-8.0F, -1.0F, -16.0F, 16.0F, 6.0F, 19.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    private static double horizontalMag(Vec3 vec) {
        return vec.x * vec.x + vec.z * vec.z;
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityOrca.ANIMATION_BITE);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, -5.0F);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(60.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
        this.animator.setAnimation(EntityOrca.ANIMATION_TAILSWING);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, -6.0F, 15.0F);
        this.animator.rotate(this.body, Maths.rad(-140.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, Maths.rad(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail2, Maths.rad(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.tailend, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(3);
        this.animator.resetKeyframe(12);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.35F, 0.35F, 0.35F);
            matrixStackIn.translate(0.0, 2.75, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setupAnim(EntityOrca entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float swimSpeed = 0.2F;
        float swimDegree = 0.4F;
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.tail1, this.tail2, this.tailend };
        this.walk(this.body, swimSpeed, swimDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, swimSpeed, swimDegree * 5.0F, false, limbSwing, limbSwingAmount);
        this.chainWave(tailBoxes, swimSpeed, swimDegree, 0.2F, limbSwing, limbSwingAmount);
        this.swing(this.fin_left, swimSpeed, swimDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.fin_right, swimSpeed, swimDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.fin_left, swimSpeed, swimDegree * 1.4F, true, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.fin_right, swimSpeed, swimDegree * 1.4F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.body.rotateAngleX += headPitch * (float) (Math.PI / 180.0);
        this.body.rotateAngleY += netHeadYaw * (float) (Math.PI / 180.0);
        if (horizontalMag(entityIn.m_20184_()) > 1.0E-7) {
            this.body.rotateAngleX = this.body.rotateAngleX + -0.05F + -0.05F * Mth.cos(ageInTicks * 0.3F);
            this.tail1.rotateAngleX = this.tail1.rotateAngleX + -0.1F * Mth.cos(ageInTicks * 0.3F);
            this.tailend.rotateAngleX = this.tailend.rotateAngleX + -0.2F * Mth.cos(ageInTicks * 0.3F);
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.fintop, this.head, this.fin_left, this.fin_right, this.jaw, this.tail1, this.tail2, this.tailend);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}