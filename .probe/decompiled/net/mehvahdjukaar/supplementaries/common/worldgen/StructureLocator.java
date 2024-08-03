package net.mehvahdjukaar.supplementaries.common.worldgen;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

public class StructureLocator {

    private static final Comparator<Vector2i> COMPARATOR = (o1, o2) -> Float.compare((float) o1.lengthSquared(), (float) o2.lengthSquared());

    @Nullable
    public static StructureLocator.LocatedStruct findNearestRandomMapFeature(ServerLevel level, @NotNull HolderSet<Structure> targets, BlockPos pos, int maximumChunkDistance, boolean newlyGenerated) {
        boolean rand = (Boolean) CommonConfigs.Tweaks.RANDOM_ADVENTURER_MAPS_RANDOM.get();
        List<StructureLocator.LocatedStruct> found = findNearestMapFeatures(level, targets, pos, maximumChunkDistance, newlyGenerated, 1, rand, !rand);
        return !found.isEmpty() ? (StructureLocator.LocatedStruct) found.get(0) : null;
    }

    public static List<StructureLocator.LocatedStruct> findNearestMapFeatures(ServerLevel level, @NotNull TagKey<Structure> tagKey, BlockPos pos, int maximumChunkDistance, boolean newlyGenerated, int requiredCount, int maxSearches) {
        HolderSet<Structure> targets = (HolderSet<Structure>) level.m_9598_().registryOrThrow(Registries.STRUCTURE).getTag(tagKey).orElse(null);
        if (targets == null) {
            return List.of();
        } else {
            if (targets.size() > maxSearches) {
                ArrayList<Holder<Structure>> list = new ArrayList(targets.stream().toList());
                Collections.shuffle(list);
                targets = HolderSet.direct(list.subList(0, maxSearches));
            }
            return findNearestMapFeatures(level, targets, pos, maximumChunkDistance, newlyGenerated, requiredCount, false, false);
        }
    }

