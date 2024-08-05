package dev.xkmc.modulargolems.content.modifier.immunes;

import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class MagicImmuneModifier extends GolemModifier {

    public MagicImmuneModifier() {
        super(StatFilterType.HEALTH, 1);
    }

    @Override
    public void onAttacked(AbstractGolemEntity<?, ?> entity, LivingAttackEvent event, int level) {
        if (level > 0 && event.getSource().is(L2DamageTypes.MAGIC)) {
            event.setCanceled(true);
        }
    }
}