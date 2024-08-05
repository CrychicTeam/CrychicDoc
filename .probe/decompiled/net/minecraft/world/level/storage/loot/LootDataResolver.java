package net.minecraft.world.level.storage.loot;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface LootDataResolver {

    @Nullable
    <T> T getElement(LootDataId<T> var1);

    @Nullable
    default <T> T getElement(LootDataType<T> lootDataTypeT0, ResourceLocation resourceLocation1) {
        return this.getElement(new LootDataId<>(lootDataTypeT0, resourceLocation1));
    }

    default <T> Optional<T> getElementOptional(LootDataId<T> lootDataIdT0) {
        return Optional.ofNullable(this.getElement(lootDataIdT0));
    }

    default <T> Optional<T> getElementOptional(LootDataType<T> lootDataTypeT0, ResourceLocation resourceLocation1) {
        return this.getElementOptional(new LootDataId<>(lootDataTypeT0, resourceLocation1));
    }

    default LootTable getLootTable(ResourceLocation resourceLocation0) {
        return (LootTable) this.getElementOptional(LootDataType.TABLE, resourceLocation0).orElse(LootTable.EMPTY);
    }
}