package net.minecraft.client.renderer.culling;

import net.minecraft.world.phys.AABB;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Frustum {

    public static final int OFFSET_STEP = 4;

    private final FrustumIntersection intersection = new FrustumIntersection();

    private final Matrix4f matrix = new Matrix4f();

    private Vector4f viewVector;

    private double camX;

    private double camY;

    private double camZ;

    public Frustum(Matrix4f matrixF0, Matrix4f matrixF1) {
        this.calculateFrustum(matrixF0, matrixF1);
    }

    public Frustum(Frustum frustum0) {
        this.intersection.set(frustum0.matrix);
        this.matrix.set(frustum0.matrix);
        this.camX = frustum0.camX;
        this.camY = frustum0.camY;
        this.camZ = frustum0.camZ;
        this.viewVector = frustum0.viewVector;
    }

    public Frustum offsetToFullyIncludeCameraCube(int int0) {
        double $$1 = Math.floor(this.camX / (double) int0) * (double) int0;
        double $$2 = Math.floor(this.camY / (double) int0) * (double) int0;
        double $$3 = Math.floor(this.camZ / (double) int0) * (double) int0;
        double $$4 = Math.ceil(this.camX / (double) int0) * (double) int0;
        double $$5 = Math.ceil(this.camY / (double) int0) * (double) int0;
        for (double $$6 = Math.ceil(this.camZ / (double) int0) * (double) int0; this.intersection.intersectAab((float) ($$1 - this.camX), (float) ($$2 - this.camY), (float) ($$3 - this.camZ), (float) ($$4 - this.camX), (float) ($$5 - this.camY), (float) ($$6 - this.camZ)) != -2; this.camZ = this.camZ - (double) (this.viewVector.z() * 4.0F)) {
            this.camX = this.camX - (double) (this.viewVector.x() * 4.0F);
            this.camY = this.camY - (double) (this.viewVector.y() * 4.0F);
        }
        return this;
    }

    public void prepare(double double0, double double1, double double2) {
        this.camX = double0;
        this.camY = double1;
        this.camZ = double2;
    }

    private void calculateFrustum(Matrix4f matrixF0, Matrix4f matrixF1) {
        matrixF1.mul(matrixF0, this.matrix);
        this.intersection.set(this.matrix);
        this.viewVector = this.matrix.transformTranspose(new Vector4f(0.0F, 0.0F, 1.0F, 0.0F));
    }

    public boolean isVisible(AABB aABB0) {
        return this.cubeInFrustum(aABB0.minX, aABB0.minY, aABB0.minZ, aABB0.maxX, aABB0.maxY, aABB0.maxZ);
    }

    private boolean cubeInFrustum(double double0, double double1, double double2, double double3, double double4, double double5) {
        float $$6 = (float) (double0 - this.camX);
        float $$7 = (float) (double1 - this.camY);
        float $$8 = (float) (double2 - this.camZ);
        float $$9 = (float) (double3 - this.camX);
        float $$10 = (float) (double4 - this.camY);
        float $$11 = (float) (double5 - this.camZ);
        return this.intersection.testAab($$6, $$7, $$8, $$9, $$10, $$11);
    }
}