package com.github.alexthe666.citadel.client.model;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.container.Transform;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelAnimator {

    private int tempTick = 0;

    private int prevTempTick;

    private boolean correctAnimation = false;

    private IAnimatedEntity entity;

    private HashMap<AdvancedModelBox, Transform> transformMap = new HashMap();

    private HashMap<AdvancedModelBox, Transform> prevTransformMap = new HashMap();

    public static ModelAnimator create() {
        return new ModelAnimator();
    }

    public IAnimatedEntity getEntity() {
        return this.entity;
    }

    public void update(IAnimatedEntity entity) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = false;
        this.entity = entity;
        this.transformMap.clear();
        this.prevTransformMap.clear();
    }

    public boolean setAnimation(Animation animation) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = this.entity.getAnimation() == animation;
        return this.correctAnimation;
    }

    public void startKeyframe(int duration) {
        if (this.correctAnimation) {
            this.prevTempTick = this.tempTick;
            this.tempTick += duration;
        }
    }

    public void setStaticKeyframe(int duration) {
        this.startKeyframe(duration);
        this.endKeyframe(true);
    }

    public void resetKeyframe(int duration) {
        this.startKeyframe(duration);
        this.endKeyframe();
    }

    public void rotate(AdvancedModelBox box, float x, float y, float z) {
        if (this.correctAnimation) {
            this.getTransform(box).addRotation(x, y, z);
        }
    }

    public void move(AdvancedModelBox box, float x, float y, float z) {
        if (this.correctAnimation) {
            this.getTransform(box).addOffset(x, y, z);
        }
    }

    private Transform getTransform(AdvancedModelBox box) {
        return (Transform) this.transformMap.computeIfAbsent(box, b -> new Transform());
    }

    public void endKeyframe() {
        this.endKeyframe(false);
    }

    private void endKeyframe(boolean stationary) {
        if (this.correctAnimation) {
            int animationTick = this.entity.getAnimationTick();
            if (animationTick >= this.prevTempTick && animationTick < this.tempTick) {
                if (stationary) {
                    for (AdvancedModelBox box : this.prevTransformMap.keySet()) {
                        Transform transform = (Transform) this.prevTransformMap.get(box);
                        box.rotateAngleX = box.rotateAngleX + transform.getRotationX();
                        box.rotateAngleY = box.rotateAngleY + transform.getRotationY();
                        box.rotateAngleZ = box.rotateAngleZ + transform.getRotationZ();
                        box.rotationPointX = box.rotationPointX + transform.getOffsetX();
                        box.rotationPointY = box.rotationPointY + transform.getOffsetY();
                        box.rotationPointZ = box.rotationPointZ + transform.getOffsetZ();
                    }
                } else {
                    float tick = ((float) (animationTick - this.prevTempTick) + Minecraft.getInstance().getFrameTime()) / (float) (this.tempTick - this.prevTempTick);
                    float inc = Mth.sin((float) ((double) tick * Math.PI / 2.0));
                    float dec = 1.0F - inc;
                    for (AdvancedModelBox box : this.prevTransformMap.keySet()) {
                        Transform transform = (Transform) this.prevTransformMap.get(box);
                        box.rotateAngleX = box.rotateAngleX + dec * transform.getRotationX();
                        box.rotateAngleY = box.rotateAngleY + dec * transform.getRotationY();
                        box.rotateAngleZ = box.rotateAngleZ + dec * transform.getRotationZ();
                        box.rotationPointX = box.rotationPointX + dec * transform.getOffsetX();
                        box.rotationPointY = box.rotationPointY + dec * transform.getOffsetY();
                        box.rotationPointZ = box.rotationPointZ + dec * transform.getOffsetZ();
                    }
                    for (AdvancedModelBox box : this.transformMap.keySet()) {
                        Transform transform = (Transform) this.transformMap.get(box);
                        box.rotateAngleX = box.rotateAngleX + inc * transform.getRotationX();
                        box.rotateAngleY = box.rotateAngleY + inc * transform.getRotationY();
                        box.rotateAngleZ = box.rotateAngleZ + inc * transform.getRotationZ();
                        box.rotationPointX = box.rotationPointX + inc * transform.getOffsetX();
                        box.rotationPointY = box.rotationPointY + inc * transform.getOffsetY();
                        box.rotationPointZ = box.rotationPointZ + inc * transform.getOffsetZ();
                    }
                }
            }
            if (!stationary) {
                this.prevTransformMap.clear();
                this.prevTransformMap.putAll(this.transformMap);
                this.transformMap.clear();
            }
        }
    }
}