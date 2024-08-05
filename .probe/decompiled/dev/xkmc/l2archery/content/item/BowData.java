package dev.xkmc.l2archery.content.item;

import dev.xkmc.l2archery.content.enchantment.IBowEnchantment;
import dev.xkmc.l2archery.content.feature.FeatureList;
import dev.xkmc.l2archery.content.feature.core.CompoundBowConfig;
import dev.xkmc.l2archery.content.feature.core.StatFeature;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public record BowData(Item item, ArrayList<Upgrade> upgrade, HashMap<Enchantment, Integer> ench) {

    public static BowData of(GenericBowItem item) {
        return new BowData(item, new ArrayList(0), new HashMap(0));
    }

    public static BowData of(GenericBowItem item, List<Upgrade> upgrade, Map<Enchantment, Integer> ench) {
        return new BowData(item, new ArrayList(upgrade), new HashMap(ench));
    }

    public static BowData of(GenericBowItem item, ItemStack stack) {
        List<Upgrade> upgrade = GenericBowItem.getUpgrades(stack);
        return of(item, upgrade, stack.getAllEnchantments());
    }

    public FeatureList getFeatures() {
        FeatureList ans = this.getItem().getFeatures(null);
        ans.stage = FeatureList.Stage.UPGRADE;
        this.upgrade.forEach(e -> ans.add(e.getFeature()));
        ans.stage = FeatureList.Stage.ENCHANT;
        this.ench.forEach((k, v) -> {
            if (k instanceof IBowEnchantment b) {
                ans.add(b.getFeature(v));
            }
        });
        return ans;
    }

    public GenericBowItem getItem() {
        return (GenericBowItem) this.item;
    }

    public IBowConfig getConfig() {
        IBowConfig ans = this.getItem().config;
        for (Upgrade up : this.upgrade) {
            if (up.getFeature() instanceof StatFeature f) {
                ans = new CompoundBowConfig(ans, f);
            }
        }
        for (Entry<Enchantment, Integer> ent : this.ench.entrySet()) {
            Object var10 = ent.getKey();
            if (var10 instanceof IBowEnchantment) {
                IBowEnchantment bowEnch = (IBowEnchantment) var10;
                if (bowEnch.getFeature((Integer) ent.getValue()) instanceof StatFeature sf) {
                    ans = new CompoundBowConfig(ans, sf);
                }
            }
        }
        return ans;
    }
}