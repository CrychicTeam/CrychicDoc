package io.github.lightman314.lightmanscurrency.util;

import io.github.lightman314.lightmanscurrency.common.items.TicketItem;
import javax.annotation.Nonnull;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemStackHelper {

    public static ItemStack skullForPlayer(@Nonnull String playerName) {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag tag = new CompoundTag();
        tag.putString("SkullOwner", playerName);
        stack.setTag(tag);
        return stack;
    }

    public static CompoundTag saveAllItems(String key, CompoundTag tag, NonNullList<ItemStack> list) {
        ListTag listTag = new ListTag();
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            if (!stack.isEmpty()) {
                CompoundTag itemCompound = new CompoundTag();
                itemCompound.putByte("Slot", (byte) i);
                stack.save(itemCompound);
                listTag.add(itemCompound);
            }
        }
        tag.put(key, listTag);
        return tag;
    }

    public static void loadAllItems(String key, CompoundTag tag, NonNullList<ItemStack> list) {
        ListTag listTag = tag.getList(key, 10);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag slotCompound = listTag.getCompound(i);
            int index = slotCompound.getByte("Slot") & 255;
            if (index < list.size()) {
                ItemStack stack = ItemStack.of(slotCompound);
                if (stack.getItem() instanceof TicketItem) {
                    TicketItem.GetTicketID(stack);
                }
                list.set(index, stack);
            }
        }
    }

    public static boolean TagEquals(ItemStack stack1, ItemStack stack2) {
        return stack1.hasTag() == stack2.hasTag() && (!stack1.hasTag() && !stack2.hasTag() || stack1.getTag().equals(stack2.getTag()));
    }
}