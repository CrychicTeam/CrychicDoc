package net.mehvahdjukaar.moonlight.api.map;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomMapData<H extends CustomMapData.DirtyCounter> {

    CustomMapData.Type<?> getType();

    default boolean persistOnCopyOrLock() {
        return true;
    }

    default boolean onItemUpdate(MapItemSavedData data, Entity entity) {
        return false;
    }

    @Nullable
    default Component onItemTooltip(MapItemSavedData data, ItemStack stack) {
        return null;
    }

    H createDirtyCounter();

    void load(CompoundTag var1);

    void loadUpdateTag(CompoundTag var1);

    void save(CompoundTag var1);

    void saveToUpdateTag(CompoundTag var1, H var2);

    default void setDirty(MapItemSavedData data, Consumer<H> dirtySetter) {
        CustomMapData.Type<?> type = this.getType();
        ((ExpandedMapData) data).setCustomDataDirty(type, dirtySetter);
    }

    public interface DirtyCounter {

        boolean isDirty();

        void clearDirty();
    }

    public static class SimpleDirtyCounter implements CustomMapData.DirtyCounter {

        private boolean dirty = true;

        public void markDirty() {
            this.dirty = true;
        }

        @Override
        public boolean isDirty() {
            return this.dirty;
        }

        @Override
        public void clearDirty() {
            this.dirty = false;
        }
    }

    public static record Type<T extends CustomMapData<?>>(ResourceLocation id, Supplier<T> factory) {

        @NotNull
        public T get(MapItemSavedData mapData) {
            return (T) ((ExpandedMapData) mapData).getCustomData().get(this.id);
        }
    }
}