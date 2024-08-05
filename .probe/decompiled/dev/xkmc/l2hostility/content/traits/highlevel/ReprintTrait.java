package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.item.traits.ReprintHandler;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ReprintTrait extends MobTrait {

    public ReprintTrait(ChatFormatting format) {
        super(format);
    }

    @Override
    public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        long total = 0L;
        int maxLv = 0;
        LivingHurtEvent event = cache.getLivingHurtEvent();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack dst = attacker.getItemBySlot(slot);
            ItemStack src = cache.getAttackTarget().getItemBySlot(slot);
            Map<Enchantment, Integer> targetEnch = src.getAllEnchantments();
            for (Entry<Enchantment, Integer> e : targetEnch.entrySet()) {
                int lv = (Integer) e.getValue();
                maxLv = Math.max(maxLv, lv);
                if (lv >= 30) {
                    total = -1L;
                } else if (total >= 0L) {
                    total += 1L << lv - 1;
                }
            }
            if (event != null && event.getSource().getDirectEntity() == attacker) {
                ReprintHandler.reprint(dst, src);
            }
        }
        int bypass = LHConfig.COMMON.reprintBypass.get();
        if (maxLv >= bypass) {
            ItemStack weapon = attacker.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!weapon.isEmpty() && (weapon.isEnchanted() || weapon.isEnchantable()) && weapon.canApplyAtEnchantingTable((Enchantment) LCEnchantments.VOID_TOUCH.get())) {
                Map<Enchantment, Integer> map = weapon.getAllEnchantments();
                map.compute((Enchantment) LCEnchantments.VOID_TOUCH.get(), (k, v) -> v == null ? 20 : Math.max(v, 20));
                map.compute(Enchantments.VANISHING_CURSE, (k, v) -> v == null ? 1 : Math.max(v, 1));
                EnchantmentHelper.setEnchantments(map, weapon);
            }
        }
        float factor = total >= 0L ? (float) total : (float) Math.pow(2.0, (double) (maxLv - 1));
        cache.addHurtModifier(DamageModifier.multTotal(1.0F + (float) (LHConfig.COMMON.reprintDamage.get() * (double) factor)));
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc", this.mapLevel(i -> Component.literal(Math.round(LHConfig.COMMON.reprintDamage.get() * (double) i.intValue() * 100.0) + "%").withStyle(ChatFormatting.AQUA))).withStyle(ChatFormatting.GRAY));
    }
}