package team.lodestar.lodestone.systems.worldevent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class WorldEventType {

    public final ResourceLocation id;

    public final WorldEventType.EventInstanceSupplier supplier;

    public final boolean clientSynced;

    public WorldEventType(ResourceLocation id, WorldEventType.EventInstanceSupplier supplier, boolean clientSynced) {
        this.id = id;
        this.supplier = supplier;
        this.clientSynced = clientSynced;
    }

    public WorldEventType(ResourceLocation id, WorldEventType.EventInstanceSupplier supplier) {
        this(id, supplier, false);
    }

    public boolean isClientSynced() {
        return this.clientSynced;
    }

    public WorldEventInstance createInstance(CompoundTag tag) {
        return this.supplier.getInstance().deserializeNBT(tag);
    }

    public interface EventInstanceSupplier {

        WorldEventInstance getInstance();
    }
}