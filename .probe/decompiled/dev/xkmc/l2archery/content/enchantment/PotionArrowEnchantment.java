package dev.xkmc.l2archery.content.enchantment;

import dev.xkmc.l2archery.content.config.BowArrowStatConfig;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PotionArrowEnchantment extends BaseBowEnchantment {

    private final int max;

    public PotionArrowEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots, int max) {
        super(pRarity, pCategory, pApplicableSlots, max);
        this.max = max;
    }

    @Override
    public BowArrowFeature getFeature(int v) {
        return BowArrowStatConfig.get().getEnchEffects(this, v);
    }
}