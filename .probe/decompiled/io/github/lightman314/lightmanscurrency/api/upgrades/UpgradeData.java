package io.github.lightman314.lightmanscurrency.api.upgrades;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;

public class UpgradeData {

    public static final UpgradeData EMPTY = new UpgradeData();

    private final Map<String, Object> data = new HashMap();

    public Set<String> getKeys() {
        return this.data.keySet();
    }

    public boolean hasKey(String tag) {
        return this.getKeys().contains(tag);
    }

    private UpgradeData() {
    }

    public UpgradeData(@Nonnull UpgradeType upgrade) {
        for (String tag : upgrade.getDataTags()) {
            Object defaultValue = upgrade.defaultTagValue(tag);
            this.data.put(tag, defaultValue);
        }
    }

    public void setValue(String tag, Object value) {
        if (this.data.containsKey(tag)) {
            this.data.put(tag, value);
        }
    }

    public Object getValue(String tag) {
        return this.data.containsKey(tag) ? this.data.get(tag) : null;
    }

    public boolean getBooleanValue(String tag) {
        return this.getValue(tag) instanceof Boolean b ? b : false;
    }

    public int getIntValue(String tag) {
        return this.getValue(tag) instanceof Integer i ? i : 0;
    }

    public long getLongValue(String tag) {
        return this.getValue(tag) instanceof Long l ? l : 0L;
    }

    public float getFloatValue(String tag) {
        return this.getValue(tag) instanceof Float f ? f : 0.0F;
    }

    public String getStringValue(String tag) {
        Object var3 = this.getValue(tag);
        return var3 instanceof String ? (String) var3 : "";
    }

    public CompoundTag getCompoundValue(String tag) {
        Object var3 = this.getValue(tag);
        return var3 instanceof CompoundTag ? (CompoundTag) var3 : new CompoundTag();
    }

    public void read(CompoundTag compound) {
        compound.getAllKeys().forEach(key -> {
            if (this.hasKey(key)) {
                if (compound.contains(key, 1)) {
                    this.setValue(key, compound.getBoolean(key));
                } else if (compound.contains(key, 3)) {
                    this.setValue(key, compound.getInt(key));
                } else if (compound.contains(key, 4)) {
                    this.setValue(key, compound.getLong(key));
                } else if (compound.contains(key, 5)) {
                    this.setValue(key, compound.getFloat(key));
                } else if (compound.contains(key, 8)) {
                    this.setValue(key, compound.getString(key));
                } else if (compound.contains(key, 10)) {
                    this.setValue(key, compound.getCompound(key));
                }
            }
        });
    }

    public CompoundTag writeToNBT() {
        return this.writeToNBT(null);
    }

    public CompoundTag writeToNBT(@Nullable UpgradeType source) {
        Map<String, Object> modifiedEntries = source == null ? this.data : getModifiedEntries(this, source);
        CompoundTag compound = new CompoundTag();
        modifiedEntries.forEach((key, value) -> {
            if (value instanceof Boolean) {
                compound.putBoolean(key, (Boolean) value);
            }
            if (value instanceof Integer) {
                compound.putInt(key, (Integer) value);
            } else if (value instanceof Float) {
                compound.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                compound.putLong(key, (Long) value);
            } else if (value instanceof String) {
                compound.putString(key, (String) value);
            } else if (value instanceof CompoundTag) {
                compound.put(key, (CompoundTag) value);
            }
        });
        return compound;
    }

    public static Map<String, Object> getModifiedEntries(UpgradeData queryData, UpgradeType source) {
        Map<String, Object> modifiedEntries = Maps.newHashMap();
        source.getDefaultData().data.forEach((key, value) -> {
            if (queryData.data.containsKey(key) && !Objects.equal(queryData.data.get(key), value)) {
                modifiedEntries.put(key, value);
            }
        });
        return modifiedEntries;
    }
}