    public static List<StructureLocator.LocatedStruct> findNearestMapFeatures(ServerLevel level, HolderSet<Structure> taggedStructures, BlockPos pos, int maximumChunkDistance, boolean newlyGenerated, int requiredCount, boolean selectRandom, boolean exitEarly) {
        List<StructureLocator.LocatedStruct> foundStructures = new ArrayList();
        if (!level.getServer().getWorldData().worldGenOptions().generateStructures()) {
            return foundStructures;
        } else if (taggedStructures.size() == 0) {
            Supplementaries.LOGGER.error("Found empty target structures for structure map. Its likely some mod broke some vanilla tag. Check your logs!");
            return foundStructures;
        } else {
            List<Holder<Structure>> selectedTargets = taggedStructures.stream().toList();
            ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
            double maxDist = Double.MAX_VALUE;
            Object var35;
            if (selectRandom) {
                Holder<Structure> selected = (Holder<Structure>) selectedTargets.get(level.f_46441_.nextInt(selectedTargets.size()));
                var35 = List.of(selected);
                Supplementaries.LOGGER.info("Searching for structure {} from pos {}", selected.unwrapKey().get(), pos);
            } else {
                var35 = new ArrayList(selectedTargets);
                Collections.shuffle((List) var35);
                Supplementaries.LOGGER.info("Searching for closest structure among {} from pos {}", Arrays.toString(var35.stream().map(ex -> (ResourceKey) ex.unwrapKey().get()).toArray()), pos);
            }
            Map<StructurePlacement, Set<Holder<Structure>>> reachableTargetsMap = new Object2ObjectArrayMap();
            ChunkGeneratorStructureState structureState = level.getChunkSource().getGeneratorState();
            for (Holder<Structure> holder : var35) {
                for (StructurePlacement structureplacement : structureState.getPlacementsForStructure(holder)) {
                    ((Set) reachableTargetsMap.computeIfAbsent(structureplacement, placement -> new ObjectArraySet())).add(holder);
                }
            }
            if (reachableTargetsMap.isEmpty()) {
                return foundStructures;
            } else {
                List<Pair<RandomSpreadStructurePlacement, Set<Holder<Structure>>>> list = new ArrayList(reachableTargetsMap.size());
                int maxSpacing = 1;
                StructureManager structuremanager = level.structureManager();
                for (Entry<StructurePlacement, Set<Holder<Structure>>> entry : reachableTargetsMap.entrySet()) {
                    StructurePlacement placement = (StructurePlacement) entry.getKey();
                    if (placement instanceof ConcentricRingsStructurePlacement) {
                        ConcentricRingsStructurePlacement concentricringsstructureplacement = (ConcentricRingsStructurePlacement) placement;
                        Pair<BlockPos, Holder<Structure>> foundPair = chunkGenerator.getNearestGeneratedStructure((Set<Holder<Structure>>) entry.getValue(), level, structuremanager, pos, newlyGenerated, concentricringsstructureplacement);
                        if (foundPair != null) {
                            double d1 = pos.m_123331_((Vec3i) foundPair.getFirst());
                            if (d1 < maxDist) {
                                maxDist = d1;
                                foundStructures.add(new StructureLocator.LocatedStruct(foundPair));
                            }
                        }
                    } else if (placement instanceof RandomSpreadStructurePlacement randomPlacement) {
                        list.add(Pair.of(randomPlacement, (Set) entry.getValue()));
                        maxSpacing = Math.max(maxSpacing, randomPlacement.spacing());
                    }
                }
                if (!list.isEmpty()) {
                    int chunkX = SectionPos.blockToSectionCoord(pos.m_123341_());
                    int chunkZ = SectionPos.blockToSectionCoord(pos.m_123343_());
                    long seed = level.getSeed();
                    StructureManager manager = level.structureManager();
                    label112: for (int k = 0; k <= maximumChunkDistance / maxSpacing; k++) {
                        int outerRing = (k + 1) * maxSpacing;
                        int innerRing = k * maxSpacing;
                        boolean lessPrecision = innerRing * 16 > 2000;
                        TreeMap<Vector2i, List<Pair<RandomSpreadStructurePlacement, Set<Holder<Structure>>>>> possiblePositions = new TreeMap(COMPARATOR);
                        for (Pair<RandomSpreadStructurePlacement, Set<Holder<Structure>>> p : list) {
                            RandomSpreadStructurePlacement placement = (RandomSpreadStructurePlacement) p.getFirst();
                            int spacing = placement.spacing();
                            for (int r = innerRing; r < outerRing; r += spacing) {
                                addAllPossibleFeatureChunksAtDistance(chunkX, chunkZ, r, seed, placement, c -> {
                                    Vector2i v = new Vector2i(c.x - chunkX, c.z - chunkZ);
                                    if (possiblePositions.containsKey(v)) {
                                        boolean ll = true;
                                    }
                                    List<Pair<RandomSpreadStructurePlacement, Set<Holder<Structure>>>> ll = (List<Pair<RandomSpreadStructurePlacement, Set<Holder<Structure>>>>) possiblePositions.computeIfAbsent(v, o -> new ArrayList());
                                    if (ll.contains(p)) {
                                        boolean var7 = true;
                                    } else {
                                        ll.add(p);
                                    }
                                });
                            }
                        }
                        for (Entry<Vector2i, List<Pair<RandomSpreadStructurePlacement, Set<Holder<Structure>>>>> e : possiblePositions.entrySet()) {
                            Vector2i vec2i = (Vector2i) e.getKey();
                            ChunkPos chunkPos = new ChunkPos(vec2i.x() + chunkX, vec2i.y() + chunkZ);
                            for (Pair<RandomSpreadStructurePlacement, Set<Holder<Structure>>> pp : (List) e.getValue()) {
                                foundStructures.addAll(getStructuresAtChunkPos((Set<Holder<Structure>>) pp.getSecond(), level, manager, newlyGenerated, (RandomSpreadStructurePlacement) pp.getFirst(), chunkPos));
                            }
                            if (foundStructures.size() >= requiredCount) {
                                break label112;
                            }
                        }
                    }
                }
                foundStructures.sort(Comparator.comparingDouble(f -> pos.m_123331_(f.pos)));
                if (foundStructures.size() >= requiredCount) {
                    foundStructures = (List<StructureLocator.LocatedStruct>) Lists.partition(foundStructures, requiredCount).get(0);
                }
                if (newlyGenerated) {
                    for (StructureLocator.LocatedStruct s : foundStructures) {
                        if (s.start != null && s.start.canBeReferenced()) {
                            structuremanager.addReference(s.start);
                        }
                    }
                }
                return foundStructures;
            }
        }
    }

