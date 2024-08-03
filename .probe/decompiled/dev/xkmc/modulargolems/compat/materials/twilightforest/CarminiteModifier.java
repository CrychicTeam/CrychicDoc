package dev.xkmc.modulargolems.compat.materials.twilightforest;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class CarminiteModifier extends GolemModifier {

    public CarminiteModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public void onDamaged(AbstractGolemEntity<?, ?> entity, LivingDamageEvent event, int level) {
        int time = MGConfig.COMMON.carminiteTime.get() * level;
        entity.m_147207_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, 4), entity);
        entity.m_147207_(new MobEffectInstance(MobEffects.INVISIBILITY, time), entity);
    }

    @Override
    public List<MutableComponent> getDetail(int level) {
        double time = (double) (MGConfig.COMMON.carminiteTime.get() * level) / 20.0;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", time).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public void onAttacked(AbstractGolemEntity<?, ?> entity, LivingAttackEvent event, int level) {
        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
            MobEffectInstance eff = entity.m_21124_(MobEffects.DAMAGE_RESISTANCE);
            if (eff != null && eff.getAmplifier() >= 4) {
                event.setCanceled(true);
            }
        }
    }
}