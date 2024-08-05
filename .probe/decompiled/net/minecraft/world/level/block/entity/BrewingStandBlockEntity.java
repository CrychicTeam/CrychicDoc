package net.minecraft.world.level.block.entity;

import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BrewingStandBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    private static final int INGREDIENT_SLOT = 3;

    private static final int FUEL_SLOT = 4;

    private static final int[] SLOTS_FOR_UP = new int[] { 3 };

    private static final int[] SLOTS_FOR_DOWN = new int[] { 0, 1, 2, 3 };

    private static final int[] SLOTS_FOR_SIDES = new int[] { 0, 1, 2, 4 };

    public static final int FUEL_USES = 20;

    public static final int DATA_BREW_TIME = 0;

    public static final int DATA_FUEL_USES = 1;

    public static final int NUM_DATA_VALUES = 2;

    private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);

    int brewTime;

    private boolean[] lastPotionCount;

    private Item ingredient;

    int fuel;

    protected final ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int p_59038_) {
            switch(p_59038_) {
                case 0:
                    return BrewingStandBlockEntity.this.brewTime;
                case 1:
                    return BrewingStandBlockEntity.this.fuel;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int p_59040_, int p_59041_) {
            switch(p_59040_) {
                case 0:
                    BrewingStandBlockEntity.this.brewTime = p_59041_;
                    break;
                case 1:
                    BrewingStandBlockEntity.this.fuel = p_59041_;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public BrewingStandBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.BREWING_STAND, blockPos0, blockState1);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.brewing");
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack $$0 : this.items) {
            if (!$$0.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static void serverTick(Level level0, BlockPos blockPos1, BlockState blockState2, BrewingStandBlockEntity brewingStandBlockEntity3) {
        ItemStack $$4 = brewingStandBlockEntity3.items.get(4);
        if (brewingStandBlockEntity3.fuel <= 0 && $$4.is(Items.BLAZE_POWDER)) {
            brewingStandBlockEntity3.fuel = 20;
            $$4.shrink(1);
            m_155232_(level0, blockPos1, blockState2);
        }
        boolean $$5 = isBrewable(brewingStandBlockEntity3.items);
        boolean $$6 = brewingStandBlockEntity3.brewTime > 0;
        ItemStack $$7 = brewingStandBlockEntity3.items.get(3);
        if ($$6) {
            brewingStandBlockEntity3.brewTime--;
            boolean $$8 = brewingStandBlockEntity3.brewTime == 0;
            if ($$8 && $$5) {
                doBrew(level0, blockPos1, brewingStandBlockEntity3.items);
                m_155232_(level0, blockPos1, blockState2);
            } else if (!$$5 || !$$7.is(brewingStandBlockEntity3.ingredient)) {
                brewingStandBlockEntity3.brewTime = 0;
                m_155232_(level0, blockPos1, blockState2);
            }
        } else if ($$5 && brewingStandBlockEntity3.fuel > 0) {
            brewingStandBlockEntity3.fuel--;
            brewingStandBlockEntity3.brewTime = 400;
            brewingStandBlockEntity3.ingredient = $$7.getItem();
            m_155232_(level0, blockPos1, blockState2);
        }
        boolean[] $$9 = brewingStandBlockEntity3.getPotionBits();
        if (!Arrays.equals($$9, brewingStandBlockEntity3.lastPotionCount)) {
            brewingStandBlockEntity3.lastPotionCount = $$9;
            BlockState $$10 = blockState2;
            if (!(blockState2.m_60734_() instanceof BrewingStandBlock)) {
                return;
            }
            for (int $$11 = 0; $$11 < BrewingStandBlock.HAS_BOTTLE.length; $$11++) {
                $$10 = (BlockState) $$10.m_61124_(BrewingStandBlock.HAS_BOTTLE[$$11], $$9[$$11]);
            }
            level0.setBlock(blockPos1, $$10, 2);
        }
    }

    private boolean[] getPotionBits() {
        boolean[] $$0 = new boolean[3];
        for (int $$1 = 0; $$1 < 3; $$1++) {
            if (!this.items.get($$1).isEmpty()) {
                $$0[$$1] = true;
            }
        }
        return $$0;
    }

    private static boolean isBrewable(NonNullList<ItemStack> nonNullListItemStack0) {
        ItemStack $$1 = nonNullListItemStack0.get(3);
        if ($$1.isEmpty()) {
            return false;
        } else if (!PotionBrewing.isIngredient($$1)) {
            return false;
        } else {
            for (int $$2 = 0; $$2 < 3; $$2++) {
                ItemStack $$3 = nonNullListItemStack0.get($$2);
                if (!$$3.isEmpty() && PotionBrewing.hasMix($$3, $$1)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static void doBrew(Level level0, BlockPos blockPos1, NonNullList<ItemStack> nonNullListItemStack2) {
        ItemStack $$3 = nonNullListItemStack2.get(3);
        for (int $$4 = 0; $$4 < 3; $$4++) {
            nonNullListItemStack2.set($$4, PotionBrewing.mix($$3, nonNullListItemStack2.get($$4)));
        }
        $$3.shrink(1);
        if ($$3.getItem().hasCraftingRemainingItem()) {
            ItemStack $$5 = new ItemStack($$3.getItem().getCraftingRemainingItem());
            if ($$3.isEmpty()) {
                $$3 = $$5;
            } else {
                Containers.dropItemStack(level0, (double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_(), $$5);
            }
        }
        nonNullListItemStack2.set(3, $$3);
        level0.m_46796_(1035, blockPos1, 0);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag0, this.items);
        this.brewTime = compoundTag0.getShort("BrewTime");
        this.fuel = compoundTag0.getByte("Fuel");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        compoundTag0.putShort("BrewTime", (short) this.brewTime);
        ContainerHelper.saveAllItems(compoundTag0, this.items);
        compoundTag0.putByte("Fuel", (byte) this.fuel);
    }

    @Override
    public ItemStack getItem(int int0) {
        return int0 >= 0 && int0 < this.items.size() ? this.items.get(int0) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        return ContainerHelper.removeItem(this.items, int0, int1);
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        return ContainerHelper.takeItem(this.items, int0);
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        if (int0 >= 0 && int0 < this.items.size()) {
            this.items.set(int0, itemStack1);
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return Container.stillValidBlockEntity(this, player0);
    }

    @Override
    public boolean canPlaceItem(int int0, ItemStack itemStack1) {
        if (int0 == 3) {
            return PotionBrewing.isIngredient(itemStack1);
        } else {
            return int0 == 4 ? itemStack1.is(Items.BLAZE_POWDER) : (itemStack1.is(Items.POTION) || itemStack1.is(Items.SPLASH_POTION) || itemStack1.is(Items.LINGERING_POTION) || itemStack1.is(Items.GLASS_BOTTLE)) && this.getItem(int0).isEmpty();
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction0) {
        if (direction0 == Direction.UP) {
            return SLOTS_FOR_UP;
        } else {
            return direction0 == Direction.DOWN ? SLOTS_FOR_DOWN : SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int int0, ItemStack itemStack1, @Nullable Direction direction2) {
        return this.canPlaceItem(int0, itemStack1);
    }

    @Override
    public boolean canTakeItemThroughFace(int int0, ItemStack itemStack1, Direction direction2) {
        return int0 == 3 ? itemStack1.is(Items.GLASS_BOTTLE) : true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    protected AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return new BrewingStandMenu(int0, inventory1, this, this.dataAccess);
    }
}