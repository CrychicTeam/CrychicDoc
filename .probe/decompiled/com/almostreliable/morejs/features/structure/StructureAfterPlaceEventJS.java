package com.almostreliable.morejs.features.structure;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.phys.AABB;

public class StructureAfterPlaceEventJS extends LevelEventJS {

    private final Structure structure;

    private final WorldGenLevel worldGenLevel;

    private final StructureManager structureManager;

    private final ChunkGenerator chunkGenerator;

    private final RandomSource randomSource;

    private final BoundingBox boundingBox;

    private final ChunkPos chunkPos;

    private final PiecesContainer piecesContainer;

    private Map<StructurePiece, BoundingBox> intersectionMap;

    public StructureAfterPlaceEventJS(Structure structure, WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        this.structure = structure;
        this.worldGenLevel = worldGenLevel;
        this.structureManager = structureManager;
        this.chunkGenerator = chunkGenerator;
        this.randomSource = randomSource;
        this.boundingBox = boundingBox;
        this.chunkPos = chunkPos;
        this.piecesContainer = piecesContainer;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public StructureManager getStructureManager() {
        return this.structureManager;
    }

    public ChunkGenerator getChunkGenerator() {
        return this.chunkGenerator;
    }

    public RandomSource getRandomSource() {
        return this.randomSource;
    }

    public BoundingBox getChunkBoundingBox() {
        return this.boundingBox;
    }

    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    public PiecesContainer getPiecesContainer() {
        return this.piecesContainer;
    }

    public BoundingBox getStructureBoundingBox() {
        return this.piecesContainer.calculateBoundingBox();
    }

    public ServerLevel getLevel() {
        return this.worldGenLevel.m_6018_();
    }

    public WorldGenLevel getWorldGenLevel() {
        return this.worldGenLevel;
    }

    public ResourceLocation getPieceType(StructurePieceType pieceType) {
        return (ResourceLocation) Objects.requireNonNull(BuiltInRegistries.STRUCTURE_PIECE.getKey(pieceType));
    }

    public ResourceLocation getId() {
        return (ResourceLocation) Objects.requireNonNull(this.structureManager.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(this.structure));
    }

    public String getGenStep() {
        return this.structure.step().getName();
    }

    public Collection<BoundingBox> getIntersectionBoxes() {
        return this.getIntersectionMap().values();
    }

    public Collection<StructurePiece> getIntersectionPieces() {
        return this.getIntersectionMap().keySet();
    }

    public Map<StructurePiece, BoundingBox> getIntersectionMap() {
        if (this.intersectionMap == null) {
            Map<StructurePiece, BoundingBox> map = new HashMap();
            for (StructurePiece sp : this.piecesContainer.pieces()) {
                if (this.boundingBox.intersects(sp.getBoundingBox())) {
                    AABB aabb = AABB.of(this.boundingBox).intersect(AABB.of(sp.getBoundingBox()));
                    BoundingBox bb = new BoundingBox((int) aabb.minX, (int) aabb.minY, (int) aabb.minZ, (int) aabb.maxX - 1, (int) aabb.maxY - 1, (int) aabb.maxZ - 1);
                    map.put(sp, bb);
                }
            }
            this.intersectionMap = map;
        }
        return this.intersectionMap;
    }

    public ResourceLocation getType() {
        return (ResourceLocation) Objects.requireNonNull(BuiltInRegistries.STRUCTURE_TYPE.getKey(this.structure.type()));
    }
}