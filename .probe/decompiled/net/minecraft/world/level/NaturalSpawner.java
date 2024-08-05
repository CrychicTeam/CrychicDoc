package net.minecraft.world.level;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public final class NaturalSpawner {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int MIN_SPAWN_DISTANCE = 24;

    public static final int SPAWN_DISTANCE_CHUNK = 8;

    public static final int SPAWN_DISTANCE_BLOCK = 128;

    static final int MAGIC_NUMBER = (int) Math.pow(17.0, 2.0);

    private static final MobCategory[] SPAWNING_CATEGORIES = (MobCategory[]) Stream.of(MobCategory.values()).filter(p_47037_ -> p_47037_ != MobCategory.MISC).toArray(MobCategory[]::new);

    private NaturalSpawner() {
    }

    public static NaturalSpawner.SpawnState createState(int int0, Iterable<Entity> iterableEntity1, NaturalSpawner.ChunkGetter naturalSpawnerChunkGetter2, LocalMobCapCalculator localMobCapCalculator3) {
        PotentialCalculator $$4 = new PotentialCalculator();
        Object2IntOpenHashMap<MobCategory> $$5 = new Object2IntOpenHashMap();
        for (Entity $$6 : iterableEntity1) {
            if ($$6 instanceof Mob $$7 && ($$7.isPersistenceRequired() || $$7.requiresCustomPersistence())) {
                continue;
            }
            MobCategory $$8 = $$6.getType().getCategory();
            if ($$8 != MobCategory.MISC) {
                BlockPos $$9 = $$6.blockPosition();
                naturalSpawnerChunkGetter2.query(ChunkPos.asLong($$9), p_275163_ -> {
                    MobSpawnSettings.MobSpawnCost $$7 = getRoughBiome($$9, p_275163_).getMobSettings().getMobSpawnCost($$6.getType());
                    if ($$7 != null) {
                        $$4.addCharge($$6.blockPosition(), $$7.charge());
                    }
                    if ($$6 instanceof Mob) {
                        localMobCapCalculator3.addMob(p_275163_.m_7697_(), $$8);
                    }
                    $$5.addTo($$8, 1);
                });
            }
        }
        return new NaturalSpawner.SpawnState(int0, $$5, $$4, localMobCapCalculator3);
    }

    static Biome getRoughBiome(BlockPos blockPos0, ChunkAccess chunkAccess1) {
        return chunkAccess1.getNoiseBiome(QuartPos.fromBlock(blockPos0.m_123341_()), QuartPos.fromBlock(blockPos0.m_123342_()), QuartPos.fromBlock(blockPos0.m_123343_())).value();
    }

    public static void spawnForChunk(ServerLevel serverLevel0, LevelChunk levelChunk1, NaturalSpawner.SpawnState naturalSpawnerSpawnState2, boolean boolean3, boolean boolean4, boolean boolean5) {
        serverLevel0.m_46473_().push("spawner");
        for (MobCategory $$6 : SPAWNING_CATEGORIES) {
            if ((boolean3 || !$$6.isFriendly()) && (boolean4 || $$6.isFriendly()) && (boolean5 || !$$6.isPersistent()) && naturalSpawnerSpawnState2.canSpawnForCategory($$6, levelChunk1.m_7697_())) {
                spawnCategoryForChunk($$6, serverLevel0, levelChunk1, naturalSpawnerSpawnState2::m_47127_, naturalSpawnerSpawnState2::m_47131_);
            }
        }
        serverLevel0.m_46473_().pop();
    }

    public static void spawnCategoryForChunk(MobCategory mobCategory0, ServerLevel serverLevel1, LevelChunk levelChunk2, NaturalSpawner.SpawnPredicate naturalSpawnerSpawnPredicate3, NaturalSpawner.AfterSpawnCallback naturalSpawnerAfterSpawnCallback4) {
        BlockPos $$5 = getRandomPosWithin(serverLevel1, levelChunk2);
        if ($$5.m_123342_() >= serverLevel1.m_141937_() + 1) {
            spawnCategoryForPosition(mobCategory0, serverLevel1, levelChunk2, $$5, naturalSpawnerSpawnPredicate3, naturalSpawnerAfterSpawnCallback4);
        }
    }

    @VisibleForDebug
    public static void spawnCategoryForPosition(MobCategory mobCategory0, ServerLevel serverLevel1, BlockPos blockPos2) {
        spawnCategoryForPosition(mobCategory0, serverLevel1, serverLevel1.m_46865_(blockPos2), blockPos2, (p_151606_, p_151607_, p_151608_) -> true, (p_151610_, p_151611_) -> {
        });
    }

    public static void spawnCategoryForPosition(MobCategory mobCategory0, ServerLevel serverLevel1, ChunkAccess chunkAccess2, BlockPos blockPos3, NaturalSpawner.SpawnPredicate naturalSpawnerSpawnPredicate4, NaturalSpawner.AfterSpawnCallback naturalSpawnerAfterSpawnCallback5) {
        StructureManager $$6 = serverLevel1.structureManager();
        ChunkGenerator $$7 = serverLevel1.getChunkSource().getGenerator();
        int $$8 = blockPos3.m_123342_();
        BlockState $$9 = chunkAccess2.m_8055_(blockPos3);
        if (!$$9.m_60796_(chunkAccess2, blockPos3)) {
            BlockPos.MutableBlockPos $$10 = new BlockPos.MutableBlockPos();
            int $$11 = 0;
            for (int $$12 = 0; $$12 < 3; $$12++) {
                int $$13 = blockPos3.m_123341_();
                int $$14 = blockPos3.m_123343_();
                int $$15 = 6;
                MobSpawnSettings.SpawnerData $$16 = null;
                SpawnGroupData $$17 = null;
                int $$18 = Mth.ceil(serverLevel1.f_46441_.nextFloat() * 4.0F);
                int $$19 = 0;
                for (int $$20 = 0; $$20 < $$18; $$20++) {
                    $$13 += serverLevel1.f_46441_.nextInt(6) - serverLevel1.f_46441_.nextInt(6);
                    $$14 += serverLevel1.f_46441_.nextInt(6) - serverLevel1.f_46441_.nextInt(6);
                    $$10.set($$13, $$8, $$14);
                    double $$21 = (double) $$13 + 0.5;
                    double $$22 = (double) $$14 + 0.5;
                    Player $$23 = serverLevel1.m_45924_($$21, (double) $$8, $$22, -1.0, false);
                    if ($$23 != null) {
                        double $$24 = $$23.m_20275_($$21, (double) $$8, $$22);
                        if (isRightDistanceToPlayerAndSpawnPoint(serverLevel1, chunkAccess2, $$10, $$24)) {
                            if ($$16 == null) {
                                Optional<MobSpawnSettings.SpawnerData> $$25 = getRandomSpawnMobAt(serverLevel1, $$6, $$7, mobCategory0, serverLevel1.f_46441_, $$10);
                                if ($$25.isEmpty()) {
                                    break;
                                }
                                $$16 = (MobSpawnSettings.SpawnerData) $$25.get();
                                $$18 = $$16.minCount + serverLevel1.f_46441_.nextInt(1 + $$16.maxCount - $$16.minCount);
                            }
                            if (isValidSpawnPostitionForType(serverLevel1, mobCategory0, $$6, $$7, $$16, $$10, $$24) && naturalSpawnerSpawnPredicate4.test($$16.type, $$10, chunkAccess2)) {
                                Mob $$26 = getMobForSpawn(serverLevel1, $$16.type);
                                if ($$26 == null) {
                                    return;
                                }
                                $$26.m_7678_($$21, (double) $$8, $$22, serverLevel1.f_46441_.nextFloat() * 360.0F, 0.0F);
                                if (isValidPositionForMob(serverLevel1, $$26, $$24)) {
                                    $$17 = $$26.finalizeSpawn(serverLevel1, serverLevel1.m_6436_($$26.m_20183_()), MobSpawnType.NATURAL, $$17, null);
                                    $$11++;
                                    $$19++;
                                    serverLevel1.m_47205_($$26);
                                    naturalSpawnerAfterSpawnCallback5.run($$26, chunkAccess2);
                                    if ($$11 >= $$26.getMaxSpawnClusterSize()) {
                                        return;
                                    }
                                    if ($$26.isMaxGroupSizeReached($$19)) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isRightDistanceToPlayerAndSpawnPoint(ServerLevel serverLevel0, ChunkAccess chunkAccess1, BlockPos.MutableBlockPos blockPosMutableBlockPos2, double double3) {
        if (double3 <= 576.0) {
            return false;
        } else {
            return serverLevel0.m_220360_().m_203195_(new Vec3((double) blockPosMutableBlockPos2.m_123341_() + 0.5, (double) blockPosMutableBlockPos2.m_123342_(), (double) blockPosMutableBlockPos2.m_123343_() + 0.5), 24.0) ? false : Objects.equals(new ChunkPos(blockPosMutableBlockPos2), chunkAccess1.getPos()) || serverLevel0.isNaturalSpawningAllowed(blockPosMutableBlockPos2);
        }
    }

    private static boolean isValidSpawnPostitionForType(ServerLevel serverLevel0, MobCategory mobCategory1, StructureManager structureManager2, ChunkGenerator chunkGenerator3, MobSpawnSettings.SpawnerData mobSpawnSettingsSpawnerData4, BlockPos.MutableBlockPos blockPosMutableBlockPos5, double double6) {
        EntityType<?> $$7 = mobSpawnSettingsSpawnerData4.type;
        if ($$7.getCategory() == MobCategory.MISC) {
            return false;
        } else if (!$$7.canSpawnFarFromPlayer() && double6 > (double) ($$7.getCategory().getDespawnDistance() * $$7.getCategory().getDespawnDistance())) {
            return false;
        } else if ($$7.canSummon() && canSpawnMobAt(serverLevel0, structureManager2, chunkGenerator3, mobCategory1, mobSpawnSettingsSpawnerData4, blockPosMutableBlockPos5)) {
            SpawnPlacements.Type $$8 = SpawnPlacements.getPlacementType($$7);
            if (!isSpawnPositionOk($$8, serverLevel0, blockPosMutableBlockPos5, $$7)) {
                return false;
            } else {
                return !SpawnPlacements.checkSpawnRules($$7, serverLevel0, MobSpawnType.NATURAL, blockPosMutableBlockPos5, serverLevel0.f_46441_) ? false : serverLevel0.m_45772_($$7.getAABB((double) blockPosMutableBlockPos5.m_123341_() + 0.5, (double) blockPosMutableBlockPos5.m_123342_(), (double) blockPosMutableBlockPos5.m_123343_() + 0.5));
            }
        } else {
            return false;
        }
    }

    @Nullable
    private static Mob getMobForSpawn(ServerLevel serverLevel0, EntityType<?> entityType1) {
        try {
            Entity var3 = entityType1.create(serverLevel0);
            if (var3 instanceof Mob) {
                return (Mob) var3;
            }
            LOGGER.warn("Can't spawn entity of type: {}", BuiltInRegistries.ENTITY_TYPE.getKey(entityType1));
        } catch (Exception var4) {
            LOGGER.warn("Failed to create mob", var4);
        }
        return null;
    }

    private static boolean isValidPositionForMob(ServerLevel serverLevel0, Mob mob1, double double2) {
        return double2 > (double) (mob1.m_6095_().getCategory().getDespawnDistance() * mob1.m_6095_().getCategory().getDespawnDistance()) && mob1.removeWhenFarAway(double2) ? false : mob1.checkSpawnRules(serverLevel0, MobSpawnType.NATURAL) && mob1.checkSpawnObstruction(serverLevel0);
    }

    private static Optional<MobSpawnSettings.SpawnerData> getRandomSpawnMobAt(ServerLevel serverLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, MobCategory mobCategory3, RandomSource randomSource4, BlockPos blockPos5) {
        Holder<Biome> $$6 = serverLevel0.m_204166_(blockPos5);
        return mobCategory3 == MobCategory.WATER_AMBIENT && $$6.is(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS) && randomSource4.nextFloat() < 0.98F ? Optional.empty() : mobsAt(serverLevel0, structureManager1, chunkGenerator2, mobCategory3, blockPos5, $$6).getRandom(randomSource4);
    }

    private static boolean canSpawnMobAt(ServerLevel serverLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, MobCategory mobCategory3, MobSpawnSettings.SpawnerData mobSpawnSettingsSpawnerData4, BlockPos blockPos5) {
        return mobsAt(serverLevel0, structureManager1, chunkGenerator2, mobCategory3, blockPos5, null).unwrap().contains(mobSpawnSettingsSpawnerData4);
    }

    private static WeightedRandomList<MobSpawnSettings.SpawnerData> mobsAt(ServerLevel serverLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, MobCategory mobCategory3, BlockPos blockPos4, @Nullable Holder<Biome> holderBiome5) {
        return isInNetherFortressBounds(blockPos4, serverLevel0, mobCategory3, structureManager1) ? NetherFortressStructure.FORTRESS_ENEMIES : chunkGenerator2.getMobsAt(holderBiome5 != null ? holderBiome5 : serverLevel0.m_204166_(blockPos4), structureManager1, mobCategory3, blockPos4);
    }

    public static boolean isInNetherFortressBounds(BlockPos blockPos0, ServerLevel serverLevel1, MobCategory mobCategory2, StructureManager structureManager3) {
        if (mobCategory2 == MobCategory.MONSTER && serverLevel1.m_8055_(blockPos0.below()).m_60713_(Blocks.NETHER_BRICKS)) {
            Structure $$4 = structureManager3.registryAccess().registryOrThrow(Registries.STRUCTURE).get(BuiltinStructures.FORTRESS);
            return $$4 == null ? false : structureManager3.getStructureAt(blockPos0, $$4).isValid();
        } else {
            return false;
        }
    }

    private static BlockPos getRandomPosWithin(Level level0, LevelChunk levelChunk1) {
        ChunkPos $$2 = levelChunk1.m_7697_();
        int $$3 = $$2.getMinBlockX() + level0.random.nextInt(16);
        int $$4 = $$2.getMinBlockZ() + level0.random.nextInt(16);
        int $$5 = levelChunk1.m_5885_(Heightmap.Types.WORLD_SURFACE, $$3, $$4) + 1;
        int $$6 = Mth.randomBetweenInclusive(level0.random, level0.m_141937_(), $$5);
        return new BlockPos($$3, $$6, $$4);
    }

    public static boolean isValidEmptySpawnBlock(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, FluidState fluidState3, EntityType<?> entityType4) {
        if (blockState2.m_60838_(blockGetter0, blockPos1)) {
            return false;
        } else if (blockState2.m_60803_()) {
            return false;
        } else if (!fluidState3.isEmpty()) {
            return false;
        } else {
            return blockState2.m_204336_(BlockTags.PREVENT_MOB_SPAWNING_INSIDE) ? false : !entityType4.isBlockDangerous(blockState2);
        }
    }

    public static boolean isSpawnPositionOk(SpawnPlacements.Type spawnPlacementsType0, LevelReader levelReader1, BlockPos blockPos2, @Nullable EntityType<?> entityType3) {
        if (spawnPlacementsType0 == SpawnPlacements.Type.NO_RESTRICTIONS) {
            return true;
        } else if (entityType3 != null && levelReader1.m_6857_().isWithinBounds(blockPos2)) {
            BlockState $$4 = levelReader1.m_8055_(blockPos2);
            FluidState $$5 = levelReader1.m_6425_(blockPos2);
            BlockPos $$6 = blockPos2.above();
            BlockPos $$7 = blockPos2.below();
            switch(spawnPlacementsType0) {
                case IN_WATER:
                    return $$5.is(FluidTags.WATER) && !levelReader1.m_8055_($$6).m_60796_(levelReader1, $$6);
                case IN_LAVA:
                    return $$5.is(FluidTags.LAVA);
                case ON_GROUND:
                default:
                    BlockState $$8 = levelReader1.m_8055_($$7);
                    return !$$8.m_60643_(levelReader1, $$7, entityType3) ? false : isValidEmptySpawnBlock(levelReader1, blockPos2, $$4, $$5, entityType3) && isValidEmptySpawnBlock(levelReader1, $$6, levelReader1.m_8055_($$6), levelReader1.m_6425_($$6), entityType3);
            }
        } else {
            return false;
        }
    }

    public static void spawnMobsForChunkGeneration(ServerLevelAccessor serverLevelAccessor0, Holder<Biome> holderBiome1, ChunkPos chunkPos2, RandomSource randomSource3) {
        MobSpawnSettings $$4 = holderBiome1.value().getMobSettings();
        WeightedRandomList<MobSpawnSettings.SpawnerData> $$5 = $$4.getMobs(MobCategory.CREATURE);
        if (!$$5.isEmpty()) {
            int $$6 = chunkPos2.getMinBlockX();
            int $$7 = chunkPos2.getMinBlockZ();
            while (randomSource3.nextFloat() < $$4.getCreatureProbability()) {
                Optional<MobSpawnSettings.SpawnerData> $$8 = $$5.getRandom(randomSource3);
                if ($$8.isPresent()) {
                    MobSpawnSettings.SpawnerData $$9 = (MobSpawnSettings.SpawnerData) $$8.get();
                    int $$10 = $$9.minCount + randomSource3.nextInt(1 + $$9.maxCount - $$9.minCount);
                    SpawnGroupData $$11 = null;
                    int $$12 = $$6 + randomSource3.nextInt(16);
                    int $$13 = $$7 + randomSource3.nextInt(16);
                    int $$14 = $$12;
                    int $$15 = $$13;
                    for (int $$16 = 0; $$16 < $$10; $$16++) {
                        boolean $$17 = false;
                        for (int $$18 = 0; !$$17 && $$18 < 4; $$18++) {
                            BlockPos $$19 = getTopNonCollidingPos(serverLevelAccessor0, $$9.type, $$12, $$13);
                            if ($$9.type.canSummon() && isSpawnPositionOk(SpawnPlacements.getPlacementType($$9.type), serverLevelAccessor0, $$19, $$9.type)) {
                                float $$20 = $$9.type.getWidth();
                                double $$21 = Mth.clamp((double) $$12, (double) $$6 + (double) $$20, (double) $$6 + 16.0 - (double) $$20);
                                double $$22 = Mth.clamp((double) $$13, (double) $$7 + (double) $$20, (double) $$7 + 16.0 - (double) $$20);
                                if (!serverLevelAccessor0.m_45772_($$9.type.getAABB($$21, (double) $$19.m_123342_(), $$22)) || !SpawnPlacements.checkSpawnRules($$9.type, serverLevelAccessor0, MobSpawnType.CHUNK_GENERATION, BlockPos.containing($$21, (double) $$19.m_123342_(), $$22), serverLevelAccessor0.m_213780_())) {
                                    continue;
                                }
                                Entity $$23;
                                try {
                                    $$23 = $$9.type.create(serverLevelAccessor0.getLevel());
                                } catch (Exception var27) {
                                    LOGGER.warn("Failed to create mob", var27);
                                    continue;
                                }
                                if ($$23 == null) {
                                    continue;
                                }
                                $$23.moveTo($$21, (double) $$19.m_123342_(), $$22, randomSource3.nextFloat() * 360.0F, 0.0F);
                                if ($$23 instanceof Mob $$26 && $$26.checkSpawnRules(serverLevelAccessor0, MobSpawnType.CHUNK_GENERATION) && $$26.checkSpawnObstruction(serverLevelAccessor0)) {
                                    $$11 = $$26.finalizeSpawn(serverLevelAccessor0, serverLevelAccessor0.m_6436_($$26.m_20183_()), MobSpawnType.CHUNK_GENERATION, $$11, null);
                                    serverLevelAccessor0.addFreshEntityWithPassengers($$26);
                                    $$17 = true;
                                }
                            }
                            $$12 += randomSource3.nextInt(5) - randomSource3.nextInt(5);
                            for ($$13 += randomSource3.nextInt(5) - randomSource3.nextInt(5); $$12 < $$6 || $$12 >= $$6 + 16 || $$13 < $$7 || $$13 >= $$7 + 16; $$13 = $$15 + randomSource3.nextInt(5) - randomSource3.nextInt(5)) {
                                $$12 = $$14 + randomSource3.nextInt(5) - randomSource3.nextInt(5);
                            }
                        }
                    }
                }
            }
        }
    }

    private static BlockPos getTopNonCollidingPos(LevelReader levelReader0, EntityType<?> entityType1, int int2, int int3) {
        int $$4 = levelReader0.getHeight(SpawnPlacements.getHeightmapType(entityType1), int2, int3);
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos(int2, $$4, int3);
        if (levelReader0.dimensionType().hasCeiling()) {
            do {
                $$5.move(Direction.DOWN);
            } while (!levelReader0.m_8055_($$5).m_60795_());
            do {
                $$5.move(Direction.DOWN);
            } while (levelReader0.m_8055_($$5).m_60795_() && $$5.m_123342_() > levelReader0.getMinBuildHeight());
        }
        if (SpawnPlacements.getPlacementType(entityType1) == SpawnPlacements.Type.ON_GROUND) {
            BlockPos $$6 = $$5.m_7495_();
            if (levelReader0.m_8055_($$6).m_60647_(levelReader0, $$6, PathComputationType.LAND)) {
                return $$6;
            }
        }
        return $$5.immutable();
    }

    @FunctionalInterface
    public interface AfterSpawnCallback {

        void run(Mob var1, ChunkAccess var2);
    }

    @FunctionalInterface
    public interface ChunkGetter {

        void query(long var1, Consumer<LevelChunk> var3);
    }

    @FunctionalInterface
    public interface SpawnPredicate {

        boolean test(EntityType<?> var1, BlockPos var2, ChunkAccess var3);
    }

    public static class SpawnState {

        private final int spawnableChunkCount;

        private final Object2IntOpenHashMap<MobCategory> mobCategoryCounts;

        private final PotentialCalculator spawnPotential;

        private final Object2IntMap<MobCategory> unmodifiableMobCategoryCounts;

        private final LocalMobCapCalculator localMobCapCalculator;

        @Nullable
        private BlockPos lastCheckedPos;

        @Nullable
        private EntityType<?> lastCheckedType;

        private double lastCharge;

        SpawnState(int int0, Object2IntOpenHashMap<MobCategory> objectIntOpenHashMapMobCategory1, PotentialCalculator potentialCalculator2, LocalMobCapCalculator localMobCapCalculator3) {
            this.spawnableChunkCount = int0;
            this.mobCategoryCounts = objectIntOpenHashMapMobCategory1;
            this.spawnPotential = potentialCalculator2;
            this.localMobCapCalculator = localMobCapCalculator3;
            this.unmodifiableMobCategoryCounts = Object2IntMaps.unmodifiable(objectIntOpenHashMapMobCategory1);
        }

        private boolean canSpawn(EntityType<?> entityType0, BlockPos blockPos1, ChunkAccess chunkAccess2) {
            this.lastCheckedPos = blockPos1;
            this.lastCheckedType = entityType0;
            MobSpawnSettings.MobSpawnCost $$3 = NaturalSpawner.getRoughBiome(blockPos1, chunkAccess2).getMobSettings().getMobSpawnCost(entityType0);
            if ($$3 == null) {
                this.lastCharge = 0.0;
                return true;
            } else {
                double $$4 = $$3.charge();
                this.lastCharge = $$4;
                double $$5 = this.spawnPotential.getPotentialEnergyChange(blockPos1, $$4);
                return $$5 <= $$3.energyBudget();
            }
        }

        private void afterSpawn(Mob mob0, ChunkAccess chunkAccess1) {
            EntityType<?> $$2 = mob0.m_6095_();
            BlockPos $$3 = mob0.m_20183_();
            double $$4;
            if ($$3.equals(this.lastCheckedPos) && $$2 == this.lastCheckedType) {
                $$4 = this.lastCharge;
            } else {
                MobSpawnSettings.MobSpawnCost $$5 = NaturalSpawner.getRoughBiome($$3, chunkAccess1).getMobSettings().getMobSpawnCost($$2);
                if ($$5 != null) {
                    $$4 = $$5.charge();
                } else {
                    $$4 = 0.0;
                }
            }
            this.spawnPotential.addCharge($$3, $$4);
            MobCategory $$8 = $$2.getCategory();
            this.mobCategoryCounts.addTo($$8, 1);
            this.localMobCapCalculator.addMob(new ChunkPos($$3), $$8);
        }

        public int getSpawnableChunkCount() {
            return this.spawnableChunkCount;
        }

        public Object2IntMap<MobCategory> getMobCategoryCounts() {
            return this.unmodifiableMobCategoryCounts;
        }

        boolean canSpawnForCategory(MobCategory mobCategory0, ChunkPos chunkPos1) {
            int $$2 = mobCategory0.getMaxInstancesPerChunk() * this.spawnableChunkCount / NaturalSpawner.MAGIC_NUMBER;
            return this.mobCategoryCounts.getInt(mobCategory0) >= $$2 ? false : this.localMobCapCalculator.canSpawn(mobCategory0, chunkPos1);
        }
    }
}