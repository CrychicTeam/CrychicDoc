package net.minecraft.client.renderer.block.model;

import com.mojang.math.Transformation;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockMath;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class FaceBakery {

    public static final int VERTEX_INT_SIZE = 8;

    private static final float RESCALE_22_5 = 1.0F / (float) Math.cos((float) (Math.PI / 8)) - 1.0F;

    private static final float RESCALE_45 = 1.0F / (float) Math.cos((float) (Math.PI / 4)) - 1.0F;

    public static final int VERTEX_COUNT = 4;

    private static final int COLOR_INDEX = 3;

    public static final int UV_INDEX = 4;

    public BakedQuad bakeQuad(Vector3f vectorF0, Vector3f vectorF1, BlockElementFace blockElementFace2, TextureAtlasSprite textureAtlasSprite3, Direction direction4, ModelState modelState5, @Nullable BlockElementRotation blockElementRotation6, boolean boolean7, ResourceLocation resourceLocation8) {
        BlockFaceUV $$9 = blockElementFace2.uv;
        if (modelState5.isUvLocked()) {
            $$9 = recomputeUVs(blockElementFace2.uv, direction4, modelState5.getRotation(), resourceLocation8);
        }
        float[] $$10 = new float[$$9.uvs.length];
        System.arraycopy($$9.uvs, 0, $$10, 0, $$10.length);
        float $$11 = textureAtlasSprite3.uvShrinkRatio();
        float $$12 = ($$9.uvs[0] + $$9.uvs[0] + $$9.uvs[2] + $$9.uvs[2]) / 4.0F;
        float $$13 = ($$9.uvs[1] + $$9.uvs[1] + $$9.uvs[3] + $$9.uvs[3]) / 4.0F;
        $$9.uvs[0] = Mth.lerp($$11, $$9.uvs[0], $$12);
        $$9.uvs[2] = Mth.lerp($$11, $$9.uvs[2], $$12);
        $$9.uvs[1] = Mth.lerp($$11, $$9.uvs[1], $$13);
        $$9.uvs[3] = Mth.lerp($$11, $$9.uvs[3], $$13);
        int[] $$14 = this.makeVertices($$9, textureAtlasSprite3, direction4, this.setupShape(vectorF0, vectorF1), modelState5.getRotation(), blockElementRotation6, boolean7);
        Direction $$15 = calculateFacing($$14);
        System.arraycopy($$10, 0, $$9.uvs, 0, $$10.length);
        if (blockElementRotation6 == null) {
            this.recalculateWinding($$14, $$15);
        }
        return new BakedQuad($$14, blockElementFace2.tintIndex, $$15, textureAtlasSprite3, boolean7);
    }

    public static BlockFaceUV recomputeUVs(BlockFaceUV blockFaceUV0, Direction direction1, Transformation transformation2, ResourceLocation resourceLocation3) {
        Matrix4f $$4 = BlockMath.getUVLockTransform(transformation2, direction1, () -> "Unable to resolve UVLock for model: " + resourceLocation3).getMatrix();
        float $$5 = blockFaceUV0.getU(blockFaceUV0.getReverseIndex(0));
        float $$6 = blockFaceUV0.getV(blockFaceUV0.getReverseIndex(0));
        Vector4f $$7 = $$4.transform(new Vector4f($$5 / 16.0F, $$6 / 16.0F, 0.0F, 1.0F));
        float $$8 = 16.0F * $$7.x();
        float $$9 = 16.0F * $$7.y();
        float $$10 = blockFaceUV0.getU(blockFaceUV0.getReverseIndex(2));
        float $$11 = blockFaceUV0.getV(blockFaceUV0.getReverseIndex(2));
        Vector4f $$12 = $$4.transform(new Vector4f($$10 / 16.0F, $$11 / 16.0F, 0.0F, 1.0F));
        float $$13 = 16.0F * $$12.x();
        float $$14 = 16.0F * $$12.y();
        float $$15;
        float $$16;
        if (Math.signum($$10 - $$5) == Math.signum($$13 - $$8)) {
            $$15 = $$8;
            $$16 = $$13;
        } else {
            $$15 = $$13;
            $$16 = $$8;
        }
        float $$19;
        float $$20;
        if (Math.signum($$11 - $$6) == Math.signum($$14 - $$9)) {
            $$19 = $$9;
            $$20 = $$14;
        } else {
            $$19 = $$14;
            $$20 = $$9;
        }
        float $$23 = (float) Math.toRadians((double) blockFaceUV0.rotation);
        Matrix3f $$24 = new Matrix3f($$4);
        Vector3f $$25 = $$24.transform(new Vector3f(Mth.cos($$23), Mth.sin($$23), 0.0F));
        int $$26 = Math.floorMod(-((int) Math.round(Math.toDegrees(Math.atan2((double) $$25.y(), (double) $$25.x())) / 90.0)) * 90, 360);
        return new BlockFaceUV(new float[] { $$15, $$19, $$16, $$20 }, $$26);
    }

    private int[] makeVertices(BlockFaceUV blockFaceUV0, TextureAtlasSprite textureAtlasSprite1, Direction direction2, float[] float3, Transformation transformation4, @Nullable BlockElementRotation blockElementRotation5, boolean boolean6) {
        int[] $$7 = new int[32];
        for (int $$8 = 0; $$8 < 4; $$8++) {
            this.bakeVertex($$7, $$8, direction2, blockFaceUV0, float3, textureAtlasSprite1, transformation4, blockElementRotation5, boolean6);
        }
        return $$7;
    }

    private float[] setupShape(Vector3f vectorF0, Vector3f vectorF1) {
        float[] $$2 = new float[Direction.values().length];
        $$2[FaceInfo.Constants.MIN_X] = vectorF0.x() / 16.0F;
        $$2[FaceInfo.Constants.MIN_Y] = vectorF0.y() / 16.0F;
        $$2[FaceInfo.Constants.MIN_Z] = vectorF0.z() / 16.0F;
        $$2[FaceInfo.Constants.MAX_X] = vectorF1.x() / 16.0F;
        $$2[FaceInfo.Constants.MAX_Y] = vectorF1.y() / 16.0F;
        $$2[FaceInfo.Constants.MAX_Z] = vectorF1.z() / 16.0F;
        return $$2;
    }

    private void bakeVertex(int[] int0, int int1, Direction direction2, BlockFaceUV blockFaceUV3, float[] float4, TextureAtlasSprite textureAtlasSprite5, Transformation transformation6, @Nullable BlockElementRotation blockElementRotation7, boolean boolean8) {
        FaceInfo.VertexInfo $$9 = FaceInfo.fromFacing(direction2).getVertexInfo(int1);
        Vector3f $$10 = new Vector3f(float4[$$9.xFace], float4[$$9.yFace], float4[$$9.zFace]);
        this.applyElementRotation($$10, blockElementRotation7);
        this.applyModelRotation($$10, transformation6);
        this.fillVertex(int0, int1, $$10, textureAtlasSprite5, blockFaceUV3);
    }

    private void fillVertex(int[] int0, int int1, Vector3f vectorF2, TextureAtlasSprite textureAtlasSprite3, BlockFaceUV blockFaceUV4) {
        int $$5 = int1 * 8;
        int0[$$5] = Float.floatToRawIntBits(vectorF2.x());
        int0[$$5 + 1] = Float.floatToRawIntBits(vectorF2.y());
        int0[$$5 + 2] = Float.floatToRawIntBits(vectorF2.z());
        int0[$$5 + 3] = -1;
        int0[$$5 + 4] = Float.floatToRawIntBits(textureAtlasSprite3.getU((double) blockFaceUV4.getU(int1)));
        int0[$$5 + 4 + 1] = Float.floatToRawIntBits(textureAtlasSprite3.getV((double) blockFaceUV4.getV(int1)));
    }

    private void applyElementRotation(Vector3f vectorF0, @Nullable BlockElementRotation blockElementRotation1) {
        if (blockElementRotation1 != null) {
            Vector3f $$2;
            Vector3f $$3;
            switch(blockElementRotation1.axis()) {
                case X:
                    $$2 = new Vector3f(1.0F, 0.0F, 0.0F);
                    $$3 = new Vector3f(0.0F, 1.0F, 1.0F);
                    break;
                case Y:
                    $$2 = new Vector3f(0.0F, 1.0F, 0.0F);
                    $$3 = new Vector3f(1.0F, 0.0F, 1.0F);
                    break;
                case Z:
                    $$2 = new Vector3f(0.0F, 0.0F, 1.0F);
                    $$3 = new Vector3f(1.0F, 1.0F, 0.0F);
                    break;
                default:
                    throw new IllegalArgumentException("There are only 3 axes");
            }
            Quaternionf $$10 = new Quaternionf().rotationAxis(blockElementRotation1.angle() * (float) (Math.PI / 180.0), $$2);
            if (blockElementRotation1.rescale()) {
                if (Math.abs(blockElementRotation1.angle()) == 22.5F) {
                    $$3.mul(RESCALE_22_5);
                } else {
                    $$3.mul(RESCALE_45);
                }
                $$3.add(1.0F, 1.0F, 1.0F);
            } else {
                $$3.set(1.0F, 1.0F, 1.0F);
            }
            this.rotateVertexBy(vectorF0, new Vector3f(blockElementRotation1.origin()), new Matrix4f().rotation($$10), $$3);
        }
    }

    public void applyModelRotation(Vector3f vectorF0, Transformation transformation1) {
        if (transformation1 != Transformation.identity()) {
            this.rotateVertexBy(vectorF0, new Vector3f(0.5F, 0.5F, 0.5F), transformation1.getMatrix(), new Vector3f(1.0F, 1.0F, 1.0F));
        }
    }

    private void rotateVertexBy(Vector3f vectorF0, Vector3f vectorF1, Matrix4f matrixF2, Vector3f vectorF3) {
        Vector4f $$4 = matrixF2.transform(new Vector4f(vectorF0.x() - vectorF1.x(), vectorF0.y() - vectorF1.y(), vectorF0.z() - vectorF1.z(), 1.0F));
        $$4.mul(new Vector4f(vectorF3, 1.0F));
        vectorF0.set($$4.x() + vectorF1.x(), $$4.y() + vectorF1.y(), $$4.z() + vectorF1.z());
    }

    public static Direction calculateFacing(int[] int0) {
        Vector3f $$1 = new Vector3f(Float.intBitsToFloat(int0[0]), Float.intBitsToFloat(int0[1]), Float.intBitsToFloat(int0[2]));
        Vector3f $$2 = new Vector3f(Float.intBitsToFloat(int0[8]), Float.intBitsToFloat(int0[9]), Float.intBitsToFloat(int0[10]));
        Vector3f $$3 = new Vector3f(Float.intBitsToFloat(int0[16]), Float.intBitsToFloat(int0[17]), Float.intBitsToFloat(int0[18]));
        Vector3f $$4 = new Vector3f($$1).sub($$2);
        Vector3f $$5 = new Vector3f($$3).sub($$2);
        Vector3f $$6 = new Vector3f($$5).cross($$4).normalize();
        if (!$$6.isFinite()) {
            return Direction.UP;
        } else {
            Direction $$7 = null;
            float $$8 = 0.0F;
            for (Direction $$9 : Direction.values()) {
                Vec3i $$10 = $$9.getNormal();
                Vector3f $$11 = new Vector3f((float) $$10.getX(), (float) $$10.getY(), (float) $$10.getZ());
                float $$12 = $$6.dot($$11);
                if ($$12 >= 0.0F && $$12 > $$8) {
                    $$8 = $$12;
                    $$7 = $$9;
                }
            }
            return $$7 == null ? Direction.UP : $$7;
        }
    }

    private void recalculateWinding(int[] int0, Direction direction1) {
        int[] $$2 = new int[int0.length];
        System.arraycopy(int0, 0, $$2, 0, int0.length);
        float[] $$3 = new float[Direction.values().length];
        $$3[FaceInfo.Constants.MIN_X] = 999.0F;
        $$3[FaceInfo.Constants.MIN_Y] = 999.0F;
        $$3[FaceInfo.Constants.MIN_Z] = 999.0F;
        $$3[FaceInfo.Constants.MAX_X] = -999.0F;
        $$3[FaceInfo.Constants.MAX_Y] = -999.0F;
        $$3[FaceInfo.Constants.MAX_Z] = -999.0F;
        for (int $$4 = 0; $$4 < 4; $$4++) {
            int $$5 = 8 * $$4;
            float $$6 = Float.intBitsToFloat($$2[$$5]);
            float $$7 = Float.intBitsToFloat($$2[$$5 + 1]);
            float $$8 = Float.intBitsToFloat($$2[$$5 + 2]);
            if ($$6 < $$3[FaceInfo.Constants.MIN_X]) {
                $$3[FaceInfo.Constants.MIN_X] = $$6;
            }
            if ($$7 < $$3[FaceInfo.Constants.MIN_Y]) {
                $$3[FaceInfo.Constants.MIN_Y] = $$7;
            }
            if ($$8 < $$3[FaceInfo.Constants.MIN_Z]) {
                $$3[FaceInfo.Constants.MIN_Z] = $$8;
            }
            if ($$6 > $$3[FaceInfo.Constants.MAX_X]) {
                $$3[FaceInfo.Constants.MAX_X] = $$6;
            }
            if ($$7 > $$3[FaceInfo.Constants.MAX_Y]) {
                $$3[FaceInfo.Constants.MAX_Y] = $$7;
            }
            if ($$8 > $$3[FaceInfo.Constants.MAX_Z]) {
                $$3[FaceInfo.Constants.MAX_Z] = $$8;
            }
        }
        FaceInfo $$9 = FaceInfo.fromFacing(direction1);
        for (int $$10 = 0; $$10 < 4; $$10++) {
            int $$11 = 8 * $$10;
            FaceInfo.VertexInfo $$12 = $$9.getVertexInfo($$10);
            float $$13 = $$3[$$12.xFace];
            float $$14 = $$3[$$12.yFace];
            float $$15 = $$3[$$12.zFace];
            int0[$$11] = Float.floatToRawIntBits($$13);
            int0[$$11 + 1] = Float.floatToRawIntBits($$14);
            int0[$$11 + 2] = Float.floatToRawIntBits($$15);
            for (int $$16 = 0; $$16 < 4; $$16++) {
                int $$17 = 8 * $$16;
                float $$18 = Float.intBitsToFloat($$2[$$17]);
                float $$19 = Float.intBitsToFloat($$2[$$17 + 1]);
                float $$20 = Float.intBitsToFloat($$2[$$17 + 2]);
                if (Mth.equal($$13, $$18) && Mth.equal($$14, $$19) && Mth.equal($$15, $$20)) {
                    int0[$$11 + 4] = $$2[$$17 + 4];
                    int0[$$11 + 4 + 1] = $$2[$$17 + 4 + 1];
                }
            }
        }
    }
}