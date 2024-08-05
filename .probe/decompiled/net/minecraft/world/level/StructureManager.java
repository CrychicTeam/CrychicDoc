package net.minecraft.world.level;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.StructureAccess;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheck;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public class StructureManager {

    private final LevelAccessor level;

    private final WorldOptions worldOptions;

    private final StructureCheck structureCheck;

    public StructureManager(LevelAccessor levelAccessor0, WorldOptions worldOptions1, StructureCheck structureCheck2) {
        this.level = levelAccessor0;
        this.worldOptions = worldOptions1;
        this.structureCheck = structureCheck2;
    }

    public StructureManager forWorldGenRegion(WorldGenRegion worldGenRegion0) {
        if (worldGenRegion0.getLevel() != this.level) {
            throw new IllegalStateException("Using invalid structure manager (source level: " + worldGenRegion0.getLevel() + ", region: " + worldGenRegion0);
        } else {
            return new StructureManager(worldGenRegion0, this.worldOptions, this.structureCheck);
        }
    }

    public List<StructureStart> startsForStructure(ChunkPos chunkPos0, Predicate<Structure> predicateStructure1) {
        Map<Structure, LongSet> $$2 = this.level.m_46819_(chunkPos0.x, chunkPos0.z, ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
        Builder<StructureStart> $$3 = ImmutableList.builder();
        for (Entry<Structure, LongSet> $$4 : $$2.entrySet()) {
            Structure $$5 = (Structure) $$4.getKey();
            if (predicateStructure1.test($$5)) {
                this.fillStartsForStructure($$5, (LongSet) $$4.getValue(), $$3::add);
            }
        }
        return $$3.build();
    }

    public List<StructureStart> startsForStructure(SectionPos sectionPos0, Structure structure1) {
        LongSet $$2 = this.level.m_46819_(sectionPos0.x(), sectionPos0.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForStructure(structure1);
        Builder<StructureStart> $$3 = ImmutableList.builder();
        this.fillStartsForStructure(structure1, $$2, $$3::add);
        return $$3.build();
    }

    public void fillStartsForStructure(Structure structure0, LongSet longSet1, Consumer<StructureStart> consumerStructureStart2) {
        LongIterator var4 = longSet1.iterator();
        while (var4.hasNext()) {
            long $$3 = (Long) var4.next();
            SectionPos $$4 = SectionPos.of(new ChunkPos($$3), this.level.m_151560_());
            StructureStart $$5 = this.getStartForStructure($$4, structure0, this.level.m_46819_($$4.x(), $$4.z(), ChunkStatus.STRUCTURE_STARTS));
            if ($$5 != null && $$5.isValid()) {
                consumerStructureStart2.accept($$5);
            }
        }
    }

    @Nullable
    public StructureStart getStartForStructure(SectionPos sectionPos0, Structure structure1, StructureAccess structureAccess2) {
        return structureAccess2.getStartForStructure(structure1);
    }

    public void setStartForStructure(SectionPos sectionPos0, Structure structure1, StructureStart structureStart2, StructureAccess structureAccess3) {
        structureAccess3.setStartForStructure(structure1, structureStart2);
    }

    public void addReferenceForStructure(SectionPos sectionPos0, Structure structure1, long long2, StructureAccess structureAccess3) {
        structureAccess3.addReferenceForStructure(structure1, long2);
    }

    public boolean shouldGenerateStructures() {
        return this.worldOptions.generateStructures();
    }

    public StructureStart getStructureAt(BlockPos blockPos0, Structure structure1) {
        for (StructureStart $$2 : this.startsForStructure(SectionPos.of(blockPos0), structure1)) {
            if ($$2.getBoundingBox().isInside(blockPos0)) {
                return $$2;
            }
        }
        return StructureStart.INVALID_START;
    }

    public StructureStart getStructureWithPieceAt(BlockPos blockPos0, ResourceKey<Structure> resourceKeyStructure1) {
        Structure $$2 = this.registryAccess().registryOrThrow(Registries.STRUCTURE).get(resourceKeyStructure1);
        return $$2 == null ? StructureStart.INVALID_START : this.getStructureWithPieceAt(blockPos0, $$2);
    }

    public StructureStart getStructureWithPieceAt(BlockPos blockPos0, TagKey<Structure> tagKeyStructure1) {
        Registry<Structure> $$2 = this.registryAccess().registryOrThrow(Registries.STRUCTURE);
        for (StructureStart $$3 : this.startsForStructure(new ChunkPos(blockPos0), p_258967_ -> (Boolean) $$2.getHolder($$2.getId(p_258967_)).map(p_248425_ -> p_248425_.is(tagKeyStructure1)).orElse(false))) {
            if (this.structureHasPieceAt(blockPos0, $$3)) {
                return $$3;
            }
        }
        return StructureStart.INVALID_START;
    }

    public StructureStart getStructureWithPieceAt(BlockPos blockPos0, Structure structure1) {
        for (StructureStart $$2 : this.startsForStructure(SectionPos.of(blockPos0), structure1)) {
            if (this.structureHasPieceAt(blockPos0, $$2)) {
                return $$2;
            }
        }
        return StructureStart.INVALID_START;
    }

    public boolean structureHasPieceAt(BlockPos blockPos0, StructureStart structureStart1) {
        for (StructurePiece $$2 : structureStart1.getPieces()) {
            if ($$2.getBoundingBox().isInside(blockPos0)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnyStructureAt(BlockPos blockPos0) {
        SectionPos $$1 = SectionPos.of(blockPos0);
        return this.level.m_46819_($$1.x(), $$1.z(), ChunkStatus.STRUCTURE_REFERENCES).hasAnyStructureReferences();
    }

    public Map<Structure, LongSet> getAllStructuresAt(BlockPos blockPos0) {
        SectionPos $$1 = SectionPos.of(blockPos0);
        return this.level.m_46819_($$1.x(), $$1.z(), ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
    }

    public StructureCheckResult checkStructurePresence(ChunkPos chunkPos0, Structure structure1, boolean boolean2) {
        return this.structureCheck.checkStart(chunkPos0, structure1, boolean2);
    }

    public void addReference(StructureStart structureStart0) {
        structureStart0.addReference();
        this.structureCheck.incrementReference(structureStart0.getChunkPos(), structureStart0.getStructure());
    }

    public RegistryAccess registryAccess() {
        return this.level.m_9598_();
    }
}