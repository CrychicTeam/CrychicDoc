package net.minecraft.world.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class ResultSlot extends Slot {

    private final CraftingContainer craftSlots;

    private final Player player;

    private int removeCount;

    public ResultSlot(Player player0, CraftingContainer craftingContainer1, Container container2, int int3, int int4, int int5) {
        super(container2, int3, int4, int5);
        this.player = player0;
        this.craftSlots = craftingContainer1;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack0) {
        return false;
    }

    @Override
    public ItemStack remove(int int0) {
        if (this.m_6657_()) {
            this.removeCount = this.removeCount + Math.min(int0, this.m_7993_().getCount());
        }
        return super.remove(int0);
    }

    @Override
    protected void onQuickCraft(ItemStack itemStack0, int int1) {
        this.removeCount += int1;
        this.checkTakeAchievements(itemStack0);
    }

    @Override
    protected void onSwapCraft(int int0) {
        this.removeCount += int0;
    }

    @Override
    protected void checkTakeAchievements(ItemStack itemStack0) {
        if (this.removeCount > 0) {
            itemStack0.onCraftedBy(this.player.m_9236_(), this.player, this.removeCount);
        }
        if (this.f_40218_ instanceof RecipeHolder $$1) {
            $$1.awardUsedRecipes(this.player, this.craftSlots.getItems());
        }
        this.removeCount = 0;
    }

    @Override
    public void onTake(Player player0, ItemStack itemStack1) {
        this.checkTakeAchievements(itemStack1);
        NonNullList<ItemStack> $$2 = player0.m_9236_().getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, this.craftSlots, player0.m_9236_());
        for (int $$3 = 0; $$3 < $$2.size(); $$3++) {
            ItemStack $$4 = this.craftSlots.m_8020_($$3);
            ItemStack $$5 = $$2.get($$3);
            if (!$$4.isEmpty()) {
                this.craftSlots.m_7407_($$3, 1);
                $$4 = this.craftSlots.m_8020_($$3);
            }
            if (!$$5.isEmpty()) {
                if ($$4.isEmpty()) {
                    this.craftSlots.m_6836_($$3, $$5);
                } else if (ItemStack.isSameItemSameTags($$4, $$5)) {
                    $$5.grow($$4.getCount());
                    this.craftSlots.m_6836_($$3, $$5);
                } else if (!this.player.getInventory().add($$5)) {
                    this.player.drop($$5, false);
                }
            }
        }
    }
}