package org.violetmoon.quark.content.tweaks.module;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "tweaks")
public class GoldToolsHaveFortuneModule extends ZetaModule {

    private static final Tier[] TIERS = new Tier[] { Tiers.WOOD, Tiers.STONE, Tiers.IRON, Tiers.DIAMOND, Tiers.NETHERITE };

    @Config
    @Config.Min(0.0)
    public static int fortuneLevel = 2;

    @Config
    @Config.Min(0.0)
    @Config.Max(4.0)
    public static int harvestLevel = 2;

    @Config
    public static boolean displayBakedEnchantmentsInTooltip = true;

    @Config
    public static boolean italicTooltip = true;

    @Config(description = "Enchantments other than Gold's Fortune/Looting to bake into items. Format is \"item+enchant@level\", such as \"minecraft:stick+sharpness@10\".")
    public static List<String> bakedEnchantments = Lists.newArrayList();

    private static final Map<Item, Object2IntMap<Enchantment>> wellBakedEnchantments = new HashMap();

    @Hint(key = "gold_tool_fortune", content = { "fortuneLevel" })
    List<Item> gold_tools = Arrays.asList(Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_SWORD);

    @Hint(key = "gold_tool_harvest_level", content = { "harvestLevel" })
    List<Item> gold_tools_2 = this.gold_tools;

    private static boolean staticEnabled;

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        staticEnabled = this.enabled;
        wellBakedEnchantments.clear();
        for (String enchantment : bakedEnchantments) {
            String[] split1 = enchantment.split("\\+");
            if (split1.length == 2) {
                ResourceLocation itemLoc = ResourceLocation.tryParse(split1[0]);
                if (itemLoc != null) {
                    Item item = BuiltInRegistries.ITEM.get(itemLoc);
                    if (item != Items.AIR) {
                        String[] split2 = split1[1].split("@");
                        if (split2.length != 0 && split2.length <= 2) {
                            ResourceLocation enchantLoc = ResourceLocation.tryParse(split2[0]);
                            if (enchantLoc != null) {
                                Enchantment enchant = BuiltInRegistries.ENCHANTMENT.get(enchantLoc);
                                if (enchant != null) {
                                    try {
                                        int strength = split2.length == 1 ? 1 : Integer.parseInt(split2[1]);
                                        Object2IntMap<Enchantment> pastry = (Object2IntMap<Enchantment>) wellBakedEnchantments.computeIfAbsent(item, it -> new Object2IntArrayMap());
                                        pastry.put(enchant, Math.max(strength, pastry.getOrDefault(enchant, 0)));
                                    } catch (NumberFormatException var12) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (fortuneLevel > 0) {
            for (Item item : BuiltInRegistries.ITEM) {
                if (item instanceof TieredItem) {
                    TieredItem tiered = (TieredItem) item;
                    if (tiered.getTier() == Tiers.GOLD) {
                        Enchantment enchant = item instanceof SwordItem ? Enchantments.MOB_LOOTING : Enchantments.BLOCK_FORTUNE;
                        Object2IntMap<Enchantment> pastry = (Object2IntMap<Enchantment>) wellBakedEnchantments.computeIfAbsent(item, it -> new Object2IntArrayMap());
                        pastry.put(enchant, Math.max(fortuneLevel, pastry.getOrDefault(enchant, 0)));
                    }
                }
            }
        }
    }

    public static int getActualEnchantmentLevel(Enchantment enchant, ItemStack stack, int prev) {
        Item item = stack.getItem();
        if (staticEnabled && wellBakedEnchantments.containsKey(item)) {
            Object2IntMap<Enchantment> pastry = (Object2IntMap<Enchantment>) wellBakedEnchantments.get(item);
            if (pastry.containsKey(enchant)) {
                int forcedLevel = pastry.getOrDefault(enchant, 0);
                if (forcedLevel > prev) {
                    return forcedLevel;
                }
            }
        }
        return prev;
    }

    public static void addEnchantmentsIfMissing(ItemStack stack, Map<Enchantment, Integer> map) {
        Item item = stack.getItem();
        if (staticEnabled && wellBakedEnchantments.containsKey(item)) {
            Object2IntMap<Enchantment> pastry = (Object2IntMap<Enchantment>) wellBakedEnchantments.get(item);
            ObjectIterator var4 = pastry.keySet().iterator();
            while (var4.hasNext()) {
                Enchantment enchantment = (Enchantment) var4.next();
                int level = (Integer) map.getOrDefault(enchantment, 0);
                int effectiveLevel = getActualEnchantmentLevel(enchantment, stack, level);
                if (level < effectiveLevel) {
                    map.put(enchantment, effectiveLevel);
                }
            }
        }
    }

    public static Tier getEffectiveTier(Tier realTier) {
        return staticEnabled && realTier == Tiers.GOLD ? TIERS[harvestLevel] : realTier;
    }

    public static boolean shouldShowEnchantments(ItemStack stack) {
        return wellBakedEnchantments.containsKey(stack.getItem());
    }

    public static void fakeEnchantmentTooltip(ItemStack stack, List<Component> components) {
        if (staticEnabled && displayBakedEnchantmentsInTooltip) {
            for (Entry<Enchantment, Integer> entry : Quark.ZETA.itemExtensions.get(stack).getAllEnchantmentsZeta(stack).entrySet()) {
                int actualLevel = EnchantmentHelper.getTagEnchantmentLevel((Enchantment) entry.getKey(), stack);
                if (actualLevel != (Integer) entry.getValue()) {
                    Component comp = ((Enchantment) entry.getKey()).getFullname((Integer) entry.getValue());
                    if (italicTooltip) {
                        comp = comp.copy().withStyle(ChatFormatting.ITALIC);
                    }
                    if (actualLevel != 0) {
                        comp = Component.translatable("quark.misc.enchantment_with_actual_level", comp, Component.translatable("enchantment.level." + actualLevel)).withStyle(ChatFormatting.GRAY);
                    }
                    components.add(comp);
                }
            }
        }
    }

    public static ListTag hideSmallerEnchantments(ItemStack stack, ListTag tag) {
        if (staticEnabled && displayBakedEnchantmentsInTooltip) {
            List<ResourceLocation> toRemove = Lists.newArrayList();
            for (Entry<Enchantment, Integer> entry : Quark.ZETA.itemExtensions.get(stack).getAllEnchantmentsZeta(stack).entrySet()) {
                int actualLevel = EnchantmentHelper.getTagEnchantmentLevel((Enchantment) entry.getKey(), stack);
                if (actualLevel != (Integer) entry.getValue() && actualLevel != 0) {
                    toRemove.add(EnchantmentHelper.getEnchantmentId((Enchantment) entry.getKey()));
                }
            }
            if (!toRemove.isEmpty()) {
                tag = tag.copy();
                tag.removeIf(it -> {
                    if (it instanceof CompoundTag compound) {
                        ResourceLocation loc = EnchantmentHelper.getEnchantmentId(compound);
                        if (loc != null) {
                            return toRemove.contains(loc);
                        }
                    }
                    return false;
                });
            }
        }
        return tag;
    }
}