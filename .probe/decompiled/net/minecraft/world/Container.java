package net.minecraft.world;

import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface Container extends Clearable {

    int LARGE_MAX_STACK_SIZE = 64;

    int DEFAULT_DISTANCE_LIMIT = 8;

    int getContainerSize();

    boolean isEmpty();

    ItemStack getItem(int var1);

    ItemStack removeItem(int var1, int var2);

    ItemStack removeItemNoUpdate(int var1);

    void setItem(int var1, ItemStack var2);

    default int getMaxStackSize() {
        return 64;
    }

    void setChanged();

    boolean stillValid(Player var1);

    default void startOpen(Player player0) {
    }

    default void stopOpen(Player player0) {
    }

    default boolean canPlaceItem(int int0, ItemStack itemStack1) {
        return true;
    }

    default boolean canTakeItem(Container container0, int int1, ItemStack itemStack2) {
        return true;
    }

    default int countItem(Item item0) {
        int $$1 = 0;
        for (int $$2 = 0; $$2 < this.getContainerSize(); $$2++) {
            ItemStack $$3 = this.getItem($$2);
            if ($$3.getItem().equals(item0)) {
                $$1 += $$3.getCount();
            }
        }
        return $$1;
    }

    default boolean hasAnyOf(Set<Item> setItem0) {
        return this.hasAnyMatching(p_216873_ -> !p_216873_.isEmpty() && setItem0.contains(p_216873_.getItem()));
    }

    default boolean hasAnyMatching(Predicate<ItemStack> predicateItemStack0) {
        for (int $$1 = 0; $$1 < this.getContainerSize(); $$1++) {
            ItemStack $$2 = this.getItem($$1);
            if (predicateItemStack0.test($$2)) {
                return true;
            }
        }
        return false;
    }

    static boolean stillValidBlockEntity(BlockEntity blockEntity0, Player player1) {
        return stillValidBlockEntity(blockEntity0, player1, 8);
    }

    static boolean stillValidBlockEntity(BlockEntity blockEntity0, Player player1, int int2) {
        Level $$3 = blockEntity0.getLevel();
        BlockPos $$4 = blockEntity0.getBlockPos();
        if ($$3 == null) {
            return false;
        } else {
            return $$3.getBlockEntity($$4) != blockEntity0 ? false : player1.m_20275_((double) $$4.m_123341_() + 0.5, (double) $$4.m_123342_() + 0.5, (double) $$4.m_123343_() + 0.5) <= (double) (int2 * int2);
        }
    }
}