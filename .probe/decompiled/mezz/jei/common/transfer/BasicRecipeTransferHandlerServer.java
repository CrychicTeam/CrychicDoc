package mezz.jei.common.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public final class BasicRecipeTransferHandlerServer {

    private static final Logger LOGGER = LogManager.getLogger();

    private BasicRecipeTransferHandlerServer() {
    }

    public static void setItems(Player player, List<TransferOperation> transferOperations, List<Slot> craftingSlots, List<Slot> inventorySlots, boolean maxTransfer, boolean requireCompleteSets) {
        if (RecipeTransferUtil.validateSlots(player, transferOperations, craftingSlots, inventorySlots)) {
            Map<Slot, BasicRecipeTransferHandlerServer.ItemStackWithSlotHint> recipeSlotToRequiredItemStack = calculateRequiredStacks(transferOperations, player);
            if (recipeSlotToRequiredItemStack != null) {
                boolean transferAsCompleteSets = requireCompleteSets || !maxTransfer;
                Map<Slot, ItemStack> recipeSlotToTakenStacks = takeItemsFromInventory(player, recipeSlotToRequiredItemStack, craftingSlots, inventorySlots, transferAsCompleteSets, maxTransfer);
                if (recipeSlotToTakenStacks.isEmpty()) {
                    LOGGER.error("Tried to transfer recipe but was unable to remove any items from the inventory.");
                } else {
                    List<ItemStack> clearedCraftingItems = clearCraftingGrid(craftingSlots, player);
                    List<ItemStack> remainderItems = putItemsIntoCraftingGrid(recipeSlotToTakenStacks, requireCompleteSets);
                    stowItems(player, inventorySlots, clearedCraftingItems);
                    stowItems(player, inventorySlots, remainderItems);
                    AbstractContainerMenu container = player.containerMenu;
                    container.broadcastChanges();
                }
            }
        }
    }

    private static int getSlotStackLimit(Map<Slot, ItemStack> recipeSlotToTakenStacks, boolean requireCompleteSets) {
        return !requireCompleteSets ? Integer.MAX_VALUE : recipeSlotToTakenStacks.entrySet().stream().mapToInt(e -> {
            Slot craftingSlot = (Slot) e.getKey();
            ItemStack transferItem = (ItemStack) e.getValue();
            return craftingSlot.mayPlace(transferItem) ? craftingSlot.getMaxStackSize(transferItem) : Integer.MAX_VALUE;
        }).min().orElse(Integer.MAX_VALUE);
    }

    private static List<ItemStack> clearCraftingGrid(List<Slot> craftingSlots, Player player) {
        List<ItemStack> clearedCraftingItems = new ArrayList();
        for (Slot craftingSlot : craftingSlots) {
            if (craftingSlot.mayPickup(player) && craftingSlot.hasItem()) {
                ItemStack craftingItem = craftingSlot.remove(Integer.MAX_VALUE);
                clearedCraftingItems.add(craftingItem);
            }
        }
        return clearedCraftingItems;
    }

    private static List<ItemStack> putItemsIntoCraftingGrid(Map<Slot, ItemStack> recipeSlotToTakenStacks, boolean requireCompleteSets) {
        int slotStackLimit = getSlotStackLimit(recipeSlotToTakenStacks, requireCompleteSets);
        List<ItemStack> remainderItems = new ArrayList();
        recipeSlotToTakenStacks.forEach((slot, stack) -> {
            if (slot.getItem().isEmpty() && slot.mayPlace(stack)) {
                ItemStack remainder = slot.safeInsert(stack, slotStackLimit);
                if (!remainder.isEmpty()) {
                    remainderItems.add(remainder);
                }
            } else {
                remainderItems.add(stack);
            }
        });
        return remainderItems;
    }

    @Nullable
    private static Map<Slot, BasicRecipeTransferHandlerServer.ItemStackWithSlotHint> calculateRequiredStacks(List<TransferOperation> transferOperations, Player player) {
        Map<Slot, BasicRecipeTransferHandlerServer.ItemStackWithSlotHint> recipeSlotToRequired = new HashMap(transferOperations.size());
        for (TransferOperation transferOperation : transferOperations) {
            Slot recipeSlot = transferOperation.craftingSlot();
            Slot inventorySlot = transferOperation.inventorySlot();
            if (!inventorySlot.mayPickup(player)) {
                LOGGER.error("Tried to transfer recipe but was given an inventory slot that the player can't pickup from: {}", inventorySlot.index);
                return null;
            }
            ItemStack slotStack = inventorySlot.getItem();
            if (slotStack.isEmpty()) {
                LOGGER.error("Tried to transfer recipe but was given an empty inventory slot as an ingredient source: {}", inventorySlot.index);
                return null;
            }
            ItemStack stack = slotStack.copy();
            stack.setCount(1);
            recipeSlotToRequired.put(recipeSlot, new BasicRecipeTransferHandlerServer.ItemStackWithSlotHint(inventorySlot, stack));
        }
        return recipeSlotToRequired;
    }

    @Nonnull
    private static Map<Slot, ItemStack> takeItemsFromInventory(Player player, Map<Slot, BasicRecipeTransferHandlerServer.ItemStackWithSlotHint> recipeSlotToRequiredItemStack, List<Slot> craftingSlots, List<Slot> inventorySlots, boolean transferAsCompleteSets, boolean maxTransfer) {
        if (!maxTransfer) {
            return removeOneSetOfItemsFromInventory(player, recipeSlotToRequiredItemStack, craftingSlots, inventorySlots, transferAsCompleteSets);
        } else {
            Map<Slot, ItemStack> recipeSlotToResult = new HashMap(recipeSlotToRequiredItemStack.size());
            while (true) {
                Map<Slot, ItemStack> foundItemsInSet = removeOneSetOfItemsFromInventory(player, recipeSlotToRequiredItemStack, craftingSlots, inventorySlots, transferAsCompleteSets);
                if (foundItemsInSet.isEmpty()) {
                    return recipeSlotToResult;
                }
                for (Slot fullSlot : merge(recipeSlotToResult, foundItemsInSet)) {
                    recipeSlotToRequiredItemStack.remove(fullSlot);
                }
            }
        }
    }

    private static Map<Slot, ItemStack> removeOneSetOfItemsFromInventory(Player player, Map<Slot, BasicRecipeTransferHandlerServer.ItemStackWithSlotHint> recipeSlotToRequiredItemStack, List<Slot> craftingSlots, List<Slot> inventorySlots, boolean transferAsCompleteSets) {
        Map<Slot, ItemStack> originalSlotContents = null;
        if (transferAsCompleteSets) {
            originalSlotContents = new HashMap();
        }
        Map<Slot, ItemStack> foundItemsInSet = new HashMap(recipeSlotToRequiredItemStack.size());
        for (Entry<Slot, BasicRecipeTransferHandlerServer.ItemStackWithSlotHint> entry : recipeSlotToRequiredItemStack.entrySet()) {
            Slot recipeSlot = (Slot) entry.getKey();
            ItemStack requiredStack = ((BasicRecipeTransferHandlerServer.ItemStackWithSlotHint) entry.getValue()).stack;
            Slot hint = ((BasicRecipeTransferHandlerServer.ItemStackWithSlotHint) entry.getValue()).hint;
            Slot slot = (Slot) getSlotWithStack(player, requiredStack, craftingSlots, inventorySlots, hint).orElse(null);
            if (slot != null) {
                if (originalSlotContents != null && !originalSlotContents.containsKey(slot)) {
                    originalSlotContents.put(slot, slot.getItem().copy());
                }
                ItemStack removedItemStack = slot.remove(1);
                foundItemsInSet.put(recipeSlot, removedItemStack);
            } else if (transferAsCompleteSets) {
                for (Entry<Slot, ItemStack> slotEntry : originalSlotContents.entrySet()) {
                    ItemStack stack = (ItemStack) slotEntry.getValue();
                    ((Slot) slotEntry.getKey()).set(stack);
                }
                return Map.of();
            }
        }
        return foundItemsInSet;
    }

    private static Set<Slot> merge(Map<Slot, ItemStack> result, Map<Slot, ItemStack> addition) {
        Set<Slot> fullSlots = new HashSet();
        addition.forEach((slot, itemStack) -> {
            assert itemStack.getCount() == 1;
            ItemStack resultItemStack = (ItemStack) result.get(slot);
            if (resultItemStack == null) {
                resultItemStack = itemStack;
                result.put(slot, itemStack);
            } else {
                assert ItemStack.isSameItemSameTags(resultItemStack, itemStack);
                resultItemStack.grow(itemStack.getCount());
            }
            if (resultItemStack.getCount() == slot.getMaxStackSize(resultItemStack)) {
                fullSlots.add(slot);
            }
        });
        return fullSlots;
    }

    private static Optional<Slot> getSlotWithStack(Player player, ItemStack stack, List<Slot> craftingSlots, List<Slot> inventorySlots, Slot hint) {
        return getSlotWithStack(player, craftingSlots, stack).or(() -> getValidatedHintSlot(player, stack, hint)).or(() -> getSlotWithStack(player, inventorySlots, stack));
    }

    private static Optional<Slot> getValidatedHintSlot(Player player, ItemStack stack, Slot hint) {
        return hint.mayPickup(player) && !hint.getItem().isEmpty() && ItemStack.isSameItemSameTags(stack, hint.getItem()) ? Optional.of(hint) : Optional.empty();
    }

    private static void stowItems(Player player, List<Slot> inventorySlots, List<ItemStack> itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            ItemStack remainder = stowItem(inventorySlots, itemStack);
            if (!remainder.isEmpty() && !player.getInventory().add(remainder)) {
                player.drop(remainder, false);
            }
        }
    }

    private static ItemStack stowItem(Collection<Slot> slots, ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack remainder = stack.copy();
            for (Slot slot : slots) {
                ItemStack inventoryStack = slot.getItem();
                if (!inventoryStack.isEmpty() && inventoryStack.isStackable()) {
                    slot.safeInsert(remainder);
                    if (remainder.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            for (Slot slotx : slots) {
                if (slotx.getItem().isEmpty()) {
                    slotx.safeInsert(remainder);
                    if (remainder.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            return remainder;
        }
    }

    private static Optional<Slot> getSlotWithStack(Player player, Collection<Slot> slots, ItemStack itemStack) {
        return slots.stream().filter(slot -> {
            ItemStack slotStack = slot.getItem();
            return ItemStack.isSameItemSameTags(itemStack, slotStack) && slot.mayPickup(player);
        }).findFirst();
    }

    private static record ItemStackWithSlotHint(Slot hint, ItemStack stack) {
    }
}