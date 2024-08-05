package net.minecraft.recipebook;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.slf4j.Logger;

public class ServerPlaceRecipe<C extends Container> implements PlaceRecipe<Integer> {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected final StackedContents stackedContents = new StackedContents();

    protected Inventory inventory;

    protected RecipeBookMenu<C> menu;

    public ServerPlaceRecipe(RecipeBookMenu<C> recipeBookMenuC0) {
        this.menu = recipeBookMenuC0;
    }

    public void recipeClicked(ServerPlayer serverPlayer0, @Nullable Recipe<C> recipeC1, boolean boolean2) {
        if (recipeC1 != null && serverPlayer0.getRecipeBook().m_12709_(recipeC1)) {
            this.inventory = serverPlayer0.m_150109_();
            if (this.testClearGrid() || serverPlayer0.isCreative()) {
                this.stackedContents.clear();
                serverPlayer0.m_150109_().fillStackedContents(this.stackedContents);
                this.menu.fillCraftSlotsStackedContents(this.stackedContents);
                if (this.stackedContents.canCraft(recipeC1, null)) {
                    this.handleRecipeClicked(recipeC1, boolean2);
                } else {
                    this.clearGrid();
                    serverPlayer0.connection.send(new ClientboundPlaceGhostRecipePacket(serverPlayer0.f_36096_.containerId, recipeC1));
                }
                serverPlayer0.m_150109_().setChanged();
            }
        }
    }

    protected void clearGrid() {
        for (int $$0 = 0; $$0 < this.menu.getSize(); $$0++) {
            if (this.menu.shouldMoveToInventory($$0)) {
                ItemStack $$1 = this.menu.m_38853_($$0).getItem().copy();
                this.inventory.placeItemBackInInventory($$1, false);
                this.menu.m_38853_($$0).set($$1);
            }
        }
        this.menu.clearCraftingContent();
    }

    protected void handleRecipeClicked(Recipe<C> recipeC0, boolean boolean1) {
        boolean $$2 = this.menu.recipeMatches(recipeC0);
        int $$3 = this.stackedContents.getBiggestCraftableStack(recipeC0, null);
        if ($$2) {
            for (int $$4 = 0; $$4 < this.menu.getGridHeight() * this.menu.getGridWidth() + 1; $$4++) {
                if ($$4 != this.menu.getResultSlotIndex()) {
                    ItemStack $$5 = this.menu.m_38853_($$4).getItem();
                    if (!$$5.isEmpty() && Math.min($$3, $$5.getMaxStackSize()) < $$5.getCount() + 1) {
                        return;
                    }
                }
            }
        }
        int $$6 = this.getStackSize(boolean1, $$3, $$2);
        IntList $$7 = new IntArrayList();
        if (this.stackedContents.canCraft(recipeC0, $$7, $$6)) {
            int $$8 = $$6;
            IntListIterator var8 = $$7.iterator();
            while (var8.hasNext()) {
                int $$9 = (Integer) var8.next();
                int $$10 = StackedContents.fromStackingIndex($$9).getMaxStackSize();
                if ($$10 < $$8) {
                    $$8 = $$10;
                }
            }
            if (this.stackedContents.canCraft(recipeC0, $$7, $$8)) {
                this.clearGrid();
                this.m_135408_(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipeC0, $$7.iterator(), $$8);
            }
        }
    }

    @Override
    public void addItemToSlot(Iterator<Integer> iteratorInteger0, int int1, int int2, int int3, int int4) {
        Slot $$5 = this.menu.m_38853_(int1);
        ItemStack $$6 = StackedContents.fromStackingIndex((Integer) iteratorInteger0.next());
        if (!$$6.isEmpty()) {
            for (int $$7 = 0; $$7 < int2; $$7++) {
                this.moveItemToGrid($$5, $$6);
            }
        }
    }

    protected int getStackSize(boolean boolean0, int int1, boolean boolean2) {
        int $$3 = 1;
        if (boolean0) {
            $$3 = int1;
        } else if (boolean2) {
            $$3 = 64;
            for (int $$4 = 0; $$4 < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; $$4++) {
                if ($$4 != this.menu.getResultSlotIndex()) {
                    ItemStack $$5 = this.menu.m_38853_($$4).getItem();
                    if (!$$5.isEmpty() && $$3 > $$5.getCount()) {
                        $$3 = $$5.getCount();
                    }
                }
            }
            if ($$3 < 64) {
                $$3++;
            }
        }
        return $$3;
    }

    protected void moveItemToGrid(Slot slot0, ItemStack itemStack1) {
        int $$2 = this.inventory.findSlotMatchingUnusedItem(itemStack1);
        if ($$2 != -1) {
            ItemStack $$3 = this.inventory.getItem($$2);
            if (!$$3.isEmpty()) {
                if ($$3.getCount() > 1) {
                    this.inventory.removeItem($$2, 1);
                } else {
                    this.inventory.removeItemNoUpdate($$2);
                }
                if (slot0.getItem().isEmpty()) {
                    slot0.set($$3.copyWithCount(1));
                } else {
                    slot0.getItem().grow(1);
                }
            }
        }
    }

    private boolean testClearGrid() {
        List<ItemStack> $$0 = Lists.newArrayList();
        int $$1 = this.getAmountOfFreeSlotsInInventory();
        for (int $$2 = 0; $$2 < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; $$2++) {
            if ($$2 != this.menu.getResultSlotIndex()) {
                ItemStack $$3 = this.menu.m_38853_($$2).getItem().copy();
                if (!$$3.isEmpty()) {
                    int $$4 = this.inventory.getSlotWithRemainingSpace($$3);
                    if ($$4 == -1 && $$0.size() <= $$1) {
                        for (ItemStack $$5 : $$0) {
                            if (ItemStack.isSameItem($$5, $$3) && $$5.getCount() != $$5.getMaxStackSize() && $$5.getCount() + $$3.getCount() <= $$5.getMaxStackSize()) {
                                $$5.grow($$3.getCount());
                                $$3.setCount(0);
                                break;
                            }
                        }
                        if (!$$3.isEmpty()) {
                            if ($$0.size() >= $$1) {
                                return false;
                            }
                            $$0.add($$3);
                        }
                    } else if ($$4 == -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int getAmountOfFreeSlotsInInventory() {
        int $$0 = 0;
        for (ItemStack $$1 : this.inventory.items) {
            if ($$1.isEmpty()) {
                $$0++;
            }
        }
        return $$0;
    }
}