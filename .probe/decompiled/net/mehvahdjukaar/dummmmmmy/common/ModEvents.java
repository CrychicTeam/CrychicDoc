package net.mehvahdjukaar.dummmmmmy.common;

import java.util.List;
import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.mehvahdjukaar.dummmmmmy.configs.CommonConfigs;
import net.mehvahdjukaar.dummmmmmy.network.ClientBoundDamageNumberMessage;
import net.mehvahdjukaar.dummmmmmy.network.NetworkHandler;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.EntityTypeTest;

public class ModEvents {

    public static void onEntityCriticalHit(Player attacker, Entity target, float damageModifier) {
        if (attacker != null && !attacker.m_9236_().isClientSide && target instanceof TargetDummyEntity dummy) {
            dummy.moist(attacker, damageModifier);
        }
    }

    public static boolean canBeScaredByScarecrow(Entity entity) {
        String name = Utils.getID(entity.getType()).toString();
        return (entity instanceof Animal || ((List) CommonConfigs.WHITELIST.get()).contains(name)) && !((List) CommonConfigs.BLACKLIST.get()).contains(name);
    }

    public static boolean isScarecrowInRange(Entity entity, Level world) {
        return !world.getEntities((EntityTypeTest) Dummmmmmy.TARGET_DUMMY.get(), entity.getBoundingBox().inflate(10.0), TargetDummyEntity::canScare).isEmpty();
    }

    public static boolean onCheckSpawn(Mob entity, LevelAccessor level) {
        if (level instanceof Level l && canBeScaredByScarecrow(entity)) {
            return isScarecrowInRange(entity, l);
        }
        return false;
    }

    public static void onEntityJoinWorld(Entity entity) {
        if (entity instanceof PathfinderMob mob && canBeScaredByScarecrow(entity)) {
            mob.f_21345_.addGoal(0, new AvoidEntityGoal(mob, TargetDummyEntity.class, (float) ((Integer) CommonConfigs.RADIUS.get()).intValue(), 1.0, 1.3, d -> ((TargetDummyEntity) d).canScare()));
        }
        if ((Boolean) CommonConfigs.DECOY.get() && entity instanceof Monster m) {
            m.f_21345_.addGoal(6, new NearestAttackableTargetGoal(m, TargetDummyEntity.class, 20, true, true, d -> ((TargetDummyEntity) d).canAttract()));
        }
    }

    public static void onEntityDamage(LivingEntity target, float amount, DamageSource source) {
        if (!target.m_9236_().isClientSide && target.m_6095_() != Dummmmmmy.TARGET_DUMMY.get()) {
            ClientBoundDamageNumberMessage message = new ClientBoundDamageNumberMessage(target.m_19879_(), amount, source, null);
            switch((CommonConfigs.Mode) CommonConfigs.DAMAGE_NUMBERS_MODE.get()) {
                case ALL_ENTITIES:
                    NetworkHandler.CHANNEL.sentToAllClientPlayersTrackingEntity(target, message);
                    break;
                case ALL_PLAYERS:
                    if (source.getEntity() instanceof ServerPlayer) {
                        NetworkHandler.CHANNEL.sentToAllClientPlayersTrackingEntity(target, message);
                    }
                    break;
                case LOCAL_PLAYER:
                    if (source.getEntity() instanceof ServerPlayer attackingPlayer) {
                        NetworkHandler.CHANNEL.sendToClientPlayer(attackingPlayer, message);
                    }
                case NONE:
            }
        }
    }

    public static void onEntityHeal(LivingEntity entity, float amount) {
        if (!entity.m_9236_().isClientSide && entity.m_6095_() != Dummmmmmy.TARGET_DUMMY.get()) {
            ClientBoundDamageNumberMessage message = new ClientBoundDamageNumberMessage(entity.m_19879_(), -amount, null, null);
            switch((CommonConfigs.Mode) CommonConfigs.HEALING_NUMBERS_MODE.get()) {
                case ALL_ENTITIES:
                    NetworkHandler.CHANNEL.sentToAllClientPlayersTrackingEntity(entity, message);
                    break;
                case ALL_PLAYERS:
                    if (entity instanceof ServerPlayer) {
                        NetworkHandler.CHANNEL.sentToAllClientPlayersTrackingEntity(entity, message);
                    }
                    break;
                case LOCAL_PLAYER:
                    if (entity instanceof ServerPlayer player) {
                        NetworkHandler.CHANNEL.sendToClientPlayer(player, message);
                    }
                case NONE:
            }
        }
    }
}