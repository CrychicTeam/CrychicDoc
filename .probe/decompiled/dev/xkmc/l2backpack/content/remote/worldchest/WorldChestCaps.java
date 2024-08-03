package dev.xkmc.l2backpack.content.remote.worldchest;

import dev.xkmc.l2backpack.content.capability.InvPickupCap;
import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.capability.PickupModeCap;
import dev.xkmc.l2backpack.content.capability.PickupTrace;
import dev.xkmc.l2backpack.content.remote.common.StorageContainer;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldChestCaps extends InvPickupCap<WorldChestInvWrapper> implements ICapabilityProvider {

    private final ItemStack stack;

    private final LazyOptional<WorldChestCaps> holder = LazyOptional.of(() -> this);

    public WorldChestCaps(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public PickupConfig getPickupMode() {
        return PickupConfig.getPickupMode(this.stack);
    }

    @Override
    public int getSignature() {
        if (this.stack.getItem() instanceof WorldChestItem item) {
            int color = item.color.ordinal();
            Optional<UUID> opt = WorldChestItem.getOwner(this.stack);
            if (opt.isPresent()) {
                return ((UUID) opt.get()).hashCode() ^ color ^ 21930;
            }
        }
        return 0;
    }

    public WorldChestInvWrapper getInv(PickupTrace trace) {
        if (this.stack.getItem() instanceof WorldChestItem item) {
            Optional<StorageContainer> opt = item.getContainer(this.stack, trace.level);
            if (opt.isPresent()) {
                StorageContainer storage = (StorageContainer) opt.get();
                return new WorldChestInvWrapper(storage.container, storage.id);
            }
        }
        return null;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == PickupModeCap.TOKEN ? this.holder.cast() : LazyOptional.empty();
    }
}