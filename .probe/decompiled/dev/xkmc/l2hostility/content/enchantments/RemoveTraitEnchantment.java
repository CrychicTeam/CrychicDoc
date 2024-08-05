package dev.xkmc.l2hostility.content.enchantments;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import java.util.function.Supplier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class RemoveTraitEnchantment extends HostilityEnchantment implements HitTargetEnchantment {

    private final Supplier<MobTrait> sup;

    public RemoveTraitEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots, Supplier<MobTrait> sup) {
        super(pRarity, pCategory, pApplicableSlots, 1);
        this.sup = sup;
    }

    @Override
    public void hitMob(LivingEntity target, MobTraitCap cap, Integer value, AttackCache cache) {
        cap.removeTrait((MobTrait) this.sup.get());
        cap.syncToClient(target);
    }
}