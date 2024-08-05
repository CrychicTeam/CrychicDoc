package dev.xkmc.modulargolems.compat.materials.l2complements;

import dev.xkmc.l2complements.content.enchantment.armors.FlameThornEnchantment;
import dev.xkmc.l2complements.content.enchantment.weapon.SoulFlameBladeEnchantment;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class SoulFlameModifier extends GolemModifier {

    public SoulFlameModifier() {
        super(StatFilterType.MASS, 3);
    }

    @Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        ((SoulFlameBladeEnchantment) LCEnchantments.FLAME_BLADE.get()).m_7677_(entity, event.getEntity(), level);
    }

    @Override
    public void onHurt(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        if (event.getSource().getDirectEntity() instanceof LivingEntity attacker) {
            ((FlameThornEnchantment) LCEnchantments.FLAME_THORN.get()).m_7675_(entity, attacker, level);
        }
    }

    @Override
    public void onAttacked(AbstractGolemEntity<?, ?> entity, LivingAttackEvent event, int level) {
        if (event.getSource().getMsgId().equals("soul_flame")) {
            event.setCanceled(true);
        }
    }
}