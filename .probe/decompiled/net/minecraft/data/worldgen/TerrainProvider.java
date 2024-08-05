package net.minecraft.data.worldgen;

import net.minecraft.util.CubicSpline;
import net.minecraft.util.Mth;
import net.minecraft.util.ToFloatFunction;
import net.minecraft.world.level.levelgen.NoiseRouterData;

public class TerrainProvider {

    private static final float DEEP_OCEAN_CONTINENTALNESS = -0.51F;

    private static final float OCEAN_CONTINENTALNESS = -0.4F;

    private static final float PLAINS_CONTINENTALNESS = 0.1F;

    private static final float BEACH_CONTINENTALNESS = -0.15F;

    private static final ToFloatFunction<Float> NO_TRANSFORM = ToFloatFunction.IDENTITY;

    private static final ToFloatFunction<Float> AMPLIFIED_OFFSET = ToFloatFunction.createUnlimited(p_236651_ -> p_236651_ < 0.0F ? p_236651_ : p_236651_ * 2.0F);

    private static final ToFloatFunction<Float> AMPLIFIED_FACTOR = ToFloatFunction.createUnlimited(p_236649_ -> 1.25F - 6.25F / (p_236649_ + 5.0F));

    private static final ToFloatFunction<Float> AMPLIFIED_JAGGEDNESS = ToFloatFunction.createUnlimited(p_236641_ -> p_236641_ * 2.0F);

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> overworldOffset(I i0, I i1, I i2, boolean boolean3) {
        ToFloatFunction<Float> $$4 = boolean3 ? AMPLIFIED_OFFSET : NO_TRANSFORM;
        CubicSpline<C, I> $$5 = buildErosionOffsetSpline(i1, i2, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, $$4);
        CubicSpline<C, I> $$6 = buildErosionOffsetSpline(i1, i2, -0.1F, 0.03F, 0.1F, 0.1F, 0.01F, -0.03F, false, false, $$4);
        CubicSpline<C, I> $$7 = buildErosionOffsetSpline(i1, i2, -0.1F, 0.03F, 0.1F, 0.7F, 0.01F, -0.03F, true, true, $$4);
        CubicSpline<C, I> $$8 = buildErosionOffsetSpline(i1, i2, -0.05F, 0.03F, 0.1F, 1.0F, 0.01F, 0.01F, true, true, $$4);
        return CubicSpline.<C, I>builder(i0, $$4).addPoint(-1.1F, 0.044F).addPoint(-1.02F, -0.2222F).addPoint(-0.51F, -0.2222F).addPoint(-0.44F, -0.12F).addPoint(-0.18F, -0.12F).addPoint(-0.16F, $$5).addPoint(-0.15F, $$5).addPoint(-0.1F, $$6).addPoint(0.25F, $$7).addPoint(1.0F, $$8).build();
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> overworldFactor(I i0, I i1, I i2, I i3, boolean boolean4) {
        ToFloatFunction<Float> $$5 = boolean4 ? AMPLIFIED_FACTOR : NO_TRANSFORM;
        return CubicSpline.<C, I>builder(i0, NO_TRANSFORM).addPoint(-0.19F, 3.95F).addPoint(-0.15F, getErosionFactor(i1, i2, i3, 6.25F, true, NO_TRANSFORM)).addPoint(-0.1F, getErosionFactor(i1, i2, i3, 5.47F, true, $$5)).addPoint(0.03F, getErosionFactor(i1, i2, i3, 5.08F, true, $$5)).addPoint(0.06F, getErosionFactor(i1, i2, i3, 4.69F, false, $$5)).build();
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> overworldJaggedness(I i0, I i1, I i2, I i3, boolean boolean4) {
        ToFloatFunction<Float> $$5 = boolean4 ? AMPLIFIED_JAGGEDNESS : NO_TRANSFORM;
        float $$6 = 0.65F;
        return CubicSpline.<C, I>builder(i0, $$5).addPoint(-0.11F, 0.0F).addPoint(0.03F, buildErosionJaggednessSpline(i1, i2, i3, 1.0F, 0.5F, 0.0F, 0.0F, $$5)).addPoint(0.65F, buildErosionJaggednessSpline(i1, i2, i3, 1.0F, 1.0F, 1.0F, 0.0F, $$5)).build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildErosionJaggednessSpline(I i0, I i1, I i2, float float3, float float4, float float5, float float6, ToFloatFunction<Float> toFloatFunctionFloat7) {
        float $$8 = -0.5775F;
        CubicSpline<C, I> $$9 = buildRidgeJaggednessSpline(i1, i2, float3, float5, toFloatFunctionFloat7);
        CubicSpline<C, I> $$10 = buildRidgeJaggednessSpline(i1, i2, float4, float6, toFloatFunctionFloat7);
        return CubicSpline.<C, I>builder(i0, toFloatFunctionFloat7).addPoint(-1.0F, $$9).addPoint(-0.78F, $$10).addPoint(-0.5775F, $$10).addPoint(-0.375F, 0.0F).build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildRidgeJaggednessSpline(I i0, I i1, float float2, float float3, ToFloatFunction<Float> toFloatFunctionFloat4) {
        float $$5 = NoiseRouterData.peaksAndValleys(0.4F);
        float $$6 = NoiseRouterData.peaksAndValleys(0.56666666F);
        float $$7 = ($$5 + $$6) / 2.0F;
        CubicSpline.Builder<C, I> $$8 = CubicSpline.builder(i1, toFloatFunctionFloat4);
        $$8.addPoint($$5, 0.0F);
        if (float3 > 0.0F) {
            $$8.addPoint($$7, buildWeirdnessJaggednessSpline(i0, float3, toFloatFunctionFloat4));
        } else {
            $$8.addPoint($$7, 0.0F);
        }
        if (float2 > 0.0F) {
            $$8.addPoint(1.0F, buildWeirdnessJaggednessSpline(i0, float2, toFloatFunctionFloat4));
        } else {
            $$8.addPoint(1.0F, 0.0F);
        }
        return $$8.build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildWeirdnessJaggednessSpline(I i0, float float1, ToFloatFunction<Float> toFloatFunctionFloat2) {
        float $$3 = 0.63F * float1;
        float $$4 = 0.3F * float1;
        return CubicSpline.<C, I>builder(i0, toFloatFunctionFloat2).addPoint(-0.01F, $$3).addPoint(0.01F, $$4).build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> getErosionFactor(I i0, I i1, I i2, float float3, boolean boolean4, ToFloatFunction<Float> toFloatFunctionFloat5) {
        CubicSpline<C, I> $$6 = CubicSpline.<C, I>builder(i1, toFloatFunctionFloat5).addPoint(-0.2F, 6.3F).addPoint(0.2F, float3).build();
        CubicSpline.Builder<C, I> $$7 = CubicSpline.<C, I>builder(i0, toFloatFunctionFloat5).addPoint(-0.6F, $$6).addPoint(-0.5F, CubicSpline.<C, I>builder(i1, toFloatFunctionFloat5).addPoint(-0.05F, 6.3F).addPoint(0.05F, 2.67F).build()).addPoint(-0.35F, $$6).addPoint(-0.25F, $$6).addPoint(-0.1F, CubicSpline.<C, I>builder(i1, toFloatFunctionFloat5).addPoint(-0.05F, 2.67F).addPoint(0.05F, 6.3F).build()).addPoint(0.03F, $$6);
        if (boolean4) {
            CubicSpline<C, I> $$8 = CubicSpline.<C, I>builder(i1, toFloatFunctionFloat5).addPoint(0.0F, float3).addPoint(0.1F, 0.625F).build();
            CubicSpline<C, I> $$9 = CubicSpline.<C, I>builder(i2, toFloatFunctionFloat5).addPoint(-0.9F, float3).addPoint(-0.69F, $$8).build();
            $$7.addPoint(0.35F, float3).addPoint(0.45F, $$9).addPoint(0.55F, $$9).addPoint(0.62F, float3);
        } else {
            CubicSpline<C, I> $$10 = CubicSpline.<C, I>builder(i2, toFloatFunctionFloat5).addPoint(-0.7F, $$6).addPoint(-0.15F, 1.37F).build();
            CubicSpline<C, I> $$11 = CubicSpline.<C, I>builder(i2, toFloatFunctionFloat5).addPoint(0.45F, $$6).addPoint(0.7F, 1.56F).build();
            $$7.addPoint(0.05F, $$11).addPoint(0.4F, $$11).addPoint(0.45F, $$10).addPoint(0.55F, $$10).addPoint(0.58F, float3);
        }
        return $$7.build();
    }

    private static float calculateSlope(float float0, float float1, float float2, float float3) {
        return (float1 - float0) / (float3 - float2);
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildMountainRidgeSplineWithPoints(I i0, float float1, boolean boolean2, ToFloatFunction<Float> toFloatFunctionFloat3) {
        CubicSpline.Builder<C, I> $$4 = CubicSpline.builder(i0, toFloatFunctionFloat3);
        float $$5 = -0.7F;
        float $$6 = -1.0F;
        float $$7 = mountainContinentalness(-1.0F, float1, -0.7F);
        float $$8 = 1.0F;
        float $$9 = mountainContinentalness(1.0F, float1, -0.7F);
        float $$10 = calculateMountainRidgeZeroContinentalnessPoint(float1);
        float $$11 = -0.65F;
        if (-0.65F < $$10 && $$10 < 1.0F) {
            float $$12 = mountainContinentalness(-0.65F, float1, -0.7F);
            float $$13 = -0.75F;
            float $$14 = mountainContinentalness(-0.75F, float1, -0.7F);
            float $$15 = calculateSlope($$7, $$14, -1.0F, -0.75F);
            $$4.addPoint(-1.0F, $$7, $$15);
            $$4.addPoint(-0.75F, $$14);
            $$4.addPoint(-0.65F, $$12);
            float $$16 = mountainContinentalness($$10, float1, -0.7F);
            float $$17 = calculateSlope($$16, $$9, $$10, 1.0F);
            float $$18 = 0.01F;
            $$4.addPoint($$10 - 0.01F, $$16);
            $$4.addPoint($$10, $$16, $$17);
            $$4.addPoint(1.0F, $$9, $$17);
        } else {
            float $$19 = calculateSlope($$7, $$9, -1.0F, 1.0F);
            if (boolean2) {
                $$4.addPoint(-1.0F, Math.max(0.2F, $$7));
                $$4.addPoint(0.0F, Mth.lerp(0.5F, $$7, $$9), $$19);
            } else {
                $$4.addPoint(-1.0F, $$7, $$19);
            }
            $$4.addPoint(1.0F, $$9, $$19);
        }
        return $$4.build();
    }

    private static float mountainContinentalness(float float0, float float1, float float2) {
        float $$3 = 1.17F;
        float $$4 = 0.46082947F;
        float $$5 = 1.0F - (1.0F - float1) * 0.5F;
        float $$6 = 0.5F * (1.0F - float1);
        float $$7 = (float0 + 1.17F) * 0.46082947F;
        float $$8 = $$7 * $$5 - $$6;
        return float0 < float2 ? Math.max($$8, -0.2222F) : Math.max($$8, 0.0F);
    }

    private static float calculateMountainRidgeZeroContinentalnessPoint(float float0) {
        float $$1 = 1.17F;
        float $$2 = 0.46082947F;
        float $$3 = 1.0F - (1.0F - float0) * 0.5F;
        float $$4 = 0.5F * (1.0F - float0);
        return $$4 / (0.46082947F * $$3) - 1.17F;
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> buildErosionOffsetSpline(I i0, I i1, float float2, float float3, float float4, float float5, float float6, float float7, boolean boolean8, boolean boolean9, ToFloatFunction<Float> toFloatFunctionFloat10) {
        float $$11 = 0.6F;
        float $$12 = 0.5F;
        float $$13 = 0.5F;
        CubicSpline<C, I> $$14 = buildMountainRidgeSplineWithPoints(i1, Mth.lerp(float5, 0.6F, 1.5F), boolean9, toFloatFunctionFloat10);
        CubicSpline<C, I> $$15 = buildMountainRidgeSplineWithPoints(i1, Mth.lerp(float5, 0.6F, 1.0F), boolean9, toFloatFunctionFloat10);
        CubicSpline<C, I> $$16 = buildMountainRidgeSplineWithPoints(i1, float5, boolean9, toFloatFunctionFloat10);
        CubicSpline<C, I> $$17 = ridgeSpline(i1, float2 - 0.15F, 0.5F * float5, Mth.lerp(0.5F, 0.5F, 0.5F) * float5, 0.5F * float5, 0.6F * float5, 0.5F, toFloatFunctionFloat10);
        CubicSpline<C, I> $$18 = ridgeSpline(i1, float2, float6 * float5, float3 * float5, 0.5F * float5, 0.6F * float5, 0.5F, toFloatFunctionFloat10);
        CubicSpline<C, I> $$19 = ridgeSpline(i1, float2, float6, float6, float3, float4, 0.5F, toFloatFunctionFloat10);
        CubicSpline<C, I> $$20 = ridgeSpline(i1, float2, float6, float6, float3, float4, 0.5F, toFloatFunctionFloat10);
        CubicSpline<C, I> $$21 = CubicSpline.<C, I>builder(i1, toFloatFunctionFloat10).addPoint(-1.0F, float2).addPoint(-0.4F, $$19).addPoint(0.0F, float4 + 0.07F).build();
        CubicSpline<C, I> $$22 = ridgeSpline(i1, -0.02F, float7, float7, float3, float4, 0.0F, toFloatFunctionFloat10);
        CubicSpline.Builder<C, I> $$23 = CubicSpline.<C, I>builder(i0, toFloatFunctionFloat10).addPoint(-0.85F, $$14).addPoint(-0.7F, $$15).addPoint(-0.4F, $$16).addPoint(-0.35F, $$17).addPoint(-0.1F, $$18).addPoint(0.2F, $$19);
        if (boolean8) {
            $$23.addPoint(0.4F, $$20).addPoint(0.45F, $$21).addPoint(0.55F, $$21).addPoint(0.58F, $$20);
        }
        $$23.addPoint(0.7F, $$22);
        return $$23.build();
    }

    private static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> ridgeSpline(I i0, float float1, float float2, float float3, float float4, float float5, float float6, ToFloatFunction<Float> toFloatFunctionFloat7) {
        float $$8 = Math.max(0.5F * (float2 - float1), float6);
        float $$9 = 5.0F * (float3 - float2);
        return CubicSpline.<C, I>builder(i0, toFloatFunctionFloat7).addPoint(-1.0F, float1, $$8).addPoint(-0.4F, float2, Math.min($$8, $$9)).addPoint(0.0F, float3, $$9).addPoint(0.4F, float4, 2.0F * (float4 - float3)).addPoint(1.0F, float5, 0.7F * (float5 - float4)).build();
    }
}