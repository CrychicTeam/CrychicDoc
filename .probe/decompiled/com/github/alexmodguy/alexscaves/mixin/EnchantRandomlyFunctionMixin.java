package com.github.alexmodguy.alexscaves.mixin;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.enchantment.ACWeaponEnchantment;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ EnchantRandomlyFunction.class })
public class EnchantRandomlyFunctionMixin {

    @Inject(method = { "Lnet/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction;enchantItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/ItemStack;" }, remap = true, at = { @At("HEAD") }, cancellable = true)
    private static void ac_enchantItem(ItemStack stack, Enchantment enchantment, RandomSource randomSource, CallbackInfoReturnable<ItemStack> cir) {
        if (enchantment instanceof ACWeaponEnchantment && !AlexsCaves.COMMON_CONFIG.enchantmentsInLoot.get()) {
            Enchantment enchantment1 = enchantment;
            boolean flag = stack.is(Items.BOOK);
            List<Enchantment> list = (List<Enchantment>) BuiltInRegistries.ENCHANTMENT.stream().filter(Enchantment::m_6592_).filter(enchantment2 -> flag || enchantment2.canEnchant(stack)).collect(Collectors.toList());
            for (int tries = 0; enchantment1 instanceof ACWeaponEnchantment && tries < 100; tries++) {
                enchantment1 = Util.getRandom(list, randomSource);
            }
            cir.setReturnValue(enchantItemNormally(stack, enchantment1, randomSource));
        }
    }

    private static ItemStack enchantItemNormally(ItemStack itemStack, Enchantment enchantment, RandomSource randomSource) {
        int i = Mth.nextInt(randomSource, enchantment.getMinLevel(), enchantment.getMaxLevel());
        if (itemStack.is(Items.BOOK)) {
            itemStack = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(itemStack, new EnchantmentInstance(enchantment, i));
        } else {
            itemStack.enchant(enchantment, i);
        }
        return itemStack;
    }
}