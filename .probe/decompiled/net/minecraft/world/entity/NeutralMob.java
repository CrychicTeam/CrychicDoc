package net.minecraft.world.entity;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public interface NeutralMob {

    String TAG_ANGER_TIME = "AngerTime";

    String TAG_ANGRY_AT = "AngryAt";

    int getRemainingPersistentAngerTime();

    void setRemainingPersistentAngerTime(int var1);

    @Nullable
    UUID getPersistentAngerTarget();

    void setPersistentAngerTarget(@Nullable UUID var1);

    void startPersistentAngerTimer();

    default void addPersistentAngerSaveData(CompoundTag compoundTag0) {
        compoundTag0.putInt("AngerTime", this.getRemainingPersistentAngerTime());
        if (this.getPersistentAngerTarget() != null) {
            compoundTag0.putUUID("AngryAt", this.getPersistentAngerTarget());
        }
    }

    default void readPersistentAngerSaveData(Level level0, CompoundTag compoundTag1) {
        this.setRemainingPersistentAngerTime(compoundTag1.getInt("AngerTime"));
        if (level0 instanceof ServerLevel) {
            if (!compoundTag1.hasUUID("AngryAt")) {
                this.setPersistentAngerTarget(null);
            } else {
                UUID $$2 = compoundTag1.getUUID("AngryAt");
                this.setPersistentAngerTarget($$2);
                Entity $$3 = ((ServerLevel) level0).getEntity($$2);
                if ($$3 != null) {
                    if ($$3 instanceof Mob) {
                        this.setLastHurtByMob((Mob) $$3);
                    }
                    if ($$3.getType() == EntityType.PLAYER) {
                        this.setLastHurtByPlayer((Player) $$3);
                    }
                }
            }
        }
    }

    default void updatePersistentAnger(ServerLevel serverLevel0, boolean boolean1) {
        LivingEntity $$2 = this.getTarget();
        UUID $$3 = this.getPersistentAngerTarget();
        if (($$2 == null || $$2.isDeadOrDying()) && $$3 != null && serverLevel0.getEntity($$3) instanceof Mob) {
            this.stopBeingAngry();
        } else {
            if ($$2 != null && !Objects.equals($$3, $$2.m_20148_())) {
                this.setPersistentAngerTarget($$2.m_20148_());
                this.startPersistentAngerTimer();
            }
            if (this.getRemainingPersistentAngerTime() > 0 && ($$2 == null || $$2.m_6095_() != EntityType.PLAYER || !boolean1)) {
                this.setRemainingPersistentAngerTime(this.getRemainingPersistentAngerTime() - 1);
                if (this.getRemainingPersistentAngerTime() == 0) {
                    this.stopBeingAngry();
                }
            }
        }
    }

    default boolean isAngryAt(LivingEntity livingEntity0) {
        if (!this.canAttack(livingEntity0)) {
            return false;
        } else {
            return livingEntity0.m_6095_() == EntityType.PLAYER && this.isAngryAtAllPlayers(livingEntity0.m_9236_()) ? true : livingEntity0.m_20148_().equals(this.getPersistentAngerTarget());
        }
    }

    default boolean isAngryAtAllPlayers(Level level0) {
        return level0.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER) && this.isAngry() && this.getPersistentAngerTarget() == null;
    }

    default boolean isAngry() {
        return this.getRemainingPersistentAngerTime() > 0;
    }

    default void playerDied(Player player0) {
        if (player0.m_9236_().getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
            if (player0.m_20148_().equals(this.getPersistentAngerTarget())) {
                this.stopBeingAngry();
            }
        }
    }

    default void forgetCurrentTargetAndRefreshUniversalAnger() {
        this.stopBeingAngry();
        this.startPersistentAngerTimer();
    }

    default void stopBeingAngry() {
        this.setLastHurtByMob(null);
        this.setPersistentAngerTarget(null);
        this.setTarget(null);
        this.setRemainingPersistentAngerTime(0);
    }

    @Nullable
    LivingEntity getLastHurtByMob();

    void setLastHurtByMob(@Nullable LivingEntity var1);

    void setLastHurtByPlayer(@Nullable Player var1);

    void setTarget(@Nullable LivingEntity var1);

    boolean canAttack(LivingEntity var1);

    @Nullable
    LivingEntity getTarget();
}