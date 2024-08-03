package net.minecraftforge.fluids.capability.wrappers;

import net.minecraft.core.Direction;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidBucketWrapper implements IFluidHandlerItem, ICapabilityProvider {

    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

    @NotNull
    protected ItemStack container;

    public FluidBucketWrapper(@NotNull ItemStack container) {
        this.container = container;
    }

    @NotNull
    @Override
    public ItemStack getContainer() {
        return this.container;
    }

    public boolean canFillFluidType(FluidStack fluid) {
        return fluid.getFluid() != Fluids.WATER && fluid.getFluid() != Fluids.LAVA ? !fluid.getFluid().getFluidType().getBucket(fluid).isEmpty() : true;
    }

    @NotNull
    public FluidStack getFluid() {
        Item item = this.container.getItem();
        if (item instanceof BucketItem) {
            return new FluidStack(((BucketItem) item).getFluid(), 1000);
        } else {
            return item instanceof MilkBucketItem && ForgeMod.MILK.isPresent() ? new FluidStack(ForgeMod.MILK.get(), 1000) : FluidStack.EMPTY;
        }
    }

    protected void setFluid(@NotNull FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            this.container = new ItemStack(Items.BUCKET);
        } else {
            this.container = FluidUtil.getFilledBucket(fluidStack);
        }
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return 1000;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (this.container.getCount() == 1 && resource.getAmount() >= 1000 && !(this.container.getItem() instanceof MilkBucketItem) && this.getFluid().isEmpty() && this.canFillFluidType(resource)) {
            if (action.execute()) {
                this.setFluid(resource);
            }
            return 1000;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        if (this.container.getCount() == 1 && resource.getAmount() >= 1000) {
            FluidStack fluidStack = this.getFluid();
            if (!fluidStack.isEmpty() && fluidStack.isFluidEqual(resource)) {
                if (action.execute()) {
                    this.setFluid(FluidStack.EMPTY);
                }
                return fluidStack;
            } else {
                return FluidStack.EMPTY;
            }
        } else {
            return FluidStack.EMPTY;
        }
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (this.container.getCount() == 1 && maxDrain >= 1000) {
            FluidStack fluidStack = this.getFluid();
            if (!fluidStack.isEmpty()) {
                if (action.execute()) {
                    this.setFluid(FluidStack.EMPTY);
                }
                return fluidStack;
            } else {
                return FluidStack.EMPTY;
            }
        } else {
            return FluidStack.EMPTY;
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(capability, this.holder);
    }
}