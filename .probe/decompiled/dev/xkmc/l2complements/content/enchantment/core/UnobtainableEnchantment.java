package dev.xkmc.l2complements.content.enchantment.core;

import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2library.base.L2Registrate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.ForgeRegistries;

public class UnobtainableEnchantment extends Enchantment implements CraftableEnchantment {

    private static final List<UnobtainableEnchantment> CACHE = new ArrayList();

    public static ItemStack makeBook(Enchantment ench, int level) {
        return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ench, level));
    }

    public static void injectTab(L2Registrate reg, EnchantmentCategory... cats) {
        reg.modifyCreativeModeTab(LCItems.TAB_ENCHMIN.getKey(), m -> {
            Set<EnchantmentCategory> set = Set.of(cats);
            getSorted().stream().filter(e -> e.allowedInCreativeTab(Items.ENCHANTED_BOOK, set)).forEach(e -> e.getCraftableLevels().forEach(i -> m.m_246267_(makeBook(e, i), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY)));
        });
        reg.modifyCreativeModeTab(LCItems.TAB_ENCHMAX.getKey(), m -> {
            Set<EnchantmentCategory> set = Set.of(cats);
            getSorted().stream().filter(e -> e.allowedInCreativeTab(Items.ENCHANTED_BOOK, set)).forEach(e -> m.m_246267_(makeBook(e, e.m_6586_()), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY));
        });
    }

    private static List<UnobtainableEnchantment> getSorted() {
        Map<String, List<UnobtainableEnchantment>> map = new TreeMap();
        Set<String> ids = new LinkedHashSet();
        for (UnobtainableEnchantment e : CACHE) {
            ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(e);
            assert id != null;
            ((List) map.computeIfAbsent(id.getNamespace(), k -> new ArrayList())).add(e);
        }
        ids.add("l2complements");
        ids.addAll(map.keySet());
        List<UnobtainableEnchantment> ans = new ArrayList();
        for (String s : ids) {
            ans.addAll((Collection) map.get(s));
        }
        return ans;
    }

    protected UnobtainableEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
        synchronized (CACHE) {
            CACHE.add(this);
        }
    }

    @Override
    public int getMinCost(int lv) {
        return 5;
    }

    @Override
    public int getMaxCost(int lv) {
        return 1;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    public ChatFormatting getColor() {
        return ChatFormatting.GREEN;
    }

    @Override
    public Component getFullname(int lv) {
        MutableComponent component = Component.translatable(this.m_44704_());
        if (lv != 1 || this.m_6586_() != 1) {
            component.append(" ").append(Component.translatable("enchantment.level." + lv));
        }
        component.withStyle(this.getColor());
        return component;
    }

    public boolean allowedInCreativeTab(Item book, Set<EnchantmentCategory> tab) {
        return tab.contains(LCEnchantments.ALL);
    }

    public int getDecoColor(String s) {
        if (s.equals("A")) {
            return -32769;
        } else if (s.equals("W")) {
            return -8433809;
        } else if (this.f_44672_ == LCEnchantments.ALL) {
            return -24753;
        } else {
            return switch(this.f_44672_) {
                case ARMOR ->
                    -11550721;
                case WEAPON ->
                    -45233;
                case BREAKABLE ->
                    -8388737;
                default ->
                    -129;
            };
        }
    }
}