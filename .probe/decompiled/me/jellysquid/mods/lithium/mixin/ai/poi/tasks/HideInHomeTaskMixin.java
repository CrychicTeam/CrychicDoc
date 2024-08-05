package me.jellysquid.mods.lithium.mixin.ai.poi.tasks;

import java.util.Optional;
import java.util.function.Predicate;
import me.jellysquid.mods.lithium.common.util.POIRegistryEntries;
import me.jellysquid.mods.lithium.common.world.interests.iterator.SinglePointOfInterestTypeFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.behavior.LocateHidingPlace;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ LocateHidingPlace.class })
public class HideInHomeTaskMixin {

    @Redirect(method = { "method_46979", "m_289100_" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/poi/PointOfInterestStorage;getPosition(Ljava/util/function/Predicate;Ljava/util/function/Predicate;Lnet/minecraft/world/poi/PointOfInterestStorage$OccupationStatus;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/util/math/random/Random;)Ljava/util/Optional;"))
    private static Optional<BlockPos> useFasterPOILookup(PoiManager pointOfInterestStorage, Predicate<Holder<PoiType>> typePredicate, Predicate<BlockPos> positionPredicate, PoiManager.Occupancy occupationStatus, BlockPos pos, int radius, RandomSource random) {
        return pointOfInterestStorage.getRandom(new SinglePointOfInterestTypeFilter(POIRegistryEntries.HOME_ENTRY), positionPredicate, occupationStatus, pos, radius, random);
    }

    @Redirect(method = { "method_46978", "m_289098_" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/poi/PointOfInterestStorage;getPosition(Ljava/util/function/Predicate;Ljava/util/function/Predicate;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/world/poi/PointOfInterestStorage$OccupationStatus;)Ljava/util/Optional;"))
    private static Optional<BlockPos> useFasterPOILookup(PoiManager pointOfInterestStorage, Predicate<Holder<PoiType>> typePredicate, Predicate<BlockPos> posPredicate, BlockPos pos, int radius, PoiManager.Occupancy occupationStatus) {
        return pointOfInterestStorage.find(new SinglePointOfInterestTypeFilter(POIRegistryEntries.HOME_ENTRY), posPredicate, pos, radius, occupationStatus);
    }
}