    private static void addAllPossibleFeatureChunksAtDistance(int chunkX, int chunkZ, int radius, long seed, RandomSpreadStructurePlacement placement, Consumer<ChunkPos> positionConsumer) {
        for (int j = -radius; j <= radius; j++) {
            boolean flag = j == -radius || j == radius;
            for (int k = -radius; k <= radius; k++) {
                boolean flag1 = k == -radius || k == radius;
                if (flag || flag1) {
                    int px = chunkX + j;
                    int pz = chunkZ + k;
                    ChunkPos chunkpos = placement.getPotentialStructureChunk(seed, px, pz);
                    positionConsumer.accept(chunkpos);
                }
            }
        }
    }

    private static Set<StructureLocator.LocatedStruct> getStructuresAtChunkPos(Set<Holder<Structure>> targets, LevelReader level, StructureManager structureManager, boolean skipKnown, RandomSpreadStructurePlacement placement, ChunkPos chunkpos) {
        Set<StructureLocator.LocatedStruct> foundStructures = new HashSet();
        for (Holder<Structure> holder : targets) {
            StructureCheckResult structurecheckresult = structureManager.checkStructurePresence(chunkpos, holder.value(), skipKnown);
            if (structurecheckresult != StructureCheckResult.START_NOT_PRESENT) {
                if (!skipKnown && structurecheckresult == StructureCheckResult.START_PRESENT) {
                    foundStructures.add(new StructureLocator.LocatedStruct(placement.m_227039_(chunkpos), holder, null));
                } else {
                    ChunkAccess chunkaccess = level.getChunk(chunkpos.x, chunkpos.z, ChunkStatus.STRUCTURE_STARTS);
                    StructureStart structurestart = structureManager.getStartForStructure(SectionPos.bottomOf(chunkaccess), holder.value(), chunkaccess);
                    if (structurestart != null && structurestart.isValid() && (!skipKnown || structurestart.canBeReferenced())) {
                        foundStructures.add(new StructureLocator.LocatedStruct(placement.m_227039_(structurestart.getChunkPos()), holder, structurestart));
                    }
                }
            }
        }
        return foundStructures;
    }

    @Nullable
    private static Set<StructureLocator.LocatedStruct> getNearestGeneratedStructureAtDistance(Set<Holder<Structure>> targets, LevelReader level, StructureManager featureManager, int x, int z, int distance, boolean newChunk, long seed, RandomSpreadStructurePlacement placement) {
        int i = placement.spacing();
        for (int j = -distance; j <= distance; j++) {
            boolean flag = j == -distance || j == distance;
            for (int k = -distance; k <= distance; k++) {
                boolean flag1 = k == -distance || k == distance;
                if (flag || flag1) {
                    int px = x + i * j;
                    int pz = z + i * k;
                    ChunkPos chunkpos = placement.getPotentialStructureChunk(seed, px, pz);
                    return getStructuresAtChunkPos(targets, level, featureManager, newChunk, placement, chunkpos);
                }
            }
        }
        return null;
    }

    @Nullable
    public BlockPos findRandomMapFeature(TagKey<Structure> tagKey, BlockPos pos, int radius, boolean unexplored, ServerLevel level) {
        if (!level.getServer().getWorldData().worldGenOptions().generateStructures()) {
            return null;
        } else {
            Optional<HolderSet.Named<Structure>> optional = level.m_9598_().registryOrThrow(Registries.STRUCTURE).getTag(tagKey);
            if (optional.isEmpty()) {
                return null;
            } else {
                HolderSet.Named<Structure> set = (HolderSet.Named<Structure>) optional.get();
                List<Holder<Structure>> list = set.m_203614_().toList();
                Holder<Structure> chosen = (Holder<Structure>) list.get(level.f_46441_.nextInt(list.size()));
                Pair<BlockPos, Holder<Structure>> pair = level.getChunkSource().getGenerator().findNearestMapStructure(level, HolderSet.direct(chosen), pos, radius, unexplored);
                return pair != null ? (BlockPos) pair.getFirst() : null;
            }
        }
    }

    public static record LocatedStruct(BlockPos pos, Holder<Structure> structure, @Nullable StructureStart start) {

        public LocatedStruct(Pair<BlockPos, Holder<Structure>> pair) {
            this((BlockPos) pair.getFirst(), (Holder<Structure>) pair.getSecond(), null);
        }
    }
}