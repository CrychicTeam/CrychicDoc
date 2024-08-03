package com.craisinlord.integrated_api.mixins.features;

import com.craisinlord.integrated_api.mixins.world.WorldGenRegionAccessor;
import com.craisinlord.integrated_api.modinit.IATags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ GeodeFeature.class })
public class NoGeodesInStructuresMixin {

    @Inject(method = { "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z" }, at = { @At("HEAD") }, cancellable = true)
    private void integrated_api_noGeodes(FeaturePlaceContext<BlockStateConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        if (context.level() instanceof WorldGenRegion) {
            Registry<Structure> configuredStructureFeatureRegistry = context.level().m_9598_().registryOrThrow(Registries.STRUCTURE);
            StructureManager structureManager = ((WorldGenRegionAccessor) context.level()).getStructureManager();
            for (Holder<Structure> configuredStructureFeature : configuredStructureFeatureRegistry.getOrCreateTag(IATags.LARGER_LOCATE_SEARCH)) {
                if (structureManager.getStructureAt(context.origin(), configuredStructureFeature.value()).isValid()) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }
}