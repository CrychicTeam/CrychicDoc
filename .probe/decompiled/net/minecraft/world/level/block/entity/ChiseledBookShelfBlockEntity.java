package net.minecraft.world.level.block.entity;

import com.mojang.logging.LogUtils;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.slf4j.Logger;

public class ChiseledBookShelfBlockEntity extends BlockEntity implements Container {

    public static final int MAX_BOOKS_IN_STORAGE = 6;

    private static final Logger LOGGER = LogUtils.getLogger();

    private final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);

    private int lastInteractedSlot = -1;

    public ChiseledBookShelfBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.CHISELED_BOOKSHELF, blockPos0, blockState1);
    }

    private void updateState(int int0) {
        if (int0 >= 0 && int0 < 6) {
            this.lastInteractedSlot = int0;
            BlockState $$1 = this.m_58900_();
            for (int $$2 = 0; $$2 < ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.size(); $$2++) {
                boolean $$3 = !this.getItem($$2).isEmpty();
                BooleanProperty $$4 = (BooleanProperty) ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get($$2);
                $$1 = (BlockState) $$1.m_61124_($$4, $$3);
            }
            ((Level) Objects.requireNonNull(this.f_58857_)).setBlock(this.f_58858_, $$1, 3);
        } else {
            LOGGER.error("Expected slot 0-5, got {}", int0);
        }
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        this.items.clear();
        ContainerHelper.loadAllItems(compoundTag0, this.items);
        this.lastInteractedSlot = compoundTag0.getInt("last_interacted_slot");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        ContainerHelper.saveAllItems(compoundTag0, this.items, true);
        compoundTag0.putInt("last_interacted_slot", this.lastInteractedSlot);
    }

    public int count() {
        return (int) this.items.stream().filter(Predicate.not(ItemStack::m_41619_)).count();
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public int getContainerSize() {
        return 6;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::m_41619_);
    }

    @Override
    public ItemStack getItem(int int0) {
        return this.items.get(int0);
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        ItemStack $$2 = (ItemStack) Objects.requireNonNullElse(this.items.get(int0), ItemStack.EMPTY);
        this.items.set(int0, ItemStack.EMPTY);
        if (!$$2.isEmpty()) {
            this.updateState(int0);
        }
        return $$2;
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        return this.removeItem(int0, 1);
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        if (itemStack1.is(ItemTags.BOOKSHELF_BOOKS)) {
            this.items.set(int0, itemStack1);
            this.updateState(int0);
        }
    }

    @Override
    public boolean canTakeItem(Container container0, int int1, ItemStack itemStack2) {
        return container0.hasAnyMatching(p_281577_ -> p_281577_.isEmpty() ? true : ItemStack.isSameItemSameTags(itemStack2, p_281577_) && p_281577_.getCount() + itemStack2.getCount() <= Math.min(p_281577_.getMaxStackSize(), container0.getMaxStackSize()));
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean stillValid(Player player0) {
        return Container.stillValidBlockEntity(this, player0);
    }

    @Override
    public boolean canPlaceItem(int int0, ItemStack itemStack1) {
        return itemStack1.is(ItemTags.BOOKSHELF_BOOKS) && this.getItem(int0).isEmpty();
    }

    public int getLastInteractedSlot() {
        return this.lastInteractedSlot;
    }
}