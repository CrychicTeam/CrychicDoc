package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityAnteater;
import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelLeafcutterAnt extends AdvancedEntityModel<EntityLeafcutterAnt> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox legfront_left;

    private final AdvancedModelBox legfront_right;

    private final AdvancedModelBox legmid_left;

    private final AdvancedModelBox legmid_right;

    private final AdvancedModelBox legback_left;

    private final AdvancedModelBox legback_right;

    private final AdvancedModelBox abdomen;

    private final AdvancedModelBox head;

    private final AdvancedModelBox leaf;

    private final AdvancedModelBox leaf_r1;

    private final AdvancedModelBox antenna_left;

    private final AdvancedModelBox antenna_right;

    private final AdvancedModelBox fangs;

    private ModelAnimator animator;

    public ModelLeafcutterAnt() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -4.0F, 0.125F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(14, 5).addBox(-1.0F, -0.4F, -2.125F, 2.0F, 2.0F, 5.0F, 0.0F, false);
        this.legfront_left = new AdvancedModelBox(this, "legfront_left");
        this.legfront_left.setPos(1.0F, 1.0F, -1.125F);
        this.body.addChild(this.legfront_left);
        this.setRotationAngle(this.legfront_left, 0.0F, 0.2618F, 0.0F);
        this.legfront_left.setTextureOffset(0, 19).addBox(0.0F, -1.0F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        this.legfront_right = new AdvancedModelBox(this, "legfront_right");
        this.legfront_right.setPos(-1.0F, 1.0F, -1.125F);
        this.body.addChild(this.legfront_right);
        this.setRotationAngle(this.legfront_right, 0.0F, -0.2618F, 0.0F);
        this.legfront_right.setTextureOffset(0, 19).addBox(-4.0F, -1.0F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        this.legmid_left = new AdvancedModelBox(this, "legmid_left");
        this.legmid_left.setPos(1.0F, 1.0F, 0.875F);
        this.body.addChild(this.legmid_left);
        this.legmid_left.setTextureOffset(0, 19).addBox(0.0F, -1.0F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        this.legmid_right = new AdvancedModelBox(this, "legmid_right");
        this.legmid_right.setPos(-1.0F, 1.0F, 0.875F);
        this.body.addChild(this.legmid_right);
        this.legmid_right.setTextureOffset(0, 19).addBox(-4.0F, -1.0F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        this.legback_left = new AdvancedModelBox(this, "legback_left");
        this.legback_left.setPos(1.0F, 1.0F, 2.875F);
        this.body.addChild(this.legback_left);
        this.setRotationAngle(this.legback_left, 0.0F, -0.3491F, 0.0F);
        this.legback_left.setTextureOffset(0, 19).addBox(0.0F, -1.0F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        this.legback_right = new AdvancedModelBox(this, "legback_right");
        this.legback_right.setPos(-1.0F, 1.0F, 2.875F);
        this.body.addChild(this.legback_right);
        this.setRotationAngle(this.legback_right, 0.0F, 0.3491F, 0.0F);
        this.legback_right.setTextureOffset(0, 19).addBox(-4.0F, -1.0F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        this.abdomen = new AdvancedModelBox(this, "abdomen");
        this.abdomen.setPos(0.0F, 0.0F, 2.875F);
        this.body.addChild(this.abdomen);
        this.abdomen.setTextureOffset(0, 0).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 4.0F, 5.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -0.5F, -2.125F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 10).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 3.0F, 5.0F, 0.0F, false);
        this.leaf = new AdvancedModelBox(this, "leaf");
        this.leaf.setPos(0.0F, 1.0F, -5.0F);
        this.head.addChild(this.leaf);
        this.setRotationAngle(this.leaf, 0.3491F, 0.0F, 0.0F);
        this.leaf_r1 = new AdvancedModelBox(this, "leaf_r1");
        this.leaf_r1.setPos(0.0F, 0.0F, 2.0F);
        this.leaf.addChild(this.leaf_r1);
        this.setRotationAngle(this.leaf_r1, 0.5672F, 0.0F, 0.0F);
        this.leaf_r1.setTextureOffset(6, 5).addBox(0.0F, -14.0F, -6.0F, 0.0F, 14.0F, 13.0F, 0.0F, false);
        this.antenna_left = new AdvancedModelBox(this, "antenna_left");
        this.antenna_left.setPos(0.0F, -2.0F, -4.0F);
        this.head.addChild(this.antenna_left);
        this.setRotationAngle(this.antenna_left, -0.3927F, -0.2618F, 0.1745F);
        this.antenna_left.setTextureOffset(12, 13).addBox(0.0F, 0.0F, -6.0F, 5.0F, 0.0F, 6.0F, 0.0F, false);
        this.antenna_right = new AdvancedModelBox(this, "antenna_right");
        this.antenna_right.setPos(0.0F, -2.0F, -4.0F);
        this.head.addChild(this.antenna_right);
        this.setRotationAngle(this.antenna_right, -0.3927F, 0.2618F, -0.1745F);
        this.antenna_right.setTextureOffset(12, 13).addBox(-5.0F, 0.0F, -6.0F, 5.0F, 0.0F, 6.0F, 0.0F, true);
        this.fangs = new AdvancedModelBox(this, "fangs");
        this.fangs.setPos(0.0F, 1.0F, -5.0F);
        this.head.addChild(this.fangs);
        this.fangs.setTextureOffset(14, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
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

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.legback_left, this.leaf, this.leaf_r1, this.legback_right, this.root, this.body, this.legfront_left, this.legfront_right, this.legmid_left, this.legmid_right, this.abdomen, this.head, new AdvancedModelBox[] { this.antenna_left, this.antenna_right, this.fangs });
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityLeafcutterAnt.ANIMATION_BITE);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, -5.0F);
        this.animator.rotate(this.head, Maths.rad(-25.0), 0.0F, 0.0F);
        this.animator.rotate(this.abdomen, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.antenna_left, Maths.rad(-25.0), Maths.rad(-25.0), 0.0F);
        this.animator.rotate(this.antenna_right, Maths.rad(-25.0), Maths.rad(25.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 2.0F);
        this.animator.rotate(this.head, Maths.rad(25.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
    }

    public void setupAnim(EntityLeafcutterAnt entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float idleSpeed = 0.25F;
        float idleDegree = 0.25F;
        float walkSpeed = 1.0F;
        float walkDegree = 1.0F;
        this.swing(this.antenna_left, idleSpeed, idleDegree, true, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.antenna_right, idleSpeed, idleDegree, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.antenna_left, idleSpeed, idleDegree * 0.25F, false, -1.0F, -0.05F, ageInTicks, 1.0F);
        this.walk(this.antenna_right, idleSpeed, idleDegree * 0.25F, false, -1.0F, -0.05F, ageInTicks, 1.0F);
        this.swing(this.legback_right, walkSpeed, walkDegree * 1.2F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
        this.flap(this.legback_right, walkSpeed, walkDegree * 0.8F, false, -1.5F, 0.4F, limbSwing, limbSwingAmount);
        this.swing(this.legfront_right, walkSpeed, walkDegree, false, 0.0F, -0.3F, limbSwing, limbSwingAmount);
        this.flap(this.legfront_right, walkSpeed, walkDegree * 0.8F, false, -1.5F, 0.4F, limbSwing, limbSwingAmount);
        this.swing(this.legmid_left, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.legmid_left, walkSpeed, walkDegree * 0.8F, false, -1.5F, -0.4F, limbSwing, limbSwingAmount);
        this.bob(this.body, walkSpeed * 2.0F, walkDegree * -0.6F, false, limbSwing, limbSwingAmount);
        float offsetleft = 2.0F;
        this.swing(this.legback_left, walkSpeed, -walkDegree * 1.2F, false, offsetleft, -0.2F, limbSwing, limbSwingAmount);
        this.flap(this.legback_left, walkSpeed, walkDegree * 0.8F, false, offsetleft - 1.5F, -0.4F, limbSwing, limbSwingAmount);
        this.swing(this.legfront_left, walkSpeed, -walkDegree, false, offsetleft, 0.3F, limbSwing, limbSwingAmount);
        this.flap(this.legfront_left, walkSpeed, walkDegree * 0.8F, false, offsetleft + 1.5F, -0.4F, limbSwing, limbSwingAmount);
        this.swing(this.legmid_right, walkSpeed, -walkDegree, false, offsetleft, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.legmid_right, walkSpeed, walkDegree * 0.8F, false, offsetleft - 1.5F, 0.4F, limbSwing, limbSwingAmount);
        this.swing(this.abdomen, walkSpeed, walkDegree * 0.2F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.faceTarget(netHeadYaw, headPitch, 1.2F, new AdvancedModelBox[] { this.head });
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    public void animateAnteater(EntityAnteater anteater, float partialTicks) {
        this.resetToDefaultPose();
        float ageInTicks = (float) anteater.f_19797_ + partialTicks;
        float struggleSpeed = 0.5F;
        float struggleDegree = 1.0F;
        this.swing(this.root, struggleSpeed, struggleDegree * 0.8F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
    }
}