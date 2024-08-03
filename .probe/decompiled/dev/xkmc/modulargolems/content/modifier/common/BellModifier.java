package dev.xkmc.modulargolems.content.modifier.common;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.AABB;

public class BellModifier extends GolemModifier {

    public BellModifier() {
        super(StatFilterType.HEALTH, 1);
    }

    @Override
    public void onSetTarget(AbstractGolemEntity<?, ?> golem, Mob mob, int level) {
        AABB aabb = golem.m_20191_().inflate(48.0);
        List<Mob> list = golem.m_9236_().m_6443_(Mob.class, aabb, golem::m_6779_);
        boolean sound = false;
        for (Mob e : list) {
            if (e instanceof Enemy && !(e instanceof Creeper) && e.m_6779_(golem)) {
                sound |= !e.m_21023_(MobEffects.GLOWING);
                EffectUtil.addEffect(e, new MobEffectInstance(MobEffects.GLOWING, 200), EffectUtil.AddReason.NONE, golem);
                if (!(e.getTarget() instanceof AbstractGolemEntity)) {
                    e.setTarget(golem);
                }
            }
        }
        if (sound) {
            golem.m_5496_(SoundEvents.BELL_BLOCK, 1.0F, 1.0F);
        }
    }
}