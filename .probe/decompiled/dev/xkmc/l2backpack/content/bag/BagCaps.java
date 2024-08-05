package dev.xkmc.l2backpack.content.bag;

import dev.xkmc.l2backpack.content.capability.InvPickupCap;
import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.capability.PickupModeCap;
import dev.xkmc.l2backpack.content.capability.PickupTrace;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BagCaps extends InvPickupCap<FastBagItemHandler> implements ICapabilityProvider {

    private final AbstractBag bag;

    private final ItemStack stack;

    private final LazyOptional<BagCaps> holder = LazyOptional.of(() -> this);

    private final BagItemHandler itemHandler;

    private final LazyOptional<BagItemHandler> handler;

    public BagCaps(AbstractBag bag, ItemStack stack) {
        this.bag = bag;
        this.stack = stack;
        this.itemHandler = new BagItemHandler(bag, stack);
        this.handler = LazyOptional.of(() -> this.itemHandler);
    }

    @Override
    public boolean isValid(ItemStack stack) {
        return this.bag.isValidContent(stack);
    }

    public FastBagItemHandler getInv(PickupTrace trace) {
        return this.itemHandler.toFast();
    }

    @Override
    public int getSignature() {
        return this.stack.hashCode();
    }

    @Override
    public PickupConfig getPickupMode() {
        return PickupConfig.getPickupMode(this.stack);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PickupModeCap.TOKEN) {
            return this.holder.cast();
        } else {
            return cap == ForgeCapabilities.ITEM_HANDLER ? this.handler.cast() : LazyOptional.empty();
        }
    }
}