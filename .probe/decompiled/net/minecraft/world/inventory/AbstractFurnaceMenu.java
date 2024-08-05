package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public abstract class AbstractFurnaceMenu extends RecipeBookMenu<Container> {

    public static final int INGREDIENT_SLOT = 0;

    public static final int FUEL_SLOT = 1;

    public static final int RESULT_SLOT = 2;

    public static final int SLOT_COUNT = 3;

    public static final int DATA_COUNT = 4;

    private static final int INV_SLOT_START = 3;

    private static final int INV_SLOT_END = 30;

    private static final int USE_ROW_SLOT_START = 30;

    private static final int USE_ROW_SLOT_END = 39;

    private final Container container;

    private final ContainerData data;

    protected final Level level;

    private final RecipeType<? extends AbstractCookingRecipe> recipeType;

    private final RecipeBookType recipeBookType;

    protected AbstractFurnaceMenu(MenuType<?> menuType0, RecipeType<? extends AbstractCookingRecipe> recipeTypeExtendsAbstractCookingRecipe1, RecipeBookType recipeBookType2, int int3, Inventory inventory4) {
        this(menuType0, recipeTypeExtendsAbstractCookingRecipe1, recipeBookType2, int3, inventory4, new SimpleContainer(3), new SimpleContainerData(4));
    }

    protected AbstractFurnaceMenu(MenuType<?> menuType0, RecipeType<? extends AbstractCookingRecipe> recipeTypeExtendsAbstractCookingRecipe1, RecipeBookType recipeBookType2, int int3, Inventory inventory4, Container container5, ContainerData containerData6) {
        super(menuType0, int3);
        this.recipeType = recipeTypeExtendsAbstractCookingRecipe1;
        this.recipeBookType = recipeBookType2;
        m_38869_(container5, 3);
        m_38886_(containerData6, 4);
        this.container = container5;
        this.data = containerData6;
        this.level = inventory4.player.m_9236_();
        this.m_38897_(new Slot(container5, 0, 56, 17));
        this.m_38897_(new FurnaceFuelSlot(this, container5, 1, 56, 53));
        this.m_38897_(new FurnaceResultSlot(inventory4.player, container5, 2, 116, 35));
        for (int $$7 = 0; $$7 < 3; $$7++) {
            for (int $$8 = 0; $$8 < 9; $$8++) {
                this.m_38897_(new Slot(inventory4, $$8 + $$7 * 9 + 9, 8 + $$8 * 18, 84 + $$7 * 18));
            }
        }
        for (int $$9 = 0; $$9 < 9; $$9++) {
            this.m_38897_(new Slot(inventory4, $$9, 8 + $$9 * 18, 142));
        }
        this.m_38884_(containerData6);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents0) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible) this.container).fillStackedContents(stackedContents0);
        }
    }

    @Override
    public void clearCraftingContent() {
        this.m_38853_(0).set(ItemStack.EMPTY);
        this.m_38853_(2).set(ItemStack.EMPTY);
    }

    @Override
    public boolean recipeMatches(Recipe<? super Container> recipeSuperContainer0) {
        return recipeSuperContainer0.matches(this.container, this.level);
    }

    @Override
    public int getResultSlotIndex() {
        return 2;
    }

    @Override
    public int getGridWidth() {
        return 1;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.container.stillValid(player0);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if (int1 == 2) {
                if (!this.m_38903_($$4, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
            } else if (int1 != 1 && int1 != 0) {
                if (this.canSmelt($$4)) {
                    if (!this.m_38903_($$4, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel($$4)) {
                    if (!this.m_38903_($$4, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (int1 >= 3 && int1 < 30) {
                    if (!this.m_38903_($$4, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (int1 >= 30 && int1 < 39 && !this.m_38903_($$4, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_($$4, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            if ($$4.isEmpty()) {
                $$3.setByPlayer(ItemStack.EMPTY);
            } else {
                $$3.setChanged();
            }
            if ($$4.getCount() == $$2.getCount()) {
                return ItemStack.EMPTY;
            }
            $$3.onTake(player0, $$4);
        }
        return $$2;
    }

    protected boolean canSmelt(ItemStack itemStack0) {
        return this.level.getRecipeManager().getRecipeFor(this.recipeType, new SimpleContainer(itemStack0), this.level).isPresent();
    }

    protected boolean isFuel(ItemStack itemStack0) {
        return AbstractFurnaceBlockEntity.isFuel(itemStack0);
    }

    public int getBurnProgress() {
        int $$0 = this.data.get(2);
        int $$1 = this.data.get(3);
        return $$1 != 0 && $$0 != 0 ? $$0 * 24 / $$1 : 0;
    }

    public int getLitProgress() {
        int $$0 = this.data.get(1);
        if ($$0 == 0) {
            $$0 = 200;
        }
        return this.data.get(0) * 13 / $$0;
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }

    @Override
    public boolean shouldMoveToInventory(int int0) {
        return int0 != 1;
    }
}