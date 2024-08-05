package dev.xkmc.l2complements.content.enchantment.digging;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class DiggerHelper {

    private static final String KEY = "l2complements:selected_digger";

    @Nullable
    public static Pair<RangeDiggingEnchantment, Integer> getDigger(ItemStack stack) {
        if (!stack.isEmpty() && stack.isEnchanted()) {
            CompoundTag root = stack.getTag();
            if (root == null) {
                return null;
            } else {
                String str = root.getString("l2complements:selected_digger");
                if (!ResourceLocation.isValidResourceLocation(str)) {
                    return null;
                } else {
                    Enchantment e = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(str));
                    if (e instanceof RangeDiggingEnchantment ench) {
                        int lv = stack.getEnchantmentLevel(e);
                        return lv <= 0 ? null : Pair.of(ench, lv);
                    } else {
                        return null;
                    }
                }
            }
        } else {
            return null;
        }
    }

    public static void rotateDigger(ItemStack stack, boolean reverse) {
        Pair<RangeDiggingEnchantment, Integer> current = getDigger(stack);
        List<Enchantment> list = new ArrayList(stack.getAllEnchantments().keySet());
        if (reverse) {
            list = Lists.reverse(list);
        }
        for (Enchantment ent : list) {
            if (ent instanceof RangeDiggingEnchantment ench) {
                if (current == null) {
                    ResourceLocation rl = ForgeRegistries.ENCHANTMENTS.getKey(ench);
                    assert rl == null;
                    stack.getOrCreateTag().putString("l2complements:selected_digger", rl.toString());
                    return;
                }
                if (ench == current.getFirst()) {
                    current = null;
                }
            }
        }
        stack.getOrCreateTag().remove("l2complements:selected_digger");
    }
}