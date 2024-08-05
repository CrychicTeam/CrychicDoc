package com.rekindled.embers.research.capability;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;

public class DefaultResearchCapability implements IResearchCapability {

    Map<String, Boolean> Checkmarks = new HashMap();

    @Override
    public void setCheckmark(String research, boolean checked) {
        this.Checkmarks.put(research, checked);
    }

    @Override
    public boolean isChecked(String research) {
        return (Boolean) this.Checkmarks.getOrDefault(research, false);
    }

    @Override
    public Map<String, Boolean> getCheckmarks() {
        return this.Checkmarks;
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        CompoundTag checkmarks = new CompoundTag();
        for (Entry<String, Boolean> entry : this.Checkmarks.entrySet()) {
            checkmarks.putBoolean((String) entry.getKey(), (Boolean) entry.getValue());
        }
        tag.put("checkmarks", checkmarks);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        CompoundTag checkmarks = tag.getCompound("checkmarks");
        this.Checkmarks.clear();
        for (String key : checkmarks.getAllKeys()) {
            this.Checkmarks.put(key, checkmarks.getBoolean(key));
        }
    }
}