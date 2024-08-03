package com.simibubi.create.content.trains.entity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;

public class CarriageSyncDataSerializer implements EntityDataSerializer<CarriageSyncData> {

    public void write(FriendlyByteBuf buffer, CarriageSyncData data) {
        data.write(buffer);
    }

    public CarriageSyncData read(FriendlyByteBuf buffer) {
        CarriageSyncData data = new CarriageSyncData();
        data.read(buffer);
        return data;
    }

    public CarriageSyncData copy(CarriageSyncData data) {
        return data.copy();
    }
}