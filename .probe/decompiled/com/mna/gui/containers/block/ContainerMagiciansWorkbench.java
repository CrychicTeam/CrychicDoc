package com.mna.gui.containers.block;

import com.mna.blocks.tileentities.wizard_lab.MagiciansWorkbenchTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.SlotMagiciansWorkbenchOutput;
import com.mna.network.ClientMessageDispatcher;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class ContainerMagiciansWorkbench extends RecipeBookMenu<CraftingContainer> {

    private final MagiciansWorkbenchTile table;

    public static int SIZE = 18;

    public static int INVENTORY_STORAGE_START = 0;

    public static int INVENTORY_STORAGE_END = 17;

    public static int FIRST_CRAFT_GRID_START = 18;

    public static int FIRST_OUTPUT_SLOT = 27;

    public static int SECOND_CRAFT_GRID_START = 28;

    public static int SECOND_OUTPUT_SLOT = 37;

    public static int PLAYER_INVENTORY_START = 38;

    public static int PLAYER_ACTION_BAR_START = 65;

    public static int PLAYER_ACTION_BAR_END = 73;

    public CraftingContainer firstCraftMatrix;

    public CraftingContainer secondCraftMatrix;

    private Inventory playerInv;

    private boolean initializing;

    public ContainerMagiciansWorkbench(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(i, playerInventory, ((MagiciansWorkbenchTile) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos())).readFrom(packetBuffer));
    }

    public ContainerMagiciansWorkbench(int windowId, Inventory playerInventory, MagiciansWorkbenchTile inventory) {
        super(ContainerInit.MAGICIANS_WORKBENCH.get(), windowId);
        this.table = inventory;
        this.playerInv = playerInventory;
        inventory.m_5856_(playerInventory.player);
        this.firstCraftMatrix = new TransientCraftingContainer(this, 3, 3);
        this.secondCraftMatrix = new TransientCraftingContainer(this, 3, 3);
        this.updateCraftingMatrices();
        int slotIndex = 0;
        for (int xpos = 0; xpos < 3; xpos++) {
            for (int ypos = 0; ypos < 6; ypos++) {
                this.m_38897_(new Slot(this.table, slotIndex++, 196 + xpos * 18, 8 + ypos * 18));
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.m_38897_(new Slot(this.firstCraftMatrix, j + i * 3, 67 + j * 20, 11 + i * 20));
            }
        }
        this.m_38897_(new SlotMagiciansWorkbenchOutput(playerInventory.player, this.firstCraftMatrix, this.table.firstResultInv, this, 0, 87, 91));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.m_38897_(new Slot(this.secondCraftMatrix, j + i * 3, 133 + j * 20, 11 + i * 20));
            }
        }
        this.m_38897_(new SlotMagiciansWorkbenchOutput(playerInventory.player, this.secondCraftMatrix, this.table.secondResultInv, this, 0, 153, 91));
        for (int var9 = 0; var9 < 9; var9++) {
            for (int ypos = 0; ypos < 3; ypos++) {
                this.m_38897_(new Slot(playerInventory, var9 + ypos * 9 + 9, 48 + var9 * 18, 121 + ypos * 18));
            }
        }
        for (int var10 = 0; var10 < 9; var10++) {
            this.m_38897_(new Slot(playerInventory, var10, 48 + var10 * 18, 179));
        }
    }

    public void updateCraftingMatrices() {
        this.initializing = true;
        for (int i = 0; i < 9; i++) {
            this.firstCraftMatrix.m_6836_(i, this.table.m_8020_(i + FIRST_CRAFT_GRID_START));
            this.secondCraftMatrix.m_6836_(i, this.table.m_8020_(i + SECOND_CRAFT_GRID_START));
        }
        this.initializing = false;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.table.m_6542_(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == FIRST_OUTPUT_SLOT || index == SECOND_OUTPUT_SLOT) {
                itemstack1.getItem().onCraftedBy(itemstack1, this.table.m_58904_(), playerIn);
                if (!this.moveItemStackTo(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= PLAYER_INVENTORY_START && index < PLAYER_ACTION_BAR_END) {
                if (!this.moveItemStackTo(itemstack1, INVENTORY_STORAGE_START, FIRST_CRAFT_GRID_START, false)) {
                    if (index < PLAYER_ACTION_BAR_START) {
                        if (!this.moveItemStackTo(itemstack1, PLAYER_ACTION_BAR_START, PLAYER_ACTION_BAR_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_START, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= FIRST_CRAFT_GRID_START && index < FIRST_OUTPUT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, INVENTORY_STORAGE_START, FIRST_CRAFT_GRID_START, false) && !this.moveItemStackTo(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= SECOND_CRAFT_GRID_START && index < SECOND_OUTPUT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, INVENTORY_STORAGE_START, FIRST_CRAFT_GRID_START, false) && !this.moveItemStackTo(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
            if (index == 0) {
                playerIn.drop(itemstack1, false);
            }
        }
        return itemstack;
    }

    @Override
    public void removed(Player playerIn) {
        super.m_6877_(playerIn);
        this.table.m_5785_(playerIn);
    }

    @Override
    public void slotsChanged(Container p_75130_1_) {
        this.updateCraftingResult(this.f_38840_, this.table.m_58904_(), this.playerInv.player, this.firstCraftMatrix, this.table.firstResultInv, FIRST_OUTPUT_SLOT);
        this.updateCraftingResult(this.f_38840_, this.table.m_58904_(), this.playerInv.player, this.secondCraftMatrix, this.table.secondResultInv, SECOND_OUTPUT_SLOT);
        if (!this.initializing) {
            for (int i = 0; i < 9; i++) {
                this.table.m_6836_(i + FIRST_CRAFT_GRID_START, this.firstCraftMatrix.m_8020_(i));
                this.table.m_6836_(i + SECOND_CRAFT_GRID_START, this.secondCraftMatrix.m_8020_(i));
            }
        }
        this.m_38946_();
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int source, int dest, boolean loop) {
        return super.m_38903_(stack, source, dest, loop);
    }

    protected void updateCraftingResult(int id, Level world, Player player, CraftingContainer inventory, ResultContainer inventoryResult, int syncSlot) {
        if (!world.isClientSide) {
            ServerPlayer serverplayerentity = (ServerPlayer) player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventory, world);
            if (optional.isPresent()) {
                CraftingRecipe icraftingrecipe = (CraftingRecipe) optional.get();
                if (inventoryResult.m_40135_(world, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.m_5874_(inventory, world.registryAccess());
                }
            }
            inventoryResult.setItem(0, itemstack);
            serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(id, this.m_182425_(), syncSlot, itemstack));
        }
    }

    public MagiciansWorkbenchTile getWorkbench() {
        return this.table;
    }

    private StackedContents getStackedContents() {
        StackedContents stackedContents = new StackedContents();
        for (int i = INVENTORY_STORAGE_START; i <= INVENTORY_STORAGE_END; i++) {
            stackedContents.accountSimpleStack(this.table.m_8020_(i));
        }
        return stackedContents;
    }

    public boolean hasComponents(int recipeIndex) {
        MagiciansWorkbenchTile.RememberedRecipe remembered = (MagiciansWorkbenchTile.RememberedRecipe) this.table.getRememberedRecipeItems().get(recipeIndex);
        StackedContents contents = this.getStackedContents();
        return contents.canCraft(remembered.getRecipe(this.table.m_58904_()), (IntList) null);
    }

    private void setRecipeItemsToGrid(int recipeIndex) {
        MagiciansWorkbenchTile.RememberedRecipe recipe = (MagiciansWorkbenchTile.RememberedRecipe) this.table.getRememberedRecipeItems().get(recipeIndex);
        int count = 0;
        int startSlot = -1;
        if (this.craftingGridIsEmpty(true)) {
            startSlot = SECOND_CRAFT_GRID_START;
        } else if (this.craftingGridIsEmpty(false)) {
            startSlot = FIRST_CRAFT_GRID_START;
        }
        if (startSlot != -1) {
            CraftingRecipe craftingRecipe = recipe.getRecipe(this.table.m_58904_());
            if (craftingRecipe != null) {
                if (craftingRecipe instanceof ShapedRecipe shapedRecipe) {
                    Ingredient[] ingreds = recipe.getComponents(this.table.m_58904_());
                    for (int j = 0; j < shapedRecipe.getHeight(); j++) {
                        for (int i = 0; i < shapedRecipe.getWidth(); i++) {
                            int idx = i + j * shapedRecipe.getWidth();
                            int slotIdx = i + j * this.getGridWidth();
                            Slot slot = this.m_38853_(startSlot + slotIdx);
                            Ingredient ingredient = ingreds[idx];
                            if (!ingredient.isEmpty()) {
                                int ingredSlot = this.getSlotForIngredient(ingredient);
                                if (ingredSlot == -1) {
                                    break;
                                }
                                ItemStack existing = this.m_38853_(ingredSlot).getItem();
                                ItemStack copyStack = existing.copy();
                                existing.shrink(1);
                                copyStack.setCount(1);
                                slot.set(copyStack);
                            } else {
                                slot.set(ItemStack.EMPTY);
                            }
                        }
                    }
                } else {
                    for (Ingredient ingred : recipe.getComponents(this.table.m_58904_())) {
                        Slot slot = this.m_38853_(startSlot + count);
                        if (!ingred.isEmpty()) {
                            int ingredSlot = this.getSlotForIngredient(ingred);
                            if (ingredSlot == -1) {
                                break;
                            }
                            ItemStack existing = this.m_38853_(ingredSlot).getItem();
                            ItemStack copyStack = existing.copy();
                            existing.shrink(1);
                            copyStack.setCount(1);
                            slot.set(copyStack);
                        } else {
                            slot.set(ItemStack.EMPTY);
                        }
                        slot.setChanged();
                        count++;
                    }
                }
            }
        }
    }

    private int getSlotForIngredient(Ingredient ingred) {
        for (int i = INVENTORY_STORAGE_START; i <= INVENTORY_STORAGE_END; i++) {
            if (ingred.test(this.m_38853_(i).getItem())) {
                return i;
            }
        }
        return -1;
    }

    public boolean gridIsFreeFor(int recipeIndex) {
        return this.craftingGridIsEmpty(false) || this.craftingGridIsEmpty(true);
    }

    public void tryClearGrid(boolean second) {
        if (this.table.m_58904_().isClientSide()) {
            ClientMessageDispatcher.sendMagiciansWorkbenchClearMessage(second);
        } else {
            int startIndex = second ? SECOND_CRAFT_GRID_START : FIRST_CRAFT_GRID_START;
            for (int i = 0; i < 9; i++) {
                ItemStack gridStack = this.table.m_8020_(i + startIndex);
                if (!gridStack.isEmpty() && (this.moveItemStackTo(gridStack, INVENTORY_STORAGE_START, INVENTORY_STORAGE_END, true) || this.moveItemStackTo(gridStack, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, false))) {
                    this.table.m_6836_(i + startIndex, ItemStack.EMPTY);
                }
            }
            this.updateCraftingMatrices();
            this.slotsChanged(this.table);
            this.m_38946_();
        }
    }

    public boolean craftingGridIsEmpty(boolean second) {
        int startSlot = second ? SECOND_CRAFT_GRID_START : FIRST_CRAFT_GRID_START;
        for (int i = 0; i < 9; i++) {
            if (!this.getWorkbench().m_8020_(startSlot + i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void moveRecipeToCraftingGrid(int recipeIndex) {
        if (this.table.getRememberedRecipeItems().size() > recipeIndex) {
            if (this.gridIsFreeFor(recipeIndex) && !this.isRecipeAlreadyInGrid(recipeIndex)) {
                if (this.table.m_58904_().isClientSide()) {
                    ClientMessageDispatcher.sendMagiciansWorkbenchRecipeSetMessage(recipeIndex);
                } else {
                    if (this.hasComponents(recipeIndex)) {
                        this.setRecipeItemsToGrid(recipeIndex);
                        this.updateCraftingMatrices();
                        this.slotsChanged(this.table);
                        this.m_38946_();
                    }
                }
            }
        }
    }

    public boolean isRecipeAlreadyInGrid(int recipeIndex) {
        if (this.table.getRememberedRecipeItems().size() <= recipeIndex) {
            return false;
        } else {
            MagiciansWorkbenchTile.RememberedRecipe recipe = (MagiciansWorkbenchTile.RememberedRecipe) this.table.getRememberedRecipeItems().get(recipeIndex);
            return ItemStack.isSameItemSameTags(this.getWorkbench().firstResultInv.getItem(0), recipe.getOutput(this.table.m_58904_())) ? true : ItemStack.isSameItemSameTags(this.getWorkbench().secondResultInv.getItem(0), recipe.getOutput(this.table.m_58904_()));
        }
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return this.firstCraftMatrix.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.firstCraftMatrix.getHeight();
    }

    @Override
    public int getSize() {
        return 5;
    }

    public CraftingContainer getCraftSlots() {
        return this.firstCraftMatrix;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int int0) {
        return int0 != this.getResultSlotIndex();
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents0) {
        this.firstCraftMatrix.m_5809_(stackedContents0);
    }

    @Override
    public void clearCraftingContent() {
        this.table.firstResultInv.clearContent();
        this.firstCraftMatrix.m_6211_();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipeSuperCraftingContainer0) {
        return recipeSuperCraftingContainer0.matches(this.firstCraftMatrix, this.playerInv.player.m_9236_());
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonIndex) {
        return this.table.toggleRecipeLock(buttonIndex);
    }
}