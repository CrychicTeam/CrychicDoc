package net.minecraft.world.level.chunk;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class ChunkStatus {

    public static final int MAX_STRUCTURE_DISTANCE = 8;

    private static final EnumSet<Heightmap.Types> PRE_FEATURES = EnumSet.of(Heightmap.Types.OCEAN_FLOOR_WG, Heightmap.Types.WORLD_SURFACE_WG);

    public static final EnumSet<Heightmap.Types> POST_FEATURES = EnumSet.of(Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE, Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);

    private static final ChunkStatus.LoadingTask PASSTHROUGH_LOAD_TASK = (p_281194_, p_281195_, p_281196_, p_281197_, p_281198_, p_281199_) -> CompletableFuture.completedFuture(Either.left(p_281199_));

    public static final ChunkStatus EMPTY = registerSimple("empty", null, -1, PRE_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_156307_, p_156308_, p_156309_, p_156310_, p_156311_) -> {
    });

    public static final ChunkStatus STRUCTURE_STARTS = register("structure_starts", EMPTY, 0, false, PRE_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_289514_, p_289515_, p_289516_, p_289517_, p_289518_, p_289519_, p_289520_, p_289521_, p_289522_) -> {
        if (p_289516_.getServer().getWorldData().worldGenOptions().generateStructures()) {
            p_289517_.createStructures(p_289516_.m_9598_(), p_289516_.getChunkSource().getGeneratorState(), p_289516_.structureManager(), p_289522_, p_289518_);
        }
        p_289516_.onStructureStartsAvailable(p_289522_);
        return CompletableFuture.completedFuture(Either.left(p_289522_));
    }, (p_281209_, p_281210_, p_281211_, p_281212_, p_281213_, p_281214_) -> {
        p_281210_.onStructureStartsAvailable(p_281214_);
        return CompletableFuture.completedFuture(Either.left(p_281214_));
    });

    public static final ChunkStatus STRUCTURE_REFERENCES = registerSimple("structure_references", STRUCTURE_STARTS, 8, PRE_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_196843_, p_196844_, p_196845_, p_196846_, p_196847_) -> {
        WorldGenRegion $$5 = new WorldGenRegion(p_196844_, p_196846_, p_196843_, -1);
        p_196845_.createReferences($$5, p_196844_.structureManager().forWorldGenRegion($$5), p_196847_);
    });

    public static final ChunkStatus BIOMES = register("biomes", STRUCTURE_REFERENCES, 8, PRE_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_281200_, p_281201_, p_281202_, p_281203_, p_281204_, p_281205_, p_281206_, p_281207_, p_281208_) -> {
        WorldGenRegion $$9 = new WorldGenRegion(p_281202_, p_281207_, p_281200_, -1);
        return p_281203_.createBiomes(p_281201_, p_281202_.getChunkSource().randomState(), Blender.of($$9), p_281202_.structureManager().forWorldGenRegion($$9), p_281208_).thenApply(p_281193_ -> Either.left(p_281193_));
    });

    public static final ChunkStatus NOISE = register("noise", BIOMES, 8, PRE_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_281161_, p_281162_, p_281163_, p_281164_, p_281165_, p_281166_, p_281167_, p_281168_, p_281169_) -> {
        WorldGenRegion $$9 = new WorldGenRegion(p_281163_, p_281168_, p_281161_, 0);
        return p_281164_.fillFromNoise(p_281162_, Blender.of($$9), p_281163_.getChunkSource().randomState(), p_281163_.structureManager().forWorldGenRegion($$9), p_281169_).thenApply(p_281218_ -> {
            if (p_281218_ instanceof ProtoChunk $$1) {
                BelowZeroRetrogen $$2 = $$1.getBelowZeroRetrogen();
                if ($$2 != null) {
                    BelowZeroRetrogen.replaceOldBedrock($$1);
                    if ($$2.hasBedrockHoles()) {
                        $$2.applyBedrockMask($$1);
                    }
                }
            }
            return Either.left(p_281218_);
        });
    });

    public static final ChunkStatus SURFACE = registerSimple("surface", NOISE, 8, PRE_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_156247_, p_156248_, p_156249_, p_156250_, p_156251_) -> {
        WorldGenRegion $$5 = new WorldGenRegion(p_156248_, p_156250_, p_156247_, 0);
        p_156249_.buildSurface($$5, p_156248_.structureManager().forWorldGenRegion($$5), p_156248_.getChunkSource().randomState(), p_156251_);
    });

    public static final ChunkStatus CARVERS = registerSimple("carvers", SURFACE, 8, POST_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_289523_, p_289524_, p_289525_, p_289526_, p_289527_) -> {
        WorldGenRegion $$5 = new WorldGenRegion(p_289524_, p_289526_, p_289523_, 0);
        if (p_289527_ instanceof ProtoChunk $$6) {
            Blender.addAroundOldChunksCarvingMaskFilter($$5, $$6);
        }
        p_289525_.applyCarvers($$5, p_289524_.getSeed(), p_289524_.getChunkSource().randomState(), p_289524_.m_7062_(), p_289524_.structureManager().forWorldGenRegion($$5), p_289527_, GenerationStep.Carving.AIR);
    });

    public static final ChunkStatus FEATURES = registerSimple("features", CARVERS, 8, POST_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_281188_, p_281189_, p_281190_, p_281191_, p_281192_) -> {
        Heightmap.primeHeightmaps(p_281192_, EnumSet.of(Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE));
        WorldGenRegion $$5 = new WorldGenRegion(p_281189_, p_281191_, p_281188_, 1);
        p_281190_.applyBiomeDecoration($$5, p_281192_, p_281189_.structureManager().forWorldGenRegion($$5));
        Blender.generateBorderTicks($$5, p_281192_);
    });

    public static final ChunkStatus INITIALIZE_LIGHT = register("initialize_light", FEATURES, 0, false, POST_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_281179_, p_281180_, p_281181_, p_281182_, p_281183_, p_281184_, p_281185_, p_281186_, p_281187_) -> initializeLight(p_281184_, p_281187_), (p_281155_, p_281156_, p_281157_, p_281158_, p_281159_, p_281160_) -> initializeLight(p_281158_, p_281160_));

    public static final ChunkStatus LIGHT = register("light", INITIALIZE_LIGHT, 1, true, POST_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_284904_, p_284905_, p_284906_, p_284907_, p_284908_, p_284909_, p_284910_, p_284911_, p_284912_) -> lightChunk(p_284909_, p_284912_), (p_284898_, p_284899_, p_284900_, p_284901_, p_284902_, p_284903_) -> lightChunk(p_284901_, p_284903_));

    public static final ChunkStatus SPAWN = registerSimple("spawn", LIGHT, 0, POST_FEATURES, ChunkStatus.ChunkType.PROTOCHUNK, (p_196758_, p_196759_, p_196760_, p_196761_, p_196762_) -> {
        if (!p_196762_.isUpgrading()) {
            p_196760_.spawnOriginalMobs(new WorldGenRegion(p_196759_, p_196761_, p_196758_, -1));
        }
    });

    public static final ChunkStatus FULL = register("full", SPAWN, 0, false, POST_FEATURES, ChunkStatus.ChunkType.LEVELCHUNK, (p_223267_, p_223268_, p_223269_, p_223270_, p_223271_, p_223272_, p_223273_, p_223274_, p_223275_) -> (CompletableFuture) p_223273_.apply(p_223275_), (p_223260_, p_223261_, p_223262_, p_223263_, p_223264_, p_223265_) -> (CompletableFuture) p_223264_.apply(p_223265_));

    private static final List<ChunkStatus> STATUS_BY_RANGE = ImmutableList.of(FULL, INITIALIZE_LIGHT, CARVERS, BIOMES, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, new ChunkStatus[0]);

    private static final IntList RANGE_BY_STATUS = Util.make(new IntArrayList(getStatusList().size()), p_283066_ -> {
        int $$1 = 0;
        for (int $$2 = getStatusList().size() - 1; $$2 >= 0; $$2--) {
            while ($$1 + 1 < STATUS_BY_RANGE.size() && $$2 <= ((ChunkStatus) STATUS_BY_RANGE.get($$1 + 1)).getIndex()) {
                $$1++;
            }
            p_283066_.add(0, $$1);
        }
    });

    private final int index;

    private final ChunkStatus parent;

    private final ChunkStatus.GenerationTask generationTask;

    private final ChunkStatus.LoadingTask loadingTask;

    private final int range;

    private final boolean hasLoadDependencies;

    private final ChunkStatus.ChunkType chunkType;

    private final EnumSet<Heightmap.Types> heightmapsAfter;

    private static CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> initializeLight(ThreadedLevelLightEngine threadedLevelLightEngine0, ChunkAccess chunkAccess1) {
        chunkAccess1.initializeLightSources();
        ((ProtoChunk) chunkAccess1).setLightEngine(threadedLevelLightEngine0);
        boolean $$2 = isLighted(chunkAccess1);
        return threadedLevelLightEngine0.initializeLight(chunkAccess1, $$2).thenApply(Either::left);
    }

    private static CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> lightChunk(ThreadedLevelLightEngine threadedLevelLightEngine0, ChunkAccess chunkAccess1) {
        boolean $$2 = isLighted(chunkAccess1);
        return threadedLevelLightEngine0.lightChunk(chunkAccess1, $$2).thenApply(Either::left);
    }

    private static ChunkStatus registerSimple(String string0, @Nullable ChunkStatus chunkStatus1, int int2, EnumSet<Heightmap.Types> enumSetHeightmapTypes3, ChunkStatus.ChunkType chunkStatusChunkType4, ChunkStatus.SimpleGenerationTask chunkStatusSimpleGenerationTask5) {
        return register(string0, chunkStatus1, int2, enumSetHeightmapTypes3, chunkStatusChunkType4, chunkStatusSimpleGenerationTask5);
    }

    private static ChunkStatus register(String string0, @Nullable ChunkStatus chunkStatus1, int int2, EnumSet<Heightmap.Types> enumSetHeightmapTypes3, ChunkStatus.ChunkType chunkStatusChunkType4, ChunkStatus.GenerationTask chunkStatusGenerationTask5) {
        return register(string0, chunkStatus1, int2, false, enumSetHeightmapTypes3, chunkStatusChunkType4, chunkStatusGenerationTask5, PASSTHROUGH_LOAD_TASK);
    }

    private static ChunkStatus register(String string0, @Nullable ChunkStatus chunkStatus1, int int2, boolean boolean3, EnumSet<Heightmap.Types> enumSetHeightmapTypes4, ChunkStatus.ChunkType chunkStatusChunkType5, ChunkStatus.GenerationTask chunkStatusGenerationTask6, ChunkStatus.LoadingTask chunkStatusLoadingTask7) {
        return Registry.register(BuiltInRegistries.CHUNK_STATUS, string0, new ChunkStatus(chunkStatus1, int2, boolean3, enumSetHeightmapTypes4, chunkStatusChunkType5, chunkStatusGenerationTask6, chunkStatusLoadingTask7));
    }

    public static List<ChunkStatus> getStatusList() {
        List<ChunkStatus> $$0 = Lists.newArrayList();
        ChunkStatus $$1;
        for ($$1 = FULL; $$1.getParent() != $$1; $$1 = $$1.getParent()) {
            $$0.add($$1);
        }
        $$0.add($$1);
        Collections.reverse($$0);
        return $$0;
    }

    private static boolean isLighted(ChunkAccess chunkAccess0) {
        return chunkAccess0.getStatus().isOrAfter(LIGHT) && chunkAccess0.isLightCorrect();
    }

    public static ChunkStatus getStatusAroundFullChunk(int int0) {
        if (int0 >= STATUS_BY_RANGE.size()) {
            return EMPTY;
        } else {
            return int0 < 0 ? FULL : (ChunkStatus) STATUS_BY_RANGE.get(int0);
        }
    }

    public static int maxDistance() {
        return STATUS_BY_RANGE.size();
    }

    public static int getDistance(ChunkStatus chunkStatus0) {
        return RANGE_BY_STATUS.getInt(chunkStatus0.getIndex());
    }

    ChunkStatus(@Nullable ChunkStatus chunkStatus0, int int1, boolean boolean2, EnumSet<Heightmap.Types> enumSetHeightmapTypes3, ChunkStatus.ChunkType chunkStatusChunkType4, ChunkStatus.GenerationTask chunkStatusGenerationTask5, ChunkStatus.LoadingTask chunkStatusLoadingTask6) {
        this.parent = chunkStatus0 == null ? this : chunkStatus0;
        this.generationTask = chunkStatusGenerationTask5;
        this.loadingTask = chunkStatusLoadingTask6;
        this.range = int1;
        this.hasLoadDependencies = boolean2;
        this.chunkType = chunkStatusChunkType4;
        this.heightmapsAfter = enumSetHeightmapTypes3;
        this.index = chunkStatus0 == null ? 0 : chunkStatus0.getIndex() + 1;
    }

    public int getIndex() {
        return this.index;
    }

    public ChunkStatus getParent() {
        return this.parent;
    }

    public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> generate(Executor executor0, ServerLevel serverLevel1, ChunkGenerator chunkGenerator2, StructureTemplateManager structureTemplateManager3, ThreadedLevelLightEngine threadedLevelLightEngine4, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> functionChunkAccessCompletableFutureEitherChunkAccessChunkHolderChunkLoadingFailure5, List<ChunkAccess> listChunkAccess6) {
        ChunkAccess $$7 = (ChunkAccess) listChunkAccess6.get(listChunkAccess6.size() / 2);
        ProfiledDuration $$8 = JvmProfiler.INSTANCE.onChunkGenerate($$7.getPos(), serverLevel1.m_46472_(), this.toString());
        return this.generationTask.doWork(this, executor0, serverLevel1, chunkGenerator2, structureTemplateManager3, threadedLevelLightEngine4, functionChunkAccessCompletableFutureEitherChunkAccessChunkHolderChunkLoadingFailure5, listChunkAccess6, $$7).thenApply(p_281217_ -> {
            p_281217_.ifLeft(p_290029_ -> {
                if (p_290029_ instanceof ProtoChunk $$1 && !$$1.getStatus().isOrAfter(this)) {
                    $$1.setStatus(this);
                }
            });
            if ($$8 != null) {
                $$8.finish();
            }
            return p_281217_;
        });
    }

    public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> load(ServerLevel serverLevel0, StructureTemplateManager structureTemplateManager1, ThreadedLevelLightEngine threadedLevelLightEngine2, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> functionChunkAccessCompletableFutureEitherChunkAccessChunkHolderChunkLoadingFailure3, ChunkAccess chunkAccess4) {
        return this.loadingTask.doWork(this, serverLevel0, structureTemplateManager1, threadedLevelLightEngine2, functionChunkAccessCompletableFutureEitherChunkAccessChunkHolderChunkLoadingFailure3, chunkAccess4);
    }

    public int getRange() {
        return this.range;
    }

    public boolean hasLoadDependencies() {
        return this.hasLoadDependencies;
    }

    public ChunkStatus.ChunkType getChunkType() {
        return this.chunkType;
    }

    public static ChunkStatus byName(String string0) {
        return BuiltInRegistries.CHUNK_STATUS.get(ResourceLocation.tryParse(string0));
    }

    public EnumSet<Heightmap.Types> heightmapsAfter() {
        return this.heightmapsAfter;
    }

    public boolean isOrAfter(ChunkStatus chunkStatus0) {
        return this.getIndex() >= chunkStatus0.getIndex();
    }

    public String toString() {
        return BuiltInRegistries.CHUNK_STATUS.getKey(this).toString();
    }

    public static enum ChunkType {

        PROTOCHUNK, LEVELCHUNK
    }

    interface GenerationTask {

        CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(ChunkStatus var1, Executor var2, ServerLevel var3, ChunkGenerator var4, StructureTemplateManager var5, ThreadedLevelLightEngine var6, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> var7, List<ChunkAccess> var8, ChunkAccess var9);
    }

    interface LoadingTask {

        CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(ChunkStatus var1, ServerLevel var2, StructureTemplateManager var3, ThreadedLevelLightEngine var4, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> var5, ChunkAccess var6);
    }

    interface SimpleGenerationTask extends ChunkStatus.GenerationTask {

        @Override
        default CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(ChunkStatus chunkStatus0, Executor executor1, ServerLevel serverLevel2, ChunkGenerator chunkGenerator3, StructureTemplateManager structureTemplateManager4, ThreadedLevelLightEngine threadedLevelLightEngine5, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> functionChunkAccessCompletableFutureEitherChunkAccessChunkHolderChunkLoadingFailure6, List<ChunkAccess> listChunkAccess7, ChunkAccess chunkAccess8) {
            this.doWork(chunkStatus0, serverLevel2, chunkGenerator3, listChunkAccess7, chunkAccess8);
            return CompletableFuture.completedFuture(Either.left(chunkAccess8));
        }

        void doWork(ChunkStatus var1, ServerLevel var2, ChunkGenerator var3, List<ChunkAccess> var4, ChunkAccess var5);
    }
}