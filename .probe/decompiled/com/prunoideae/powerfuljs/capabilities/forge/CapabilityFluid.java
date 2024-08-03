package com.prunoideae.powerfuljs.capabilities.forge;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;

public class CapabilityFluid {

    public CapabilityFluid.NormalTankBuilderItemStack itemStack(int capacity) {
        return new CapabilityFluid.NormalTankBuilderItemStack(capacity);
    }

    public CapabilityFluid.CustomTankBuilderItemStack customItemStack() {
        return new CapabilityFluid.CustomTankBuilderItemStack();
    }

    public CapabilityFluid.CustomTankBuilderBlockEntity customBlockEntity() {
        return new CapabilityFluid.CustomTankBuilderBlockEntity();
    }

    public static class CustomTankBuilderBlockEntity extends CapabilityBuilderForge<BlockEntity, IFluidHandler> {

        private CapabilityFluid.FluidIOBlockEntity onFill;

        private CapabilityFluid.FluidIOBlockEntity onDrain;

        private BiPredicate<BlockEntity, FluidStackJS> isFluidGood;

        private ToIntFunction<BlockEntity> getCapacity;

        private Function<BlockEntity, FluidStackJS> getFluid;

        public CapabilityFluid.CustomTankBuilderBlockEntity onFill(CapabilityFluid.FluidIOBlockEntity onFill) {
            this.onFill = onFill;
            return this;
        }

        public CapabilityFluid.CustomTankBuilderBlockEntity onDrain(CapabilityFluid.FluidIOBlockEntity onDrain) {
            this.onDrain = onDrain;
            return this;
        }

        public CapabilityFluid.CustomTankBuilderBlockEntity isFluidGood(BiPredicate<BlockEntity, FluidStackJS> isFluidGood) {
            this.isFluidGood = isFluidGood;
            return this;
        }

        public CapabilityFluid.CustomTankBuilderBlockEntity getCapacity(ToIntFunction<BlockEntity> getCapacity) {
            this.getCapacity = getCapacity;
            return this;
        }

        public CapabilityFluid.CustomTankBuilderBlockEntity getFluid(Function<BlockEntity, FluidStackJS> getFluid) {
            this.getFluid = getFluid;
            return this;
        }

        public IFluidHandler getCapability(BlockEntity instance) {
            return new IFluidHandler() {

                @Override
                public int getTanks() {
                    return 1;
                }

                @NotNull
                @Override
                public FluidStack getFluidInTank(int i) {
                    return CustomTankBuilderBlockEntity.this.getFluid != null ? FluidStackHooksForge.toForge(((FluidStackJS) CustomTankBuilderBlockEntity.this.getFluid.apply(instance)).getFluidStack()) : FluidStack.EMPTY;
                }

                @Override
                public int getTankCapacity(int i) {
                    return CustomTankBuilderBlockEntity.this.getCapacity != null ? CustomTankBuilderBlockEntity.this.getCapacity.applyAsInt(instance) : 1000;
                }

                @Override
                public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
                    return CustomTankBuilderBlockEntity.this.isFluidGood == null || CustomTankBuilderBlockEntity.this.isFluidGood.test(instance, FluidStackJS.of(FluidStackHooksForge.fromForge(fluidStack)));
                }

                @Override
                public int fill(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
                    return CustomTankBuilderBlockEntity.this.onFill != null ? CustomTankBuilderBlockEntity.this.onFill.transfer(instance, FluidStackJS.of(FluidStackHooksForge.fromForge(fluidStack)), fluidAction.simulate()) : 0;
                }

                @NotNull
                @Override
                public FluidStack drain(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
                    int drained = CustomTankBuilderBlockEntity.this.onDrain != null ? CustomTankBuilderBlockEntity.this.onDrain.transfer(instance, FluidStackJS.of(FluidStackHooksForge.fromForge(fluidStack)), fluidAction.simulate()) : 0;
                    return drained == 0 ? FluidStack.EMPTY : new FluidStack(fluidStack, drained);
                }

                @NotNull
                @Override
                public FluidStack drain(int i, IFluidHandler.FluidAction fluidAction) {
                    FluidStack inTank = this.getFluidInTank(0).copy();
                    if (inTank.isEmpty()) {
                        return inTank;
                    } else {
                        inTank.setAmount(i);
                        return this.drain(inTank, fluidAction);
                    }
                }
            };
        }

