package com.mna.inventory.stack_extension;

import com.mna.ManaAndArtifice;
import com.mna.gui.containers.IExtendedItemHandler;
import java.io.File;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class MAInventoryData<T extends IExtendedItemHandler> extends SavedData {

    private UUID id;

    private final T inventory;

    private int size;

    public static String ID(UUID id) {
        return "mna-inventory-" + id.toString();
    }

    public MAInventoryData(UUID id, int size, Function<Integer, T> builder) {
        this.id = id;
        this.size = size;
        this.inventory = (T) builder.apply(this.size);
    }

    public T getInventory() {
        return this.inventory;
    }

    public UUID getUUID() {
        return this.id;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("size", this.size);
        compound.putUUID("id", this.id);
        if (this.inventory != null) {
            compound.put("inventory", this.inventory.serialize());
        } else {
            ManaAndArtifice.LOGGER.error("MAInventoryData inventory is null - writing empty inventory.");
            compound.put("inventory", new CompoundTag());
        }
        return compound;
    }

    @Override
    public void save(File fileIn) {
        super.save(fileIn);
    }

    @Override
    public void setDirty() {
        super.setDirty();
    }
}