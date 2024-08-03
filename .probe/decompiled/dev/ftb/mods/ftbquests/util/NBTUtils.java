package dev.ftb.mods.ftbquests.util;

import com.google.common.collect.Sets;
import dev.ftb.mods.ftbquests.item.MissingItem;
import java.util.Set;
import java.util.stream.IntStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NBTUtils {

    public static ItemStack read(CompoundTag nbt, String key) {
        Tag nbt1 = nbt.get(key);
        if (nbt1 instanceof CompoundTag) {
            return MissingItem.readItem((CompoundTag) nbt1);
        } else if (nbt1 instanceof StringTag) {
            CompoundTag nbt2 = new CompoundTag();
            nbt2.putString("id", nbt1.getAsString());
            nbt2.putByte("Count", (byte) 1);
            return MissingItem.readItem(nbt2);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static void write(CompoundTag nbt, String key, ItemStack stack) {
        if (!stack.isEmpty()) {
            CompoundTag nbt1 = MissingItem.writeItem(stack);
            if (nbt1.size() == 2 && nbt1.getInt("Count") == 1) {
                nbt.putString(key, nbt1.getString("id"));
            } else {
                nbt.put(key, nbt1);
            }
        }
    }

    public static boolean compareNbt(@Nullable Tag tagA, @Nullable Tag tagB, boolean fuzzy, boolean compareLists) {
        if (tagA == tagB) {
            return true;
        } else if (tagA == null) {
            return true;
        } else if (tagB == null) {
            return false;
        } else if (!tagA.getType().equals(tagB.getType())) {
            return false;
        } else {
            if (tagA instanceof CompoundTag compoundA && tagB instanceof CompoundTag compoundB) {
                Set<String> keysA = compoundA.getAllKeys();
                Set<String> keysB = compoundB.getAllKeys();
                if (fuzzy || keysA.size() == keysB.size() && Sets.intersection(keysA, keysB).size() == keysA.size()) {
                    return keysA.stream().allMatch(key -> compareNbt(compoundA.get(key), compoundB.get(key), fuzzy, compareLists));
                }
                return false;
            }
            if (!compareLists || !(tagA instanceof ListTag listA) || !(tagB instanceof ListTag listB)) {
                return tagA.equals(tagB);
            }
            if (listA.isEmpty()) {
                return listB.isEmpty();
            } else {
                return listA.size() != listB.size() ? false : IntStream.range(0, listA.size()).allMatch(i -> compareNbt(listA.get(i), listB.get(i), fuzzy, true));
            }
        }
    }
}