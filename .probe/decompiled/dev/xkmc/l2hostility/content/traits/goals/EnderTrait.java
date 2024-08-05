package dev.xkmc.l2hostility.content.traits.goals;

import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class EnderTrait extends LegendaryTrait {

    public EnderTrait(ChatFormatting format) {
        super(format);
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        if (!mob.m_9236_().isClientSide()) {
            int duration = LHConfig.COMMON.teleportDuration.get();
            int r = LHConfig.COMMON.teleportRange.get();
            if (mob.f_19797_ % duration == 0 && mob instanceof Mob m && m.getTarget() != null) {
                Vec3 old = mob.m_20182_();
                Vec3 target = m.getTarget().m_20182_();
                if (target.distanceTo(old) > (double) r) {
                    target = target.subtract(old).normalize().scale((double) r).add(old);
                }
                EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(m, target.x, target.y, target.z);
                if (event.isCanceled()) {
                    return;
                }
                mob.m_6021_(target.x(), target.y(), target.z());
                if (!mob.m_9236_().m_45786_(mob)) {
                    mob.m_6021_(old.x(), old.y(), old.z());
                    return;
                }
                mob.m_9236_().m_214171_(GameEvent.TELEPORT, m.m_20182_(), GameEvent.Context.of(mob));
                if (!mob.m_20067_()) {
                    mob.m_9236_().playSound(null, mob.f_19854_, mob.f_19855_, mob.f_19856_, SoundEvents.ENDERMAN_TELEPORT, mob.m_5720_(), 1.0F, 1.0F);
                    mob.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) && !event.getSource().is(L2DamageTypes.MAGIC) && (teleport(entity) || event.getSource().is(DamageTypeTags.IS_PROJECTILE))) {
            event.setCanceled(true);
        }
    }

    private static boolean teleport(LivingEntity entity) {
        int r = LHConfig.COMMON.teleportRange.get();
        if (!entity.m_9236_().isClientSide() && entity.isAlive() && r > 0) {
            double d0 = entity.m_20185_() + (entity.getRandom().nextDouble() - 0.5) * (double) r * 2.0;
            double d1 = entity.m_20186_() + (double) (entity.getRandom().nextInt(r * 2) - r);
            double d2 = entity.m_20189_() + (entity.getRandom().nextDouble() - 0.5) * (double) r * 2.0;
            return teleport(entity, d0, d1, d2);
        } else {
            return false;
        }
    }

    private static boolean teleport(LivingEntity entity, double pX, double pY, double pZ) {
        BlockPos.MutableBlockPos ipos = new BlockPos.MutableBlockPos(pX, pY, pZ);
        while (ipos.m_123342_() > entity.m_9236_().m_141937_() && !entity.m_9236_().getBlockState(ipos).m_280555_()) {
            ipos.move(Direction.DOWN);
        }
        BlockState blockstate = entity.m_9236_().getBlockState(ipos);
        boolean flag = blockstate.m_280555_();
        boolean flag1 = blockstate.m_60819_().is(FluidTags.WATER);
        if (flag && !flag1) {
            EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(entity, pX, pY, pZ);
            if (event.isCanceled()) {
                return false;
            } else {
                Vec3 vec3 = entity.m_20182_();
                boolean flag2 = entity.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
                if (flag2) {
                    entity.m_9236_().m_214171_(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));
                    if (!entity.m_20067_()) {
                        entity.m_9236_().playSound(null, entity.f_19854_, entity.f_19855_, entity.f_19856_, SoundEvents.ENDERMAN_TELEPORT, entity.m_5720_(), 1.0F, 1.0F);
                        entity.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                }
                return flag2;
            }
        } else {
            return false;
        }
    }
}