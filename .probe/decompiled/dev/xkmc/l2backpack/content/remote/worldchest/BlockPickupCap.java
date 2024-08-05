package dev.xkmc.l2backpack.content.remote.worldchest;

import dev.xkmc.l2backpack.content.capability.InvPickupCap;
import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.capability.PickupTrace;
import dev.xkmc.l2backpack.content.remote.common.StorageContainer;
import java.util.UUID;

public class BlockPickupCap extends InvPickupCap<WorldChestInvWrapper> {

    private final WorldChestBlockEntity be;

    private final StorageContainer storage;

    private final PickupConfig config;

    public BlockPickupCap(WorldChestBlockEntity be, StorageContainer storage, PickupConfig config) {
        this.be = be;
        this.storage = storage;
        this.config = config;
    }

    @Override
    public PickupConfig getPickupMode() {
        return this.config;
    }

    @Override
    public int getSignature() {
        int color = this.be.color;
        UUID opt = this.be.owner_id;
        return opt == null ? 0 : opt.hashCode() ^ color ^ 21930;
    }

    public WorldChestInvWrapper getInv(PickupTrace trace) {
        return new WorldChestInvWrapper(this.storage.container, this.storage.id);
    }
}