package dev.xkmc.l2archery.content.config;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2archery.content.enchantment.PotionArrowEnchantment;
import dev.xkmc.l2archery.content.feature.core.PotionArrowFeature;
import dev.xkmc.l2archery.content.item.GenericArrowItem;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.content.stats.BowArrowStatType;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import dev.xkmc.l2archery.init.L2Archery;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.HashMap;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

@SerialClass
public class BowArrowStatConfig extends BaseConfig {

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<Item, HashMap<BowArrowStatType, Double>> bow_stats = new HashMap();

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<Item, HashMap<BowArrowStatType, Double>> arrow_stats = new HashMap();

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<Item, HashMap<MobEffect, BowArrowStatConfig.ConfigEffect>> bow_effects = new HashMap();

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<Item, HashMap<MobEffect, BowArrowStatConfig.ConfigEffect>> arrow_effects = new HashMap();

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<Enchantment, HashMap<MobEffect, BowArrowStatConfig.EnchantmentConfigEffect>> enchantment_effects = new HashMap();

    @ConfigCollect(CollectType.MAP_COLLECT)
    @SerialField
    public HashMap<Upgrade, HashMap<MobEffect, BowArrowStatConfig.ConfigEffect>> upgrade_effects = new HashMap();

    private final HashMap<BowArrowStatConfig.ConfigIdentifier, PotionArrowFeature> potion_cache = new HashMap();

    public static BowArrowStatConfig get() {
        return L2Archery.STATS.getMerged();
    }

    private <T> PotionArrowFeature getEffects(T upgrade, HashMap<T, HashMap<MobEffect, BowArrowStatConfig.ConfigEffect>> config) {
        BowArrowStatConfig.ConfigIdentifier id = new BowArrowStatConfig.ConfigIdentifier(upgrade, 0);
        if (this.potion_cache.containsKey(id)) {
            return (PotionArrowFeature) this.potion_cache.get(id);
        } else {
            HashMap<MobEffect, BowArrowStatConfig.ConfigEffect> map = (HashMap<MobEffect, BowArrowStatConfig.ConfigEffect>) config.get(upgrade);
            PotionArrowFeature ans = map != null ? new PotionArrowFeature(map.entrySet().stream().map(e -> new MobEffectInstance((MobEffect) e.getKey(), ((BowArrowStatConfig.ConfigEffect) e.getValue()).duration(), ((BowArrowStatConfig.ConfigEffect) e.getValue()).amplifier())).toList()) : PotionArrowFeature.NULL;
            this.potion_cache.put(id, ans);
            return ans;
        }
    }

    public PotionArrowFeature getUpgradeEffects(Upgrade upgrade) {
        return this.getEffects(upgrade, this.upgrade_effects);
    }

    public PotionArrowFeature getBowEffects(GenericBowItem bow) {
        return this.getEffects(bow, this.bow_effects);
    }

    public PotionArrowFeature getArrowEffects(GenericArrowItem arrow) {
        return this.getEffects(arrow, this.arrow_effects);
    }

    public PotionArrowFeature getEnchEffects(PotionArrowEnchantment enchantment, int lv) {
        BowArrowStatConfig.ConfigIdentifier id = new BowArrowStatConfig.ConfigIdentifier(enchantment, lv);
        if (this.potion_cache.containsKey(id)) {
            return (PotionArrowFeature) this.potion_cache.get(id);
        } else {
            HashMap<MobEffect, BowArrowStatConfig.EnchantmentConfigEffect> map = (HashMap<MobEffect, BowArrowStatConfig.EnchantmentConfigEffect>) this.enchantment_effects.get(enchantment);
            PotionArrowFeature ans = map != null ? new PotionArrowFeature(map.entrySet().stream().map(e -> new MobEffectInstance((MobEffect) e.getKey(), ((BowArrowStatConfig.EnchantmentConfigEffect) e.getValue()).duration() + ((BowArrowStatConfig.EnchantmentConfigEffect) e.getValue()).duration_bonus() * (lv - 1), ((BowArrowStatConfig.EnchantmentConfigEffect) e.getValue()).amplifier() + ((BowArrowStatConfig.EnchantmentConfigEffect) e.getValue()).amplifier_bonus() * (lv - 1))).toList()) : PotionArrowFeature.NULL;
            this.potion_cache.put(id, ans);
            return ans;
        }
    }

    public BowBuilder putBow(RegistryEntry<GenericBowItem> bow) {
        return new BowBuilder(this, bow);
    }

    public ArrowBuilder putArrow(RegistryEntry<GenericArrowItem> arrow) {
        return new ArrowBuilder(this, arrow);
    }

    public EnchBuilder putEnchantment(RegistryEntry<PotionArrowEnchantment> arrow) {
        return new EnchBuilder(this, arrow);
    }

    public UpgradeBuilder putUpgrade(RegistryEntry<Upgrade> arrow) {
        return new UpgradeBuilder(this, arrow);
    }

    public static record ConfigEffect(int duration, int amplifier) {
    }

    public static record ConfigIdentifier(Object item, int lv) {
    }

    public static record EnchantmentConfigEffect(int duration, int amplifier, int duration_bonus, int amplifier_bonus) {
    }
}