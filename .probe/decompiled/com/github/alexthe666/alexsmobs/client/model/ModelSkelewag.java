package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySkelewag;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class ModelSkelewag extends AdvancedEntityModel<EntitySkelewag> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox flag;

    private final AdvancedModelBox left_fin;

    private final AdvancedModelBox right_fin;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail_fin;

    private ModelAnimator animator;

    public ModelSkelewag() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -9.0F, -3.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(17, 11).addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 20.0F, 0.0F, false);
        this.body.setTextureOffset(50, 66).addBox(-0.5F, -16.0F, -15.0F, 1.0F, 15.0F, 2.0F, 0.0F, false);
        this.body.setTextureOffset(23, 34).addBox(-0.5F, -12.0F, -11.0F, 1.0F, 11.0F, 2.0F, 0.0F, false);
        this.body.setTextureOffset(19, 6).addBox(-0.5F, -9.0F, -7.0F, 1.0F, 8.0F, 2.0F, 0.0F, false);
        this.body.setTextureOffset(26, 6).addBox(-0.5F, -7.0F, -3.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(0.0F, -4.0F, 1.0F, 0.0F, 3.0F, 2.0F, 0.0F, false);
        this.body.setTextureOffset(45, 34).addBox(-2.0F, 1.0F, -12.0F, 4.0F, 6.0F, 10.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(-0.5F, 0.0F, -17.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(23, 54).addBox(-2.0F, -1.0F, -7.0F, 5.0F, 7.0F, 8.0F, 0.0F, false);
        this.head.setTextureOffset(50, 51).addBox(-0.5F, -1.0F, -19.0F, 2.0F, 2.0F, 12.0F, 0.0F, false);
        this.head.setTextureOffset(42, 17).addBox(0.0F, -1.0F, -31.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        this.flag = new AdvancedModelBox(this, "flag");
        this.flag.setRotationPoint(0.5F, -10.0F, -15.0F);
        this.body.addChild(this.flag);
        this.setRotationAngle(this.flag, 0.0F, 0.1309F, 0.0F);
        this.flag.setTextureOffset(0, 0).addBox(0.0F, -5.0F, 0.0F, 0.0F, 12.0F, 18.0F, 0.0F, false);
        this.left_fin = new AdvancedModelBox(this, "left_fin");
        this.left_fin.setRotationPoint(2.0F, 7.0F, -10.0F);
        this.body.addChild(this.left_fin);
        this.setRotationAngle(this.left_fin, 0.0F, 0.0F, 0.7854F);
        this.left_fin.setTextureOffset(19, 0).addBox(0.0F, 0.0F, -2.0F, 10.0F, 1.0F, 4.0F, 0.0F, false);
        this.right_fin = new AdvancedModelBox(this, "right_fin");
        this.right_fin.setRotationPoint(-2.0F, 7.0F, -10.0F);
        this.body.addChild(this.right_fin);
        this.setRotationAngle(this.right_fin, 0.0F, 0.0F, -0.7854F);
        this.right_fin.setTextureOffset(19, 0).addBox(-10.0F, 0.0F, -2.0F, 10.0F, 1.0F, 4.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(23, 34).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 17.0F, 0.0F, false);
        this.tail.setTextureOffset(9, 0).addBox(0.0F, -3.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        this.tail.setTextureOffset(57, 17).addBox(0.0F, -2.0F, 4.0F, 0.0F, 1.0F, 10.0F, 0.0F, false);
        this.tail.setTextureOffset(42, 0).addBox(-2.0F, 1.0F, 2.0F, 4.0F, 5.0F, 11.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 0).addBox(0.0F, 4.0F, 5.0F, 0.0F, 6.0F, 8.0F, 0.0F, false);
        this.tail_fin = new AdvancedModelBox(this, "tail_fin");
        this.tail_fin.setRotationPoint(0.0F, 0.0F, 15.0F);
        this.tail.addChild(this.tail_fin);
        this.tail_fin.setTextureOffset(0, 31).addBox(0.0F, -12.0F, 0.0F, 0.0F, 25.0F, 11.0F, 0.0F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntitySkelewag.ANIMATION_STAB);
        this.animator.startKeyframe(3);
        this.animator.move(this.body, 0.0F, 0.0F, 10.0F);
        this.animator.move(this.head, 1.0F, 0.0F, -1.0F);
        this.animator.rotate(this.body, Maths.rad(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), Maths.rad(-5.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.startKeyframe(2);
        this.animator.move(this.body, 0.0F, 0.0F, -10.0F);
        this.animator.rotate(this.body, 0.0F, 0.0F, Maths.rad(-10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(3);
        this.animator.setAnimation(EntitySkelewag.ANIMATION_SLASH);
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 5.0F);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-40.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), Maths.rad(-10.0), Maths.rad(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 5.0F);
        this.animator.rotate(this.body, 0.0F, Maths.rad(40.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(-10.0), Maths.rad(10.0), Maths.rad(10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 5.0F);
        this.animator.rotate(this.body, 0.0F, Maths.rad(-40.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, Maths.rad(20.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(-10.0), Maths.rad(-10.0), Maths.rad(-10.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.body, 0.0F, 0.0F, 5.0F);
        this.animator.rotate(this.body, 0.0F, Maths.rad(40.0), 0.0F);
        this.animator.rotate(this.tail, 0.0F, Maths.rad(-20.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(10.0), Maths.rad(10.0), Maths.rad(10.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(EntitySkelewag entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float idleSpeed = 0.2F;
        float idleDegree = 0.3F;
        float swimSpeed = 0.55F;
        float swimDegree = 0.5F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float landProgress = entity.prevOnLandProgress + (entity.onLandProgress - entity.prevOnLandProgress) * partialTick;
        float fallApartProgress = entity.f_20919_ > 0 ? ((float) entity.f_20919_ + partialTick) / 20.0F : 0.0F;
        this.progressRotationPrev(this.body, landProgress, 0.0F, 0.0F, Maths.rad(-90.0), 5.0F);
        this.progressPositionPrev(this.body, landProgress, 0.0F, 3.0F, 6.0F, 5.0F);
        this.progressPositionPrev(this.tail, fallApartProgress, 0.0F, 0.0F, 4.0F, 1.0F);
        this.progressPositionPrev(this.tail_fin, fallApartProgress, 0.0F, 0.0F, 4.0F, 1.0F);
        this.progressPositionPrev(this.right_fin, fallApartProgress, 0.0F, 1.0F, 2.0F, 1.0F);
        this.progressPositionPrev(this.left_fin, fallApartProgress, 0.0F, 1.0F, 2.0F, 1.0F);
        this.progressPositionPrev(this.head, fallApartProgress, 0.0F, 0.0F, -1.0F, 1.0F);
        this.progressRotationPrev(this.right_fin, fallApartProgress, 0.0F, Maths.rad(25.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.left_fin, fallApartProgress, 0.0F, Maths.rad(-25.0), 0.0F, 1.0F);
        AdvancedModelBox[] tailBoxes = new AdvancedModelBox[] { this.body, this.tail, this.tail_fin };
        this.chainSwing(tailBoxes, idleSpeed, idleDegree * 0.1F, 3.0, ageInTicks, 1.0F);
        this.bob(this.body, idleSpeed, idleDegree, false, ageInTicks, 1.0F);
        this.bob(this.left_fin, idleSpeed, idleDegree, false, ageInTicks, 1.0F);
        this.bob(this.right_fin, idleSpeed, idleDegree, false, ageInTicks, 1.0F);
        this.swing(this.flag, idleSpeed, idleDegree * 0.2F, false, 3.0F, 0.05F, ageInTicks, 1.0F);
        this.chainSwing(tailBoxes, swimSpeed, swimDegree, -2.0, limbSwing, limbSwingAmount);
        this.swing(this.head, swimSpeed, swimDegree, true, -0.5F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.left_fin, swimSpeed, swimDegree, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.right_fin, swimSpeed, swimDegree, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.left_fin, swimSpeed, -1.5F * swimDegree, false, limbSwing, limbSwingAmount);
        this.bob(this.right_fin, swimSpeed, -1.5F * swimDegree, false, limbSwing, limbSwingAmount);
        this.swing(this.flag, swimSpeed, swimDegree * 0.6F, false, 2.0F, 0.3F, limbSwing, limbSwingAmount);
        this.body.rotateAngleX += headPitch * (float) (Math.PI / 180.0);
        this.head.rotateAngleX -= headPitch * 0.5F * (float) (Math.PI / 180.0);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.tail, this.tail_fin, this.head, this.left_fin, this.right_fin, this.flag);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}