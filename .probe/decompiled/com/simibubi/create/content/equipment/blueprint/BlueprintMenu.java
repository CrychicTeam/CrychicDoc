package com.simibubi.create.content.equipment.blueprint;

import com.simibubi.create.AllMenuTypes;
import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BlueprintMenu extends GhostItemMenu<BlueprintEntity.BlueprintSection> {

    public BlueprintMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public BlueprintMenu(MenuType<?> type, int id, Inventory inv, BlueprintEntity.BlueprintSection section) {
        super(type, id, inv, section);
    }

    public static BlueprintMenu create(int id, Inventory inv, BlueprintEntity.BlueprintSection section) {
        return new BlueprintMenu((MenuType<?>) AllMenuTypes.CRAFTING_BLUEPRINT.get(), id, inv, section);
    }

    @Override
    protected boolean allowRepeats() {
        return true;
    }

    @Override
    protected void addSlots() {
        this.addPlayerSlots(8, 131);
        int x = 29;
        int y = 21;
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.m_38897_(new BlueprintMenu.BlueprintCraftSlot(this.ghostInventory, index++, x + col * 18, y + row * 18));
            }
        }
        this.m_38897_(new BlueprintMenu.BlueprintCraftSlot(this.ghostInventory, index++, 123, 40));
        this.m_38897_(new SlotItemHandler(this.ghostInventory, index++, 135, 57));
    }

    public void onCraftMatrixChanged() {
        Level level = this.contentHolder.getBlueprintWorld();
        if (!level.isClientSide) {
            ServerPlayer serverplayerentity = (ServerPlayer) this.player;
            CraftingContainer craftingInventory = new BlueprintMenu.BlueprintCraftingInventory(this, this.ghostInventory);
            Optional<CraftingRecipe> optional = this.player.m_20194_().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInventory, this.player.m_20193_());
            if (!optional.isPresent()) {
                if (!this.ghostInventory.getStackInSlot(9).isEmpty()) {
                    if (this.contentHolder.inferredIcon) {
                        this.ghostInventory.setStackInSlot(9, ItemStack.EMPTY);
                        serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(this.f_38840_, this.m_182425_(), 45, ItemStack.EMPTY));
                        this.contentHolder.inferredIcon = false;
                    }
                }
            } else {
                CraftingRecipe icraftingrecipe = (CraftingRecipe) optional.get();
                ItemStack itemstack = icraftingrecipe.m_5874_(craftingInventory, level.registryAccess());
                this.ghostInventory.setStackInSlot(9, itemstack);
                this.contentHolder.inferredIcon = true;
                ItemStack toSend = itemstack.copy();
                toSend.getOrCreateTag().putBoolean("InferredFromRecipe", true);
                serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(this.f_38840_, this.m_182425_(), 45, toSend));
            }
        }
    }

    @Override
    public void setItem(int slotId, int stateId, ItemStack stack) {
        if (slotId == 45) {
            if (stack.hasTag()) {
                this.contentHolder.inferredIcon = stack.getTag().getBoolean("InferredFromRecipe");
                stack.getTag().remove("InferredFromRecipe");
            } else {
                this.contentHolder.inferredIcon = false;
            }
        }
        super.m_182406_(slotId, stateId, stack);
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return this.contentHolder.getItems();
    }

    protected void initAndReadInventory(BlueprintEntity.BlueprintSection contentHolder) {
        super.initAndReadInventory(contentHolder);
    }

    protected void saveData(BlueprintEntity.BlueprintSection contentHolder) {
        contentHolder.save(this.ghostInventory);
    }

    @OnlyIn(Dist.CLIENT)
    protected BlueprintEntity.BlueprintSection createOnClient(FriendlyByteBuf extraData) {
        int entityID = extraData.readVarInt();
        int section = extraData.readVarInt();
        return !(Minecraft.getInstance().level.getEntity(entityID) instanceof BlueprintEntity blueprintEntity) ? null : blueprintEntity.getSection(section);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.contentHolder != null && this.contentHolder.canPlayerUse(player);
    }

    class BlueprintCraftSlot extends SlotItemHandler {

        private int index;

        public BlueprintCraftSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.index = index;
        }

        @Override
        public void setChanged() {
            super.m_6654_();
            if (this.index == 9 && this.m_6657_() && !BlueprintMenu.this.contentHolder.getBlueprintWorld().isClientSide) {
                BlueprintMenu.this.contentHolder.inferredIcon = false;
                ServerPlayer serverplayerentity = (ServerPlayer) BlueprintMenu.this.player;
                serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(BlueprintMenu.this.f_38840_, BlueprintMenu.this.m_182425_(), 45, this.m_7993_()));
            }
            if (this.index < 9) {
                BlueprintMenu.this.onCraftMatrixChanged();
            }
        }
    }

    static class BlueprintCraftingInventory extends TransientCraftingContainer {

        public BlueprintCraftingInventory(AbstractContainerMenu menu, ItemStackHandler items) {
            super(menu, 3, 3);
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    ItemStack stack = items.getStackInSlot(y * 3 + x);
                    this.m_6836_(y * 3 + x, stack == null ? ItemStack.EMPTY : stack.copy());
                }
            }
        }
    }
}