package org.embeddedt.modernfix.common.mixin.perf.faster_structure_location;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheck;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.embeddedt.modernfix.duck.IStructureCheck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ StructureCheck.class })
public class StructureCheckMixin implements IStructureCheck {

    @Shadow
    @Final
    private Registry<Structure> structureConfigs;

    private ChunkGeneratorStructureState mfix$structureState;

    @Override
    public void mfix$setStructureState(ChunkGeneratorStructureState state) {
        this.mfix$structureState = state;
    }

    @ModifyExpressionValue(method = { "checkStart" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/StructureCheck;tryLoadFromStorage(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/levelgen/structure/Structure;ZJ)Lnet/minecraft/world/level/levelgen/structure/StructureCheckResult;") })
    private StructureCheckResult mfix$checkForValidPosition(StructureCheckResult storageResult, ChunkPos chunkPos, Structure structure, boolean skipKnownStructures) {
        if (storageResult != null) {
            return storageResult;
        } else if (this.mfix$structureState != null) {
            Holder<Structure> structureHolder = this.structureConfigs.wrapAsHolder(structure);
            for (StructurePlacement placement : this.mfix$structureState.getPlacementsForStructure(structureHolder)) {
                if (placement.isStructureChunk(this.mfix$structureState, chunkPos.x, chunkPos.z)) {
                    return null;
                }
            }
            return StructureCheckResult.START_NOT_PRESENT;
        } else {
            return null;
        }
    }
}