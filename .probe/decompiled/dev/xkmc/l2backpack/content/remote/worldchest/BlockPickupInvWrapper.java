package dev.xkmc.l2backpack.content.remote.worldchest;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.capability.PickupTrace;
import dev.xkmc.l2backpack.content.remote.common.StorageContainer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlockPickupInvWrapper extends WorldChestInvWrapper {

    private final BlockPickupCap cap;

    private final ServerLevel level;

    public BlockPickupInvWrapper(ServerLevel level, WorldChestBlockEntity be, StorageContainer storage, PickupConfig config) {
        super(storage.container, storage.id);
        this.level = level;
        this.cap = new BlockPickupCap(be, storage, config);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack copy = stack.copy();
        this.cap.doPickup(copy, new PickupTrace(simulate, this.level));
        return copy;
    }
}