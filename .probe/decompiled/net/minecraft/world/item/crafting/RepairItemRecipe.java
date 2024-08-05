package net.minecraft.world.item.crafting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class RepairItemRecipe extends CustomRecipe {

    public RepairItemRecipe(ResourceLocation resourceLocation0, CraftingBookCategory craftingBookCategory1) {
        super(resourceLocation0, craftingBookCategory1);
    }

    public boolean matches(CraftingContainer craftingContainer0, Level level1) {
        List<ItemStack> $$2 = Lists.newArrayList();
        for (int $$3 = 0; $$3 < craftingContainer0.m_6643_(); $$3++) {
            ItemStack $$4 = craftingContainer0.m_8020_($$3);
            if (!$$4.isEmpty()) {
                $$2.add($$4);
                if ($$2.size() > 1) {
                    ItemStack $$5 = (ItemStack) $$2.get(0);
                    if (!$$4.is($$5.getItem()) || $$5.getCount() != 1 || $$4.getCount() != 1 || !$$5.getItem().canBeDepleted()) {
                        return false;
                    }
                }
            }
        }
        return $$2.size() == 2;
    }

    public ItemStack assemble(CraftingContainer craftingContainer0, RegistryAccess registryAccess1) {
        List<ItemStack> $$2 = Lists.newArrayList();
        for (int $$3 = 0; $$3 < craftingContainer0.m_6643_(); $$3++) {
            ItemStack $$4 = craftingContainer0.m_8020_($$3);
            if (!$$4.isEmpty()) {
                $$2.add($$4);
                if ($$2.size() > 1) {
                    ItemStack $$5 = (ItemStack) $$2.get(0);
                    if (!$$4.is($$5.getItem()) || $$5.getCount() != 1 || $$4.getCount() != 1 || !$$5.getItem().canBeDepleted()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        if ($$2.size() == 2) {
            ItemStack $$6 = (ItemStack) $$2.get(0);
            ItemStack $$7 = (ItemStack) $$2.get(1);
            if ($$6.is($$7.getItem()) && $$6.getCount() == 1 && $$7.getCount() == 1 && $$6.getItem().canBeDepleted()) {
                Item $$8 = $$6.getItem();
                int $$9 = $$8.getMaxDamage() - $$6.getDamageValue();
                int $$10 = $$8.getMaxDamage() - $$7.getDamageValue();
                int $$11 = $$9 + $$10 + $$8.getMaxDamage() * 5 / 100;
                int $$12 = $$8.getMaxDamage() - $$11;
                if ($$12 < 0) {
                    $$12 = 0;
                }
                ItemStack $$13 = new ItemStack($$6.getItem());
                $$13.setDamageValue($$12);
                Map<Enchantment, Integer> $$14 = Maps.newHashMap();
                Map<Enchantment, Integer> $$15 = EnchantmentHelper.getEnchantments($$6);
                Map<Enchantment, Integer> $$16 = EnchantmentHelper.getEnchantments($$7);
                BuiltInRegistries.ENCHANTMENT.stream().filter(Enchantment::m_6589_).forEach(p_44144_ -> {
                    int $$4 = Math.max((Integer) $$15.getOrDefault(p_44144_, 0), (Integer) $$16.getOrDefault(p_44144_, 0));
                    if ($$4 > 0) {
                        $$14.put(p_44144_, $$4);
                    }
                });
                if (!$$14.isEmpty()) {
                    EnchantmentHelper.setEnchantments($$14, $$13);
                }
                return $$13;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return int0 * int1 >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.REPAIR_ITEM;
    }
}