        public Capability<IFluidHandler> getCapabilityKey() {
            return ForgeCapabilities.FLUID_HANDLER;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:custom_tank_be");
        }
    }

    public static class CustomTankBuilderItemStack extends CapabilityBuilderForge<ItemStack, IFluidHandlerItem> {

        private CapabilityFluid.FluidIOItemStack onFill;

        private CapabilityFluid.FluidIOItemStack onDrain;

        private BiPredicate<ItemStack, FluidStackJS> isFluidGood;

        private ToIntFunction<ItemStack> getCapacity;

        public CapabilityFluid.CustomTankBuilderItemStack withCapacity(int capacity) {
            return this.getCapacity(i -> capacity);
        }

        public CapabilityFluid.CustomTankBuilderItemStack getCapacity(ToIntFunction<ItemStack> getCapacity) {
            this.getCapacity = getCapacity;
            return this;
        }

        public CapabilityFluid.CustomTankBuilderItemStack acceptFluid(Fluid fluid) {
            return this.isFluidGood((i, fluidStackJS) -> fluidStackJS.getFluid() == fluid);
        }

        public CapabilityFluid.CustomTankBuilderItemStack isFluidGood(BiPredicate<ItemStack, FluidStackJS> isFluidGood) {
            this.isFluidGood = isFluidGood;
            return this;
        }

        public CapabilityFluid.CustomTankBuilderItemStack onFill(CapabilityFluid.FluidIOItemStack onFill) {
            this.onFill = onFill;
            return this;
        }

        public CapabilityFluid.CustomTankBuilderItemStack onDrain(CapabilityFluid.FluidIOItemStack onDrain) {
            this.onDrain = onDrain;
            return this;
        }

        @HideFromJS
        public IFluidHandlerItem getCapability(ItemStack instance) {
            return new CapabilityFluid.FluidHandlerItemStackCustom(instance, 1000, this.onFill, this.onDrain, this.isFluidGood, this.getCapacity);
        }

        @HideFromJS
        public Capability<IFluidHandlerItem> getCapabilityKey() {
            return ForgeCapabilities.FLUID_HANDLER_ITEM;
        }

        @HideFromJS
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:custom_tank_item");
        }
    }

    public static class FluidHandlerItemStackCustom extends FluidHandlerItemStack {

        private final CapabilityFluid.FluidIOItemStack onFill;

        private final CapabilityFluid.FluidIOItemStack onDrain;

        private final BiPredicate<ItemStack, FluidStackJS> isFluidGood;

        private final ToIntFunction<ItemStack> getCapacity;

        public FluidHandlerItemStackCustom(@NotNull ItemStack container, int capacity, CapabilityFluid.FluidIOItemStack onFill, CapabilityFluid.FluidIOItemStack onDrain, BiPredicate<ItemStack, FluidStackJS> isFluidGood, ToIntFunction<ItemStack> getCapacity) {
            super(container, capacity);
            this.onDrain = onDrain;
            this.onFill = onFill;
            this.isFluidGood = isFluidGood;
            this.getCapacity = getCapacity;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return this.isFluidGood == null || this.isFluidGood.test(this.container, FluidStackJS.of(FluidStackHooksForge.fromForge(stack)));
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction doFill) {
            return this.onFill == null ? super.fill(resource, doFill) : this.onFill.transfer(this.container, FluidStackJS.of(FluidStackHooksForge.fromForge(resource)), doFill.simulate());
        }

        @NotNull
        @Override
        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            if (this.onDrain == null) {
                return super.drain(resource, action);
            } else {
                FluidStack stack = resource.copy();
                stack.setAmount(this.onDrain.transfer(this.container, FluidStackJS.of(FluidStackHooksForge.fromForge(resource)), action.simulate()));
                return stack;
            }
        }

        @NotNull
        @Override
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            if (this.onDrain == null) {
                return super.drain(maxDrain, action);
            } else {
                FluidStack stack = this.getFluid().copy();
                FluidStack drain = this.getFluid().copy();
                drain.setAmount(maxDrain);
                stack.setAmount(this.onDrain.transfer(this.container, FluidStackJS.of(FluidStackHooksForge.fromForge(drain)), action.simulate()));
                return stack;
            }
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return this.isFluidValid(0, fluid);
        }

        @Override
        public boolean canDrainFluidType(FluidStack fluid) {
            return this.isFluidValid(0, fluid);
        }

        @Override
        public int getTankCapacity(int tank) {
            return this.getCapacity == null ? 1000 : this.getCapacity.applyAsInt(this.container);
        }
    }

    @FunctionalInterface
    public interface FluidIOBlockEntity {

        int transfer(BlockEntity var1, FluidStackJS var2, boolean var3);
    }

    @FunctionalInterface
    public interface FluidIOItemStack {

        int transfer(ItemStack var1, FluidStackJS var2, boolean var3);
    }

    public static class NormalTankBuilderItemStack extends CapabilityBuilderForge<ItemStack, IFluidHandlerItem> {

        private final int capacity;

        private NormalTankBuilderItemStack(int capacity) {
            this.capacity = capacity;
        }

        public IFluidHandlerItem getCapability(ItemStack instance) {
            return new FluidHandlerItemStack(instance, this.capacity);
        }

        public Capability<IFluidHandlerItem> getCapabilityKey() {
            return ForgeCapabilities.FLUID_HANDLER_ITEM;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:normal_tank_item");
        }
    }
}