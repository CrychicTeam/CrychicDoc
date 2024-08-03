package com.mojang.blaze3d.vertex;

import net.minecraft.core.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class SheetedDecalTextureGenerator extends DefaultedVertexConsumer {

    private final VertexConsumer delegate;

    private final Matrix4f cameraInversePose;

    private final Matrix3f normalInversePose;

    private final float textureScale;

    private float x;

    private float y;

    private float z;

    private int overlayU;

    private int overlayV;

    private int lightCoords;

    private float nx;

    private float ny;

    private float nz;

    public SheetedDecalTextureGenerator(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3) {
        this.delegate = vertexConsumer0;
        this.cameraInversePose = new Matrix4f(matrixF1).invert();
        this.normalInversePose = new Matrix3f(matrixF2).invert();
        this.textureScale = float3;
        this.resetState();
    }

    private void resetState() {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
        this.overlayU = 0;
        this.overlayV = 10;
        this.lightCoords = 15728880;
        this.nx = 0.0F;
        this.ny = 1.0F;
        this.nz = 0.0F;
    }

    @Override
    public void endVertex() {
        Vector3f $$0 = this.normalInversePose.transform(new Vector3f(this.nx, this.ny, this.nz));
        Direction $$1 = Direction.getNearest($$0.x(), $$0.y(), $$0.z());
        Vector4f $$2 = this.cameraInversePose.transform(new Vector4f(this.x, this.y, this.z, 1.0F));
        $$2.rotateY((float) Math.PI);
        $$2.rotateX((float) (-Math.PI / 2));
        $$2.rotate($$1.getRotation());
        float $$3 = -$$2.x() * this.textureScale;
        float $$4 = -$$2.y() * this.textureScale;
        this.delegate.vertex((double) this.x, (double) this.y, (double) this.z).color(1.0F, 1.0F, 1.0F, 1.0F).uv($$3, $$4).overlayCoords(this.overlayU, this.overlayV).uv2(this.lightCoords).normal(this.nx, this.ny, this.nz).endVertex();
        this.resetState();
    }

    @Override
    public VertexConsumer vertex(double double0, double double1, double double2) {
        this.x = (float) double0;
        this.y = (float) double1;
        this.z = (float) double2;
        return this;
    }

    @Override
    public VertexConsumer color(int int0, int int1, int int2, int int3) {
        return this;
    }

    @Override
    public VertexConsumer uv(float float0, float float1) {
        return this;
    }

    @Override
    public VertexConsumer overlayCoords(int int0, int int1) {
        this.overlayU = int0;
        this.overlayV = int1;
        return this;
    }

    @Override
    public VertexConsumer uv2(int int0, int int1) {
        this.lightCoords = int0 | int1 << 16;
        return this;
    }

    @Override
    public VertexConsumer normal(float float0, float float1, float float2) {
        this.nx = float0;
        this.ny = float1;
        this.nz = float2;
        return this;
    }
}