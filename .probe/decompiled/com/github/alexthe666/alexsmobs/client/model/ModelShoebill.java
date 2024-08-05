package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityShoebill;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;

public class ModelShoebill extends AdvancedEntityModel<EntityShoebill> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox wing_left;

    private final AdvancedModelBox wing_right;

    private final AdvancedModelBox wing_right_pivot;

    private final AdvancedModelBox wing_left_pivot;

    private final AdvancedModelBox leg_left;

    private final AdvancedModelBox leg_right;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox crest;

    private final AdvancedModelBox beak;

    private final AdvancedModelBox jaw;

    public ModelAnimator animator;

    public ModelShoebill() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -14.0F, 0.0F);
        this.root.addChild(this.body);
        this.setRotationAngle(this.body, 0.9599F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 15).addBox(-2.5F, -5.0F, -2.0F, 5.0F, 10.0F, 5.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, 5.0F, 2.5F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, -0.3054F, 0.0F, 0.0F);
        this.tail.setTextureOffset(0, 31).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 5.0F, 3.0F, 0.0F, false);
        this.wing_left_pivot = new AdvancedModelBox(this, "wing_left_pivot");
        this.wing_left_pivot.setPos(2.5F, -4.0F, 1.0F);
        this.body.addChild(this.wing_left_pivot);
        this.wing_left = new AdvancedModelBox(this, "wing_left");
        this.wing_left_pivot.addChild(this.wing_left);
        this.wing_left.setTextureOffset(21, 21).addBox(0.0F, 0.0F, -4.0F, 1.0F, 12.0F, 6.0F, 0.0F, false);
        this.wing_right_pivot = new AdvancedModelBox(this, "wing_right_pivot");
        this.wing_right_pivot.setPos(-2.5F, -4.0F, 1.0F);
        this.body.addChild(this.wing_right_pivot);
        this.wing_right = new AdvancedModelBox(this, "wing_right");
        this.wing_right_pivot.addChild(this.wing_right);
        this.wing_right.setTextureOffset(21, 21).addBox(-1.0F, 0.0F, -4.0F, 1.0F, 12.0F, 6.0F, 0.0F, true);
        this.leg_left = new AdvancedModelBox(this, "leg_left");
        this.leg_left.setPos(2.0F, 2.3F, -2.0F);
        this.body.addChild(this.leg_left);
        this.setRotationAngle(this.leg_left, 0.6109F, 0.0F, 0.0F);
        this.leg_left.setTextureOffset(0, 0).addBox(-2.0F, -3.0F, -11.0F, 3.0F, 3.0F, 11.0F, 0.0F, false);
        this.leg_right = new AdvancedModelBox(this, "leg_right");
        this.leg_right.setPos(-2.0F, 2.3F, -2.0F);
        this.body.addChild(this.leg_right);
        this.setRotationAngle(this.leg_right, 0.6109F, 0.0F, 0.0F);
        this.leg_right.setTextureOffset(0, 0).addBox(-1.0F, -3.0F, -11.0F, 3.0F, 3.0F, 11.0F, 0.0F, true);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setPos(0.0F, -4.8F, 1.0F);
        this.body.addChild(this.neck);
        this.setRotationAngle(this.neck, -0.7418F, 0.0F, 0.0F);
        this.neck.setTextureOffset(35, 0).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -3.8F, -0.3F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(25, 11).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);
        this.crest = new AdvancedModelBox(this, "crest");
        this.crest.setPos(0.0F, -2.0F, 2.0F);
        this.head.addChild(this.crest);
        this.crest.setTextureOffset(0, 0).addBox(0.0F, -2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, false);
        this.beak = new AdvancedModelBox(this, "beak");
        this.beak.setPos(0.0F, -2.9F, -2.0F);
        this.head.addChild(this.beak);
        this.setRotationAngle(this.beak, 0.3491F, 0.0F, 0.0F);
        this.beak.setTextureOffset(18, 0).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 2.0F, 5.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this, "jaw");
        this.jaw.setPos(0.0F, 2.0F, 0.0F);
        this.beak.addChild(this.jaw);
        this.setRotationAngle(this.jaw, -0.1745F, 0.0F, 0.0F);
        this.jaw.setTextureOffset(41, 36).addBox(-1.5F, 0.0F, -5.0F, 3.0F, 1.0F, 5.0F, -0.1F, false);
        this.updateDefaultPose();
        this.animator = new ModelAnimator();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        this.animator.setAnimation(EntityShoebill.ANIMATION_FISH);
        this.animator.startKeyframe(15);
        this.animator.rotate(this.neck, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, 0.5F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.body, Maths.rad(40.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_left, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.leg_right, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(70.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-50.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(20.0), 0.0F, 0.0F);
        this.animator.move(this.body, 0.0F, 1.0F, 0.0F);
        this.animator.move(this.neck, 0.0F, 0.0F, -3.0F);
        this.animator.move(this.head, 0.0F, 0.0F, -2.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(3);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(EntityShoebill.ANIMATION_BEAKSHAKE);
        this.animator.startKeyframe(4);
        this.animator.rotate(this.crest, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(-40.0), Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(40.0), 0.0F);
        this.animator.move(this.head, 0.0F, 0.5F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.crest, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(-40.0), Maths.rad(-10.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(-40.0), 0.0F);
        this.animator.move(this.head, 0.0F, 0.5F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.crest, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(-40.0), Maths.rad(10.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(40.0), 0.0F);
        this.animator.move(this.head, 0.0F, 0.5F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(4);
        this.animator.rotate(this.crest, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(-40.0), Maths.rad(-10.0), 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), Maths.rad(-40.0), 0.0F);
        this.animator.move(this.head, 0.0F, 0.5F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(4);
        this.animator.setAnimation(EntityShoebill.ANIMATION_ATTACK);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.crest, Maths.rad(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, Maths.rad(-40.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(40.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(30.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, 0.5F, 0.0F);
        this.animator.move(this.crest, 0.0F, -0.5F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, Maths.rad(70.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, Maths.rad(-80.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, Maths.rad(5.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.leg_left, this.leg_right, this.wing_left, this.wing_right, this.tail, this.head, this.neck, this.crest, this.beak, this.jaw, new AdvancedModelBox[] { this.wing_left_pivot, this.wing_right_pivot });
    }

    public void setupAnim(EntityShoebill entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float walkSpeed = 0.7F;
        float walkDegree = 0.4F;
        float idleSpeed = 0.05F;
        float idleDegree = 0.2F;
        float flapSpeed = 0.4F;
        float flapDegree = 0.2F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float flyProgress = entity.prevFlyProgress + (entity.flyProgress - entity.prevFlyProgress) * partialTick;
        float scaledLimbSwing = Math.min(1.0F, limbSwingAmount * 1.6F);
        float runProgress = Math.max(5.0F * scaledLimbSwing - flyProgress, 0.0F);
        this.progressRotationPrev(this.body, runProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, runProgress, Maths.rad(-25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, runProgress, Maths.rad(-25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.neck, runProgress, Maths.rad(-55.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, runProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.body, flyProgress, Maths.rad(35.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_right, flyProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leg_left, flyProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.wing_right, flyProgress, 0.0F, Maths.rad(90.0), Maths.rad(80.0), 5.0F);
        this.progressRotationPrev(this.wing_left, flyProgress, 0.0F, Maths.rad(-90.0), Maths.rad(-80.0), 5.0F);
        this.progressRotationPrev(this.neck, flyProgress, Maths.rad(-70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, flyProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, flyProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.wing_right_pivot, flyProgress, 1.0F, 4.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.wing_left_pivot, flyProgress, -1.0F, 4.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leg_right, flyProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leg_left, flyProgress, 0.0F, -1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.body, flyProgress, 0.0F, 5.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, flyProgress, 0.0F, 1.5F, 0.0F, 5.0F);
        this.walk(this.neck, idleSpeed, idleDegree, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, -idleSpeed, idleDegree, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.tail, idleSpeed * 2.0F, idleDegree * 0.5F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        if (flyProgress > 0.0F) {
            this.walk(this.leg_right, walkSpeed, walkDegree * 0.2F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.leg_left, walkSpeed, walkDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.swing(this.wing_right_pivot, flapSpeed, flapDegree * 5.0F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.swing(this.wing_left_pivot, flapSpeed, flapDegree * 5.0F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.walk(this.neck, flapSpeed, flapDegree * 0.85F, false, 0.0F, 0.2F, ageInTicks, 1.0F);
            this.walk(this.head, flapSpeed, flapDegree * 0.85F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.bob(this.body, flapSpeed * 0.3F, flapDegree * 4.0F, true, ageInTicks, 1.0F);
        } else {
            this.walk(this.leg_right, walkSpeed, walkDegree * 1.85F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.leg_left, walkSpeed, walkDegree * 1.85F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.neck, walkSpeed, walkDegree * 0.85F, false, 2.0F, 0.2F, limbSwing, limbSwingAmount);
            this.walk(this.head, walkSpeed, walkDegree * 0.85F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.tail, walkSpeed * 0.5F, walkDegree * 0.15F, true, -2.0F, 0.2F, limbSwing, limbSwingAmount);
        }
        this.head.rotateAngleY += netHeadYaw * (float) (Math.PI / 180.0);
    }
}