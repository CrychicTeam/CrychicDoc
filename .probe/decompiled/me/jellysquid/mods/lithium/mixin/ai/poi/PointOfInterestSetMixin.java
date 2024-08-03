package me.jellysquid.mods.lithium.mixin.ai.poi;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMaps;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import me.jellysquid.mods.lithium.common.world.interests.PointOfInterestSetExtended;
import me.jellysquid.mods.lithium.common.world.interests.iterator.SinglePointOfInterestTypeFilter;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiSection;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PoiSection.class })
public class PointOfInterestSetMixin implements PointOfInterestSetExtended {

    @Mutable
    @Shadow
    @Final
    private Map<Holder<PoiType>, Set<PoiRecord>> byType;

    private static <K, V> Iterable<? extends Entry<K, V>> getPointsByTypeIterator(Map<K, V> map) {
        return (Iterable<? extends Entry<K, V>>) (map instanceof Reference2ReferenceMap ? Reference2ReferenceMaps.fastIterable((Reference2ReferenceMap) map) : map.entrySet());
    }

    @Inject(method = { "<init>(Ljava/lang/Runnable;ZLjava/util/List;)V" }, at = { @At("RETURN") })
    private void reinit(Runnable updateListener, boolean bl, List<PoiRecord> list, CallbackInfo ci) {
        this.byType = new Reference2ReferenceOpenHashMap(this.byType);
    }

    @Override
    public void collectMatchingPoints(Predicate<Holder<PoiType>> type, PoiManager.Occupancy status, Consumer<PoiRecord> consumer) {
        if (type instanceof SinglePointOfInterestTypeFilter) {
            this.getWithSingleTypeFilter(((SinglePointOfInterestTypeFilter) type).getType(), status, consumer);
        } else {
            this.getWithDynamicTypeFilter(type, status, consumer);
        }
    }

    private void getWithDynamicTypeFilter(Predicate<Holder<PoiType>> type, PoiManager.Occupancy status, Consumer<PoiRecord> consumer) {
        for (Entry<Holder<PoiType>, Set<PoiRecord>> entry : getPointsByTypeIterator(this.byType)) {
            if (type.test((Holder) entry.getKey()) && !((Set) entry.getValue()).isEmpty()) {
                for (PoiRecord poi : (Set) entry.getValue()) {
                    if (status.getTest().test(poi)) {
                        consumer.accept(poi);
                    }
                }
            }
        }
    }

    private void getWithSingleTypeFilter(Holder<PoiType> type, PoiManager.Occupancy status, Consumer<PoiRecord> consumer) {
        Set<PoiRecord> entries = (Set<PoiRecord>) this.byType.get(type);
        if (entries != null && !entries.isEmpty()) {
            for (PoiRecord poi : entries) {
                if (status.getTest().test(poi)) {
                    consumer.accept(poi);
                }
            }
        }
    }

    @Redirect(method = { "add(Lnet/minecraft/world/poi/PointOfInterest;)Z" }, at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"))
    private <K, V> K computeIfAbsent(Map<K, V> map, K key, Function<? super K, ? extends V> mappingFunction) {
        return (K) map.computeIfAbsent(key, o -> new ObjectOpenHashSet());
    }
}