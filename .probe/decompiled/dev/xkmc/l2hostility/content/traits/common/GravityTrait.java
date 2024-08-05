package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class GravityTrait extends AuraEffectTrait {

    public GravityTrait(Supplier<MobEffect> eff) {
        super(eff);
    }

    @Override
    public void onDamaged(int level, LivingEntity mob, AttackCache cache) {
        LivingEntity e = cache.getAttacker();
        if (e != null && !e.m_20096_()) {
            if (CurioCompat.hasItemInCurio(e, (Item) LHItems.ABRAHADABRA.get())) {
                return;
            }
            e.m_5997_(0.0, (double) (-level), 0.0);
            if (e instanceof ServerPlayer) {
                e.f_19864_ = true;
            }
        }
    }
}