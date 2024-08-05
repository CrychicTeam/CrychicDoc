package com.github.alexthe666.iceandfire.mixin.gen;

import com.github.alexthe666.iceandfire.datagen.IafStructures;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LakeFeature.class })
public class NoLakesInStructuresMixin {

    @Inject(method = { "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z" }, at = { @At("HEAD") }, cancellable = true)
    private void iaf_noLakesInMausoleum(FeaturePlaceContext<BlockStateConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        if (context.level() instanceof WorldGenRegion) {
            Registry<Structure> configuredStructureFeatureRegistry = context.level().m_9598_().registryOrThrow(Registries.STRUCTURE);
            StructureManager structureManager = context.level().m_6018_().structureManager();
            for (Optional<Structure> structure : List.of(configuredStructureFeatureRegistry.getOptional(IafStructures.MAUSOLEUM), configuredStructureFeatureRegistry.getOptional(IafStructures.GRAVEYARD), configuredStructureFeatureRegistry.getOptional(IafStructures.GORGON_TEMPLE))) {
                if (structure.isPresent() && structureManager.getStructureAt(context.origin(), (Structure) structure.get()).isValid()) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }
}