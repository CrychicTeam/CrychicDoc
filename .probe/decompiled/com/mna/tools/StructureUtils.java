package com.mna.tools;

import com.mna.api.tools.MATags;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public class StructureUtils {

    public static boolean isPointInStructure(ServerLevel world, BlockPos pos) {
        return world.structureManager().hasAnyStructureAt(pos);
    }

    public static boolean isPointInStructure(ServerLevel world, BlockPos pos, ResourceLocation structureID, int pieceIndex) {
        Structure csf = MATags.getStructure(world, structureID);
        return csf == null ? false : world.structureManager().getAllStructuresAt(pos).entrySet().stream().filter(entry -> csf == entry.getKey()).anyMatch(entry -> {
            StructureStart start = world.structureManager().getStructureAt(pos, (Structure) entry.getKey());
            if (start == null) {
                return false;
            } else if (start.getPieces().size() == 0) {
                return true;
            } else {
                if (pieceIndex < 0) {
                    for (StructurePiece piece : start.getPieces()) {
                        if (piece.getBoundingBox().isInside(pos)) {
                            return true;
                        }
                    }
                } else if (pieceIndex < start.getPieces().size()) {
                    return ((StructurePiece) start.getPieces().get(pieceIndex)).getBoundingBox().isInside(pos);
                }
                return false;
            }
        });
    }

    public static boolean isPointInAnyStructure(ServerLevel world, BlockPos pos, ResourceLocation structureTagID) {
        List<Holder<Structure>> taggedStructures = MATags.getStructureContents(world, structureTagID);
        return taggedStructures.size() == 0 ? false : world.structureManager().getAllStructuresAt(pos).entrySet().stream().filter(entry -> taggedStructures.stream().anyMatch(structure -> structure.value() == entry.getKey())).anyMatch(entry -> {
            StructureStart start = world.structureManager().getStructureAt(pos, (Structure) entry.getKey());
            if (start == null) {
                return false;
            } else if (start.getPieces().size() == 0) {
                return true;
            } else {
                for (StructurePiece piece : start.getPieces()) {
                    if (piece.getBoundingBox().isInside(pos)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public static BoundingBox getStructureBoundingBoxAt(ServerLevel world, BlockPos pos, ResourceLocation structureID, int subPieceIndex) {
        Structure csf = MATags.getStructure(world, structureID);
        if (csf == null) {
            return null;
        } else {
            StructureStart start = world.structureManager().getStructureWithPieceAt(pos, csf);
            if (start == null || start.getPieces().size() == 0) {
                return null;
            } else {
                return subPieceIndex > -1 && subPieceIndex < start.getPieces().size() ? ((StructurePiece) start.getPieces().get(subPieceIndex)).getBoundingBox() : start.getBoundingBox();
            }
        }
    }

    public static List<Holder<Structure>> getAllStructures(ServerLevel world) {
        List<Holder<Structure>> result = new ArrayList();
        Registry<Structure> registry = world.m_9598_().registryOrThrow(Registries.STRUCTURE);
        registry.asHolderIdMap().forEach(holder -> result.add(holder));
        return result;
    }

    public static List<ResourceLocation> getAllStructureIDs(ServerLevel world) {
        List<ResourceLocation> result = new ArrayList();
        Registry<Structure> registry = world.m_9598_().registryOrThrow(Registries.STRUCTURE);
        registry.entrySet().forEach(entry -> result.add(((ResourceKey) entry.getKey()).location()));
        return result;
    }

    @Nullable
    public static ResourceLocation getStructureID(ServerLevel world, Holder<Structure> structure) {
        if (!structure.isBound()) {
            return null;
        } else {
            Registry<Structure> registry = world.m_9598_().registryOrThrow(Registries.STRUCTURE);
            return registry.getKey((Structure) structure.get());
        }
    }
}