package se.mickelus.tetra.blocks.holo;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public class ScanHelper {

    public static boolean hasStructure(String id, ServerLevel level, ChunkPos chunkPos) {
        Registry<Structure> registry = level.m_9598_().registryOrThrow(Registries.STRUCTURE);
        HolderSet.ListBacked<Structure> holders = (HolderSet.ListBacked<Structure>) getHolders(getKey(id), registry).get();
        return !hasStructure(holders, level, level.structureManager(), false, chunkPos).isEmpty();
    }

    private static Set<Holder<Structure>> hasStructure(HolderSet.ListBacked<Structure> holders, LevelReader level, StructureManager structureManager, boolean skipGenerated, ChunkPos chunkPos) {
        return (Set<Holder<Structure>>) holders.stream().filter(holder -> hasStructure(holder, level, structureManager, skipGenerated, chunkPos)).collect(Collectors.toUnmodifiableSet());
    }

    private static boolean hasStructure(Holder<Structure> holder, LevelReader level, StructureManager structureManager, boolean skipGenerated, ChunkPos chunkPos) {
        StructureCheckResult result = structureManager.checkStructurePresence(chunkPos, holder.value(), skipGenerated);
        if (result != StructureCheckResult.START_NOT_PRESENT) {
            if (!skipGenerated && result == StructureCheckResult.START_PRESENT) {
                return true;
            }
            ChunkAccess chunk = level.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS);
            StructureStart structurestart = structureManager.getStartForStructure(SectionPos.bottomOf(chunk), holder.value(), chunk);
            if (structurestart != null && structurestart.isValid()) {
                return true;
            }
        }
        return false;
    }

    private static Either<ResourceKey<Structure>, TagKey<Structure>> getKey(String identifier) {
        return identifier.startsWith("#") ? Either.right(TagKey.create(Registries.STRUCTURE, new ResourceLocation(identifier.substring(1)))) : Either.left(ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(identifier)));
    }

    private static Optional<? extends HolderSet.ListBacked<Structure>> getHolders(Either<ResourceKey<Structure>, TagKey<Structure>> key, Registry<Structure> registry) {
        return (Optional<? extends HolderSet.ListBacked<Structure>>) key.map(p_214494_ -> registry.getHolder(p_214494_).map(xva$0 -> HolderSet.direct(xva$0)), registry::m_203431_);
    }
}