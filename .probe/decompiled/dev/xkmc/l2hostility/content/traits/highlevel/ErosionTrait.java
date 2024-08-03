package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.item.traits.DurabilityEater;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class ErosionTrait extends SlotIterateDamageTrait {

    public ErosionTrait(ChatFormatting format) {
        super(format);
    }

    @Override
    public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        if (!CurioCompat.hasItemInCurio(cache.getAttackTarget(), (Item) LHItems.ABRAHADABRA.get())) {
            int count = this.process(level, attacker, cache.getAttackTarget());
            if (count < level) {
                cache.addHurtModifier(DamageModifier.multTotal((float) (LHConfig.COMMON.erosionDamage.get() * (double) level * (double) (level - count))));
            }
        }
    }

    @Override
    protected void perform(LivingEntity target, EquipmentSlot slot) {
        DurabilityEater.erosion(target, slot);
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc", this.mapLevel(i -> Component.literal(i + "").withStyle(ChatFormatting.AQUA)), this.mapLevel(i -> Component.literal(Math.round(LHConfig.COMMON.erosionDurability.get() * (double) i.intValue() * 100.0) + "%").withStyle(ChatFormatting.AQUA)), this.mapLevel(i -> Component.literal(Math.round(LHConfig.COMMON.erosionDamage.get() * (double) i.intValue() * 100.0) + "%").withStyle(ChatFormatting.AQUA))).withStyle(ChatFormatting.GRAY));
    }
}