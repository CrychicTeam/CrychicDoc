package net.minecraft.world;

import javax.annotation.concurrent.Immutable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

@Immutable
public class LockCode {

    public static final LockCode NO_LOCK = new LockCode("");

    public static final String TAG_LOCK = "Lock";

    private final String key;

    public LockCode(String string0) {
        this.key = string0;
    }

    public boolean unlocksWith(ItemStack itemStack0) {
        return this.key.isEmpty() || !itemStack0.isEmpty() && itemStack0.hasCustomHoverName() && this.key.equals(itemStack0.getHoverName().getString());
    }

    public void addToTag(CompoundTag compoundTag0) {
        if (!this.key.isEmpty()) {
            compoundTag0.putString("Lock", this.key);
        }
    }

    public static LockCode fromTag(CompoundTag compoundTag0) {
        return compoundTag0.contains("Lock", 8) ? new LockCode(compoundTag0.getString("Lock")) : NO_LOCK;
    }
}