package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.init.ModEffect;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class IgnisAttackModifier extends GolemModifier {

    public IgnisAttackModifier(StatFilterType type, int maxLevel) {
        super(type, maxLevel);
    }

    @Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> golem, LivingHurtEvent event, int level) {
        LivingEntity target = event.getEntity();
        MobEffect eff = (MobEffect) ModEffect.EFFECTBLAZING_BRAND.get();
        MobEffectInstance old = target.getEffect(eff);
        int i = old == null ? 0 : Math.min(4, old.getAmplifier() + 1);
        MobEffectInstance ins = new MobEffectInstance(eff, 240, i, false, true, true);
        target.addEffect(ins);
        golem.m_5634_((float) level * (float) CMConfig.IgnisHealingMultiplier * (float) (i + 1));
    }

    @Override
    public void modifySource(AbstractGolemEntity<?, ?> golem, CreateSourceEvent event, int value) {
        if (golem.m_21223_() < golem.m_21233_() / 2.0F && event.getResult() != null && event.getResult().toRoot() == L2DamageTypes.MOB_ATTACK) {
            event.enable(DefaultDamageState.BYPASS_ARMOR);
        }
    }
}