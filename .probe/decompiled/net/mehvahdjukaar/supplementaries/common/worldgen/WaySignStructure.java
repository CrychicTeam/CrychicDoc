package net.mehvahdjukaar.supplementaries.common.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import java.util.Optional;
import java.util.Set;
import net.mehvahdjukaar.moonlight.api.misc.StrOpt;
import net.mehvahdjukaar.moonlight.api.misc.WeakHashSet;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.mehvahdjukaar.supplementaries.reg.ModWorldgenRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class WaySignStructure extends Structure {

    public static final Codec<WaySignStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(m_226567_(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool), StrOpt.of(ResourceLocation.CODEC, "start_jigsaw_name").forGetter(structure -> structure.startJigsawName), Codec.INT.fieldOf("min_y").forGetter(structure -> structure.minY), Codec.INT.fieldOf("max_y").forGetter(structure -> structure.maxY)).apply(instance, WaySignStructure::new)).codec();

    private final Holder<StructureTemplatePool> startPool;

    private final Optional<ResourceLocation> startJigsawName;

    private final int minY;

    private final int maxY;

    private static final Set<Holder<Biome>> VALID_BIOMES = new WeakHashSet<Holder<Biome>>();

    public WaySignStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int minY, int maxY) {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public StructureType<?> type() {
        return (StructureType<?>) ModWorldgenRegistry.WAY_SIGN.get();
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        Optional<BlockPos> suitablePosition = getSuitablePosition(context, this);
        if (suitablePosition.isEmpty()) {
            return Optional.empty();
        } else {
            BlockPos blockPos = (BlockPos) suitablePosition.get();
            return JigsawPlacement.addPieces(context, this.startPool, this.startJigsawName, 3, blockPos, false, Optional.empty(), 32);
        }
    }

    private static Optional<BlockPos> getSuitablePosition(Structure.GenerationContext context, WaySignStructure structure) {
        ChunkPos chunkPos = context.chunkPos();
        ChunkGenerator generator = context.chunkGenerator();
        LevelHeightAccessor levelHeightAccessor = context.heightAccessor();
        RandomState randomState = context.randomState();
        Set<Holder<Biome>> biomes = context.biomeSource().possibleBiomes();
        boolean hasVillages = false;
        for (Holder<Biome> v : VALID_BIOMES) {
            if (biomes.contains(v)) {
                hasVillages = true;
                break;
            }
        }
        if (!hasVillages) {
            return Optional.empty();
        } else {
            int x = chunkPos.getMiddleBlockX();
            int z = chunkPos.getMiddleBlockZ();
            int y = generator.getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor, randomState);
            if (y < structure.minY || y > structure.maxY) {
                return Optional.empty();
            } else if (y <= 105 && y >= generator.getSeaLevel()) {
                IntList list = new IntArrayList();
                list.add(y);
                if (isPosNotValid(generator, x + 2, z + 2, list, levelHeightAccessor, randomState)) {
                    return Optional.empty();
                } else if (isPosNotValid(generator, x + 2, z - 2, list, levelHeightAccessor, randomState)) {
                    return Optional.empty();
                } else if (isPosNotValid(generator, x - 2, z + 2, list, levelHeightAccessor, randomState)) {
                    return Optional.empty();
                } else if (isPosNotValid(generator, x - 2, z - 2, list, levelHeightAccessor, randomState)) {
                    return Optional.empty();
                } else {
                    IntRBTreeSet set = new IntRBTreeSet(list);
                    if (set.lastInt() - set.firstInt() > 1) {
                        return Optional.empty();
                    } else {
                        int sum = 0;
                        IntListIterator var14 = list.iterator();
                        while (var14.hasNext()) {
                            Integer vx = (Integer) var14.next();
                            sum += vx;
                        }
                        return Optional.of(new BlockPos(x, Math.round((float) sum / 5.0F) + 1, z));
                    }
                }
            } else {
                return Optional.empty();
            }
        }
    }

    private static boolean isPosNotValid(ChunkGenerator gen, int x, int z, IntList heightMap, LevelHeightAccessor heightLimitView, RandomState randomState) {
        int y = gen.getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, heightLimitView, randomState);
        NoiseColumn noisecolumn = gen.getBaseColumn(x, z, heightLimitView, randomState);
        BlockState state = noisecolumn.getBlock(y);
        try {
            if (state.m_60819_().isEmpty()) {
                heightMap.add(y);
                return false;
            } else {
                return true;
            }
        } catch (Exception var10) {
            return true;
        }
    }

    public static void recomputeValidStructureCache(RegistryAccess access) {
        for (Holder<Structure> s : access.registryOrThrow(Registries.STRUCTURE).getTagOrEmpty(ModTags.WAY_SIGN_DESTINATIONS)) {
            VALID_BIOMES.addAll(s.value().biomes().stream().toList());
        }
    }

    public static void clearCache() {
        VALID_BIOMES.clear();
    }

    public static class Type implements StructureType<WaySignStructure> {

        @Override
        public Codec<WaySignStructure> codec() {
            return WaySignStructure.CODEC;
        }
    }
}