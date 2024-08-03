package se.mickelus.tetra.module.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TierData<T> {

    public Map<T, Float> levelMap = new HashMap();

    public Map<T, Float> efficiencyMap = new HashMap();

    public boolean contains(T key) {
        return this.levelMap.containsKey(key);
    }

    public int getLevel(T key) {
        return this.contains(key) ? Math.round((Float) this.levelMap.get(key)) : 0;
    }

    public Map<T, Integer> getLevelMap() {
        return (Map<T, Integer>) this.levelMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> Math.round((Float) e.getValue())));
    }

    public float getEfficiency(T key) {
        return this.efficiencyMap.containsKey(key) ? (Float) this.efficiencyMap.get(key) : 0.0F;
    }

    public Set<T> getValues() {
        return (Set<T>) Stream.concat(this.levelMap.entrySet().stream().filter(entry -> (Float) entry.getValue() > 0.0F), this.efficiencyMap.entrySet().stream().filter(entry -> (Float) entry.getValue() > 0.0F)).map(Entry::getKey).collect(Collectors.toUnmodifiableSet());
    }
}