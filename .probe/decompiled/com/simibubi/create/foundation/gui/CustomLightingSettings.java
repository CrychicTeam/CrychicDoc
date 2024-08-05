package com.simibubi.create.foundation.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CustomLightingSettings implements ILightingSettings {

    private Vector3f light1;

    private Vector3f light2;

    private Matrix4f lightMatrix;

    protected CustomLightingSettings(float yRot, float xRot) {
        this.init(yRot, xRot, 0.0F, 0.0F, false);
    }

    protected CustomLightingSettings(float yRot1, float xRot1, float yRot2, float xRot2) {
        this.init(yRot1, xRot1, yRot2, xRot2, true);
    }

    protected void init(float yRot1, float xRot1, float yRot2, float xRot2, boolean doubleLight) {
        this.light1 = new Vector3f(0.0F, 0.0F, 1.0F);
        this.light1.rotate(Axis.YP.rotationDegrees(yRot1));
        this.light1.rotate(Axis.XN.rotationDegrees(xRot1));
        if (doubleLight) {
            this.light2 = new Vector3f(0.0F, 0.0F, 1.0F);
            this.light2.rotate(Axis.YP.rotationDegrees(yRot2));
            this.light2.rotate(Axis.XN.rotationDegrees(xRot2));
        } else {
            this.light2 = new Vector3f();
        }
        this.lightMatrix = new Matrix4f();
        this.lightMatrix.identity();
    }

    @Override
    public void applyLighting() {
        RenderSystem.setupLevelDiffuseLighting(this.light1, this.light2, this.lightMatrix);
    }

    public static CustomLightingSettings.Builder builder() {
        return new CustomLightingSettings.Builder();
    }

    public static class Builder {

        private float yRot1;

        private float xRot1;

        private float yRot2;

        private float xRot2;

        private boolean doubleLight;

        public CustomLightingSettings.Builder firstLightRotation(float yRot, float xRot) {
            this.yRot1 = yRot;
            this.xRot1 = xRot;
            return this;
        }

        public CustomLightingSettings.Builder secondLightRotation(float yRot, float xRot) {
            this.yRot2 = yRot;
            this.xRot2 = xRot;
            this.doubleLight = true;
            return this;
        }

        public CustomLightingSettings.Builder doubleLight() {
            this.doubleLight = true;
            return this;
        }

        public CustomLightingSettings build() {
            return this.doubleLight ? new CustomLightingSettings(this.yRot1, this.xRot1, this.yRot2, this.xRot2) : new CustomLightingSettings(this.yRot1, this.xRot1);
        }
    }
}