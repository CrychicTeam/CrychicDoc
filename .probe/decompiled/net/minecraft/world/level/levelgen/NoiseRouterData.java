package net.minecraft.world.level.levelgen;

import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoiseRouterData {

    public static final float GLOBAL_OFFSET = -0.50375F;

    private static final float ORE_THICKNESS = 0.08F;

    private static final double VEININESS_FREQUENCY = 1.5;

    private static final double NOODLE_SPACING_AND_STRAIGHTNESS = 1.5;

    private static final double SURFACE_DENSITY_THRESHOLD = 1.5625;

    private static final double CHEESE_NOISE_TARGET = -0.703125;

    public static final int ISLAND_CHUNK_DISTANCE = 64;

    public static final long ISLAND_CHUNK_DISTANCE_SQR = 4096L;

    private static final DensityFunction BLENDING_FACTOR = DensityFunctions.constant(10.0);

    private static final DensityFunction BLENDING_JAGGEDNESS = DensityFunctions.zero();

    private static final ResourceKey<DensityFunction> ZERO = createKey("zero");

    private static final ResourceKey<DensityFunction> Y = createKey("y");

    private static final ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");

    private static final ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");

    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_OVERWORLD = createKey("overworld/base_3d_noise");

    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_NETHER = createKey("nether/base_3d_noise");

    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_END = createKey("end/base_3d_noise");

    public static final ResourceKey<DensityFunction> CONTINENTS = createKey("overworld/continents");

    public static final ResourceKey<DensityFunction> EROSION = createKey("overworld/erosion");

    public static final ResourceKey<DensityFunction> RIDGES = createKey("overworld/ridges");

    public static final ResourceKey<DensityFunction> RIDGES_FOLDED = createKey("overworld/ridges_folded");

    public static final ResourceKey<DensityFunction> OFFSET = createKey("overworld/offset");

    public static final ResourceKey<DensityFunction> FACTOR = createKey("overworld/factor");

    public static final ResourceKey<DensityFunction> JAGGEDNESS = createKey("overworld/jaggedness");

    public static final ResourceKey<DensityFunction> DEPTH = createKey("overworld/depth");

    private static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("overworld/sloped_cheese");

    public static final ResourceKey<DensityFunction> CONTINENTS_LARGE = createKey("overworld_large_biomes/continents");

    public static final ResourceKey<DensityFunction> EROSION_LARGE = createKey("overworld_large_biomes/erosion");

    private static final ResourceKey<DensityFunction> OFFSET_LARGE = createKey("overworld_large_biomes/offset");

    private static final ResourceKey<DensityFunction> FACTOR_LARGE = createKey("overworld_large_biomes/factor");

    private static final ResourceKey<DensityFunction> JAGGEDNESS_LARGE = createKey("overworld_large_biomes/jaggedness");

    private static final ResourceKey<DensityFunction> DEPTH_LARGE = createKey("overworld_large_biomes/depth");

    private static final ResourceKey<DensityFunction> SLOPED_CHEESE_LARGE = createKey("overworld_large_biomes/sloped_cheese");

    private static final ResourceKey<DensityFunction> OFFSET_AMPLIFIED = createKey("overworld_amplified/offset");

    private static final ResourceKey<DensityFunction> FACTOR_AMPLIFIED = createKey("overworld_amplified/factor");

    private static final ResourceKey<DensityFunction> JAGGEDNESS_AMPLIFIED = createKey("overworld_amplified/jaggedness");

    private static final ResourceKey<DensityFunction> DEPTH_AMPLIFIED = createKey("overworld_amplified/depth");

    private static final ResourceKey<DensityFunction> SLOPED_CHEESE_AMPLIFIED = createKey("overworld_amplified/sloped_cheese");

    private static final ResourceKey<DensityFunction> SLOPED_CHEESE_END = createKey("end/sloped_cheese");

    private static final ResourceKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = createKey("overworld/caves/spaghetti_roughness_function");

    private static final ResourceKey<DensityFunction> ENTRANCES = createKey("overworld/caves/entrances");

    private static final ResourceKey<DensityFunction> NOODLE = createKey("overworld/caves/noodle");

    private static final ResourceKey<DensityFunction> PILLARS = createKey("overworld/caves/pillars");

    private static final ResourceKey<DensityFunction> SPAGHETTI_2D_THICKNESS_MODULATOR = createKey("overworld/caves/spaghetti_2d_thickness_modulator");

    private static final ResourceKey<DensityFunction> SPAGHETTI_2D = createKey("overworld/caves/spaghetti_2d");

    private static ResourceKey<DensityFunction> createKey(String string0) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(string0));
    }

    public static Holder<? extends DensityFunction> bootstrap(BootstapContext<DensityFunction> bootstapContextDensityFunction0) {
        HolderGetter<NormalNoise.NoiseParameters> $$1 = bootstapContextDensityFunction0.lookup(Registries.NOISE);
        HolderGetter<DensityFunction> $$2 = bootstapContextDensityFunction0.lookup(Registries.DENSITY_FUNCTION);
        bootstapContextDensityFunction0.register(ZERO, DensityFunctions.zero());
        int $$3 = DimensionType.MIN_Y * 2;
        int $$4 = DimensionType.MAX_Y * 2;
        bootstapContextDensityFunction0.register(Y, DensityFunctions.yClampedGradient($$3, $$4, (double) $$3, (double) $$4));
        DensityFunction $$5 = registerAndWrap(bootstapContextDensityFunction0, SHIFT_X, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA($$1.getOrThrow(Noises.SHIFT)))));
        DensityFunction $$6 = registerAndWrap(bootstapContextDensityFunction0, SHIFT_Z, DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB($$1.getOrThrow(Noises.SHIFT)))));
        bootstapContextDensityFunction0.register(BASE_3D_NOISE_OVERWORLD, BlendedNoise.createUnseeded(0.25, 0.125, 80.0, 160.0, 8.0));
        bootstapContextDensityFunction0.register(BASE_3D_NOISE_NETHER, BlendedNoise.createUnseeded(0.25, 0.375, 80.0, 60.0, 8.0));
        bootstapContextDensityFunction0.register(BASE_3D_NOISE_END, BlendedNoise.createUnseeded(0.25, 0.25, 80.0, 160.0, 4.0));
        Holder<DensityFunction> $$7 = bootstapContextDensityFunction0.register(CONTINENTS, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d($$5, $$6, 0.25, $$1.getOrThrow(Noises.CONTINENTALNESS))));
        Holder<DensityFunction> $$8 = bootstapContextDensityFunction0.register(EROSION, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d($$5, $$6, 0.25, $$1.getOrThrow(Noises.EROSION))));
        DensityFunction $$9 = registerAndWrap(bootstapContextDensityFunction0, RIDGES, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d($$5, $$6, 0.25, $$1.getOrThrow(Noises.RIDGE))));
        bootstapContextDensityFunction0.register(RIDGES_FOLDED, peaksAndValleys($$9));
        DensityFunction $$10 = DensityFunctions.noise($$1.getOrThrow(Noises.JAGGED), 1500.0, 0.0);
        registerTerrainNoises(bootstapContextDensityFunction0, $$2, $$10, $$7, $$8, OFFSET, FACTOR, JAGGEDNESS, DEPTH, SLOPED_CHEESE, false);
        Holder<DensityFunction> $$11 = bootstapContextDensityFunction0.register(CONTINENTS_LARGE, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d($$5, $$6, 0.25, $$1.getOrThrow(Noises.CONTINENTALNESS_LARGE))));
        Holder<DensityFunction> $$12 = bootstapContextDensityFunction0.register(EROSION_LARGE, DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d($$5, $$6, 0.25, $$1.getOrThrow(Noises.EROSION_LARGE))));
        registerTerrainNoises(bootstapContextDensityFunction0, $$2, $$10, $$11, $$12, OFFSET_LARGE, FACTOR_LARGE, JAGGEDNESS_LARGE, DEPTH_LARGE, SLOPED_CHEESE_LARGE, false);
        registerTerrainNoises(bootstapContextDensityFunction0, $$2, $$10, $$7, $$8, OFFSET_AMPLIFIED, FACTOR_AMPLIFIED, JAGGEDNESS_AMPLIFIED, DEPTH_AMPLIFIED, SLOPED_CHEESE_AMPLIFIED, true);
        bootstapContextDensityFunction0.register(SLOPED_CHEESE_END, DensityFunctions.add(DensityFunctions.endIslands(0L), getFunction($$2, BASE_3D_NOISE_END)));
        bootstapContextDensityFunction0.register(SPAGHETTI_ROUGHNESS_FUNCTION, spaghettiRoughnessFunction($$1));
        bootstapContextDensityFunction0.register(SPAGHETTI_2D_THICKNESS_MODULATOR, DensityFunctions.cacheOnce(DensityFunctions.mappedNoise($$1.getOrThrow(Noises.SPAGHETTI_2D_THICKNESS), 2.0, 1.0, -0.6, -1.3)));
        bootstapContextDensityFunction0.register(SPAGHETTI_2D, spaghetti2D($$2, $$1));
        bootstapContextDensityFunction0.register(ENTRANCES, entrances($$2, $$1));
        bootstapContextDensityFunction0.register(NOODLE, noodle($$2, $$1));
        return bootstapContextDensityFunction0.register(PILLARS, pillars($$1));
    }

    private static void registerTerrainNoises(BootstapContext<DensityFunction> bootstapContextDensityFunction0, HolderGetter<DensityFunction> holderGetterDensityFunction1, DensityFunction densityFunction2, Holder<DensityFunction> holderDensityFunction3, Holder<DensityFunction> holderDensityFunction4, ResourceKey<DensityFunction> resourceKeyDensityFunction5, ResourceKey<DensityFunction> resourceKeyDensityFunction6, ResourceKey<DensityFunction> resourceKeyDensityFunction7, ResourceKey<DensityFunction> resourceKeyDensityFunction8, ResourceKey<DensityFunction> resourceKeyDensityFunction9, boolean boolean10) {
        DensityFunctions.Spline.Coordinate $$11 = new DensityFunctions.Spline.Coordinate(holderDensityFunction3);
        DensityFunctions.Spline.Coordinate $$12 = new DensityFunctions.Spline.Coordinate(holderDensityFunction4);
        DensityFunctions.Spline.Coordinate $$13 = new DensityFunctions.Spline.Coordinate(holderGetterDensityFunction1.getOrThrow(RIDGES));
        DensityFunctions.Spline.Coordinate $$14 = new DensityFunctions.Spline.Coordinate(holderGetterDensityFunction1.getOrThrow(RIDGES_FOLDED));
        DensityFunction $$15 = registerAndWrap(bootstapContextDensityFunction0, resourceKeyDensityFunction5, splineWithBlending(DensityFunctions.add(DensityFunctions.constant(-0.50375F), DensityFunctions.spline(TerrainProvider.overworldOffset($$11, $$12, $$14, boolean10))), DensityFunctions.blendOffset()));
        DensityFunction $$16 = registerAndWrap(bootstapContextDensityFunction0, resourceKeyDensityFunction6, splineWithBlending(DensityFunctions.spline(TerrainProvider.overworldFactor($$11, $$12, $$13, $$14, boolean10)), BLENDING_FACTOR));
        DensityFunction $$17 = registerAndWrap(bootstapContextDensityFunction0, resourceKeyDensityFunction8, DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5), $$15));
        DensityFunction $$18 = registerAndWrap(bootstapContextDensityFunction0, resourceKeyDensityFunction7, splineWithBlending(DensityFunctions.spline(TerrainProvider.overworldJaggedness($$11, $$12, $$13, $$14, boolean10)), BLENDING_JAGGEDNESS));
        DensityFunction $$19 = DensityFunctions.mul($$18, densityFunction2.halfNegative());
        DensityFunction $$20 = noiseGradientDensity($$16, DensityFunctions.add($$17, $$19));
        bootstapContextDensityFunction0.register(resourceKeyDensityFunction9, DensityFunctions.add($$20, getFunction(holderGetterDensityFunction1, BASE_3D_NOISE_OVERWORLD)));
    }

    private static DensityFunction registerAndWrap(BootstapContext<DensityFunction> bootstapContextDensityFunction0, ResourceKey<DensityFunction> resourceKeyDensityFunction1, DensityFunction densityFunction2) {
        return new DensityFunctions.HolderHolder(bootstapContextDensityFunction0.register(resourceKeyDensityFunction1, densityFunction2));
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> holderGetterDensityFunction0, ResourceKey<DensityFunction> resourceKeyDensityFunction1) {
        return new DensityFunctions.HolderHolder(holderGetterDensityFunction0.getOrThrow(resourceKeyDensityFunction1));
    }

    private static DensityFunction peaksAndValleys(DensityFunction densityFunction0) {
        return DensityFunctions.mul(DensityFunctions.add(DensityFunctions.add(densityFunction0.abs(), DensityFunctions.constant(-0.6666666666666666)).abs(), DensityFunctions.constant(-0.3333333333333333)), DensityFunctions.constant(-3.0));
    }

    public static float peaksAndValleys(float float0) {
        return -(Math.abs(Math.abs(float0) - 0.6666667F) - 0.33333334F) * 3.0F;
    }

    private static DensityFunction spaghettiRoughnessFunction(HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters0) {
        DensityFunction $$1 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters0.getOrThrow(Noises.SPAGHETTI_ROUGHNESS));
        DensityFunction $$2 = DensityFunctions.mappedNoise(holderGetterNormalNoiseNoiseParameters0.getOrThrow(Noises.SPAGHETTI_ROUGHNESS_MODULATOR), 0.0, -0.1);
        return DensityFunctions.cacheOnce(DensityFunctions.mul($$2, DensityFunctions.add($$1.abs(), DensityFunctions.constant(-0.4))));
    }

    private static DensityFunction entrances(HolderGetter<DensityFunction> holderGetterDensityFunction0, HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters1) {
        DensityFunction $$2 = DensityFunctions.cacheOnce(DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.SPAGHETTI_3D_RARITY), 2.0, 1.0));
        DensityFunction $$3 = DensityFunctions.mappedNoise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.SPAGHETTI_3D_THICKNESS), -0.065, -0.088);
        DensityFunction $$4 = DensityFunctions.weirdScaledSampler($$2, holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.SPAGHETTI_3D_1), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE1);
        DensityFunction $$5 = DensityFunctions.weirdScaledSampler($$2, holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.SPAGHETTI_3D_2), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE1);
        DensityFunction $$6 = DensityFunctions.add(DensityFunctions.max($$4, $$5), $$3).clamp(-1.0, 1.0);
        DensityFunction $$7 = getFunction(holderGetterDensityFunction0, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction $$8 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.CAVE_ENTRANCE), 0.75, 0.5);
        DensityFunction $$9 = DensityFunctions.add(DensityFunctions.add($$8, DensityFunctions.constant(0.37)), DensityFunctions.yClampedGradient(-10, 30, 0.3, 0.0));
        return DensityFunctions.cacheOnce(DensityFunctions.min($$9, DensityFunctions.add($$7, $$6)));
    }

    private static DensityFunction noodle(HolderGetter<DensityFunction> holderGetterDensityFunction0, HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters1) {
        DensityFunction $$2 = getFunction(holderGetterDensityFunction0, Y);
        int $$3 = -64;
        int $$4 = -60;
        int $$5 = 320;
        DensityFunction $$6 = yLimitedInterpolatable($$2, DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.NOODLE), 1.0, 1.0), -60, 320, -1);
        DensityFunction $$7 = yLimitedInterpolatable($$2, DensityFunctions.mappedNoise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.NOODLE_THICKNESS), 1.0, 1.0, -0.05, -0.1), -60, 320, 0);
        double $$8 = 2.6666666666666665;
        DensityFunction $$9 = yLimitedInterpolatable($$2, DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.NOODLE_RIDGE_A), 2.6666666666666665, 2.6666666666666665), -60, 320, 0);
        DensityFunction $$10 = yLimitedInterpolatable($$2, DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.NOODLE_RIDGE_B), 2.6666666666666665, 2.6666666666666665), -60, 320, 0);
        DensityFunction $$11 = DensityFunctions.mul(DensityFunctions.constant(1.5), DensityFunctions.max($$9.abs(), $$10.abs()));
        return DensityFunctions.rangeChoice($$6, -1000000.0, 0.0, DensityFunctions.constant(64.0), DensityFunctions.add($$7, $$11));
    }

    private static DensityFunction pillars(HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters0) {
        double $$1 = 25.0;
        double $$2 = 0.3;
        DensityFunction $$3 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters0.getOrThrow(Noises.PILLAR), 25.0, 0.3);
        DensityFunction $$4 = DensityFunctions.mappedNoise(holderGetterNormalNoiseNoiseParameters0.getOrThrow(Noises.PILLAR_RARENESS), 0.0, -2.0);
        DensityFunction $$5 = DensityFunctions.mappedNoise(holderGetterNormalNoiseNoiseParameters0.getOrThrow(Noises.PILLAR_THICKNESS), 0.0, 1.1);
        DensityFunction $$6 = DensityFunctions.add(DensityFunctions.mul($$3, DensityFunctions.constant(2.0)), $$4);
        return DensityFunctions.cacheOnce(DensityFunctions.mul($$6, $$5.cube()));
    }

    private static DensityFunction spaghetti2D(HolderGetter<DensityFunction> holderGetterDensityFunction0, HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters1) {
        DensityFunction $$2 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.SPAGHETTI_2D_MODULATOR), 2.0, 1.0);
        DensityFunction $$3 = DensityFunctions.weirdScaledSampler($$2, holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.SPAGHETTI_2D), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE2);
        DensityFunction $$4 = DensityFunctions.mappedNoise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.SPAGHETTI_2D_ELEVATION), 0.0, (double) Math.floorDiv(-64, 8), 8.0);
        DensityFunction $$5 = getFunction(holderGetterDensityFunction0, SPAGHETTI_2D_THICKNESS_MODULATOR);
        DensityFunction $$6 = DensityFunctions.add($$4, DensityFunctions.yClampedGradient(-64, 320, 8.0, -40.0)).abs();
        DensityFunction $$7 = DensityFunctions.add($$6, $$5).cube();
        double $$8 = 0.083;
        DensityFunction $$9 = DensityFunctions.add($$3, DensityFunctions.mul(DensityFunctions.constant(0.083), $$5));
        return DensityFunctions.max($$9, $$7).clamp(-1.0, 1.0);
    }

    private static DensityFunction underground(HolderGetter<DensityFunction> holderGetterDensityFunction0, HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters1, DensityFunction densityFunction2) {
        DensityFunction $$3 = getFunction(holderGetterDensityFunction0, SPAGHETTI_2D);
        DensityFunction $$4 = getFunction(holderGetterDensityFunction0, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction $$5 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.CAVE_LAYER), 8.0);
        DensityFunction $$6 = DensityFunctions.mul(DensityFunctions.constant(4.0), $$5.square());
        DensityFunction $$7 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.CAVE_CHEESE), 0.6666666666666666);
        DensityFunction $$8 = DensityFunctions.add(DensityFunctions.add(DensityFunctions.constant(0.27), $$7).clamp(-1.0, 1.0), DensityFunctions.add(DensityFunctions.constant(1.5), DensityFunctions.mul(DensityFunctions.constant(-0.64), densityFunction2)).clamp(0.0, 0.5));
        DensityFunction $$9 = DensityFunctions.add($$6, $$8);
        DensityFunction $$10 = DensityFunctions.min(DensityFunctions.min($$9, getFunction(holderGetterDensityFunction0, ENTRANCES)), DensityFunctions.add($$3, $$4));
        DensityFunction $$11 = getFunction(holderGetterDensityFunction0, PILLARS);
        DensityFunction $$12 = DensityFunctions.rangeChoice($$11, -1000000.0, 0.03, DensityFunctions.constant(-1000000.0), $$11);
        return DensityFunctions.max($$10, $$12);
    }

    private static DensityFunction postProcess(DensityFunction densityFunction0) {
        DensityFunction $$1 = DensityFunctions.blendDensity(densityFunction0);
        return DensityFunctions.mul(DensityFunctions.interpolated($$1), DensityFunctions.constant(0.64)).squeeze();
    }

    protected static NoiseRouter overworld(HolderGetter<DensityFunction> holderGetterDensityFunction0, HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters1, boolean boolean2, boolean boolean3) {
        DensityFunction $$4 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.AQUIFER_BARRIER), 0.5);
        DensityFunction $$5 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction $$6 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        DensityFunction $$7 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.AQUIFER_LAVA));
        DensityFunction $$8 = getFunction(holderGetterDensityFunction0, SHIFT_X);
        DensityFunction $$9 = getFunction(holderGetterDensityFunction0, SHIFT_Z);
        DensityFunction $$10 = DensityFunctions.shiftedNoise2d($$8, $$9, 0.25, holderGetterNormalNoiseNoiseParameters1.getOrThrow(boolean2 ? Noises.TEMPERATURE_LARGE : Noises.TEMPERATURE));
        DensityFunction $$11 = DensityFunctions.shiftedNoise2d($$8, $$9, 0.25, holderGetterNormalNoiseNoiseParameters1.getOrThrow(boolean2 ? Noises.VEGETATION_LARGE : Noises.VEGETATION));
        DensityFunction $$12 = getFunction(holderGetterDensityFunction0, boolean2 ? FACTOR_LARGE : (boolean3 ? FACTOR_AMPLIFIED : FACTOR));
        DensityFunction $$13 = getFunction(holderGetterDensityFunction0, boolean2 ? DEPTH_LARGE : (boolean3 ? DEPTH_AMPLIFIED : DEPTH));
        DensityFunction $$14 = noiseGradientDensity(DensityFunctions.cache2d($$12), $$13);
        DensityFunction $$15 = getFunction(holderGetterDensityFunction0, boolean2 ? SLOPED_CHEESE_LARGE : (boolean3 ? SLOPED_CHEESE_AMPLIFIED : SLOPED_CHEESE));
        DensityFunction $$16 = DensityFunctions.min($$15, DensityFunctions.mul(DensityFunctions.constant(5.0), getFunction(holderGetterDensityFunction0, ENTRANCES)));
        DensityFunction $$17 = DensityFunctions.rangeChoice($$15, -1000000.0, 1.5625, $$16, underground(holderGetterDensityFunction0, holderGetterNormalNoiseNoiseParameters1, $$15));
        DensityFunction $$18 = DensityFunctions.min(postProcess(slideOverworld(boolean3, $$17)), getFunction(holderGetterDensityFunction0, NOODLE));
        DensityFunction $$19 = getFunction(holderGetterDensityFunction0, Y);
        int $$20 = Stream.of(OreVeinifier.VeinType.values()).mapToInt(p_224495_ -> p_224495_.minY).min().orElse(-DimensionType.MIN_Y * 2);
        int $$21 = Stream.of(OreVeinifier.VeinType.values()).mapToInt(p_224457_ -> p_224457_.maxY).max().orElse(-DimensionType.MIN_Y * 2);
        DensityFunction $$22 = yLimitedInterpolatable($$19, DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.ORE_VEININESS), 1.5, 1.5), $$20, $$21, 0);
        float $$23 = 4.0F;
        DensityFunction $$24 = yLimitedInterpolatable($$19, DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.ORE_VEIN_A), 4.0, 4.0), $$20, $$21, 0).abs();
        DensityFunction $$25 = yLimitedInterpolatable($$19, DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.ORE_VEIN_B), 4.0, 4.0), $$20, $$21, 0).abs();
        DensityFunction $$26 = DensityFunctions.add(DensityFunctions.constant(-0.08F), DensityFunctions.max($$24, $$25));
        DensityFunction $$27 = DensityFunctions.noise(holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.ORE_GAP));
        return new NoiseRouter($$4, $$5, $$6, $$7, $$10, $$11, getFunction(holderGetterDensityFunction0, boolean2 ? CONTINENTS_LARGE : CONTINENTS), getFunction(holderGetterDensityFunction0, boolean2 ? EROSION_LARGE : EROSION), $$13, getFunction(holderGetterDensityFunction0, RIDGES), slideOverworld(boolean3, DensityFunctions.add($$14, DensityFunctions.constant(-0.703125)).clamp(-64.0, 64.0)), $$18, $$22, $$26, $$27);
    }

    private static NoiseRouter noNewCaves(HolderGetter<DensityFunction> holderGetterDensityFunction0, HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters1, DensityFunction densityFunction2) {
        DensityFunction $$3 = getFunction(holderGetterDensityFunction0, SHIFT_X);
        DensityFunction $$4 = getFunction(holderGetterDensityFunction0, SHIFT_Z);
        DensityFunction $$5 = DensityFunctions.shiftedNoise2d($$3, $$4, 0.25, holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.TEMPERATURE));
        DensityFunction $$6 = DensityFunctions.shiftedNoise2d($$3, $$4, 0.25, holderGetterNormalNoiseNoiseParameters1.getOrThrow(Noises.VEGETATION));
        DensityFunction $$7 = postProcess(densityFunction2);
        return new NoiseRouter(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), $$5, $$6, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), $$7, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    private static DensityFunction slideOverworld(boolean boolean0, DensityFunction densityFunction1) {
        return slide(densityFunction1, -64, 384, boolean0 ? 16 : 80, boolean0 ? 0 : 64, -0.078125, 0, 24, boolean0 ? 0.4 : 0.1171875);
    }

    private static DensityFunction slideNetherLike(HolderGetter<DensityFunction> holderGetterDensityFunction0, int int1, int int2) {
        return slide(getFunction(holderGetterDensityFunction0, BASE_3D_NOISE_NETHER), int1, int2, 24, 0, 0.9375, -8, 24, 2.5);
    }

    private static DensityFunction slideEndLike(DensityFunction densityFunction0, int int1, int int2) {
        return slide(densityFunction0, int1, int2, 72, -184, -23.4375, 4, 32, -0.234375);
    }

    protected static NoiseRouter nether(HolderGetter<DensityFunction> holderGetterDensityFunction0, HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters1) {
        return noNewCaves(holderGetterDensityFunction0, holderGetterNormalNoiseNoiseParameters1, slideNetherLike(holderGetterDensityFunction0, 0, 128));
    }

    protected static NoiseRouter caves(HolderGetter<DensityFunction> holderGetterDensityFunction0, HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters1) {
        return noNewCaves(holderGetterDensityFunction0, holderGetterNormalNoiseNoiseParameters1, slideNetherLike(holderGetterDensityFunction0, -64, 192));
    }

    protected static NoiseRouter floatingIslands(HolderGetter<DensityFunction> holderGetterDensityFunction0, HolderGetter<NormalNoise.NoiseParameters> holderGetterNormalNoiseNoiseParameters1) {
        return noNewCaves(holderGetterDensityFunction0, holderGetterNormalNoiseNoiseParameters1, slideEndLike(getFunction(holderGetterDensityFunction0, BASE_3D_NOISE_END), 0, 256));
    }

    private static DensityFunction slideEnd(DensityFunction densityFunction0) {
        return slideEndLike(densityFunction0, 0, 128);
    }

    protected static NoiseRouter end(HolderGetter<DensityFunction> holderGetterDensityFunction0) {
        DensityFunction $$1 = DensityFunctions.cache2d(DensityFunctions.endIslands(0L));
        DensityFunction $$2 = postProcess(slideEnd(getFunction(holderGetterDensityFunction0, SLOPED_CHEESE_END)));
        return new NoiseRouter(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), $$1, DensityFunctions.zero(), DensityFunctions.zero(), slideEnd(DensityFunctions.add($$1, DensityFunctions.constant(-0.703125))), $$2, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    protected static NoiseRouter none() {
        return new NoiseRouter(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    private static DensityFunction splineWithBlending(DensityFunction densityFunction0, DensityFunction densityFunction1) {
        DensityFunction $$2 = DensityFunctions.lerp(DensityFunctions.blendAlpha(), densityFunction1, densityFunction0);
        return DensityFunctions.flatCache(DensityFunctions.cache2d($$2));
    }

    private static DensityFunction noiseGradientDensity(DensityFunction densityFunction0, DensityFunction densityFunction1) {
        DensityFunction $$2 = DensityFunctions.mul(densityFunction1, densityFunction0);
        return DensityFunctions.mul(DensityFunctions.constant(4.0), $$2.quarterNegative());
    }

    private static DensityFunction yLimitedInterpolatable(DensityFunction densityFunction0, DensityFunction densityFunction1, int int2, int int3, int int4) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(densityFunction0, (double) int2, (double) (int3 + 1), densityFunction1, DensityFunctions.constant((double) int4)));
    }

    private static DensityFunction slide(DensityFunction densityFunction0, int int1, int int2, int int3, int int4, double double5, int int6, int int7, double double8) {
        DensityFunction $$10 = DensityFunctions.yClampedGradient(int1 + int2 - int3, int1 + int2 - int4, 1.0, 0.0);
        DensityFunction $$9 = DensityFunctions.lerp($$10, double5, densityFunction0);
        DensityFunction $$11 = DensityFunctions.yClampedGradient(int1 + int6, int1 + int7, 0.0, 1.0);
        return DensityFunctions.lerp($$11, double8, $$9);
    }

    protected static final class QuantizedSpaghettiRarity {

        protected static double getSphaghettiRarity2D(double double0) {
            if (double0 < -0.75) {
                return 0.5;
            } else if (double0 < -0.5) {
                return 0.75;
            } else if (double0 < 0.5) {
                return 1.0;
            } else {
                return double0 < 0.75 ? 2.0 : 3.0;
            }
        }

        protected static double getSpaghettiRarity3D(double double0) {
            if (double0 < -0.5) {
                return 0.75;
            } else if (double0 < 0.0) {
                return 1.0;
            } else {
                return double0 < 0.5 ? 1.5 : 2.0;
            }
        }
    }
}