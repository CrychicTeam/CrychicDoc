package me.jellysquid.mods.lithium.mixin.block.hopper;

import me.jellysquid.mods.lithium.api.inventory.LithiumInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

public class InventoryAccessors {

    @Mixin({ AbstractFurnaceBlockEntity.class })
    public abstract static class InventoryAccessorAbstractFurnaceBlockEntity implements LithiumInventory {

        @Accessor("inventory")
        @Override
        public abstract NonNullList<ItemStack> getInventoryLithium();

        @Accessor("inventory")
        @Override
        public abstract void setInventoryLithium(NonNullList<ItemStack> var1);
    }

    @Mixin({ BarrelBlockEntity.class })
    public abstract static class InventoryAccessorBarrelBlockEntity implements LithiumInventory {

        @Accessor("inventory")
        @Override
        public abstract NonNullList<ItemStack> getInventoryLithium();

        @Accessor("inventory")
        @Override
        public abstract void setInventoryLithium(NonNullList<ItemStack> var1);
    }

    @Mixin({ BrewingStandBlockEntity.class })
    public abstract static class InventoryAccessorBrewingStandBlockEntity implements LithiumInventory {

        @Accessor("inventory")
        @Override
        public abstract NonNullList<ItemStack> getInventoryLithium();

        @Accessor("inventory")
        @Override
        public abstract void setInventoryLithium(NonNullList<ItemStack> var1);
    }

    @Mixin({ ChestBlockEntity.class })
    public abstract static class InventoryAccessorChestBlockEntity implements LithiumInventory {

        @Accessor("inventory")
        @Override
        public abstract NonNullList<ItemStack> getInventoryLithium();

        @Accessor("inventory")
        @Override
        public abstract void setInventoryLithium(NonNullList<ItemStack> var1);
    }

    @Mixin({ DispenserBlockEntity.class })
    public abstract static class InventoryAccessorDispenserBlockEntity implements LithiumInventory {

        @Accessor("inventory")
        @Override
        public abstract NonNullList<ItemStack> getInventoryLithium();

        @Accessor("inventory")
        @Override
        public abstract void setInventoryLithium(NonNullList<ItemStack> var1);
    }

    @Mixin({ HopperBlockEntity.class })
    public abstract static class InventoryAccessorHopperBlockEntity implements LithiumInventory {

        @Accessor("inventory")
        @Override
        public abstract NonNullList<ItemStack> getInventoryLithium();

        @Accessor("inventory")
        @Override
        public abstract void setInventoryLithium(NonNullList<ItemStack> var1);
    }

    @Mixin({ ShulkerBoxBlockEntity.class })
    public abstract static class InventoryAccessorShulkerBoxBlockEntity implements LithiumInventory {

        @Accessor("inventory")
        @Override
        public abstract NonNullList<ItemStack> getInventoryLithium();

        @Accessor("inventory")
        @Override
        public abstract void setInventoryLithium(NonNullList<ItemStack> var1);
    }

    @Mixin({ AbstractMinecartContainer.class })
    public abstract static class InventoryAccessorStorageMinecartEntity implements LithiumInventory {

        @Accessor("inventory")
        @Override
        public abstract NonNullList<ItemStack> getInventoryLithium();

        @Accessor("inventory")
        @Override
        public abstract void setInventoryLithium(NonNullList<ItemStack> var1);
    }
}