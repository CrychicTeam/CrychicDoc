package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class FieryTrait extends SelfEffectTrait {

    public FieryTrait() {
        super(() -> MobEffects.FIRE_RESISTANCE);
    }

    @Override
    public void onHurtTarget(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        assert cache.getLivingHurtEvent() != null;
        if (cache.getLivingHurtEvent().getAmount() > 0.0F && cache.getLivingHurtEvent().getSource().getDirectEntity() instanceof LivingEntity le) {
            le.m_20254_(LHConfig.COMMON.fieryTime.get());
        }
    }

    @Override
    public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
        if (event.getSource().getDirectEntity() instanceof LivingEntity le) {
            le.m_20254_(LHConfig.COMMON.fieryTime.get());
        }
        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            event.setCanceled(true);
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc", Component.literal(LHConfig.COMMON.fieryTime.get() + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        super.addDetail(list);
    }
}