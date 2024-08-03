package io.redspace.ironsspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.effect.SummonTimer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public interface MagicSummon extends AntiMagicSusceptible {

    LivingEntity getSummoner();

    void onUnSummon();

    @Override
    default void onAntiMagic(MagicData playerMagicData) {
        this.onUnSummon();
    }

    default boolean shouldIgnoreDamage(DamageSource damageSource) {
        return !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && damageSource.getEntity() != null && !ServerConfigs.CAN_ATTACK_OWN_SUMMONS.get() ? this.getSummoner() != null && damageSource.getEntity() != null && (damageSource.getEntity().equals(this.getSummoner()) || this.getSummoner().m_7307_(damageSource.getEntity())) : false;
    }

    default boolean isAlliedHelper(Entity entity) {
        if (this.getSummoner() == null) {
            return false;
        } else {
            boolean isFellowSummon;
            boolean var10000;
            label28: {
                isFellowSummon = entity == this.getSummoner() || entity.isAlliedTo(this.getSummoner());
                if (entity instanceof OwnableEntity ownableEntity && ownableEntity.getOwner() == this.getSummoner()) {
                    var10000 = true;
                    break label28;
                }
                var10000 = false;
            }
            boolean hasCommonOwner = var10000;
            return isFellowSummon || hasCommonOwner;
        }
    }

    default void onDeathHelper() {
        if (this instanceof LivingEntity entity) {
            Level level = entity.m_9236_();
            Component deathMessage = entity.getCombatTracker().getDeathMessage();
            if (!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getSummoner() instanceof ServerPlayer player) {
                player.sendSystemMessage(deathMessage);
            }
        }
    }

    default void onRemovedHelper(Entity entity, SummonTimer timer) {
        Entity.RemovalReason reason = entity.getRemovalReason();
        if (reason != null && this.getSummoner() instanceof ServerPlayer player && reason.shouldDestroy()) {
            MobEffectInstance effect = player.m_21124_(timer);
            if (effect != null) {
                MobEffectInstance decrement = new MobEffectInstance(timer, effect.getDuration(), effect.getAmplifier() - 1, false, false, true);
                if (decrement.getAmplifier() >= 0) {
                    player.m_21221_().put(timer, decrement);
                    player.connection.send(new ClientboundUpdateMobEffectPacket(player.m_19879_(), decrement));
                } else {
                    player.m_21195_(timer);
                }
            }
            if (reason.equals(Entity.RemovalReason.DISCARDED)) {
                player.sendSystemMessage(Component.translatable("ui.irons_spellbooks.summon_despawn_message", ((Entity) this).getDisplayName()));
            }
        }
    }
}