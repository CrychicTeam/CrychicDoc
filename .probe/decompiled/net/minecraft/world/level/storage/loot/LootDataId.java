package net.minecraft.world.level.storage.loot;

import net.minecraft.resources.ResourceLocation;

public record LootDataId<T>(LootDataType<T> f_278383_, ResourceLocation f_278500_) {

    private final LootDataType<T> type;

    private final ResourceLocation location;

    public LootDataId(LootDataType<T> f_278383_, ResourceLocation f_278500_) {
        this.type = f_278383_;
        this.location = f_278500_;
    }
}