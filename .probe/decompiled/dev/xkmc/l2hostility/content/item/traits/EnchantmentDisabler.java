package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentDisabler {

    private static final String ENCH = "Enchantments";

    private static final String ROOT = "l2hostility_enchantment";

    private static final String OLD = "originalEnchantments";

    private static final String TIME = "startTime";

    public static void disableEnchantment(Level level, ItemStack stack, int duration) {
        CompoundTag root = stack.getOrCreateTag();
        if (root.contains("Enchantments", 9)) {
            double durability = stack.getMaxDamage() == 0 ? 0.0 : 1.0 * (double) stack.getDamageValue() / (double) stack.getMaxDamage();
            CompoundTag tag = stack.getOrCreateTagElement("l2hostility_enchantment");
            ListTag list = root.getList("Enchantments", 10);
            ListTag cache = new ListTag();
            list.removeIf(e -> {
                if (noDispell(e)) {
                    return false;
                } else {
                    cache.add(e);
                    return true;
                }
            });
            tag.put("originalEnchantments", cache);
            tag.putLong("startTime", level.getGameTime() + (long) duration);
            if (stack.isDamageableItem()) {
                stack.setDamageValue(Mth.clamp((int) Math.floor(durability * (double) stack.getMaxDamage()), 0, stack.getMaxDamage() - 1));
            }
        }
    }

    private static boolean noDispell(Tag e) {
        if (e instanceof CompoundTag c) {
            ResourceLocation id = new ResourceLocation(c.getString("id"));
            return ForgeRegistries.ENCHANTMENTS.tags().getTag(LHTagGen.NO_DISPELL).contains(ForgeRegistries.ENCHANTMENTS.getValue(id));
        } else {
            return false;
        }
    }

    public static void tickStack(Level level, Entity user, ItemStack stack) {
        if (!level.isClientSide()) {
            if (user instanceof Player player && !player.getAbilities().instabuild && stack.isEnchanted() && stack.getEnchantmentLevel((Enchantment) LHEnchantments.VANISH.get()) > 0) {
                stack.setCount(0);
                return;
            }
            if (stack.getTag() != null && stack.getTag().contains("l2hostility_enchantment", 10)) {
                CompoundTag root = stack.getOrCreateTag();
                CompoundTag tag = stack.getOrCreateTagElement("l2hostility_enchantment");
                long time = tag.getLong("startTime");
                if (level.getGameTime() >= time) {
                    stack.getTag().remove("l2hostility_enchantment");
                    ListTag list = root.getList("Enchantments", 10);
                    ListTag cache = tag.getList("originalEnchantments", 10);
                    cache.addAll(list);
                    stack.getTag().put("Enchantments", cache);
                }
            }
        }
    }

    public static void modifyTooltip(ItemStack stack, List<Component> tooltip, Level level) {
        if (stack.getTag() != null && stack.getTag().contains("l2hostility_enchantment", 10)) {
            CompoundTag tag = stack.getOrCreateTagElement("l2hostility_enchantment");
            long time = Math.max(0L, tag.getLong("startTime") - level.getGameTime());
            ListTag list = tag.getList("originalEnchantments", 10);
            tooltip.add(LangData.TOOLTIP_DISABLE.get(Component.literal(list.size() + "").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal(time / 20L + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.RED));
            List<Component> disabled = new ArrayList();
            ItemStack.appendEnchantmentNames(disabled, list);
            for (Component e : disabled) {
                tooltip.add(e.copy().withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }
}