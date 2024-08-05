package fr.frinn.custommachinery.common.integration.kubejs.function;

import dev.architectury.fluid.FluidStack;
import dev.latvian.mods.kubejs.fluid.EmptyFluidStackJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.rhino.Wrapper;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.component.ChunkloadMachineComponent;
import fr.frinn.custommachinery.common.component.DataMachineComponent;
import fr.frinn.custommachinery.common.component.EnergyMachineComponent;
import fr.frinn.custommachinery.common.component.FluidMachineComponent;
import fr.frinn.custommachinery.common.component.ItemMachineComponent;
import fr.frinn.custommachinery.common.component.handler.FluidComponentHandler;
import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.TaskDelayer;
import fr.frinn.custommachinery.common.util.Utils;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class MachineJS {

    private final CustomMachineTile internal;

    private final CompoundTag nbt;

    protected MachineJS(CustomMachineTile internal) {
        this.internal = internal;
        this.nbt = ((DataMachineComponent) this.internal.getComponentManager().getComponent((MachineComponentType) Registration.DATA_MACHINE_COMPONENT.get()).orElseThrow()).getData();
    }

    public static MachineJS of(Object o) {
        if (o instanceof Wrapper w) {
            o = w.unwrap();
        }
        if (o instanceof BlockEntity blockEntity && blockEntity instanceof CustomMachineTile customMachineTile) {
            return new MachineJS(customMachineTile);
        }
        return o instanceof BlockContainerJS blockContainerJS ? of(blockContainerJS.getEntity()) : null;
    }

    public String getId() {
        return this.internal.getId().toString();
    }

    public void setId(String id) {
        ResourceLocation loc = ResourceLocation.tryParse(id);
        if (loc != null) {
            TaskDelayer.enqueue(0, () -> {
                this.internal.resetProcess();
                this.internal.refreshMachine(loc);
            });
        } else {
            throw new IllegalArgumentException("Invalid machine ID: " + id);
        }
    }

    @Nullable
    public CompoundTag getData() {
        return this.nbt;
    }

    public boolean getPaused() {
        return this.internal.isPaused();
    }

    public void setPaused(boolean paused) {
        this.internal.setPaused(paused);
    }

    @Nullable
    public Component getOwnerName() {
        return this.internal.getOwnerName();
    }

    @Nullable
    public UUID getOwnerId() {
        return this.internal.getOwnerId();
    }

    public boolean isOwner(LivingEntity entity) {
        return this.internal.isOwner(entity);
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.internal.getOwner();
    }

    public void setOwner(LivingEntity entity) {
        this.internal.setOwner(entity);
    }

    public long getEnergyStored() {
        return (Long) this.internal.getComponentManager().getComponent((MachineComponentType) Registration.ENERGY_MACHINE_COMPONENT.get()).map(EnergyMachineComponent::getEnergy).orElse(0L);
    }

    public void setEnergyStored(long energy) {
        this.internal.getComponentManager().getComponent((MachineComponentType) Registration.ENERGY_MACHINE_COMPONENT.get()).ifPresent(component -> component.setEnergy(energy));
    }

    public long getEnergyCapacity() {
        return (Long) this.internal.getComponentManager().getComponent((MachineComponentType) Registration.ENERGY_MACHINE_COMPONENT.get()).map(EnergyMachineComponent::getCapacity).orElse(0L);
    }

    public int addEnergy(int toAdd, boolean simulate) {
        return (Integer) this.internal.getComponentManager().getComponent((MachineComponentType) Registration.ENERGY_MACHINE_COMPONENT.get()).map(component -> component.receiveRecipeEnergy(toAdd, simulate)).orElse(0);
    }

    public int removeEnergy(int toRemove, boolean simulate) {
        return (Integer) this.internal.getComponentManager().getComponent((MachineComponentType) Registration.ENERGY_MACHINE_COMPONENT.get()).map(component -> component.extractRecipeEnergy(toRemove, simulate)).orElse(0);
    }

    public FluidStackJS getFluidStored(String tank) {
        return (FluidStackJS) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(tank)).map(component -> {
            FluidStack stack = component.getFluidStack();
            return FluidStackJS.of(stack.getFluid(), stack.getAmount(), stack.getTag());
        }).orElse(EmptyFluidStackJS.INSTANCE);
    }

    public void setFluidStored(String tank, FluidStackJS stackJS) {
        FluidStack stack = stackJS.getFluidStack();
        this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(tank)).ifPresent(x -> x.setFluidStack(stack));
    }

    public long getFluidCapacity(String tank) {
        return (Long) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(tank)).map(FluidMachineComponent::getCapacity).orElse(0L);
    }

    public long addFluid(FluidStackJS stackJS, boolean simulate) {
        FluidStack stack = stackJS.getFluidStack();
        return (Long) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).map(handler -> (FluidComponentHandler) handler).map(handler -> handler.fill(stack, simulate)).orElse(stack.getAmount());
    }

    public long addFluidToTank(String tank, FluidStackJS stackJS, boolean simulate) {
        return (Long) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(tank)).map(component -> component.insert(stackJS.getFluid(), stackJS.kjs$getAmount(), stackJS.getNbt(), simulate)).orElse(stackJS.kjs$getAmount());
    }

    public FluidStackJS removeFluid(FluidStackJS stackJS, boolean simulate) {
        FluidStack stack = stackJS.getFluidStack();
        return (FluidStackJS) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).map(handler -> (FluidComponentHandler) handler).map(handler -> {
            FluidStack removed = handler.drain(stack, simulate);
            return FluidStackJS.of(removed);
        }).orElse(EmptyFluidStackJS.INSTANCE);
    }

    public FluidStackJS removeFluidFromTank(String tank, long amount, boolean simulate) {
        return (FluidStackJS) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(tank)).map(component -> {
            FluidStack stack = component.extract(amount, simulate);
            return FluidStackJS.of(stack.getFluid(), stack.getAmount(), stack.getTag());
        }).orElse(EmptyFluidStackJS.INSTANCE);
    }

    public ItemStack getItemStored(String slot) {
        return (ItemStack) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(slot)).map(ItemMachineComponent::getItemStack).orElse(ItemStack.EMPTY);
    }

    public void setItemStored(String slot, ItemStack stack) {
        this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(slot)).ifPresent(component -> component.setItemStack(stack));
    }

    public int getItemCapacity(String slot) {
        return (Integer) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(slot)).map(ItemMachineComponent::getCapacity).orElse(0);
    }

    public ItemStack addItemToSlot(String slot, ItemStack stack, boolean simulate) {
        return (ItemStack) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(slot)).map(component -> {
            int inserted = component.insert(stack.getItem(), stack.getCount(), stack.getTag(), simulate, true);
            return Utils.makeItemStack(stack.getItem(), stack.getCount() - inserted, stack.getTag());
        }).orElse(stack);
    }

    public ItemStack removeItemFromSlot(String slot, int toRemove, boolean simulate) {
        return (ItemStack) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(slot)).map(component -> component.extract(toRemove, simulate, true)).orElse(ItemStack.EMPTY);
    }

    public void lockSlot(String slot) {
        this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(slot)).ifPresent(component -> component.setLocked(true));
    }

    public void unlockSlot(String slot) {
        this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(slot)).ifPresent(component -> component.setLocked(false));
    }

    public boolean isSlotLocked(String slot) {
        return (Boolean) this.internal.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(slot)).map(ItemMachineComponent::isLocked).orElseThrow(() -> new IllegalArgumentException("Invalid slot id: " + slot));
    }

    public void enableChunkload(int radius) {
        this.internal.getComponentManager().getComponent((MachineComponentType) Registration.CHUNKLOAD_MACHINE_COMPONENT.get()).ifPresent(component -> component.setActive((ServerLevel) this.internal.m_58904_(), radius));
    }

    public void disableChunkload() {
        this.internal.getComponentManager().getComponent((MachineComponentType) Registration.CHUNKLOAD_MACHINE_COMPONENT.get()).ifPresent(component -> component.setInactive((ServerLevel) this.internal.m_58904_()));
    }

    public boolean isChunkloadEnabled() {
        return (Boolean) this.internal.getComponentManager().getComponent((MachineComponentType) Registration.CHUNKLOAD_MACHINE_COMPONENT.get()).map(ChunkloadMachineComponent::isActive).orElse(false);
    }

    public int getChunkloadRadius() {
        return (Integer) this.internal.getComponentManager().getComponent((MachineComponentType) Registration.CHUNKLOAD_MACHINE_COMPONENT.get()).map(ChunkloadMachineComponent::getRadius).orElse(0);
    }
}