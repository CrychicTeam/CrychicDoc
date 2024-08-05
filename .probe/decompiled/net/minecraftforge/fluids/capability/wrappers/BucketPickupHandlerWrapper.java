package net.minecraftforge.fluids.capability.wrappers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class BucketPickupHandlerWrapper implements IFluidHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final BucketPickup bucketPickupHandler;

    protected final Level world;

    protected final BlockPos blockPos;

    public BucketPickupHandlerWrapper(BucketPickup bucketPickupHandler, Level world, BlockPos blockPos) {
        this.bucketPickupHandler = bucketPickupHandler;
        this.world = world;
        this.blockPos = blockPos;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        if (tank == 0) {
            FluidState fluidState = this.world.getFluidState(this.blockPos);
            if (!fluidState.isEmpty()) {
                return new FluidStack(fluidState.getType(), 1000);
            }
        }
        return FluidStack.EMPTY;
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
        return 0;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        if (!resource.isEmpty() && 1000 <= resource.getAmount()) {
            FluidState fluidState = this.world.getFluidState(this.blockPos);
            if (!fluidState.isEmpty() && resource.getFluid() == fluidState.getType()) {
                if (action.execute()) {
                    ItemStack itemStack = this.bucketPickupHandler.pickupBlock(this.world, this.blockPos, this.world.getBlockState(this.blockPos));
                    if (itemStack != ItemStack.EMPTY && itemStack.getItem() instanceof BucketItem bucket) {
                        FluidStack extracted = new FluidStack(bucket.getFluid(), 1000);
                        if (!resource.isFluidEqual(extracted)) {
                            LOGGER.error("Fluid removed without successfully being picked up. Fluid {} at {} in {} matched requested type, but after performing pickup was {}.", ForgeRegistries.FLUIDS.getKey(fluidState.getType()), this.blockPos, this.world.dimension().location(), ForgeRegistries.FLUIDS.getKey(bucket.getFluid()));
                            return FluidStack.EMPTY;
                        }
                        return extracted;
                    }
                } else {
                    FluidStack extracted = new FluidStack(fluidState.getType(), 1000);
                    if (resource.isFluidEqual(extracted)) {
                        return extracted;
                    }
                }
            }
        }
        return FluidStack.EMPTY;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (1000 <= maxDrain) {
            FluidState fluidState = this.world.getFluidState(this.blockPos);
            if (!fluidState.isEmpty()) {
                if (action.simulate()) {
                    return new FluidStack(fluidState.getType(), 1000);
                }
                ItemStack itemStack = this.bucketPickupHandler.pickupBlock(this.world, this.blockPos, this.world.getBlockState(this.blockPos));
                if (itemStack != ItemStack.EMPTY && itemStack.getItem() instanceof BucketItem bucket) {
                    return new FluidStack(bucket.getFluid(), 1000);
                }
            }
        }
        return FluidStack.EMPTY;
    }
}