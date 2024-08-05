package com.mna.api.entities.construct.ai.parameter;

import com.mna.api.items.DynamicItemFilter;
import net.minecraft.nbt.CompoundTag;

public class ConstructTaskFilterParameter extends ConstructAITaskParameter {

    private DynamicItemFilter _filter = new DynamicItemFilter();

    public ConstructTaskFilterParameter(String id) {
        super(id, ConstructParameterTypes.FILTER);
    }

    public DynamicItemFilter getValue() {
        return this._filter;
    }

    @Override
    public void loadData(CompoundTag nbt) {
        super.loadData(nbt);
        if (nbt.contains("filter")) {
            this._filter.loadFromTag(nbt.getCompound("filter"));
        }
    }

    @Override
    public CompoundTag saveData() {
        CompoundTag tag = super.saveData();
        tag.put("filter", this._filter.getTag());
        return tag;
    }
}