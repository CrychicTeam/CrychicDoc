package com.craisinlord.idas.mixin.iceandfire;

import com.craisinlord.idas.IDAS;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ChunkGenerator.class })
public class DisableIaFStructures {

    @Inject(method = { "tryGenerateStructure" }, at = { @At("HEAD") }, cancellable = true)
    void idas_DisableIaFStructures(StructureSet.StructureSelectionEntry structureSetEntry, StructureManager structureManager, RegistryAccess registryAccess, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkAccess chunkAccess, ChunkPos chunkPos, SectionPos sectionPos, CallbackInfoReturnable<Boolean> cir) {
        if (IDAS.CONFIG.general.disableIaFStructures && (structureSetEntry.structure().is(new ResourceLocation("iceandfire:mausoleum")) || structureSetEntry.structure().is(new ResourceLocation("iceandfire:gorgon_temple")) || structureSetEntry.structure().is(new ResourceLocation("iceandfire:graveyard")))) {
            cir.setReturnValue(false);
        }
    }
}