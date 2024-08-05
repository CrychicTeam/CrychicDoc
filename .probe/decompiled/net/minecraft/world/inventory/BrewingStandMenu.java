package net.minecraft.world.inventory;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;

public class BrewingStandMenu extends AbstractContainerMenu {

    private static final int BOTTLE_SLOT_START = 0;

    private static final int BOTTLE_SLOT_END = 2;

    private static final int INGREDIENT_SLOT = 3;

    private static final int FUEL_SLOT = 4;

    private static final int SLOT_COUNT = 5;

    private static final int DATA_COUNT = 2;

    private static final int INV_SLOT_START = 5;

    private static final int INV_SLOT_END = 32;

    private static final int USE_ROW_SLOT_START = 32;

    private static final int USE_ROW_SLOT_END = 41;

    private final Container brewingStand;

    private final ContainerData brewingStandData;

    private final Slot ingredientSlot;

    public BrewingStandMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, new SimpleContainer(5), new SimpleContainerData(2));
    }

    public BrewingStandMenu(int int0, Inventory inventory1, Container container2, ContainerData containerData3) {
        super(MenuType.BREWING_STAND, int0);
        m_38869_(container2, 5);
        m_38886_(containerData3, 2);
        this.brewingStand = container2;
        this.brewingStandData = containerData3;
        this.m_38897_(new BrewingStandMenu.PotionSlot(container2, 0, 56, 51));
        this.m_38897_(new BrewingStandMenu.PotionSlot(container2, 1, 79, 58));
        this.m_38897_(new BrewingStandMenu.PotionSlot(container2, 2, 102, 51));
        this.ingredientSlot = this.m_38897_(new BrewingStandMenu.IngredientsSlot(container2, 3, 79, 17));
        this.m_38897_(new BrewingStandMenu.FuelSlot(container2, 4, 17, 17));
        this.m_38884_(containerData3);
        for (int $$4 = 0; $$4 < 3; $$4++) {
            for (int $$5 = 0; $$5 < 9; $$5++) {
                this.m_38897_(new Slot(inventory1, $$5 + $$4 * 9 + 9, 8 + $$5 * 18, 84 + $$4 * 18));
            }
        }
        for (int $$6 = 0; $$6 < 9; $$6++) {
            this.m_38897_(new Slot(inventory1, $$6, 8 + $$6 * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.brewingStand.stillValid(player0);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if ((int1 < 0 || int1 > 2) && int1 != 3 && int1 != 4) {
                if (BrewingStandMenu.FuelSlot.mayPlaceItem($$2)) {
                    if (this.m_38903_($$4, 4, 5, false) || this.ingredientSlot.mayPlace($$4) && !this.m_38903_($$4, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.ingredientSlot.mayPlace($$4)) {
                    if (!this.m_38903_($$4, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (BrewingStandMenu.PotionSlot.mayPlaceItem($$2) && $$2.getCount() == 1) {
                    if (!this.m_38903_($$4, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (int1 >= 5 && int1 < 32) {
                    if (!this.m_38903_($$4, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (int1 >= 32 && int1 < 41) {
                    if (!this.m_38903_($$4, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.m_38903_($$4, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.m_38903_($$4, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
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

    public int getFuel() {
        return this.brewingStandData.get(1);
    }

    public int getBrewingTicks() {
        return this.brewingStandData.get(0);
    }

    static class FuelSlot extends Slot {

        public FuelSlot(Container container0, int int1, int int2, int int3) {
            super(container0, int1, int2, int3);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack0) {
            return mayPlaceItem(itemStack0);
        }

        public static boolean mayPlaceItem(ItemStack itemStack0) {
            return itemStack0.is(Items.BLAZE_POWDER);
        }

        @Override
        public int getMaxStackSize() {
            return 64;
        }
    }

    static class IngredientsSlot extends Slot {

        public IngredientsSlot(Container container0, int int1, int int2, int int3) {
            super(container0, int1, int2, int3);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack0) {
            return PotionBrewing.isIngredient(itemStack0);
        }

        @Override
        public int getMaxStackSize() {
            return 64;
        }
    }

    static class PotionSlot extends Slot {

        public PotionSlot(Container container0, int int1, int int2, int int3) {
            super(container0, int1, int2, int3);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack0) {
            return mayPlaceItem(itemStack0);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void onTake(Player player0, ItemStack itemStack1) {
            Potion $$2 = PotionUtils.getPotion(itemStack1);
            if (player0 instanceof ServerPlayer) {
                CriteriaTriggers.BREWED_POTION.trigger((ServerPlayer) player0, $$2);
            }
            super.onTake(player0, itemStack1);
        }

        public static boolean mayPlaceItem(ItemStack itemStack0) {
            return itemStack0.is(Items.POTION) || itemStack0.is(Items.SPLASH_POTION) || itemStack0.is(Items.LINGERING_POTION) || itemStack0.is(Items.GLASS_BOTTLE);
        }
    }
}