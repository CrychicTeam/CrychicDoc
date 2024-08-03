package com.structureessentials.mixin;

import com.mojang.datafixers.util.Pair;
import com.structureessentials.StructureEssentials;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ChunkGenerator.class })
public class StructureSearchTimeoutMixin {

    @Unique
    private long time = 0L;

    @Unique
    private static long staticTime = 0L;

    @Inject(method = { "getNearestGeneratedStructure(Ljava/util/Set;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/core/BlockPos;ZLnet/minecraft/world/level/levelgen/structure/placement/ConcentricRingsStructurePlacement;)Lcom/mojang/datafixers/util/Pair;" }, at = { @At("HEAD") })
    private void onSearchStart(Set<Holder<Structure>> setHolderStructure0, ServerLevel serverLevel0, StructureManager structureManager1, BlockPos blockPos2, boolean boolean3, ConcentricRingsStructurePlacement concentricRingsStructurePlacement4, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir) {
        this.time = System.currentTimeMillis();
    }

    @Inject(method = { "getNearestGeneratedStructure(Ljava/util/Set;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/core/BlockPos;ZLnet/minecraft/world/level/levelgen/structure/placement/ConcentricRingsStructurePlacement;)Lcom/mojang/datafixers/util/Pair;" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;getStructureGeneratingAt(Ljava/util/Set;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/world/level/StructureManager;ZLnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement;Lnet/minecraft/world/level/ChunkPos;)Lcom/mojang/datafixers/util/Pair;") }, cancellable = true)
    private void onSearchTiming(Set<Holder<Structure>> holderSet, ServerLevel serverLevel0, StructureManager structureManager1, BlockPos blockPos2, boolean boolean3, ConcentricRingsStructurePlacement concentricRingsStructurePlacement4, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir) {
        if (this.time != 0L && System.currentTimeMillis() - this.time > (long) StructureEssentials.config.getCommonConfig().structureSearchTimeout * 1000L) {
            StructureEssentials.LOGGER.info("Structure searched for " + getStructurename(holderSet) + " timed out, took: " + (System.currentTimeMillis() - this.time) / 1000L + " seconds.");
            cir.setReturnValue(null);
        }
    }

    @Inject(method = { "getNearestGeneratedStructure(Ljava/util/Set;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/world/level/StructureManager;IIIZJLnet/minecraft/world/level/levelgen/structure/placement/RandomSpreadStructurePlacement;)Lcom/mojang/datafixers/util/Pair;" }, at = { @At("HEAD") })
    private static void onSearchStartStatic(Set<Holder<Structure>> setHolderStructure0, LevelReader levelReader0, StructureManager structureManager1, int int2, int int3, int int4, boolean boolean5, long long6, RandomSpreadStructurePlacement randomSpreadStructurePlacement7, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir) {
        staticTime = System.currentTimeMillis();
    }

    @Inject(method = { "getNearestGeneratedStructure(Ljava/util/Set;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/world/level/StructureManager;IIIZJLnet/minecraft/world/level/levelgen/structure/placement/RandomSpreadStructurePlacement;)Lcom/mojang/datafixers/util/Pair;" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;getStructureGeneratingAt(Ljava/util/Set;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/world/level/StructureManager;ZLnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement;Lnet/minecraft/world/level/ChunkPos;)Lcom/mojang/datafixers/util/Pair;") })
    private static void onSearchStartStaticTiming(Set<Holder<Structure>> holderSet, LevelReader levelReader0, StructureManager structureManager1, int int2, int int3, int int4, boolean boolean5, long long6, RandomSpreadStructurePlacement randomSpreadStructurePlacement7, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir) {
        if (staticTime != 0L && System.currentTimeMillis() - staticTime > (long) StructureEssentials.config.getCommonConfig().structureSearchTimeout * 1000L) {
            StructureEssentials.LOGGER.info("Structure searched for " + getStructurename(holderSet) + " timed out, took: " + (System.currentTimeMillis() - staticTime) / 1000L + " seconds.");
            cir.setReturnValue(null);
        }
    }

    @Unique
    private static String getStructurename(Set<Holder<Structure>> holderSet) {
        for (Holder<Structure> holder : holderSet) {
            if (holder.unwrapKey().isPresent()) {
                return ((ResourceKey) holder.unwrapKey().get()).location().toString();
            }
        }
        return "unkown structure";
    }
}