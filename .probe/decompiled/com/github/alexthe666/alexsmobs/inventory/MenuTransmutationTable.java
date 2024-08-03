package com.github.alexthe666.alexsmobs.inventory;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.message.MessageTransmuteFromMenu;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityTransmutationTable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class MenuTransmutationTable extends AbstractContainerMenu {

    private final ContainerLevelAccess access;

    private long lastSoundTime;

    private final Player player;

    private final Slot transmuteSlot;

    private TileEntityTransmutationTable table;

    public final Container container = new SimpleContainer(1) {

        @Override
        public void setChanged() {
            MenuTransmutationTable.this.slotsChanged(this);
            super.setChanged();
        }
    };

    public MenuTransmutationTable(int i, Inventory inventory) {
        this(i, inventory, ContainerLevelAccess.NULL, AlexsMobs.PROXY.getClientSidePlayer(), null);
    }

    public MenuTransmutationTable(int id, Inventory inventory, ContainerLevelAccess access, Player player, TileEntityTransmutationTable table) {
        super(AMMenuRegistry.TRANSMUTATION_TABLE.get(), id);
        this.table = table;
        this.player = player;
        this.access = access;
        this.m_38897_(this.transmuteSlot = new Slot(this.container, 0, 83, 83) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                ResourceLocation name = ForgeRegistries.ITEMS.getKey(stack.getItem());
                return stack.getMaxStackSize() > 1 && (name == null || !AMConfig.transmutationBlacklist.contains(name.toString()));
            }
        });
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 119 + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.m_38897_(new Slot(inventory, k, 8 + k * 18, 177));
        }
        if (table != null && player != null && !table.hasPossibilities()) {
            table.setRerollPlayerUUID(player.m_20148_());
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return m_38889_(this.access, player, AMBlockRegistry.TRANSMUTATION_TABLE.get());
    }

    @Override
    public void slotsChanged(Container container) {
        if (this.table != null && !this.table.hasPossibilities()) {
            this.table.setRerollPlayerUUID(this.player.m_20148_());
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotIndex != 0) {
                if (!this.m_38903_(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 1, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemstack1);
            this.m_38946_();
        }
        return itemstack;
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (player.m_9236_().isClientSide) {
            AlexsMobs.sendMSGToServer(new MessageTransmuteFromMenu(player.m_19879_(), buttonId));
        }
        return true;
    }

    public void transmute(Player player, int buttonId) {
        ItemStack from = this.transmuteSlot.getItem();
        int cost = AMConfig.transmutingExperienceCost;
        ItemStack setTo = this.table.getPossibility(buttonId).copy();
        double divisible = (double) from.getMaxStackSize() / (double) setTo.getMaxStackSize();
        if (!player.m_9236_().isClientSide && this.table != null && divisible > 0.0 && this.table.hasPossibilities() && !from.isEmpty() && (player.experienceLevel >= cost || player.getAbilities().instabuild)) {
            int newStackSize = (int) Math.floor((double) from.getCount() / divisible);
            setTo.setCount(Math.max(newStackSize, 1));
            this.transmuteSlot.set(setTo);
            player.giveExperienceLevels(-cost);
            this.table.postTransmute(player, from, setTo);
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((p_39152_, p_39153_) -> this.m_150411_(player, this.container));
    }
}