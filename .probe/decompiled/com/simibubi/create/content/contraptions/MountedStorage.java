package com.simibubi.create.content.contraptions;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import com.simibubi.create.content.logistics.crate.BottomlessItemHandler;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class MountedStorage {

    private static final ItemStackHandler dummyHandler = new ItemStackHandler();

    ItemStackHandler handler;

    boolean noFuel;

    boolean valid;

    private BlockEntity blockEntity;

    public static boolean canUseAsStorage(BlockEntity be) {
        if (be == null) {
            return false;
        } else if (be instanceof MechanicalCrafterBlockEntity) {
            return false;
        } else if (AllBlockEntityTypes.CREATIVE_CRATE.is(be)) {
            return true;
        } else if (be instanceof ShulkerBoxBlockEntity) {
            return true;
        } else if (be instanceof ChestBlockEntity) {
            return true;
        } else if (be instanceof BarrelBlockEntity) {
            return true;
        } else if (be instanceof ItemVaultBlockEntity) {
            return true;
        } else {
            try {
                LazyOptional<IItemHandler> capability = be.getCapability(ForgeCapabilities.ITEM_HANDLER);
                IItemHandler handler = capability.orElse(null);
                return handler instanceof ItemStackHandler ? !(handler instanceof ProcessingInventory) : canUseModdedInventory(be, handler);
            } catch (Exception var3) {
                return false;
            }
        }
    }

    public static boolean canUseModdedInventory(BlockEntity be, IItemHandler handler) {
        if (!(handler instanceof IItemHandlerModifiable validItemHandler)) {
            return false;
        } else {
            BlockState blockState = be.getBlockState();
            if (AllTags.AllBlockTags.CONTRAPTION_INVENTORY_DENY.matches(blockState)) {
                return false;
            } else {
                String blockId = ForgeRegistries.BLOCKS.getKey(blockState.m_60734_()).getPath();
                return blockId.contains("ender") ? false : blockId.endsWith("_chest") || blockId.endsWith("_barrel");
            }
        }
    }

    public MountedStorage(BlockEntity be) {
        this.blockEntity = be;
        this.handler = dummyHandler;
        this.noFuel = be instanceof ItemVaultBlockEntity;
    }

    public void removeStorageFromWorld() {
        this.valid = false;
        if (this.blockEntity != null) {
            if (this.blockEntity instanceof ChestBlockEntity) {
                CompoundTag tag = this.blockEntity.saveWithFullMetadata();
                if (!tag.contains("LootTable", 8)) {
                    this.handler = new ItemStackHandler(((ChestBlockEntity) this.blockEntity).getContainerSize());
                    NonNullList<ItemStack> items = NonNullList.withSize(this.handler.getSlots(), ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(tag, items);
                    for (int i = 0; i < items.size(); i++) {
                        this.handler.setStackInSlot(i, items.get(i));
                    }
                    this.valid = true;
                }
            } else {
                IItemHandler beHandler = this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(dummyHandler);
                if (beHandler != dummyHandler) {
                    if (this.blockEntity instanceof ItemVaultBlockEntity) {
                        this.handler = ((ItemVaultBlockEntity) this.blockEntity).getInventoryOfBlock();
                        this.valid = true;
                    } else if (beHandler instanceof ItemStackHandler) {
                        this.handler = (ItemStackHandler) beHandler;
                        this.valid = true;
                    } else if (beHandler instanceof IItemHandlerModifiable inv) {
                        this.handler = new ItemStackHandler(beHandler.getSlots());
                        for (int slot = 0; slot < this.handler.getSlots(); slot++) {
                            this.handler.setStackInSlot(slot, inv.getStackInSlot(slot));
                            inv.setStackInSlot(slot, ItemStack.EMPTY);
                        }
                        this.valid = true;
                    }
                }
            }
        }
    }

    public void addStorageToWorld(BlockEntity be) {
        if (!(this.handler instanceof BottomlessItemHandler)) {
            if (be instanceof ChestBlockEntity) {
                CompoundTag tag = be.saveWithFullMetadata();
                tag.remove("Items");
                NonNullList<ItemStack> items = NonNullList.withSize(this.handler.getSlots(), ItemStack.EMPTY);
                for (int i = 0; i < items.size(); i++) {
                    items.set(i, this.handler.getStackInSlot(i));
                }
                ContainerHelper.saveAllItems(tag, items);
                be.load(tag);
            } else if (be instanceof ItemVaultBlockEntity) {
                ((ItemVaultBlockEntity) be).applyInventoryToBlock(this.handler);
            } else {
                LazyOptional<IItemHandler> capability = be.getCapability(ForgeCapabilities.ITEM_HANDLER);
                IItemHandler teHandler = capability.orElse(null);
                if (teHandler instanceof IItemHandlerModifiable inv) {
                    for (int slot = 0; slot < Math.min(inv.getSlots(), this.handler.getSlots()); slot++) {
                        inv.setStackInSlot(slot, this.handler.getStackInSlot(slot));
                    }
                }
            }
        }
    }

    public IItemHandlerModifiable getItemHandler() {
        return this.handler;
    }

    public CompoundTag serialize() {
        if (!this.valid) {
            return null;
        } else {
            CompoundTag tag = this.handler.serializeNBT();
            if (this.noFuel) {
                NBTHelper.putMarker(tag, "NoFuel");
            }
            if (!(this.handler instanceof BottomlessItemHandler)) {
                return tag;
            } else {
                NBTHelper.putMarker(tag, "Bottomless");
                tag.put("ProvidedStack", this.handler.getStackInSlot(0).serializeNBT());
                return tag;
            }
        }
    }

    public static MountedStorage deserialize(CompoundTag nbt) {
        MountedStorage storage = new MountedStorage(null);
        storage.handler = new ItemStackHandler();
        if (nbt == null) {
            return storage;
        } else {
            storage.valid = true;
            storage.noFuel = nbt.contains("NoFuel");
            if (nbt.contains("Bottomless")) {
                ItemStack providedStack = ItemStack.of(nbt.getCompound("ProvidedStack"));
                storage.handler = new BottomlessItemHandler(() -> providedStack);
                return storage;
            } else {
                storage.handler.deserializeNBT(nbt);
                return storage;
            }
        }
    }

    public boolean isValid() {
        return this.valid;
    }

    public boolean canUseForFuel() {
        return !this.noFuel;
    }
}