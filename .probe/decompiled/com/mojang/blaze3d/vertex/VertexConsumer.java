package com.mojang.blaze3d.vertex;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Vec3i;
import net.minecraft.util.FastColor;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public interface VertexConsumer {

    VertexConsumer vertex(double var1, double var3, double var5);

    VertexConsumer color(int var1, int var2, int var3, int var4);

    VertexConsumer uv(float var1, float var2);

    VertexConsumer overlayCoords(int var1, int var2);

    VertexConsumer uv2(int var1, int var2);

    VertexConsumer normal(float var1, float var2, float var3);

    void endVertex();

    default void vertex(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, int int9, int int10, float float11, float float12, float float13) {
        this.vertex((double) float0, (double) float1, (double) float2);
        this.color(float3, float4, float5, float6);
        this.uv(float7, float8);
        this.overlayCoords(int9);
        this.uv2(int10);
        this.normal(float11, float12, float13);
        this.endVertex();
    }

    void defaultColor(int var1, int var2, int var3, int var4);

    void unsetDefaultColor();

    default VertexConsumer color(float float0, float float1, float float2, float float3) {
        return this.color((int) (float0 * 255.0F), (int) (float1 * 255.0F), (int) (float2 * 255.0F), (int) (float3 * 255.0F));
    }

    default VertexConsumer color(int int0) {
        return this.color(FastColor.ARGB32.red(int0), FastColor.ARGB32.green(int0), FastColor.ARGB32.blue(int0), FastColor.ARGB32.alpha(int0));
    }

    default VertexConsumer uv2(int int0) {
        return this.uv2(int0 & 65535, int0 >> 16 & 65535);
    }

    default VertexConsumer overlayCoords(int int0) {
        return this.overlayCoords(int0 & 65535, int0 >> 16 & 65535);
    }

    default void putBulkData(PoseStack.Pose poseStackPose0, BakedQuad bakedQuad1, float float2, float float3, float float4, int int5, int int6) {
        this.putBulkData(poseStackPose0, bakedQuad1, new float[] { 1.0F, 1.0F, 1.0F, 1.0F }, float2, float3, float4, new int[] { int5, int5, int5, int5 }, int6, false);
    }

    default void putBulkData(PoseStack.Pose poseStackPose0, BakedQuad bakedQuad1, float[] float2, float float3, float float4, float float5, int[] int6, int int7, boolean boolean8) {
        float[] $$9 = new float[] { float2[0], float2[1], float2[2], float2[3] };
        int[] $$10 = new int[] { int6[0], int6[1], int6[2], int6[3] };
        int[] $$11 = bakedQuad1.getVertices();
        Vec3i $$12 = bakedQuad1.getDirection().getNormal();
        Matrix4f $$13 = poseStackPose0.pose();
        Vector3f $$14 = poseStackPose0.normal().transform(new Vector3f((float) $$12.getX(), (float) $$12.getY(), (float) $$12.getZ()));
        int $$15 = 8;
        int $$16 = $$11.length / 8;
        MemoryStack $$17 = MemoryStack.stackPush();
        try {
            ByteBuffer $$18 = $$17.malloc(DefaultVertexFormat.BLOCK.getVertexSize());
            IntBuffer $$19 = $$18.asIntBuffer();
            for (int $$20 = 0; $$20 < $$16; $$20++) {
                $$19.clear();
                $$19.put($$11, $$20 * 8, 8);
                float $$21 = $$18.getFloat(0);
                float $$22 = $$18.getFloat(4);
                float $$23 = $$18.getFloat(8);
                float $$27;
                float $$28;
                float $$29;
                if (boolean8) {
                    float $$24 = (float) ($$18.get(12) & 255) / 255.0F;
                    float $$25 = (float) ($$18.get(13) & 255) / 255.0F;
                    float $$26 = (float) ($$18.get(14) & 255) / 255.0F;
                    $$27 = $$24 * $$9[$$20] * float3;
                    $$28 = $$25 * $$9[$$20] * float4;
                    $$29 = $$26 * $$9[$$20] * float5;
                } else {
                    $$27 = $$9[$$20] * float3;
                    $$28 = $$9[$$20] * float4;
                    $$29 = $$9[$$20] * float5;
                }
                int $$33 = $$10[$$20];
                float $$34 = $$18.getFloat(16);
                float $$35 = $$18.getFloat(20);
                Vector4f $$36 = $$13.transform(new Vector4f($$21, $$22, $$23, 1.0F));
                this.vertex($$36.x(), $$36.y(), $$36.z(), $$27, $$28, $$29, 1.0F, $$34, $$35, int7, $$33, $$14.x(), $$14.y(), $$14.z());
            }
        } catch (Throwable var33) {
            if ($$17 != null) {
                try {
                    $$17.close();
                } catch (Throwable var32) {
                    var33.addSuppressed(var32);
                }
            }
            throw var33;
        }
        if ($$17 != null) {
            $$17.close();
        }
    }

    default VertexConsumer vertex(Matrix4f matrixF0, float float1, float float2, float float3) {
        Vector4f $$4 = matrixF0.transform(new Vector4f(float1, float2, float3, 1.0F));
        return this.vertex((double) $$4.x(), (double) $$4.y(), (double) $$4.z());
    }

    default VertexConsumer normal(Matrix3f matrixF0, float float1, float float2, float float3) {
        Vector3f $$4 = matrixF0.transform(new Vector3f(float1, float2, float3));
        return this.normal($$4.x(), $$4.y(), $$4.z());
    }
}