package com.structureessentials.mixin;

import com.mojang.datafixers.util.Pair;
import com.structureessentials.StructureEssentials;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ChunkGenerator.class })
public class StructureSearchSpeedupMixin {

    @Inject(method = { "getStructureGeneratingAt" }, at = { @At("HEAD") }, cancellable = true)
    private static void onFind(Set<Holder<Structure>> holderSet, LevelReader level, StructureManager structureManager, boolean load, StructurePlacement placement, ChunkPos pos, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir) {
        if (!holderSet.isEmpty() && StructureEssentials.config.getCommonConfig().useFastStructureLookup) {
            boolean found = false;
            int[] yLevels = Mth.outFromOrigin(65, level.getMinBuildHeight() + 1, level.m_151558_(), 64).toArray();
            BlockPos worldPos = pos.getWorldPosition();
            label37: for (int i = 0; i < 4; i++) {
                int xQuart = QuartPos.fromBlock(worldPos.m_123341_() + i * 4);
                int zQuart = QuartPos.fromBlock(worldPos.m_123343_() + i * 4);
                for (int yBlock : yLevels) {
                    int yQuart = QuartPos.fromBlock(yBlock);
                    Holder<Biome> holder = ((ServerLevel) level).getChunkSource().getGenerator().getBiomeSource().getNoiseBiome(xQuart, yQuart, zQuart, ((ServerLevel) level).getChunkSource().randomState().sampler());
                    for (Holder<Structure> structureHolder : holderSet) {
                        if (structureHolder.value().biomes().contains(holder)) {
                            found = true;
                            break label37;
                        }
                    }
                }
            }
            if (!found) {
                cir.setReturnValue(null);
            }
        }
    }
}