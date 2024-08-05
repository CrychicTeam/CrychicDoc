package dev.architectury.core.item;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.jetbrains.annotations.Nullable;

public class ArchitecturyBucketItem extends BucketItem {

    public ArchitecturyBucketItem(Supplier<? extends Fluid> fluid, Item.Properties properties) {
        super(fluid, properties);
    }

    public final Fluid getContainedFluid() {
        return this.getFluid();
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return (ICapabilityProvider) (this.getClass() == ArchitecturyBucketItem.class ? new FluidBucketWrapper(stack) : super.initCapabilities(stack, nbt));
    }
}