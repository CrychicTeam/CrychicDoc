package dev.xkmc.l2complements.content.feature;

import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public enum EntityFeature {

    STABLE_BODY(List.of(new SlotEnchantmentFeaturePredicate(EquipmentSlot.CHEST, LCEnchantments.STABLE_BODY::get))),
    PROJECTILE_REJECT(List.of(new EnchantmentFeaturePredicate(LCEnchantments.ENCH_PROJECTILE::get))),
    EXPLOSION_REJECT(List.of(new EnchantmentFeaturePredicate(LCEnchantments.ENCH_EXPLOSION::get))),
    FIRE_REJECT(List.of(new EnchantmentFeaturePredicate(LCEnchantments.ENCH_FIRE::get))),
    ENVIRONMENTAL_REJECT(List.of(new EnchantmentFeaturePredicate(LCEnchantments.ENCH_ENVIRONMENT::get))),
    MAGIC_REJECT(List.of(new EnchantmentFeaturePredicate(LCEnchantments.ENCH_MAGIC::get))),
    OWNER_PROTECTION(List.of(new EnchantmentFeaturePredicate(LCEnchantments.ENCH_MATES::get))),
    INVINCIBLE(List.of(new EnchantmentFeaturePredicate(LCEnchantments.ENCH_INVINCIBLE::get))),
    SNOW_WALKER(List.of(new EnchantmentFeaturePredicate(LCEnchantments.SNOW_WALKER::get))),
    LAVA_WALKER(List.of());

    private final ArrayList<FeaturePredicate> list;

    private EntityFeature(List<FeaturePredicate> list) {
        this.list = new ArrayList(list);
    }

    public void add(FeaturePredicate pred) {
        this.list.add(pred);
    }

    public boolean test(LivingEntity e) {
        for (FeaturePredicate x : this.list) {
            if (x.test(e)) {
                return true;
            }
        }
        return false;
    }
}