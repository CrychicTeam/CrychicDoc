package dev.xkmc.modulargolems.content.modifier.immunes;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.function.Consumer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class FireImmuneModifier extends GolemModifier {

    public FireImmuneModifier() {
        super(StatFilterType.HEALTH, 1);
    }

    @Override
    public void onRegisterFlag(Consumer<GolemFlags> addFlag) {
        addFlag.accept(GolemFlags.FIRE_IMMUNE);
    }

    @Override
    public void onAttacked(AbstractGolemEntity<?, ?> entity, LivingAttackEvent event, int level) {
        if (level > 0 && event.getSource().is(DamageTypeTags.IS_FIRE)) {
            event.setCanceled(true);
        }
    }
}