package com.structureessentials.mixin;

import com.structureessentials.StructureEssentials;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ PlacedFeature.class })
public abstract class PlacedFeatureErrorMixin {

    PlacedFeature self = (PlacedFeature) this;

    @Shadow
    protected abstract boolean placeWithContext(PlacementContext var1, RandomSource var2, BlockPos var3);

    @Redirect(method = { "place" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;placeWithContext(Lnet/minecraft/world/level/levelgen/placement/PlacementContext;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z"))
    private boolean onPlace(PlacedFeature feature, PlacementContext context, RandomSource randomSource, BlockPos pos) {
        try {
            return this.placeWithContext(context, randomSource, pos);
        } catch (Exception var6) {
            if (this.self.feature() instanceof Holder.Reference) {
                StructureEssentials.LOGGER.warn("Feature: " + ((Holder.Reference) this.self.feature()).key() + " errored during placement at " + pos);
                return false;
            } else {
                StructureEssentials.LOGGER.warn("Unkown feature +" + this.self.feature() + " errored during placement at " + pos, var6);
                return false;
            }
        }
    }

    @Redirect(method = { "placeWithBiomeCheck" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;placeWithContext(Lnet/minecraft/world/level/levelgen/placement/PlacementContext;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z"))
    private boolean onPlaceWithBiome(PlacedFeature feature, PlacementContext context, RandomSource randomSource, BlockPos pos) {
        try {
            return this.placeWithContext(context, randomSource, pos);
        } catch (Exception var6) {
            if (this.self.feature() instanceof Holder.Reference) {
                StructureEssentials.LOGGER.warn("Feature: " + ((Holder.Reference) this.self.feature()).key() + " errored during placement at " + pos);
                return false;
            } else {
                StructureEssentials.LOGGER.warn("Unkown feature" + this.self.feature() + " errored during placement at " + pos, var6);
                return false;
            }
        }
    }
}