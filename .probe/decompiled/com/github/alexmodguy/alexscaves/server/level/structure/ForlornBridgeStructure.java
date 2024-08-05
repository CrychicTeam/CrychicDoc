package com.github.alexmodguy.alexscaves.server.level.structure;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.level.structure.piece.ForlornBridgeStructurePiece;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class ForlornBridgeStructure extends Structure {

    public static int BRIDGE_SECTION_LENGTH = 6;

    public static int BRIDGE_SECTION_WIDTH = 4;

    public static final Codec<ForlornBridgeStructure> CODEC = m_226607_(settings -> new ForlornBridgeStructure(settings));

    protected ForlornBridgeStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        return this.atYCaveBiomePoint(context, Heightmap.Types.OCEAN_FLOOR_WG, builder -> this.generatePieces(builder, context));
    }

    protected Optional<Structure.GenerationStub> atYCaveBiomePoint(Structure.GenerationContext context, Heightmap.Types heightMap, Consumer<StructurePiecesBuilder> builderConsumer) {
        ChunkPos chunkpos = context.chunkPos();
        int i = chunkpos.getMiddleBlockX();
        int j = chunkpos.getMiddleBlockZ();
        int k = -10;
        return Optional.of(new Structure.GenerationStub(new BlockPos(i, k, j), builderConsumer));
    }

    public void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        int i = context.chunkPos().getMinBlockX();
        int j = context.chunkPos().getMinBlockZ();
        int k = context.chunkGenerator().getSeaLevel();
        BlockPos xzCoords = new BlockPos(i, -10, j);
        int biomeUp = this.biomeContinuesInDirectionFor(context.biomeSource(), context.randomState(), Direction.UP, xzCoords, 32);
        int biomeDown = this.biomeContinuesInDirectionFor(context.biomeSource(), context.randomState(), Direction.DOWN, xzCoords, 32);
        BlockPos center = xzCoords.below(biomeDown).above(worldgenrandom.m_188503_(Math.max(biomeUp, 10)));
        Direction bridgeDirection = Util.getRandom(ACMath.HORIZONTAL_DIRECTIONS, worldgenrandom);
        int biomeForwards = this.biomeContinuesInDirectionFor(context.biomeSource(), context.randomState(), bridgeDirection, center, 32 + worldgenrandom.m_188503_(6) * 16);
        int biomeBackwards = this.biomeContinuesInDirectionFor(context.biomeSource(), context.randomState(), bridgeDirection.getOpposite(), center, 32 + worldgenrandom.m_188503_(6) * 16);
        int maxSections = (int) Math.ceil((double) ((biomeBackwards + biomeForwards) / BRIDGE_SECTION_LENGTH));
        for (int section = 0; section <= maxSections; section++) {
            BlockPos at = center.relative(bridgeDirection, section * BRIDGE_SECTION_LENGTH - BRIDGE_SECTION_LENGTH / 2 - biomeBackwards);
            builder.addPiece(new ForlornBridgeStructurePiece(at, section, maxSections, bridgeDirection));
        }
    }

    private static Holder<Biome> getBiomeHolder(BiomeSource biomeSource, RandomState randomState, BlockPos pos) {
        return biomeSource.getNoiseBiome(QuartPos.fromBlock(pos.m_123341_()), QuartPos.fromBlock(pos.m_123342_()), QuartPos.fromBlock(pos.m_123343_()), randomState.sampler());
    }

    protected int biomeContinuesInDirectionFor(BiomeSource biomeSource, RandomState randomState, Direction direction, BlockPos start, int cutoff) {
        int i;
        for (i = 0; i < cutoff; i += 16) {
            BlockPos check = start.relative(direction, i);
            Holder<Biome> biomeHolder = getBiomeHolder(biomeSource, randomState, check);
            if (!biomeHolder.is(ACBiomeRegistry.FORLORN_HOLLOWS)) {
                break;
            }
        }
        return Math.min(i, cutoff);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    @Override
    public StructureType<?> type() {
        return ACStructureRegistry.FORLORN_BRIDGE.get();
    }
}