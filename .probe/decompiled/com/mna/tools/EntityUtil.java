package com.mna.tools;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import com.mna.effects.beneficial.EffectCamouflage;
import com.mna.entities.sorcery.EntityDecoy;
import com.mna.items.ItemInit;
import com.mna.network.ServerMessageDispatcher;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;

public class EntityUtil {

    public static boolean doElytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks, float manaCost, float maxSpeed) {
        if (!(entity instanceof Player)) {
            return false;
        } else if (maxSpeed <= 0.0F) {
            return true;
        } else {
            if (flightTicks % 100 == 0 && stack.getItem() instanceof IFactionSpecific) {
                ((IFactionSpecific) stack.getItem()).usedByPlayer((Player) entity);
            }
            IPlayerMagic magic = (IPlayerMagic) ((Player) entity).getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic != null && magic.getCastingResource().hasEnoughAbsolute(entity, manaCost)) {
                Vec3 look = entity.m_20154_();
                if (!entity.m_6144_()) {
                    magic.getCastingResource().consume(entity, manaCost);
                    Vec3 motion = entity.m_20184_();
                    double lookScale = 0.06;
                    Vec3 scaled_look = look.scale(lookScale);
                    motion = motion.add(scaled_look);
                    if (motion.length() > (double) maxSpeed) {
                        motion = motion.scale((double) maxSpeed / motion.length());
                    }
                    entity.m_20256_(motion);
                } else {
                    magic.getCastingResource().consume(entity, manaCost * 0.75F);
                    Vec3 motion = entity.m_20184_();
                    float minLength = 0.1F;
                    double lookScale = -0.01;
                    Vec3 scaled_look = look.scale(lookScale);
                    motion = motion.add(scaled_look);
                    if (motion.length() < (double) minLength) {
                        motion = motion.scale((double) minLength / motion.length());
                    }
                    entity.m_20256_(motion);
                }
                if (entity.m_9236_().isClientSide()) {
                    Vec3 pos = entity.m_20182_().add(look.scale(3.0));
                    for (int i = 0; i < 5; i++) {
                        entity.m_9236_().addParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.2F).setColor(10, 10, 10), pos.x - 0.5 + Math.random(), pos.y - 0.5 + Math.random(), pos.z - 0.5 + Math.random(), -look.x, -look.y, -look.z);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public static void SetLiftSpeed(Player p, float speed) {
        p.getPersistentData().putFloat("lift_speed", 0.055F);
        if (!p.m_9236_().isClientSide() && p instanceof ServerPlayer) {
            ServerMessageDispatcher.sendSetLiftSpeedMessage((ServerPlayer) p, speed);
        }
    }

    public static List<LivingEntity> getEntitiesWithinCone(Level world, Vec3 origin, Vec3 forward, float distance, float minAngle, float maxAngle, Predicate<LivingEntity> selectionPredicate) {
        List<Entity> entities = world.getEntities((Entity) null, new AABB(BlockPos.containing(origin)).inflate((double) distance), e -> {
            if (e instanceof LivingEntity) {
                Vec3 direction = e.position().subtract(origin).normalize();
                float angleDeg = (float) (Math.acos(forward.dot(direction)) * 180.0 / Math.PI);
                if (angleDeg >= minAngle && angleDeg <= maxAngle) {
                    return selectionPredicate.test((LivingEntity) e);
                }
            }
            return false;
        });
        return (List<LivingEntity>) entities.stream().map(e -> (LivingEntity) e).collect(Collectors.toList());
    }

    public static float getSoulsRestored(Player soulRecipient, Entity target) {
        if (soulRecipient != null) {
            if (!(target instanceof LivingEntity) || SummonUtils.isSummon(target)) {
                return 0.0F;
            } else if (target instanceof EntityDecoy) {
                return 0.0F;
            } else {
                MutableFloat restoreAmount = new MutableFloat(1.0F);
                if (target instanceof Player) {
                    restoreAmount.setValue((float) GeneralConfigValues.SoulsForPlayerKill);
                } else if (target instanceof Villager) {
                    restoreAmount.setValue((float) GeneralConfigValues.SoulsForVillagerKill);
                } else if (target instanceof IFactionEnemy) {
                    restoreAmount.setValue((float) GeneralConfigValues.SoulsForFactionMobKill);
                } else if (((LivingEntity) target).isInvertedHealAndHarm()) {
                    restoreAmount.setValue((float) GeneralConfigValues.SoulsForUndeadKill);
                } else if (target instanceof Animal) {
                    restoreAmount.setValue((float) GeneralConfigValues.SoulsForAnimalKill);
                } else if (target instanceof AbstractGolem) {
                    restoreAmount.setValue(0.0F);
                } else if (target instanceof Mob) {
                    restoreAmount.setValue((float) GeneralConfigValues.SoulsForMobKill);
                }
                if (((LivingEntity) target).hasEffect(EffectInit.SOUL_VULNERABILITY.get())) {
                    restoreAmount.setValue(restoreAmount.getValue() * 5.0F);
                }
                if (ItemInit.BONE_RING.get().isEquippedAndHasMana(soulRecipient, 3.5F, true)) {
                    restoreAmount.setValue(restoreAmount.getValue() * 2.25F);
                }
                return restoreAmount.getValue();
            }
        } else {
            return 0.0F;
        }
    }

    @Nullable
    public static Vec3 getRetreatPos(Entity e, Vec3 direction, int maxDistance) {
        Vec3 startPos = e.position().add(0.0, 0.5, 0.0);
        Vec3 delta = direction.normalize();
        delta = new Vec3(delta.x, 0.0, delta.z);
        Vec3 endPos = startPos.add(delta.scale((double) maxDistance));
        int count;
        for (count = 0; !e.level().m_46859_(BlockPos.containing(endPos.x, endPos.y, endPos.z)) && count < 10; endPos = endPos.add(0.0, 1.0, 0.0)) {
            count++;
        }
        if (count >= 10) {
            return null;
        } else {
            ClipContext ctx = new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, e);
            BlockHitResult brtr = e.level().m_45547_(ctx);
            return brtr.getType() == HitResult.Type.MISS ? endPos : brtr.m_82450_().add(delta);
        }
    }

    public static void removeInvisibility(LivingEntity living) {
        if (!shouldBeInvisible(living)) {
            living.m_6842_(false);
        }
    }

    public static boolean shouldBeInvisible(LivingEntity living) {
        return living.hasEffect(MobEffects.INVISIBILITY) || living.hasEffect(EffectInit.MIST_FORM.get()) || living.hasEffect(EffectInit.TRUE_INVISIBILITY.get()) || EffectCamouflage.getCamoflagePercent(living) > 0.5F;
    }
}