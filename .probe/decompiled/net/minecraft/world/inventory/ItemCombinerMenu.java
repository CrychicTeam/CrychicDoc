package net.minecraft.world.inventory;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ItemCombinerMenu extends AbstractContainerMenu {

    private static final int INVENTORY_SLOTS_PER_ROW = 9;

    private static final int INVENTORY_SLOTS_PER_COLUMN = 3;

    protected final ContainerLevelAccess access;

    protected final Player player;

    protected final Container inputSlots;

    private final List<Integer> inputSlotIndexes;

    protected final ResultContainer resultSlots = new ResultContainer();

    private final int resultSlotIndex;

    protected abstract boolean mayPickup(Player var1, boolean var2);

    protected abstract void onTake(Player var1, ItemStack var2);

    protected abstract boolean isValidBlock(BlockState var1);

    public ItemCombinerMenu(@Nullable MenuType<?> menuType0, int int1, Inventory inventory2, ContainerLevelAccess containerLevelAccess3) {
        super(menuType0, int1);
        this.access = containerLevelAccess3;
        this.player = inventory2.player;
        ItemCombinerMenuSlotDefinition $$4 = this.createInputSlotDefinitions();
        this.inputSlots = this.createContainer($$4.getNumOfInputSlots());
        this.inputSlotIndexes = $$4.getInputSlotIndexes();
        this.resultSlotIndex = $$4.getResultSlotIndex();
        this.createInputSlots($$4);
        this.createResultSlot($$4);
        this.createInventorySlots(inventory2);
    }

    private void createInputSlots(ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition0) {
        for (final ItemCombinerMenuSlotDefinition.SlotDefinition $$1 : itemCombinerMenuSlotDefinition0.getSlots()) {
            this.m_38897_(new Slot(this.inputSlots, $$1.slotIndex(), $$1.x(), $$1.y()) {

                @Override
                public boolean mayPlace(ItemStack p_267156_) {
                    return $$1.mayPlace().test(p_267156_);
                }
            });
        }
    }

    private void createResultSlot(ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition0) {
        this.m_38897_(new Slot(this.resultSlots, itemCombinerMenuSlotDefinition0.getResultSlot().slotIndex(), itemCombinerMenuSlotDefinition0.getResultSlot().x(), itemCombinerMenuSlotDefinition0.getResultSlot().y()) {

            @Override
            public boolean mayPlace(ItemStack p_39818_) {
                return false;
            }

            @Override
            public boolean mayPickup(Player p_39813_) {
                return ItemCombinerMenu.this.mayPickup(p_39813_, this.m_6657_());
            }

            @Override
            public void onTake(Player p_150604_, ItemStack p_150605_) {
                ItemCombinerMenu.this.onTake(p_150604_, p_150605_);
            }
        });
    }

    private void createInventorySlots(Inventory inventory0) {
        for (int $$1 = 0; $$1 < 3; $$1++) {
            for (int $$2 = 0; $$2 < 9; $$2++) {
                this.m_38897_(new Slot(inventory0, $$2 + $$1 * 9 + 9, 8 + $$2 * 18, 84 + $$1 * 18));
            }
        }
        for (int $$3 = 0; $$3 < 9; $$3++) {
            this.m_38897_(new Slot(inventory0, $$3, 8 + $$3 * 18, 142));
        }
    }

    public abstract void createResult();

    protected abstract ItemCombinerMenuSlotDefinition createInputSlotDefinitions();

    private SimpleContainer createContainer(int int0) {
        return new SimpleContainer(int0) {

            @Override
            public void setChanged() {
                super.setChanged();
                ItemCombinerMenu.this.slotsChanged(this);
            }
        };
    }

    @Override
    public void slotsChanged(Container container0) {
        super.slotsChanged(container0);
        if (container0 == this.inputSlots) {
            this.createResult();
        }
    }

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        this.access.execute((p_39796_, p_39797_) -> this.m_150411_(player0, this.inputSlots));
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.access.evaluate((p_39785_, p_39786_) -> !this.isValidBlock(p_39785_.getBlockState(p_39786_)) ? false : player0.m_20275_((double) p_39786_.m_123341_() + 0.5, (double) p_39786_.m_123342_() + 0.5, (double) p_39786_.m_123343_() + 0.5) <= 64.0, true);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            int $$5 = this.getInventorySlotStart();
            int $$6 = this.getUseRowEnd();
            if (int1 == this.getResultSlot()) {
                if (!this.m_38903_($$4, $$5, $$6, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
            } else if (this.inputSlotIndexes.contains(int1)) {
                if (!this.m_38903_($$4, $$5, $$6, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.canMoveIntoInputSlots($$4) && int1 >= this.getInventorySlotStart() && int1 < this.getUseRowEnd()) {
                int $$7 = this.getSlotToQuickMoveTo($$2);
                if (!this.m_38903_($$4, $$7, this.getResultSlot(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (int1 >= this.getInventorySlotStart() && int1 < this.getInventorySlotEnd()) {
                if (!this.m_38903_($$4, this.getUseRowStart(), this.getUseRowEnd(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (int1 >= this.getUseRowStart() && int1 < this.getUseRowEnd() && !this.m_38903_($$4, this.getInventorySlotStart(), this.getInventorySlotEnd(), false)) {
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

    protected boolean canMoveIntoInputSlots(ItemStack itemStack0) {
        return true;
    }

    public int getSlotToQuickMoveTo(ItemStack itemStack0) {
        return this.inputSlots.isEmpty() ? 0 : (Integer) this.inputSlotIndexes.get(0);
    }

    public int getResultSlot() {
        return this.resultSlotIndex;
    }

    private int getInventorySlotStart() {
        return this.getResultSlot() + 1;
    }

    private int getInventorySlotEnd() {
        return this.getInventorySlotStart() + 27;
    }

    private int getUseRowStart() {
        return this.getInventorySlotEnd();
    }

    private int getUseRowEnd() {
        return this.getUseRowStart() + 9;
    }
}