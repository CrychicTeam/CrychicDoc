package dev.xkmc.l2backpack.content.remote.player;

import dev.xkmc.l2backpack.content.capability.InvPickupCap;
import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.capability.PickupModeCap;
import dev.xkmc.l2backpack.content.capability.PickupTrace;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnderBackpackCaps extends InvPickupCap<InvWrapper> implements ICapabilityProvider {

    private final ItemStack stack;

    private final LazyOptional<EnderBackpackCaps> holder = LazyOptional.of(() -> this);

    public EnderBackpackCaps(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public PickupConfig getPickupMode() {
        return PickupConfig.getPickupMode(this.stack);
    }

    @Override
    public int getSignature() {
        return 0;
    }

    @Nullable
    public InvWrapper getInv(PickupTrace trace) {
        return trace.player == null ? null : new InvWrapper(trace.player.m_36327_());
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == PickupModeCap.TOKEN ? this.holder.cast() : LazyOptional.empty();
    }
}