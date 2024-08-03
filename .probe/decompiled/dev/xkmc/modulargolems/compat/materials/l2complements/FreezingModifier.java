package dev.xkmc.modulargolems.compat.materials.l2complements;

import dev.xkmc.l2complements.content.enchantment.armors.IceThornEnchantment;
import dev.xkmc.l2complements.content.enchantment.weapon.IceBladeEnchantment;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.function.Consumer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class FreezingModifier extends GolemModifier {

    public FreezingModifier() {
        super(StatFilterType.MASS, 3);
    }

    @Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        ((IceBladeEnchantment) LCEnchantments.ICE_BLADE.get()).m_7677_(entity, event.getEntity(), level);
    }

    @Override
    public void onHurt(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        if (event.getSource().getDirectEntity() instanceof LivingEntity attacker) {
            ((IceThornEnchantment) LCEnchantments.ICE_THORN.get()).m_7675_(entity, attacker, level);
        }
    }

    @Override
    public void onAttacked(AbstractGolemEntity<?, ?> entity, LivingAttackEvent event, int level) {
        if (event.getSource().is(DamageTypeTags.IS_FREEZING)) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onRegisterFlag(Consumer<GolemFlags> addFlag) {
        addFlag.accept(GolemFlags.FREEZE_IMMUNE);
    }
}