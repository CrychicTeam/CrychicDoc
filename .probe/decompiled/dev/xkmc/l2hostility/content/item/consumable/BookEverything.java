package dev.xkmc.l2hostility.content.item.consumable;

import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class BookEverything extends Item {

    @Nullable
    private static Enchantment getEnch(ItemStack stack) {
        String name = stack.getHoverName().getString();
        try {
            ResourceLocation rl = new ResourceLocation(name.trim());
            return !ForgeRegistries.ENCHANTMENTS.containsKey(rl) ? null : ForgeRegistries.ENCHANTMENTS.getValue(rl);
        } catch (Exception var3) {
            return null;
        }
    }

    public static boolean allow(Enchantment ench) {
        return !ench.isTreasureOnly() && ench.isAllowedOnBooks() && ench.isDiscoverable() ? ench.getMaxCost(ench.getMaxLevel()) >= ench.getMinCost(ench.getMaxLevel()) : false;
    }

    public static int cost(Enchantment ench) {
        return Math.max(ench.getMaxLevel(), ench.getMaxCost(ench.getMaxLevel()) / 10);
    }

    public BookEverything(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!player.m_6144_()) {
            Enchantment ench = getEnch(stack);
            if (ench == null) {
                return InteractionResultHolder.fail(stack);
            } else if (!allow(ench)) {
                return InteractionResultHolder.fail(stack);
            } else {
                int cost = cost(ench);
                if (player.experienceLevel < cost) {
                    return InteractionResultHolder.fail(stack);
                } else {
                    if (!level.isClientSide()) {
                        player.giveExperienceLevels(-cost);
                        EnchantmentInstance ins = new EnchantmentInstance(ench, ench.getMaxLevel());
                        ItemStack result = EnchantedBookItem.createForEnchantment(ins);
                        player.getInventory().placeItemBackInInventory(result);
                    }
                    return InteractionResultHolder.success(stack);
                }
            }
        } else {
            if (!level.isClientSide()) {
                ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
                ListTag listtag = EnchantedBookItem.getEnchantments(result);
                for (Entry<ResourceKey<Enchantment>, Enchantment> e : ForgeRegistries.ENCHANTMENTS.getEntries()) {
                    if (allow((Enchantment) e.getValue())) {
                        listtag.add(EnchantmentHelper.storeEnchantment(((ResourceKey) e.getKey()).location(), ((Enchantment) e.getValue()).getMaxLevel()));
                    }
                }
                result.getOrCreateTag().put("StoredEnchantments", listtag);
                stack.shrink(1);
                player.getInventory().placeItemBackInInventory(result);
            }
            return InteractionResultHolder.consume(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.ITEM_BOOK_EVERYTHING_USE.get().withStyle(ChatFormatting.GRAY));
        list.add(LangData.ITEM_BOOK_EVERYTHING_SHIFT.get().withStyle(ChatFormatting.GOLD));
        Enchantment e = getEnch(stack);
        if (e == null) {
            list.add(LangData.ITEM_BOOK_EVERYTHING_INVALID.get().withStyle(ChatFormatting.GRAY));
        } else if (allow(e)) {
            int cost = cost(e);
            list.add(LangData.ITEM_BOOK_EVERYTHING_READY.get(e.getFullname(e.getMaxLevel()), cost).withStyle(ChatFormatting.GREEN));
        } else {
            list.add(LangData.ITEM_BOOK_EVERYTHING_FORBIDDEN.get(e.getFullname(e.getMaxLevel())).withStyle(ChatFormatting.RED));
        }
    }
}