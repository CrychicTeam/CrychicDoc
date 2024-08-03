package net.minecraft.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import java.util.BitSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ModelBlockRenderer {

    private static final int FACE_CUBIC = 0;

    private static final int FACE_PARTIAL = 1;

    static final Direction[] DIRECTIONS = Direction.values();

    private final BlockColors blockColors;

    private static final int CACHE_SIZE = 100;

    static final ThreadLocal<ModelBlockRenderer.Cache> CACHE = ThreadLocal.withInitial(ModelBlockRenderer.Cache::new);

    public ModelBlockRenderer(BlockColors blockColors0) {
        this.blockColors = blockColors0;
    }

    public void tesselateBlock(BlockAndTintGetter blockAndTintGetter0, BakedModel bakedModel1, BlockState blockState2, BlockPos blockPos3, PoseStack poseStack4, VertexConsumer vertexConsumer5, boolean boolean6, RandomSource randomSource7, long long8, int int9) {
        boolean $$10 = Minecraft.useAmbientOcclusion() && blockState2.m_60791_() == 0 && bakedModel1.useAmbientOcclusion();
        Vec3 $$11 = blockState2.m_60824_(blockAndTintGetter0, blockPos3);
        poseStack4.translate($$11.x, $$11.y, $$11.z);
        try {
            if ($$10) {
                this.tesselateWithAO(blockAndTintGetter0, bakedModel1, blockState2, blockPos3, poseStack4, vertexConsumer5, boolean6, randomSource7, long8, int9);
            } else {
                this.tesselateWithoutAO(blockAndTintGetter0, bakedModel1, blockState2, blockPos3, poseStack4, vertexConsumer5, boolean6, randomSource7, long8, int9);
            }
        } catch (Throwable var17) {
            CrashReport $$13 = CrashReport.forThrowable(var17, "Tesselating block model");
            CrashReportCategory $$14 = $$13.addCategory("Block model being tesselated");
            CrashReportCategory.populateBlockDetails($$14, blockAndTintGetter0, blockPos3, blockState2);
            $$14.setDetail("Using AO", $$10);
            throw new ReportedException($$13);
        }
    }

    public void tesselateWithAO(BlockAndTintGetter blockAndTintGetter0, BakedModel bakedModel1, BlockState blockState2, BlockPos blockPos3, PoseStack poseStack4, VertexConsumer vertexConsumer5, boolean boolean6, RandomSource randomSource7, long long8, int int9) {
        float[] $$10 = new float[DIRECTIONS.length * 2];
        BitSet $$11 = new BitSet(3);
        ModelBlockRenderer.AmbientOcclusionFace $$12 = new ModelBlockRenderer.AmbientOcclusionFace();
        BlockPos.MutableBlockPos $$13 = blockPos3.mutable();
        for (Direction $$14 : DIRECTIONS) {
            randomSource7.setSeed(long8);
            List<BakedQuad> $$15 = bakedModel1.getQuads(blockState2, $$14, randomSource7);
            if (!$$15.isEmpty()) {
                $$13.setWithOffset(blockPos3, $$14);
                if (!boolean6 || Block.shouldRenderFace(blockState2, blockAndTintGetter0, blockPos3, $$14, $$13)) {
                    this.renderModelFaceAO(blockAndTintGetter0, blockState2, blockPos3, poseStack4, vertexConsumer5, $$15, $$10, $$11, $$12, int9);
                }
            }
        }
        randomSource7.setSeed(long8);
        List<BakedQuad> $$16 = bakedModel1.getQuads(blockState2, null, randomSource7);
        if (!$$16.isEmpty()) {
            this.renderModelFaceAO(blockAndTintGetter0, blockState2, blockPos3, poseStack4, vertexConsumer5, $$16, $$10, $$11, $$12, int9);
        }
    }

    public void tesselateWithoutAO(BlockAndTintGetter blockAndTintGetter0, BakedModel bakedModel1, BlockState blockState2, BlockPos blockPos3, PoseStack poseStack4, VertexConsumer vertexConsumer5, boolean boolean6, RandomSource randomSource7, long long8, int int9) {
        BitSet $$10 = new BitSet(3);
        BlockPos.MutableBlockPos $$11 = blockPos3.mutable();
        for (Direction $$12 : DIRECTIONS) {
            randomSource7.setSeed(long8);
            List<BakedQuad> $$13 = bakedModel1.getQuads(blockState2, $$12, randomSource7);
            if (!$$13.isEmpty()) {
                $$11.setWithOffset(blockPos3, $$12);
                if (!boolean6 || Block.shouldRenderFace(blockState2, blockAndTintGetter0, blockPos3, $$12, $$11)) {
                    int $$14 = LevelRenderer.getLightColor(blockAndTintGetter0, blockState2, $$11);
                    this.renderModelFaceFlat(blockAndTintGetter0, blockState2, blockPos3, $$14, int9, false, poseStack4, vertexConsumer5, $$13, $$10);
                }
            }
        }
        randomSource7.setSeed(long8);
        List<BakedQuad> $$15 = bakedModel1.getQuads(blockState2, null, randomSource7);
        if (!$$15.isEmpty()) {
            this.renderModelFaceFlat(blockAndTintGetter0, blockState2, blockPos3, -1, int9, true, poseStack4, vertexConsumer5, $$15, $$10);
        }
    }

    private void renderModelFaceAO(BlockAndTintGetter blockAndTintGetter0, BlockState blockState1, BlockPos blockPos2, PoseStack poseStack3, VertexConsumer vertexConsumer4, List<BakedQuad> listBakedQuad5, float[] float6, BitSet bitSet7, ModelBlockRenderer.AmbientOcclusionFace modelBlockRendererAmbientOcclusionFace8, int int9) {
        for (BakedQuad $$10 : listBakedQuad5) {
            this.calculateShape(blockAndTintGetter0, blockState1, blockPos2, $$10.getVertices(), $$10.getDirection(), float6, bitSet7);
            modelBlockRendererAmbientOcclusionFace8.calculate(blockAndTintGetter0, blockState1, blockPos2, $$10.getDirection(), float6, bitSet7, $$10.isShade());
            this.putQuadData(blockAndTintGetter0, blockState1, blockPos2, vertexConsumer4, poseStack3.last(), $$10, modelBlockRendererAmbientOcclusionFace8.brightness[0], modelBlockRendererAmbientOcclusionFace8.brightness[1], modelBlockRendererAmbientOcclusionFace8.brightness[2], modelBlockRendererAmbientOcclusionFace8.brightness[3], modelBlockRendererAmbientOcclusionFace8.lightmap[0], modelBlockRendererAmbientOcclusionFace8.lightmap[1], modelBlockRendererAmbientOcclusionFace8.lightmap[2], modelBlockRendererAmbientOcclusionFace8.lightmap[3], int9);
        }
    }

    private void putQuadData(BlockAndTintGetter blockAndTintGetter0, BlockState blockState1, BlockPos blockPos2, VertexConsumer vertexConsumer3, PoseStack.Pose poseStackPose4, BakedQuad bakedQuad5, float float6, float float7, float float8, float float9, int int10, int int11, int int12, int int13, int int14) {
        float $$16;
        float $$17;
        float $$18;
        if (bakedQuad5.isTinted()) {
            int $$15 = this.blockColors.getColor(blockState1, blockAndTintGetter0, blockPos2, bakedQuad5.getTintIndex());
            $$16 = (float) ($$15 >> 16 & 0xFF) / 255.0F;
            $$17 = (float) ($$15 >> 8 & 0xFF) / 255.0F;
            $$18 = (float) ($$15 & 0xFF) / 255.0F;
        } else {
            $$16 = 1.0F;
            $$17 = 1.0F;
            $$18 = 1.0F;
        }
        vertexConsumer3.putBulkData(poseStackPose4, bakedQuad5, new float[] { float6, float7, float8, float9 }, $$16, $$17, $$18, new int[] { int10, int11, int12, int13 }, int14, true);
    }

    private void calculateShape(BlockAndTintGetter blockAndTintGetter0, BlockState blockState1, BlockPos blockPos2, int[] int3, Direction direction4, @Nullable float[] float5, BitSet bitSet6) {
        float $$7 = 32.0F;
        float $$8 = 32.0F;
        float $$9 = 32.0F;
        float $$10 = -32.0F;
        float $$11 = -32.0F;
        float $$12 = -32.0F;
        for (int $$13 = 0; $$13 < 4; $$13++) {
            float $$14 = Float.intBitsToFloat(int3[$$13 * 8]);
            float $$15 = Float.intBitsToFloat(int3[$$13 * 8 + 1]);
            float $$16 = Float.intBitsToFloat(int3[$$13 * 8 + 2]);
            $$7 = Math.min($$7, $$14);
            $$8 = Math.min($$8, $$15);
            $$9 = Math.min($$9, $$16);
            $$10 = Math.max($$10, $$14);
            $$11 = Math.max($$11, $$15);
            $$12 = Math.max($$12, $$16);
        }
        if (float5 != null) {
            float5[Direction.WEST.get3DDataValue()] = $$7;
            float5[Direction.EAST.get3DDataValue()] = $$10;
            float5[Direction.DOWN.get3DDataValue()] = $$8;
            float5[Direction.UP.get3DDataValue()] = $$11;
            float5[Direction.NORTH.get3DDataValue()] = $$9;
            float5[Direction.SOUTH.get3DDataValue()] = $$12;
            int $$17 = DIRECTIONS.length;
            float5[Direction.WEST.get3DDataValue() + $$17] = 1.0F - $$7;
            float5[Direction.EAST.get3DDataValue() + $$17] = 1.0F - $$10;
            float5[Direction.DOWN.get3DDataValue() + $$17] = 1.0F - $$8;
            float5[Direction.UP.get3DDataValue() + $$17] = 1.0F - $$11;
            float5[Direction.NORTH.get3DDataValue() + $$17] = 1.0F - $$9;
            float5[Direction.SOUTH.get3DDataValue() + $$17] = 1.0F - $$12;
        }
        float $$18 = 1.0E-4F;
        float $$19 = 0.9999F;
        switch(direction4) {
            case DOWN:
                bitSet6.set(1, $$7 >= 1.0E-4F || $$9 >= 1.0E-4F || $$10 <= 0.9999F || $$12 <= 0.9999F);
                bitSet6.set(0, $$8 == $$11 && ($$8 < 1.0E-4F || blockState1.m_60838_(blockAndTintGetter0, blockPos2)));
                break;
            case UP:
                bitSet6.set(1, $$7 >= 1.0E-4F || $$9 >= 1.0E-4F || $$10 <= 0.9999F || $$12 <= 0.9999F);
                bitSet6.set(0, $$8 == $$11 && ($$11 > 0.9999F || blockState1.m_60838_(blockAndTintGetter0, blockPos2)));
                break;
            case NORTH:
                bitSet6.set(1, $$7 >= 1.0E-4F || $$8 >= 1.0E-4F || $$10 <= 0.9999F || $$11 <= 0.9999F);
                bitSet6.set(0, $$9 == $$12 && ($$9 < 1.0E-4F || blockState1.m_60838_(blockAndTintGetter0, blockPos2)));
                break;
            case SOUTH:
                bitSet6.set(1, $$7 >= 1.0E-4F || $$8 >= 1.0E-4F || $$10 <= 0.9999F || $$11 <= 0.9999F);
                bitSet6.set(0, $$9 == $$12 && ($$12 > 0.9999F || blockState1.m_60838_(blockAndTintGetter0, blockPos2)));
                break;
            case WEST:
                bitSet6.set(1, $$8 >= 1.0E-4F || $$9 >= 1.0E-4F || $$11 <= 0.9999F || $$12 <= 0.9999F);
                bitSet6.set(0, $$7 == $$10 && ($$7 < 1.0E-4F || blockState1.m_60838_(blockAndTintGetter0, blockPos2)));
                break;
            case EAST:
                bitSet6.set(1, $$8 >= 1.0E-4F || $$9 >= 1.0E-4F || $$11 <= 0.9999F || $$12 <= 0.9999F);
                bitSet6.set(0, $$7 == $$10 && ($$10 > 0.9999F || blockState1.m_60838_(blockAndTintGetter0, blockPos2)));
        }
    }

    private void renderModelFaceFlat(BlockAndTintGetter blockAndTintGetter0, BlockState blockState1, BlockPos blockPos2, int int3, int int4, boolean boolean5, PoseStack poseStack6, VertexConsumer vertexConsumer7, List<BakedQuad> listBakedQuad8, BitSet bitSet9) {
        for (BakedQuad $$10 : listBakedQuad8) {
            if (boolean5) {
                this.calculateShape(blockAndTintGetter0, blockState1, blockPos2, $$10.getVertices(), $$10.getDirection(), null, bitSet9);
                BlockPos $$11 = bitSet9.get(0) ? blockPos2.relative($$10.getDirection()) : blockPos2;
                int3 = LevelRenderer.getLightColor(blockAndTintGetter0, blockState1, $$11);
            }
            float $$12 = blockAndTintGetter0.getShade($$10.getDirection(), $$10.isShade());
            this.putQuadData(blockAndTintGetter0, blockState1, blockPos2, vertexConsumer7, poseStack6.last(), $$10, $$12, $$12, $$12, $$12, int3, int3, int3, int3, int4);
        }
    }

    public void renderModel(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, @Nullable BlockState blockState2, BakedModel bakedModel3, float float4, float float5, float float6, int int7, int int8) {
        RandomSource $$9 = RandomSource.create();
        long $$10 = 42L;
        for (Direction $$11 : DIRECTIONS) {
            $$9.setSeed(42L);
            renderQuadList(poseStackPose0, vertexConsumer1, float4, float5, float6, bakedModel3.getQuads(blockState2, $$11, $$9), int7, int8);
        }
        $$9.setSeed(42L);
        renderQuadList(poseStackPose0, vertexConsumer1, float4, float5, float6, bakedModel3.getQuads(blockState2, null, $$9), int7, int8);
    }

    private static void renderQuadList(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, float float2, float float3, float float4, List<BakedQuad> listBakedQuad5, int int6, int int7) {
        for (BakedQuad $$8 : listBakedQuad5) {
            float $$9;
            float $$10;
            float $$11;
            if ($$8.isTinted()) {
                $$9 = Mth.clamp(float2, 0.0F, 1.0F);
                $$10 = Mth.clamp(float3, 0.0F, 1.0F);
                $$11 = Mth.clamp(float4, 0.0F, 1.0F);
            } else {
                $$9 = 1.0F;
                $$10 = 1.0F;
                $$11 = 1.0F;
            }
            vertexConsumer1.putBulkData(poseStackPose0, $$8, $$9, $$10, $$11, int6, int7);
        }
    }

    public static void enableCaching() {
        ((ModelBlockRenderer.Cache) CACHE.get()).enable();
    }

    public static void clearCache() {
        ((ModelBlockRenderer.Cache) CACHE.get()).disable();
    }

    protected static enum AdjacencyInfo {

        DOWN(new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH }, 0.5F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.SOUTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.SOUTH }),
        UP(new Direction[] { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH }, 1.0F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.SOUTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.SOUTH }),
        NORTH(new Direction[] { Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST }, 0.8F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_WEST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_EAST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_EAST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_WEST }),
        SOUTH(new Direction[] { Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP }, 0.8F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.WEST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_WEST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.WEST, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.WEST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.EAST }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_EAST, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.EAST, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.EAST }),
        WEST(new Direction[] { Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH }, 0.6F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.SOUTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.SOUTH }),
        EAST(new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH }, 0.6F, true, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.SOUTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.FLIP_DOWN, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.DOWN, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.NORTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_NORTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.NORTH }, new ModelBlockRenderer.SizeInfo[] { ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.SOUTH, ModelBlockRenderer.SizeInfo.FLIP_UP, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.FLIP_SOUTH, ModelBlockRenderer.SizeInfo.UP, ModelBlockRenderer.SizeInfo.SOUTH });

        final Direction[] corners;

        final boolean doNonCubicWeight;

        final ModelBlockRenderer.SizeInfo[] vert0Weights;

        final ModelBlockRenderer.SizeInfo[] vert1Weights;

        final ModelBlockRenderer.SizeInfo[] vert2Weights;

        final ModelBlockRenderer.SizeInfo[] vert3Weights;

        private static final ModelBlockRenderer.AdjacencyInfo[] BY_FACING = Util.make(new ModelBlockRenderer.AdjacencyInfo[6], p_111134_ -> {
            p_111134_[Direction.DOWN.get3DDataValue()] = DOWN;
            p_111134_[Direction.UP.get3DDataValue()] = UP;
            p_111134_[Direction.NORTH.get3DDataValue()] = NORTH;
            p_111134_[Direction.SOUTH.get3DDataValue()] = SOUTH;
            p_111134_[Direction.WEST.get3DDataValue()] = WEST;
            p_111134_[Direction.EAST.get3DDataValue()] = EAST;
        });

        private AdjacencyInfo(Direction[] p_111122_, float p_111123_, boolean p_111124_, ModelBlockRenderer.SizeInfo[] p_111125_, ModelBlockRenderer.SizeInfo[] p_111126_, ModelBlockRenderer.SizeInfo[] p_111127_, ModelBlockRenderer.SizeInfo[] p_111128_) {
            this.corners = p_111122_;
            this.doNonCubicWeight = p_111124_;
            this.vert0Weights = p_111125_;
            this.vert1Weights = p_111126_;
            this.vert2Weights = p_111127_;
            this.vert3Weights = p_111128_;
        }

        public static ModelBlockRenderer.AdjacencyInfo fromFacing(Direction p_111132_) {
            return BY_FACING[p_111132_.get3DDataValue()];
        }
    }

    static class AmbientOcclusionFace {

        final float[] brightness = new float[4];

        final int[] lightmap = new int[4];

        public AmbientOcclusionFace() {
        }

        public void calculate(BlockAndTintGetter blockAndTintGetter0, BlockState blockState1, BlockPos blockPos2, Direction direction3, float[] float4, BitSet bitSet5, boolean boolean6) {
            BlockPos $$7 = bitSet5.get(0) ? blockPos2.relative(direction3) : blockPos2;
            ModelBlockRenderer.AdjacencyInfo $$8 = ModelBlockRenderer.AdjacencyInfo.fromFacing(direction3);
            BlockPos.MutableBlockPos $$9 = new BlockPos.MutableBlockPos();
            ModelBlockRenderer.Cache $$10 = (ModelBlockRenderer.Cache) ModelBlockRenderer.CACHE.get();
            $$9.setWithOffset($$7, $$8.corners[0]);
            BlockState $$11 = blockAndTintGetter0.m_8055_($$9);
            int $$12 = $$10.getLightColor($$11, blockAndTintGetter0, $$9);
            float $$13 = $$10.getShadeBrightness($$11, blockAndTintGetter0, $$9);
            $$9.setWithOffset($$7, $$8.corners[1]);
            BlockState $$14 = blockAndTintGetter0.m_8055_($$9);
            int $$15 = $$10.getLightColor($$14, blockAndTintGetter0, $$9);
            float $$16 = $$10.getShadeBrightness($$14, blockAndTintGetter0, $$9);
            $$9.setWithOffset($$7, $$8.corners[2]);
            BlockState $$17 = blockAndTintGetter0.m_8055_($$9);
            int $$18 = $$10.getLightColor($$17, blockAndTintGetter0, $$9);
            float $$19 = $$10.getShadeBrightness($$17, blockAndTintGetter0, $$9);
            $$9.setWithOffset($$7, $$8.corners[3]);
            BlockState $$20 = blockAndTintGetter0.m_8055_($$9);
            int $$21 = $$10.getLightColor($$20, blockAndTintGetter0, $$9);
            float $$22 = $$10.getShadeBrightness($$20, blockAndTintGetter0, $$9);
            BlockState $$23 = blockAndTintGetter0.m_8055_($$9.setWithOffset($$7, $$8.corners[0]).move(direction3));
            boolean $$24 = !$$23.m_60831_(blockAndTintGetter0, $$9) || $$23.m_60739_(blockAndTintGetter0, $$9) == 0;
            BlockState $$25 = blockAndTintGetter0.m_8055_($$9.setWithOffset($$7, $$8.corners[1]).move(direction3));
            boolean $$26 = !$$25.m_60831_(blockAndTintGetter0, $$9) || $$25.m_60739_(blockAndTintGetter0, $$9) == 0;
            BlockState $$27 = blockAndTintGetter0.m_8055_($$9.setWithOffset($$7, $$8.corners[2]).move(direction3));
            boolean $$28 = !$$27.m_60831_(blockAndTintGetter0, $$9) || $$27.m_60739_(blockAndTintGetter0, $$9) == 0;
            BlockState $$29 = blockAndTintGetter0.m_8055_($$9.setWithOffset($$7, $$8.corners[3]).move(direction3));
            boolean $$30 = !$$29.m_60831_(blockAndTintGetter0, $$9) || $$29.m_60739_(blockAndTintGetter0, $$9) == 0;
            float $$34;
            int $$35;
            if (!$$28 && !$$24) {
                $$34 = $$13;
                $$35 = $$12;
            } else {
                $$9.setWithOffset($$7, $$8.corners[0]).move($$8.corners[2]);
                BlockState $$31 = blockAndTintGetter0.m_8055_($$9);
                $$34 = $$10.getShadeBrightness($$31, blockAndTintGetter0, $$9);
                $$35 = $$10.getLightColor($$31, blockAndTintGetter0, $$9);
            }
            float $$39;
            int $$40;
            if (!$$30 && !$$24) {
                $$39 = $$13;
                $$40 = $$12;
            } else {
                $$9.setWithOffset($$7, $$8.corners[0]).move($$8.corners[3]);
                BlockState $$36 = blockAndTintGetter0.m_8055_($$9);
                $$39 = $$10.getShadeBrightness($$36, blockAndTintGetter0, $$9);
                $$40 = $$10.getLightColor($$36, blockAndTintGetter0, $$9);
            }
            float $$44;
            int $$45;
            if (!$$28 && !$$26) {
                $$44 = $$13;
                $$45 = $$12;
            } else {
                $$9.setWithOffset($$7, $$8.corners[1]).move($$8.corners[2]);
                BlockState $$41 = blockAndTintGetter0.m_8055_($$9);
                $$44 = $$10.getShadeBrightness($$41, blockAndTintGetter0, $$9);
                $$45 = $$10.getLightColor($$41, blockAndTintGetter0, $$9);
            }
            float $$49;
            int $$50;
            if (!$$30 && !$$26) {
                $$49 = $$13;
                $$50 = $$12;
            } else {
                $$9.setWithOffset($$7, $$8.corners[1]).move($$8.corners[3]);
                BlockState $$46 = blockAndTintGetter0.m_8055_($$9);
                $$49 = $$10.getShadeBrightness($$46, blockAndTintGetter0, $$9);
                $$50 = $$10.getLightColor($$46, blockAndTintGetter0, $$9);
            }
            int $$51 = $$10.getLightColor(blockState1, blockAndTintGetter0, blockPos2);
            $$9.setWithOffset(blockPos2, direction3);
            BlockState $$52 = blockAndTintGetter0.m_8055_($$9);
            if (bitSet5.get(0) || !$$52.m_60804_(blockAndTintGetter0, $$9)) {
                $$51 = $$10.getLightColor($$52, blockAndTintGetter0, $$9);
            }
            float $$53 = bitSet5.get(0) ? $$10.getShadeBrightness(blockAndTintGetter0.m_8055_($$7), blockAndTintGetter0, $$7) : $$10.getShadeBrightness(blockAndTintGetter0.m_8055_(blockPos2), blockAndTintGetter0, blockPos2);
            ModelBlockRenderer.AmbientVertexRemap $$54 = ModelBlockRenderer.AmbientVertexRemap.fromFacing(direction3);
            if (bitSet5.get(1) && $$8.doNonCubicWeight) {
                float $$59 = ($$22 + $$13 + $$39 + $$53) * 0.25F;
                float $$60 = ($$19 + $$13 + $$34 + $$53) * 0.25F;
                float $$61 = ($$19 + $$16 + $$44 + $$53) * 0.25F;
                float $$62 = ($$22 + $$16 + $$49 + $$53) * 0.25F;
                float $$63 = float4[$$8.vert0Weights[0].shape] * float4[$$8.vert0Weights[1].shape];
                float $$64 = float4[$$8.vert0Weights[2].shape] * float4[$$8.vert0Weights[3].shape];
                float $$65 = float4[$$8.vert0Weights[4].shape] * float4[$$8.vert0Weights[5].shape];
                float $$66 = float4[$$8.vert0Weights[6].shape] * float4[$$8.vert0Weights[7].shape];
                float $$67 = float4[$$8.vert1Weights[0].shape] * float4[$$8.vert1Weights[1].shape];
                float $$68 = float4[$$8.vert1Weights[2].shape] * float4[$$8.vert1Weights[3].shape];
                float $$69 = float4[$$8.vert1Weights[4].shape] * float4[$$8.vert1Weights[5].shape];
                float $$70 = float4[$$8.vert1Weights[6].shape] * float4[$$8.vert1Weights[7].shape];
                float $$71 = float4[$$8.vert2Weights[0].shape] * float4[$$8.vert2Weights[1].shape];
                float $$72 = float4[$$8.vert2Weights[2].shape] * float4[$$8.vert2Weights[3].shape];
                float $$73 = float4[$$8.vert2Weights[4].shape] * float4[$$8.vert2Weights[5].shape];
                float $$74 = float4[$$8.vert2Weights[6].shape] * float4[$$8.vert2Weights[7].shape];
                float $$75 = float4[$$8.vert3Weights[0].shape] * float4[$$8.vert3Weights[1].shape];
                float $$76 = float4[$$8.vert3Weights[2].shape] * float4[$$8.vert3Weights[3].shape];
                float $$77 = float4[$$8.vert3Weights[4].shape] * float4[$$8.vert3Weights[5].shape];
                float $$78 = float4[$$8.vert3Weights[6].shape] * float4[$$8.vert3Weights[7].shape];
                this.brightness[$$54.vert0] = $$59 * $$63 + $$60 * $$64 + $$61 * $$65 + $$62 * $$66;
                this.brightness[$$54.vert1] = $$59 * $$67 + $$60 * $$68 + $$61 * $$69 + $$62 * $$70;
                this.brightness[$$54.vert2] = $$59 * $$71 + $$60 * $$72 + $$61 * $$73 + $$62 * $$74;
                this.brightness[$$54.vert3] = $$59 * $$75 + $$60 * $$76 + $$61 * $$77 + $$62 * $$78;
                int $$79 = this.blend($$21, $$12, $$40, $$51);
                int $$80 = this.blend($$18, $$12, $$35, $$51);
                int $$81 = this.blend($$18, $$15, $$45, $$51);
                int $$82 = this.blend($$21, $$15, $$50, $$51);
                this.lightmap[$$54.vert0] = this.blend($$79, $$80, $$81, $$82, $$63, $$64, $$65, $$66);
                this.lightmap[$$54.vert1] = this.blend($$79, $$80, $$81, $$82, $$67, $$68, $$69, $$70);
                this.lightmap[$$54.vert2] = this.blend($$79, $$80, $$81, $$82, $$71, $$72, $$73, $$74);
                this.lightmap[$$54.vert3] = this.blend($$79, $$80, $$81, $$82, $$75, $$76, $$77, $$78);
            } else {
                float $$55 = ($$22 + $$13 + $$39 + $$53) * 0.25F;
                float $$56 = ($$19 + $$13 + $$34 + $$53) * 0.25F;
                float $$57 = ($$19 + $$16 + $$44 + $$53) * 0.25F;
                float $$58 = ($$22 + $$16 + $$49 + $$53) * 0.25F;
                this.lightmap[$$54.vert0] = this.blend($$21, $$12, $$40, $$51);
                this.lightmap[$$54.vert1] = this.blend($$18, $$12, $$35, $$51);
                this.lightmap[$$54.vert2] = this.blend($$18, $$15, $$45, $$51);
                this.lightmap[$$54.vert3] = this.blend($$21, $$15, $$50, $$51);
                this.brightness[$$54.vert0] = $$55;
                this.brightness[$$54.vert1] = $$56;
                this.brightness[$$54.vert2] = $$57;
                this.brightness[$$54.vert3] = $$58;
            }
            float $$83 = blockAndTintGetter0.getShade(direction3, boolean6);
            for (int $$84 = 0; $$84 < this.brightness.length; $$84++) {
                this.brightness[$$84] = this.brightness[$$84] * $$83;
            }
        }

        private int blend(int int0, int int1, int int2, int int3) {
            if (int0 == 0) {
                int0 = int3;
            }
            if (int1 == 0) {
                int1 = int3;
            }
            if (int2 == 0) {
                int2 = int3;
            }
            return int0 + int1 + int2 + int3 >> 2 & 16711935;
        }

        private int blend(int int0, int int1, int int2, int int3, float float4, float float5, float float6, float float7) {
            int $$8 = (int) ((float) (int0 >> 16 & 0xFF) * float4 + (float) (int1 >> 16 & 0xFF) * float5 + (float) (int2 >> 16 & 0xFF) * float6 + (float) (int3 >> 16 & 0xFF) * float7) & 0xFF;
            int $$9 = (int) ((float) (int0 & 0xFF) * float4 + (float) (int1 & 0xFF) * float5 + (float) (int2 & 0xFF) * float6 + (float) (int3 & 0xFF) * float7) & 0xFF;
            return $$8 << 16 | $$9;
        }
    }

    static enum AmbientVertexRemap {

        DOWN(0, 1, 2, 3),
        UP(2, 3, 0, 1),
        NORTH(3, 0, 1, 2),
        SOUTH(0, 1, 2, 3),
        WEST(3, 0, 1, 2),
        EAST(1, 2, 3, 0);

        final int vert0;

        final int vert1;

        final int vert2;

        final int vert3;

        private static final ModelBlockRenderer.AmbientVertexRemap[] BY_FACING = Util.make(new ModelBlockRenderer.AmbientVertexRemap[6], p_111204_ -> {
            p_111204_[Direction.DOWN.get3DDataValue()] = DOWN;
            p_111204_[Direction.UP.get3DDataValue()] = UP;
            p_111204_[Direction.NORTH.get3DDataValue()] = NORTH;
            p_111204_[Direction.SOUTH.get3DDataValue()] = SOUTH;
            p_111204_[Direction.WEST.get3DDataValue()] = WEST;
            p_111204_[Direction.EAST.get3DDataValue()] = EAST;
        });

        private AmbientVertexRemap(int p_111195_, int p_111196_, int p_111197_, int p_111198_) {
            this.vert0 = p_111195_;
            this.vert1 = p_111196_;
            this.vert2 = p_111197_;
            this.vert3 = p_111198_;
        }

        public static ModelBlockRenderer.AmbientVertexRemap fromFacing(Direction p_111202_) {
            return BY_FACING[p_111202_.get3DDataValue()];
        }
    }

    static class Cache {

        private boolean enabled;

        private final Long2IntLinkedOpenHashMap colorCache = Util.make(() -> {
            Long2IntLinkedOpenHashMap $$0 = new Long2IntLinkedOpenHashMap(100, 0.25F) {

                protected void rehash(int p_111238_) {
                }
            };
            $$0.defaultReturnValue(Integer.MAX_VALUE);
            return $$0;
        });

        private final Long2FloatLinkedOpenHashMap brightnessCache = Util.make(() -> {
            Long2FloatLinkedOpenHashMap $$0 = new Long2FloatLinkedOpenHashMap(100, 0.25F) {

                protected void rehash(int p_111245_) {
                }
            };
            $$0.defaultReturnValue(Float.NaN);
            return $$0;
        });

        private Cache() {
        }

        public void enable() {
            this.enabled = true;
        }

        public void disable() {
            this.enabled = false;
            this.colorCache.clear();
            this.brightnessCache.clear();
        }

        public int getLightColor(BlockState blockState0, BlockAndTintGetter blockAndTintGetter1, BlockPos blockPos2) {
            long $$3 = blockPos2.asLong();
            if (this.enabled) {
                int $$4 = this.colorCache.get($$3);
                if ($$4 != Integer.MAX_VALUE) {
                    return $$4;
                }
            }
            int $$5 = LevelRenderer.getLightColor(blockAndTintGetter1, blockState0, blockPos2);
            if (this.enabled) {
                if (this.colorCache.size() == 100) {
                    this.colorCache.removeFirstInt();
                }
                this.colorCache.put($$3, $$5);
            }
            return $$5;
        }

        public float getShadeBrightness(BlockState blockState0, BlockAndTintGetter blockAndTintGetter1, BlockPos blockPos2) {
            long $$3 = blockPos2.asLong();
            if (this.enabled) {
                float $$4 = this.brightnessCache.get($$3);
                if (!Float.isNaN($$4)) {
                    return $$4;
                }
            }
            float $$5 = blockState0.m_60792_(blockAndTintGetter1, blockPos2);
            if (this.enabled) {
                if (this.brightnessCache.size() == 100) {
                    this.brightnessCache.removeFirstFloat();
                }
                this.brightnessCache.put($$3, $$5);
            }
            return $$5;
        }
    }

    protected static enum SizeInfo {

        DOWN(Direction.DOWN, false),
        UP(Direction.UP, false),
        NORTH(Direction.NORTH, false),
        SOUTH(Direction.SOUTH, false),
        WEST(Direction.WEST, false),
        EAST(Direction.EAST, false),
        FLIP_DOWN(Direction.DOWN, true),
        FLIP_UP(Direction.UP, true),
        FLIP_NORTH(Direction.NORTH, true),
        FLIP_SOUTH(Direction.SOUTH, true),
        FLIP_WEST(Direction.WEST, true),
        FLIP_EAST(Direction.EAST, true);

        final int shape;

        private SizeInfo(Direction p_111264_, boolean p_111265_) {
            this.shape = p_111264_.get3DDataValue() + (p_111265_ ? ModelBlockRenderer.DIRECTIONS.length : 0);
        }
    }
}