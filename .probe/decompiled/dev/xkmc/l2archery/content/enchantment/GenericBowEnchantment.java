package dev.xkmc.l2archery.content.enchantment;

import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class GenericBowEnchantment extends BaseBowEnchantment {

    private final BowArrowFeature[] features;

    public GenericBowEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots, int max, Function<Integer, BowArrowFeature> gen) {
        super(pRarity, pCategory, pApplicableSlots, max);
        this.features = new BowArrowFeature[max];
        for (int i = 0; i < max; i++) {
            this.features[i] = (BowArrowFeature) gen.apply(i + 1);
        }
    }

    @Override
    public BowArrowFeature getFeature(int v) {
        return this.features[Mth.clamp(v - 1, 0, this.max - 1)];
    }
}