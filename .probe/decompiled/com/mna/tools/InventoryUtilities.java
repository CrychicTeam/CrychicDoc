package com.mna.tools;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.items.DynamicItemFilter;
import com.mna.api.items.IPositionalItem;
import com.mna.api.tools.MATags;
import com.mna.inventory.InventoryRitualKit;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.ritual.PractitionersPouchPatches;
import com.mna.items.sorcery.ItemEntityCrystal;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import top.theillusivec4.curios.api.CuriosApi;

public class InventoryUtilities {

    public static boolean hasRoomFor(IItemHandler inventory, ItemStack item) {
        ItemStack workingCopy = item.copy();
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (inventory.isItemValid(i, workingCopy)) {
                ItemStack remaining = inventory.insertItem(i, workingCopy, true);
                if (remaining.isEmpty()) {
                    return true;
                }
                workingCopy.setCount(remaining.getCount());
            }
        }
        return false;
    }

    public static boolean mergeIntoInventory(IItemHandler handler, ItemStack toMerge) {
        return mergeIntoInventory(handler, toMerge, toMerge.getCount());
    }

    public static boolean mergeIntoInventory(IItemHandler handler, ItemStack toMerge, int quantity) {
        if (quantity > toMerge.getCount()) {
            quantity = toMerge.getCount();
        }
        int emptySlot = -1;
        ItemStack insertStack = toMerge.copy();
        for (int i = 0; i < handler.getSlots(); i++) {
            if (handler.isItemValid(i, insertStack)) {
                ItemStack inventoryStack = handler.getStackInSlot(i);
                if (inventoryStack.isEmpty()) {
                    if (emptySlot == -1) {
                        emptySlot = i;
                    }
                } else if (ItemStack.isSameItemSameTags(inventoryStack, insertStack) && inventoryStack.getCount() < handler.getSlotLimit(i)) {
                    int spaceAvailable = handler.getSlotLimit(i) - inventoryStack.getCount();
                    ItemStack split = insertStack.split(Math.min(quantity, spaceAvailable));
                    ItemStack remainder = handler.insertItem(i, split, false);
                    int amountInserted = split.getCount() - remainder.getCount();
                    quantity -= amountInserted;
                    toMerge.setCount(toMerge.getCount() - amountInserted);
                    if (quantity <= 0) {
                        return true;
                    }
                    if (remainder.getCount() > 0) {
                        insertStack = remainder;
                    }
                }
            }
        }
        if (quantity > 0 && emptySlot > -1) {
            ItemStack insert = ItemStack.EMPTY;
            int maxStackSize = handler.getSlotLimit(emptySlot);
            if (insertStack.getCount() > maxStackSize) {
                insert = insertStack.split(maxStackSize);
                insert = handler.insertItem(emptySlot, insert, false);
                insertStack.setCount(insertStack.getCount() + insert.getCount());
            } else {
                insertStack = handler.insertItem(emptySlot, insertStack, false);
            }
            toMerge.setCount(insertStack.getCount());
            return toMerge.getCount() == 0;
        } else {
            return false;
        }
    }

    public static ItemStack mergeToPlayerInvPrioritizeOffhand(Player player, ItemStack toMerge) {
        if (player == null) {
            return toMerge;
        } else {
            ItemStack summonerOffhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
            if (summonerOffhand.isEmpty()) {
                player.setItemSlot(EquipmentSlot.OFFHAND, toMerge);
                return ItemStack.EMPTY;
            } else {
                if (ItemStack.isSameItemSameTags(toMerge, summonerOffhand) && summonerOffhand.getCount() < summonerOffhand.getMaxStackSize()) {
                    int toAdd = Math.min(summonerOffhand.getMaxStackSize() - summonerOffhand.getCount(), toMerge.getCount());
                    summonerOffhand.setCount(summonerOffhand.getCount() + toAdd);
                    toMerge.setCount(toMerge.getCount() - toAdd);
                    if (toMerge.getCount() <= 0) {
                        return ItemStack.EMPTY;
                    }
                }
                return player.addItem(toMerge) ? ItemStack.EMPTY : toMerge;
            }
        }
    }

    public static ItemStack getFirstItemFromContainer(List<Item> items, int maxCount, IItemHandler handler, Direction side) {
        return getFirstItemFromContainer(items, maxCount, handler, side, false);
    }

    public static ItemStack getFirstItemFromContainer(List<Item> items, int maxCount, IItemHandler handler, Direction side, boolean simulate) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty() && (items.size() == 0 || items.contains(stack.getItem()))) {
                int count = Math.min(stack.getCount(), maxCount);
                return handler.extractItem(i, count, simulate);
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getFirstItemFromContainer(DynamicItemFilter filter, int maxCount, IItemHandler handler, Direction side) {
        return getFirstItemFromContainer(filter, maxCount, handler, side, false);
    }

    public static ItemStack getFirstItemFromContainer(DynamicItemFilter filter, int maxCount, IItemHandler handler, Direction side, boolean simulate) {
        return getFirstItemFromContainer(filter, maxCount, handler, side, false, simulate);
    }

    public static ItemStack getFirstItemFromContainer(DynamicItemFilter filter, int maxCount, IItemHandler handler, Direction side, boolean randomFilterItem, boolean simulate) {
        ArrayList<Integer> slots = new ArrayList();
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                slots.add(i);
            }
        }
        if (randomFilterItem) {
            Collections.shuffle(slots);
        }
        for (Integer ix : slots) {
            ItemStack stack = handler.getStackInSlot(ix);
            if (!stack.isEmpty() && filter.matches(stack)) {
                int count = Math.min(stack.getCount(), maxCount);
                if (simulate) {
                    ItemStack extractedSimulated = handler.extractItem(ix, count, true);
                    if (!extractedSimulated.isEmpty()) {
                        return extractedSimulated;
                    }
                } else if (!handler.extractItem(ix, count, true).isEmpty()) {
                    return handler.extractItem(ix, count, false);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean hasStackInInventory(DynamicItemFilter search, IItemHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && search.matches(stack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasStackInInventory(ItemStack search, boolean ignoreDurability, boolean compareNBT, IItemHandler inventory) {
        int countToFind = search.getCount();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                boolean equal = ignoreDurability ? ItemStack.isSameItem(stack, search) : ItemStack.isSameItem(stack, search) && stack.getDamageValue() == search.getDamageValue();
                if (compareNBT) {
                    equal &= ManaAndArtificeMod.getItemHelper().AreTagsEqual(stack, search);
                }
                if (equal) {
                    countToFind -= stack.getCount();
                    if (countToFind <= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean removeItemFromInventory(ItemStack search, boolean ignoreDurability, boolean compareNBT, IItemHandler inventory) {
        int countToRemove = search.getCount();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                boolean equal = ignoreDurability ? ItemStack.isSameItem(stack, search) : ItemStack.isSameItem(stack, search) && stack.getDamageValue() == search.getDamageValue();
                if (compareNBT) {
                    if (search.getItem() == ItemInit.ENTITY_ENTRAPMENT_CRYSTAL.get()) {
                        equal &= ItemEntityCrystal.getEntityType(search) == ItemEntityCrystal.getEntityType(stack);
                    } else {
                        equal &= ManaAndArtificeMod.getItemHelper().AreTagsEqual(stack, search);
                    }
                }
                if (equal) {
                    int removeQty = Math.min(countToRemove, stack.getCount());
                    stack.shrink(removeQty);
                    countToRemove -= removeQty;
                    if (countToRemove <= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasStackInInventory(ItemStack search, boolean ignoreDurability, boolean compareNBT, IItemHandler inventory, Direction face) {
        int countToFind = search.getCount();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.extractItem(i, countToFind, true);
            if (!stack.isEmpty()) {
                boolean equal = ignoreDurability ? ItemStack.isSameItem(stack, search) : ItemStack.isSameItem(stack, search) && stack.getDamageValue() == search.getDamageValue();
                if (compareNBT) {
                    if (search.getItem() == ItemInit.ENTITY_ENTRAPMENT_CRYSTAL.get()) {
                        equal &= ItemEntityCrystal.getEntityType(search) == ItemEntityCrystal.getEntityType(stack);
                    } else {
                        equal &= ManaAndArtificeMod.getItemHelper().AreTagsEqual(stack, search);
                    }
                }
                if (equal) {
                    countToFind -= stack.getCount();
                    if (countToFind <= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean removeItemFromInventory(ItemStack search, boolean ignoreDurability, boolean compareNBT, IItemHandler inventory, Direction face) {
        int countToRemove = search.getCount();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.extractItem(i, countToRemove, true);
            if (!stack.isEmpty()) {
                boolean equal = ignoreDurability ? ItemStack.isSameItem(stack, search) : ItemStack.isSameItem(stack, search) && stack.getDamageValue() == search.getDamageValue();
                if (compareNBT) {
                    equal &= ManaAndArtificeMod.getItemHelper().AreTagsEqual(stack, search);
                }
                if (equal) {
                    ItemStack extracted = inventory.extractItem(i, countToRemove, false);
                    if (!extracted.isEmpty()) {
                        countToRemove -= extracted.getCount();
                        if (countToRemove <= 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean consumeAcrossInventories(ItemStack search, boolean ignoreDurability, boolean compareNBT, boolean simulate, List<Pair<IItemHandler, Direction>> inventories) {
        int count = search.getCount();
        for (int idx = 0; idx < inventories.size(); idx++) {
            IItemHandler inventory = (IItemHandler) ((Pair) inventories.get(idx)).getFirst();
            if (inventory != null) {
                boolean invConsumed = false;
                for (int i = 0; i < inventory.getSlots(); i++) {
                    ItemStack stack = inventory.extractItem(i, count, true);
                    if (!stack.isEmpty()) {
                        boolean equal = ignoreDurability ? ItemStack.isSameItem(stack, search) : ItemStack.isSameItem(stack, search) && stack.getDamageValue() == search.getDamageValue();
                        if (compareNBT) {
                            boolean tagMatch = ManaAndArtificeMod.getItemHelper().AreTagsEqual(stack, search);
                            if (stack.getTag() == null && search.getTag() != null && search.getTag().isEmpty()) {
                                tagMatch = true;
                            } else if (stack.getTag() != null && search.getTag() == null && stack.getTag().isEmpty()) {
                                tagMatch = true;
                            }
                            equal &= tagMatch;
                        }
                        if (equal) {
                            if (!simulate) {
                                ItemStack extracted = inventory.extractItem(i, count, false);
                                if (!extracted.isEmpty()) {
                                    count -= extracted.getCount();
                                    invConsumed = true;
                                }
                            } else {
                                count -= stack.getCount();
                                invConsumed = true;
                            }
                            if (count <= 0) {
                                if (inventory instanceof ItemInventoryBase) {
                                    ((ItemInventoryBase) inventory).writeItemStack();
                                }
                                return true;
                            }
                        }
                    }
                }
                if (invConsumed && inventory instanceof ItemInventoryBase) {
                    ((ItemInventoryBase) inventory).writeItemStack();
                }
            }
        }
        return false;
    }

    public static boolean consumeAcrossInventories(List<Item> search, int count, boolean ignoreDurability, boolean compareNBT, boolean simulate, List<Pair<IItemHandler, Direction>> inventories) {
        for (int idx = 0; idx < inventories.size(); idx++) {
            IItemHandler inventory = (IItemHandler) ((Pair) inventories.get(idx)).getFirst();
            if (inventory != null) {
                boolean invConsumed = false;
                for (int i = 0; i < inventory.getSlots(); i++) {
                    ItemStack stack = inventory.extractItem(i, count, true);
                    if (!stack.isEmpty()) {
                        boolean equal = search.contains(stack.getItem());
                        if (equal) {
                            if (!simulate) {
                                ItemStack extracted = inventory.extractItem(i, count, false);
                                if (!extracted.isEmpty()) {
                                    count -= extracted.getCount();
                                    invConsumed = true;
                                }
                            } else {
                                count -= stack.getCount();
                                invConsumed = true;
                            }
                            if (count <= 0) {
                                if (inventory instanceof ItemInventoryBase) {
                                    ((ItemInventoryBase) inventory).writeItemStack();
                                }
                                return true;
                            }
                        }
                    }
                }
                if (invConsumed && inventory instanceof ItemInventoryBase) {
                    ((ItemInventoryBase) inventory).writeItemStack();
                }
            }
        }
        return false;
    }

    public static int countItem(ItemStack search, IItemHandler inventory, Direction face, boolean compareNBT, boolean ignoreDurability) {
        int i = 0;
        for (int j = 0; j < inventory.getSlots(); j++) {
            ItemStack stack = inventory.getStackInSlot(j);
            boolean equal = ignoreDurability ? ItemStack.isSameItem(stack, search) : ItemStack.isSameItem(stack, search) && stack.getDamageValue() == search.getDamageValue();
            if (equal) {
                if (compareNBT) {
                    boolean tagMatch = false;
                    if (search.getItem() == ItemInit.ENTITY_ENTRAPMENT_CRYSTAL.get()) {
                        tagMatch = ItemEntityCrystal.getEntityType(search) == ItemEntityCrystal.getEntityType(stack);
                    } else {
                        tagMatch = ManaAndArtificeMod.getItemHelper().AreTagsEqual(stack, search);
                    }
                    if (stack.getTag() == null && search.getTag() != null && search.getTag().isEmpty()) {
                        tagMatch = true;
                    } else if (stack.getTag() != null && search.getTag() == null && stack.getTag().isEmpty()) {
                        tagMatch = true;
                    }
                    equal &= tagMatch;
                }
                if (equal) {
                    i += stack.getCount();
                }
            }
        }
        return i;
    }

    public static int countItem(DynamicItemFilter filter, IItemHandler inventory, Direction face) {
        int i = 0;
        for (int j = 0; j < inventory.getSlots(); j++) {
            ItemStack stack = inventory.getStackInSlot(j);
            boolean equal = filter.matches(stack);
            if (equal) {
                i += stack.getCount();
            }
        }
        return i;
    }

    public static ItemStack removeSingleItemFromInventory(ResourceLocation search, Container inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && MATags.isItemEqual(stack, search)) {
                return stack.split(1);
            }
        }
        return ItemStack.EMPTY;
    }

    public static void redirectCaptureOrDrop(Player player, Level world, List<ItemStack> drops, boolean allowRedirect) {
        if (allowRedirect) {
            ItemStack mainHand = player.m_21205_();
            ItemStack offHand = player.m_21206_();
            BlockPos redirLoc = ItemInit.RUNE_MARKING.get().getLocation(mainHand.getItem() instanceof IPositionalItem ? mainHand : offHand);
            if (redirLoc != null) {
                Direction redirFace = ItemInit.RUNE_MARKING.get().getFace(mainHand.getItem() instanceof IPositionalItem ? mainHand : offHand);
                if (world.isLoaded(redirLoc)) {
                    BlockEntity te = world.getBlockEntity(redirLoc);
                    if (te != null) {
                        LazyOptional<IItemHandler> handler = te.getCapability(ForgeCapabilities.ITEM_HANDLER, redirFace);
                        if (handler.isPresent()) {
                            for (int i = 0; i < drops.size(); i++) {
                                if (mergeIntoInventory((IItemHandler) handler.resolve().get(), (ItemStack) drops.get(i))) {
                                    drops.remove(i);
                                    i--;
                                }
                            }
                        }
                    }
                }
            }
        }
        ArrayList<InventoryRitualKit> playerKits = new ArrayList();
        for (int ix = 0; ix < player.getInventory().getContainerSize(); ix++) {
            ItemStack stack = player.getInventory().getItem(ix);
            if (stack.getItem() instanceof ItemPractitionersPouch && ((ItemPractitionersPouch) stack.getItem()).getPatchLevel(stack, PractitionersPouchPatches.COLLECTION) > 0) {
                playerKits.add(new InventoryRitualKit(stack));
            }
        }
        for (ItemStack stack : drops) {
            for (InventoryRitualKit kit : playerKits) {
                stack = kit.addItem(stack);
                if (stack.isEmpty()) {
                    break;
                }
            }
            if (!stack.isEmpty() && !player.addItem(stack)) {
                player.m_19983_(stack);
            }
        }
        for (InventoryRitualKit kitx : playerKits) {
            kitx.writeItemStack();
        }
    }

    public static Pair<Boolean, Boolean> getCaptureAndRedirect(Player player) {
        boolean captureDrops = false;
        boolean redirectDrops = false;
        if (CuriosApi.getCuriosHelper().findFirstCurio(player, ItemInit.COLLECTOR_RING_GREATER.get()).isPresent()) {
            captureDrops = true;
            redirectDrops = true;
        } else if (CuriosApi.getCuriosHelper().findFirstCurio(player, ItemInit.COLLECTOR_RING_LESSER.get()).isPresent()) {
            captureDrops = true;
        }
        return new Pair(captureDrops, redirectDrops);
    }

    public static void DropItemAt(ItemStack stack, Vec3 pos, Level world, boolean randomVelocity) {
        DropItemAt(stack, pos, world, randomVelocity ? new Vec3(((double) world.random.nextFloat() - 0.5) * 0.1, ((double) world.random.nextFloat() - 0.5) * 0.1, ((double) world.random.nextFloat() - 0.5) * 0.1) : Vec3.ZERO);
    }

    public static void DropItemAt(ItemStack stack, Vec3 pos, Level world, Vec3 velocity) {
        ItemEntity item = new ItemEntity(world, pos.x, pos.y, pos.z, stack.copy());
        item.m_20256_(velocity);
        world.m_7967_(item);
    }

    public static boolean hasEmptySlot(IItemHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static void consumeByTag(IItemHandler inventory, ResourceLocation tag, int quantity) {
        List<Item> items = MATags.getItemTagContents(tag);
        consumeAcrossInventories(items, quantity, true, false, false, Arrays.asList(new Pair(inventory, Direction.UP)));
    }
}