package com.github.alexmodguy.alexscaves.server.level.structure;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public abstract class AbstractCaveGenerationStructure extends Structure {

    private final ResourceKey<Biome> matchingBiome;

    protected AbstractCaveGenerationStructure(Structure.StructureSettings settings, ResourceKey<Biome> matchingBiome) {
        super(settings);
        this.matchingBiome = matchingBiome;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        return this.atYCaveBiomePoint(context, Heightmap.Types.OCEAN_FLOOR_WG, builder -> this.generatePieces(builder, context));
    }

    protected Optional<Structure.GenerationStub> atYCaveBiomePoint(Structure.GenerationContext context, Heightmap.Types heightMap, Consumer<StructurePiecesBuilder> builderConsumer) {
        ChunkPos chunkpos = context.chunkPos();
        int i = chunkpos.getMiddleBlockX();
        int j = chunkpos.getMiddleBlockZ();
        int k = this.getGenerateYHeight(context.random(), i, j);
        return Optional.of(new Structure.GenerationStub(new BlockPos(i, k, j), builderConsumer));
    }

    public void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        int i = context.chunkPos().getMinBlockX();
        int j = context.chunkPos().getMinBlockZ();
        int k = context.chunkGenerator().getSeaLevel();
        BlockPos center = new BlockPos(i, this.getGenerateYHeight(context.random(), i, j), j);
        int heightRad = this.getHeightRadius(context.random(), context.chunkGenerator().getSeaLevel());
        int widthRad = this.getWidthRadius(context.random());
        int biomeUp = this.biomeContinuesInDirectionFor(context.biomeSource(), context.randomState(), Direction.UP, center, heightRad) + this.getYExpand();
        int biomeDown = this.biomeContinuesInDirectionFor(context.biomeSource(), context.randomState(), Direction.DOWN, center, heightRad) + this.getYExpand();
        BlockPos ground = center.below(biomeDown - 2);
        int biomeEast = this.biomeContinuesInDirectionFor(context.biomeSource(), context.randomState(), Direction.EAST, ground, widthRad);
        int biomeWest = this.biomeContinuesInDirectionFor(context.biomeSource(), context.randomState(), Direction.WEST, ground, widthRad);
        int biomeNorth = this.biomeContinuesInDirectionFor(context.biomeSource(), context.randomState(), Direction.NORTH, ground, widthRad);
        int biomeSouth = this.biomeContinuesInDirectionFor(context.biomeSource(), context.randomState(), Direction.SOUTH, ground, widthRad);
        int widthBlocks = (biomeEast + biomeWest + biomeNorth + biomeSouth) / 4;
        int heightBlocks = (biomeUp + biomeDown) / 2;
        int widthChunks = (int) Math.ceil((double) ((float) (widthBlocks + 16) / 16.0F / 2.0F)) + 2;
        int heightChunks = this.getHeightOverride((int) Math.ceil((double) ((float) (heightBlocks + 16) / 16.0F / 2.0F)));
        int minYChunks = (int) Math.ceil((double) ((float) context.chunkGenerator().getMinY() / 16.0F));
        int maxYChunks = (int) Math.ceil(20.0);
        for (int chunkX = -widthChunks; chunkX <= widthChunks; chunkX++) {
            for (int chunkZ = -widthChunks; chunkZ <= widthChunks; chunkZ++) {
                for (int chunkY = Math.max(-heightChunks, minYChunks); chunkY <= Math.min(heightChunks, maxYChunks); chunkY++) {
                    StructurePiece piece = this.createPiece(center.offset(new BlockPos(chunkX * 16, chunkY * 16, chunkZ * 16)), center, heightBlocks, widthBlocks, context.randomState());
                    builder.addPiece(piece);
                }
            }
        }
    }

    protected int getHeightOverride(int heightIn) {
        return heightIn;
    }

    protected int getYExpand() {
        return -5;
    }

    protected abstract StructurePiece createPiece(BlockPos var1, BlockPos var2, int var3, int var4, RandomState var5);

    private static Holder<Biome> getBiomeHolder(BiomeSource biomeSource, RandomState randomState, BlockPos pos) {
        return biomeSource.getNoiseBiome(QuartPos.fromBlock(pos.m_123341_()), QuartPos.fromBlock(pos.m_123342_()), QuartPos.fromBlock(pos.m_123343_()), randomState.sampler());
    }

    protected int biomeContinuesInDirectionFor(BiomeSource biomeSource, RandomState randomState, Direction direction, BlockPos start, int cutoff) {
        int i;
        for (i = 0; i < cutoff; i += 16) {
            BlockPos check = start.relative(direction, i);
            Holder<Biome> biomeHolder = getBiomeHolder(biomeSource, randomState, check);
            if (!biomeHolder.is(this.matchingBiome)) {
                break;
            }
        }
        return Math.min(i, cutoff);
    }

    public abstract int getGenerateYHeight(WorldgenRandom var1, int var2, int var3);

    public abstract int getWidthRadius(WorldgenRandom var1);

    public abstract int getHeightRadius(WorldgenRandom var1, int var2);

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.RAW_GENERATION;
    }
}