package com.mna.api.entities.construct.ai.parameter;

import net.minecraft.nbt.CompoundTag;

public class ConstructTaskIntegerParameter extends ConstructAITaskParameter {

    private int min;

    private int max;

    private int value;

    private int step;

    public ConstructTaskIntegerParameter(String id) {
        this(id, 0, 1);
    }

    public ConstructTaskIntegerParameter(String id, int min, int max) {
        this(id, min, max, min, 1);
    }

    public ConstructTaskIntegerParameter(String id, int min, int max, int step, int defaultValue) {
        super(id, ConstructParameterTypes.INTEGER);
        this.min = min;
        this.max = max;
        this.value = defaultValue;
    }

    public int getMinimum() {
        return this.min;
    }

    public int getMaximum() {
        return this.max;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getStep() {
        return this.step;
    }

    @Override
    public void loadData(CompoundTag nbt) {
        super.loadData(nbt);
        if (nbt.contains("min")) {
            this.min = nbt.getInt("min");
        }
        if (nbt.contains("max")) {
            this.min = nbt.getInt("max");
        }
        if (nbt.contains("value")) {
            this.value = nbt.getInt("value");
        }
    }

    @Override
    public CompoundTag saveData() {
        CompoundTag tag = super.saveData();
        tag.putInt("min", this.min);
        tag.putInt("max", this.max);
        tag.putInt("value", this.value);
        return tag;
    }
}