package se.mickelus.tetra.module.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EnumTierData<T extends Enum> {

    public Map<T, Integer> levelMap = new HashMap();

    public Map<T, Float> efficiencyMap = new HashMap();

    public boolean contains(T key) {
        return this.levelMap.containsKey(key);
    }

    public int getLevel(T key) {
        return this.contains(key) ? (Integer) this.levelMap.get(key) : 0;
    }

    public float getEfficiency(T key) {
        return this.efficiencyMap.containsKey(key) ? (Float) this.efficiencyMap.get(key) : 0.0F;
    }

    public Set<T> getValues() {
        return this.levelMap.keySet();
    }
}