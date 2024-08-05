package com.github.alexthe666.citadel.client.model;

import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.github.alexthe666.citadel.client.model.container.TextureOffset;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AdvancedEntityModel<T extends Entity> extends BasicEntityModel<T> {

    private float movementScale = 1.0F;

    private final Map<String, TextureOffset> modelTextureMap = Maps.newHashMap();

    public int texWidth = 32;

    public int texHeight = 32;

    public void updateDefaultPose() {
        this.getAllParts().forEach(modelRenderer -> modelRenderer.updateDefaultPose());
    }

    protected void setTextureOffset(String partName, int x, int y) {
        this.modelTextureMap.put(partName, new TextureOffset(x, y));
    }

    public TextureOffset getTextureOffset(String partName) {
        return (TextureOffset) this.modelTextureMap.get(partName);
    }

    public void resetToDefaultPose() {
        this.getAllParts().forEach(modelRenderer -> modelRenderer.resetToDefaultPose());
    }

    public void faceTarget(float yaw, float pitch, float rotationDivisor, AdvancedModelBox... boxes) {
        float actualRotationDivisor = rotationDivisor * (float) boxes.length;
        float yawAmount = yaw / (180.0F / (float) Math.PI) / actualRotationDivisor;
        float pitchAmount = pitch / (180.0F / (float) Math.PI) / actualRotationDivisor;
        for (AdvancedModelBox box : boxes) {
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }
    }

    public void chainSwing(AdvancedModelBox[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleY = boxes[index].rotateAngleY + this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    public void chainWave(AdvancedModelBox[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleX = boxes[index].rotateAngleX + this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    public void chainFlap(AdvancedModelBox[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleZ = boxes[index].rotateAngleZ + this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    private float calculateChainRotation(float speed, float degree, float swing, float swingAmount, float offset, int boxIndex) {
        return Mth.cos(swing * speed * this.movementScale + offset * (float) boxIndex) * swingAmount * degree * this.movementScale;
    }

    private float calculateChainOffset(double rootOffset, AdvancedModelBox... boxes) {
        return (float) (rootOffset * Math.PI / (double) (2 * boxes.length));
    }

    public float getMovementScale() {
        return this.movementScale;
    }

    public void setMovementScale(float movementScale) {
        this.movementScale = movementScale;
    }

    public void walk(AdvancedModelBox box, float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        box.walk(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    public void flap(AdvancedModelBox box, float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount) {
        box.flap(speed, degree, invert, offset, weight, flap, flapAmount);
    }

    public void swing(AdvancedModelBox box, float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount) {
        box.swing(speed, degree, invert, offset, weight, swing, swingAmount);
    }

    public void bob(AdvancedModelBox box, float speed, float degree, boolean bounce, float f, float f1) {
        box.bob(speed, degree, bounce, f, f1);
    }

    public float moveBox(float speed, float degree, boolean bounce, float f, float f1) {
        return bounce ? -Mth.abs(Mth.sin(f * speed) * f1 * degree) : Mth.sin(f * speed) * f1 * degree - f1 * degree;
    }

    public void setRotateAngle(AdvancedModelBox model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void rotate(ModelAnimator animator, AdvancedModelBox model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians((double) x), (float) Math.toRadians((double) y), (float) Math.toRadians((double) z));
    }

    public void rotateMinus(ModelAnimator animator, AdvancedModelBox model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians((double) x) - model.defaultRotationX, (float) Math.toRadians((double) y) - model.defaultRotationY, (float) Math.toRadians((double) z) - model.defaultRotationZ);
    }

    public void progressRotation(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ, float divisor) {
        model.rotateAngleX = model.rotateAngleX + progress * (rotX - model.defaultRotationX) / divisor;
        model.rotateAngleY = model.rotateAngleY + progress * (rotY - model.defaultRotationY) / divisor;
        model.rotateAngleZ = model.rotateAngleZ + progress * (rotZ - model.defaultRotationZ) / divisor;
    }

    public void progressRotationPrev(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ, float divisor) {
        model.rotateAngleX += progress * rotX / divisor;
        model.rotateAngleY += progress * rotY / divisor;
        model.rotateAngleZ += progress * rotZ / divisor;
    }

    public void progressPosition(AdvancedModelBox model, float progress, float x, float y, float z, float divisor) {
        model.rotationPointX = model.rotationPointX + progress * (x - model.defaultPositionX) / divisor;
        model.rotationPointY = model.rotationPointY + progress * (y - model.defaultPositionY) / divisor;
        model.rotationPointZ = model.rotationPointZ + progress * (z - model.defaultPositionZ) / divisor;
    }

    public void progressPositionPrev(AdvancedModelBox model, float progress, float x, float y, float z, float divisor) {
        model.rotationPointX += progress * x / divisor;
        model.rotationPointY += progress * y / divisor;
        model.rotationPointZ += progress * z / divisor;
    }

    public abstract Iterable<AdvancedModelBox> getAllParts();
}