package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHDamageTypes;
import dev.xkmc.l2hostility.init.network.TraitEffectToClient;
import dev.xkmc.l2hostility.init.network.TraitEffects;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class KillerAuraTrait extends LegendaryTrait {

    public KillerAuraTrait(ChatFormatting format) {
        super(format);
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        int itv = LHConfig.COMMON.killerAuraInterval.get() / level;
        int damage = LHConfig.COMMON.killerAuraDamage.get() * level;
        int range = LHConfig.COMMON.killerAuraRange.get();
        if (!mob.m_9236_().isClientSide() && mob.f_19797_ % itv == 0) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
            AABB box = mob.m_20191_().inflate((double) range);
            for (LivingEntity e : mob.m_9236_().m_45976_(LivingEntity.class, box)) {
                if ((e instanceof Player pl && !pl.getAbilities().instabuild || e instanceof Mob target && target.getTarget() == mob || mob instanceof Mob mobmob && mobmob.getTarget() == e) && !(e.m_20270_(mob) > (float) range) && !CurioCompat.hasItemInCurio(e, (Item) LHItems.ABRAHADABRA.get())) {
                    TraitEffectCache cache = new TraitEffectCache(e);
                    cap.traitEvent((k, v) -> k.postHurtPlayer(v, mob, cache));
                    e.hurt(new DamageSource(LHDamageTypes.forKey(mob.m_9236_(), LHDamageTypes.KILLER_AURA), null, mob), (float) damage);
                }
            }
            L2Hostility.HANDLER.toTrackingPlayers(new TraitEffectToClient(mob, this, TraitEffects.AURA), mob);
        }
        if (mob.m_9236_().isClientSide()) {
            Vec3 center = mob.m_20182_();
            float tpi = (float) (Math.PI * 2);
            Vec3 v0 = new Vec3(0.0, (double) range, 0.0);
            v0 = v0.xRot(tpi / 4.0F).yRot(mob.getRandom().nextFloat() * tpi);
            mob.m_9236_().addAlwaysVisibleParticle(ParticleTypes.FLAME, center.x + v0.x, center.y + v0.y + 0.5, center.z + v0.z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc", this.mapLevel(i -> Component.literal(LHConfig.COMMON.killerAuraDamage.get() * i + "").withStyle(ChatFormatting.AQUA)), Component.literal(LHConfig.COMMON.killerAuraRange.get() + "").withStyle(ChatFormatting.AQUA), this.mapLevel(i -> Component.literal((double) Math.round((double) LHConfig.COMMON.killerAuraInterval.get().intValue() * 5.0 / (double) i.intValue()) * 0.01 + "").withStyle(ChatFormatting.AQUA))).withStyle(ChatFormatting.GRAY));
    }
}