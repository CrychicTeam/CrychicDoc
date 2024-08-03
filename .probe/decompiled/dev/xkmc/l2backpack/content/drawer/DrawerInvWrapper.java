package dev.xkmc.l2backpack.content.drawer;

import dev.xkmc.l2backpack.content.capability.InvPickupCap;
import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.capability.PickupModeCap;
import dev.xkmc.l2backpack.content.capability.PickupTrace;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DrawerInvWrapper extends InvPickupCap<BaseDrawerInvAccess> implements ICapabilityProvider {

    private final ItemStack stack;

    private final Function<PickupTrace, BaseDrawerInvAccess> access;

    private final LazyOptional<DrawerInvWrapper> holder = LazyOptional.of(() -> this);

    public DrawerInvWrapper(ItemStack stack, Function<PickupTrace, BaseDrawerInvAccess> access) {
        this.stack = stack;
        this.access = access;
    }

    @Nullable
    public BaseDrawerInvAccess getInv(PickupTrace trace) {
        return (BaseDrawerInvAccess) this.access.apply(trace);
    }

    public boolean mayStack(BaseDrawerInvAccess inv, int slot, ItemStack stack, PickupConfig config) {
        return inv.mayStack(inv, slot, stack, config);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == PickupModeCap.TOKEN ? this.holder.cast() : LazyOptional.empty();
    }

    @Override
    public PickupConfig getPickupMode() {
        return PickupConfig.getPickupMode(this.stack);
    }

    @Override
    public int getSignature() {
        return this.stack.hashCode();
    }
}