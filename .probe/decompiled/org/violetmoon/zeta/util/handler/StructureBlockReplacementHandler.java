package org.violetmoon.zeta.util.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.jetbrains.annotations.Nullable;

public class StructureBlockReplacementHandler {

    private static Set<StructureBlockReplacementHandler.StructureFunction> functions = new HashSet();

    private static final ThreadLocal<StructureBlockReplacementHandler.StructureHolder> structureHolder = new ThreadLocal();

    public static void addReplacement(StructureBlockReplacementHandler.StructureFunction func) {
        functions.add(func);
    }

    @Nullable
    public static Holder<Structure> getStructure(ServerLevelAccessor accessor, StructureBlockReplacementHandler.StructureHolder structure) {
        Optional<? extends Registry<Structure>> registry = accessor.m_9598_().registry(Registries.STRUCTURE);
        Optional<Holder<Structure>> holder = registry.flatMap(reg -> reg.getResourceKey(structure.currentStructure).flatMap(reg::m_203636_));
        return holder.isEmpty() ? null : (Holder) holder.get();
    }

    @Nullable
    public static ResourceKey<Structure> getStructureKey(ServerLevelAccessor accessor, StructureBlockReplacementHandler.StructureHolder structure) {
        Optional<ResourceKey<Structure>> res = accessor.m_9598_().registry(Registries.STRUCTURE).flatMap(it -> it.getResourceKey(structure.currentStructure));
        return res.isEmpty() ? null : (ResourceKey) res.get();
    }

    @Nullable
    public static ResourceLocation getStructureRes(ServerLevelAccessor accessor, StructureBlockReplacementHandler.StructureHolder structure) {
        Optional<ResourceLocation> res = accessor.m_9598_().registry(Registries.STRUCTURE).map(it -> it.getKey(structure.currentStructure));
        return res.isEmpty() ? null : (ResourceLocation) res.get();
    }

    public static boolean isStructure(ServerLevelAccessor accessor, StructureBlockReplacementHandler.StructureHolder structure, ResourceKey<Structure> target) {
        ResourceKey<Structure> curr = getStructureKey(accessor, structure);
        return curr != null && curr.equals(target);
    }

    public static BlockState getResultingBlockState(ServerLevelAccessor level, BlockState blockstate) {
        StructureBlockReplacementHandler.StructureHolder curr = getCurrentStructureHolder();
        if (curr != null && curr.currentStructure != null) {
            for (StructureBlockReplacementHandler.StructureFunction fun : functions) {
                BlockState res = fun.transformBlockstate(level, blockstate, curr);
                if (res != null) {
                    return res;
                }
            }
        }
        return blockstate;
    }

    private static StructureBlockReplacementHandler.StructureHolder getCurrentStructureHolder() {
        return (StructureBlockReplacementHandler.StructureHolder) structureHolder.get();
    }

    public static void setActiveStructure(Structure structure, PiecesContainer components) {
        StructureBlockReplacementHandler.StructureHolder curr = getCurrentStructureHolder();
        if (curr == null) {
            curr = new StructureBlockReplacementHandler.StructureHolder();
            structureHolder.set(curr);
        }
        curr.currentStructure = structure;
        curr.currentComponents = components == null ? null : components.pieces();
    }

    @FunctionalInterface
    public interface StructureFunction {

        BlockState transformBlockstate(ServerLevelAccessor var1, BlockState var2, StructureBlockReplacementHandler.StructureHolder var3);
    }

    public static class StructureHolder {

        public Structure currentStructure;

        public List<StructurePiece> currentComponents;
    }
}