package dev.shadowsoffire.placebo.menu;

import dev.shadowsoffire.placebo.mixin.AbstractContainerMenuInvoker;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class QuickMoveHandler {

    protected List<QuickMoveHandler.QuickMoveRule> rules = new ArrayList();

    public ItemStack quickMoveStack(QuickMoveHandler.IExposedContainer container, Player player, int index) {
        if (this.rules.isEmpty()) {
            throw new RuntimeException("Quick Move requires at least one rule to be registered");
        } else {
            ItemStack slotStackCopy = ItemStack.EMPTY;
            Slot slot = container.getMenuSlot(index);
            if (slot != null && slot.hasItem()) {
                ItemStack slotStack = slot.getItem();
                slotStackCopy = slotStack.copy();
                boolean matched = false;
                for (QuickMoveHandler.QuickMoveRule rule : this.rules) {
                    if (rule.req.test(slotStack, index) && slot.mayPickup(player)) {
                        if (!container.moveMenuItemStackTo(slotStack, rule.startIdx, rule.endIdx, rule.reversed)) {
                            return ItemStack.EMPTY;
                        }
                        slot.onTake(player, slotStack);
                        container.onQuickMove(slotStackCopy, slotStack, slot);
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    return ItemStack.EMPTY;
                }
                if (slotStack.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }
            }
            return slotStackCopy;
        }
    }

    public void registerRule(BiPredicate<ItemStack, Integer> req, int startIdx, int endIdx, boolean reversed) {
        this.rules.add(new QuickMoveHandler.QuickMoveRule(req, startIdx, endIdx, reversed));
    }

    public void registerRule(BiPredicate<ItemStack, Integer> req, int startIdx, int endIdx) {
        this.registerRule(req, startIdx, endIdx, false);
    }

    public interface IExposedContainer {

        default boolean moveMenuItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
            return ((AbstractContainerMenuInvoker) this)._moveItemStackTo(pStack, pStartIndex, pEndIndex, pReverseDirection);
        }

        default Slot getMenuSlot(int index) {
            return ((AbstractContainerMenu) this).getSlot(index);
        }

        default void onQuickMove(ItemStack original, ItemStack remaining, Slot slot) {
            slot.setChanged();
        }
    }

    protected static record QuickMoveRule(BiPredicate<ItemStack, Integer> req, int startIdx, int endIdx, boolean reversed) {
    }
}