package com.craisinlord.integrated_villages.mixins;

import com.craisinlord.integrated_villages.IntegratedVillages;
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
public class DisableVanillaVillagesMixin {

    @Inject(method = { "tryGenerateStructure" }, at = { @At("HEAD") }, cancellable = true)
    void integrated_villages_disableVanillaVillages(StructureSet.StructureSelectionEntry structureSetEntry, StructureManager structureManager, RegistryAccess registryAccess, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkAccess chunkAccess, ChunkPos chunkPos, SectionPos sectionPos, CallbackInfoReturnable<Boolean> cir) {
        if (IntegratedVillages.CONFIG.general.disableVanillaVillages && (structureSetEntry.structure().is(new ResourceLocation("village_desert")) || structureSetEntry.structure().is(new ResourceLocation("village_plains")) || structureSetEntry.structure().is(new ResourceLocation("village_snowy")) || structureSetEntry.structure().is(new ResourceLocation("village_savanna")) || structureSetEntry.structure().is(new ResourceLocation("village_taiga")) || structureSetEntry.structure().is(new ResourceLocation("terralith:fortified_desert_village")) || structureSetEntry.structure().is(new ResourceLocation("terralith:fortified_village")))) {
            cir.setReturnValue(false);
        }
    }
}