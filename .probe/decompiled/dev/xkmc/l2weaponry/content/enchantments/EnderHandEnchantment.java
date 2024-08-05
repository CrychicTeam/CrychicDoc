package dev.xkmc.l2weaponry.content.enchantments;

import dev.xkmc.l2complements.content.enchantment.core.SingleLevelEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.SourceModifierEnchantment;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import dev.xkmc.l2weaponry.init.data.LWNegateStates;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnderHandEnchantment extends SingleLevelEnchantment implements SourceModifierEnchantment {

    public EnderHandEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void modify(CreateSourceEvent event, ItemStack stack, int value) {
        if (event.getOriginal().equals(DamageTypes.TRIDENT)) {
            event.enable(LWNegateStates.NO_PROJECTILE);
            event.setDirect(event.getAttacker());
            PlayerAttackCache cache = new PlayerAttackCache();
            cache.setupAttackerProfile(event.getAttacker(), stack);
            event.setPlayerAttackCache(cache);
        }
    }
}