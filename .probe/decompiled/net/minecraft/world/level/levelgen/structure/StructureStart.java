package net.minecraft.world.level.levelgen.structure;

import com.mojang.logging.LogUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
import org.slf4j.Logger;

public final class StructureStart {

    public static final String INVALID_START_ID = "INVALID";

    public static final StructureStart INVALID_START = new StructureStart(null, new ChunkPos(0, 0), 0, new PiecesContainer(List.of()));

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Structure structure;

    private final PiecesContainer pieceContainer;

    private final ChunkPos chunkPos;

    private int references;

    @Nullable
    private volatile BoundingBox cachedBoundingBox;

    public StructureStart(Structure structure0, ChunkPos chunkPos1, int int2, PiecesContainer piecesContainer3) {
        this.structure = structure0;
        this.chunkPos = chunkPos1;
        this.references = int2;
        this.pieceContainer = piecesContainer3;
    }

    @Nullable
    public static StructureStart loadStaticStart(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1, long long2) {
        String $$3 = compoundTag1.getString("id");
        if ("INVALID".equals($$3)) {
            return INVALID_START;
        } else {
            Registry<Structure> $$4 = structurePieceSerializationContext0.registryAccess().registryOrThrow(Registries.STRUCTURE);
            Structure $$5 = $$4.get(new ResourceLocation($$3));
            if ($$5 == null) {
                LOGGER.error("Unknown stucture id: {}", $$3);
                return null;
            } else {
                ChunkPos $$6 = new ChunkPos(compoundTag1.getInt("ChunkX"), compoundTag1.getInt("ChunkZ"));
                int $$7 = compoundTag1.getInt("references");
                ListTag $$8 = compoundTag1.getList("Children", 10);
                try {
                    PiecesContainer $$9 = PiecesContainer.load($$8, structurePieceSerializationContext0);
                    if ($$5 instanceof OceanMonumentStructure) {
                        $$9 = OceanMonumentStructure.regeneratePiecesAfterLoad($$6, long2, $$9);
                    }
                    return new StructureStart($$5, $$6, $$7, $$9);
                } catch (Exception var11) {
                    LOGGER.error("Failed Start with id {}", $$3, var11);
                    return null;
                }
            }
        }
    }

    public BoundingBox getBoundingBox() {
        BoundingBox $$0 = this.cachedBoundingBox;
        if ($$0 == null) {
            $$0 = this.structure.adjustBoundingBox(this.pieceContainer.calculateBoundingBox());
            this.cachedBoundingBox = $$0;
        }
        return $$0;
    }

    public void placeInChunk(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5) {
        List<StructurePiece> $$6 = this.pieceContainer.pieces();
        if (!$$6.isEmpty()) {
            BoundingBox $$7 = ((StructurePiece) $$6.get(0)).boundingBox;
            BlockPos $$8 = $$7.getCenter();
            BlockPos $$9 = new BlockPos($$8.m_123341_(), $$7.minY(), $$8.m_123343_());
            for (StructurePiece $$10 : $$6) {
                if ($$10.getBoundingBox().intersects(boundingBox4)) {
                    $$10.postProcess(worldGenLevel0, structureManager1, chunkGenerator2, randomSource3, boundingBox4, chunkPos5, $$9);
                }
            }
            this.structure.afterPlace(worldGenLevel0, structureManager1, chunkGenerator2, randomSource3, boundingBox4, chunkPos5, this.pieceContainer);
        }
    }

    public CompoundTag createTag(StructurePieceSerializationContext structurePieceSerializationContext0, ChunkPos chunkPos1) {
        CompoundTag $$2 = new CompoundTag();
        if (this.isValid()) {
            $$2.putString("id", structurePieceSerializationContext0.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(this.structure).toString());
            $$2.putInt("ChunkX", chunkPos1.x);
            $$2.putInt("ChunkZ", chunkPos1.z);
            $$2.putInt("references", this.references);
            $$2.put("Children", this.pieceContainer.save(structurePieceSerializationContext0));
            return $$2;
        } else {
            $$2.putString("id", "INVALID");
            return $$2;
        }
    }

    public boolean isValid() {
        return !this.pieceContainer.isEmpty();
    }

    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    public boolean canBeReferenced() {
        return this.references < this.getMaxReferences();
    }

    public void addReference() {
        this.references++;
    }

    public int getReferences() {
        return this.references;
    }

    protected int getMaxReferences() {
        return 1;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public List<StructurePiece> getPieces() {
        return this.pieceContainer.pieces();
    }
}