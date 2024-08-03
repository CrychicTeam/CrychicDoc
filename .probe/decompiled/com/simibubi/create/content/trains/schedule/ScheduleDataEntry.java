package com.simibubi.create.content.trains.schedule;

import net.minecraft.nbt.CompoundTag;

public abstract class ScheduleDataEntry implements IScheduleInput {

    protected CompoundTag data = new CompoundTag();

    @Override
    public CompoundTag getData() {
        return this.data;
    }

    @Override
    public void setData(CompoundTag data) {
        this.data = data;
        this.readAdditional(data);
    }

    protected void writeAdditional(CompoundTag tag) {
    }

    protected void readAdditional(CompoundTag tag) {
    }

    protected <T> T enumData(String key, Class<T> enumClass) {
        T[] enumConstants = (T[]) enumClass.getEnumConstants();
        return enumConstants[this.data.getInt(key) % enumConstants.length];
    }

    protected String textData(String key) {
        return this.data.getString(key);
    }

    protected int intData(String key) {
        return this.data.getInt(key);
    }
}