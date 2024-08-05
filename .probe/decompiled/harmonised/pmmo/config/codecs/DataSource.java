package harmonised.pmmo.config.codecs;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ReqType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface DataSource<T> {

    T combine(T var1);

    boolean isUnconfigured();

    default Set<String> getTagValues() {
        return Set.of();
    }

    default Map<String, Long> getXpValues(EventType type, CompoundTag nbt) {
        return new HashMap();
    }

    default void setXpValues(EventType type, Map<String, Long> award) {
    }

    default Map<String, Double> getBonuses(ModifierDataType type, CompoundTag nbt) {
        return new HashMap();
    }

    default void setBonuses(ModifierDataType type, Map<String, Double> bonuses) {
    }

    default Map<String, Integer> getReqs(ReqType type, CompoundTag nbt) {
        return new HashMap();
    }

    default void setReqs(ReqType type, Map<String, Integer> reqs) {
    }

    default Map<ResourceLocation, Integer> getNegativeEffect() {
        return new HashMap();
    }

    default void setNegativeEffects(Map<ResourceLocation, Integer> neg) {
    }

    default Map<ResourceLocation, Integer> getPositiveEffect() {
        return new HashMap();
    }

    default void setPositiveEffects(Map<ResourceLocation, Integer> pos) {
    }

    static <K, V> HashMap<K, V> clearEmptyValues(Map<K, V> map) {
        HashMap<K, V> outMap = new HashMap();
        map.forEach((key, value) -> {
            boolean isEmpty = false;
            if (value instanceof Collection) {
                isEmpty = ((Collection) value).isEmpty();
            } else if (value instanceof Map) {
                isEmpty = ((Map) value).isEmpty();
            }
            if (!isEmpty) {
                outMap.put(key, value);
            }
        });
        return outMap;
    }
}