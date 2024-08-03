package com.mna.api.entities;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.faction.IFaction;
import java.util.HashMap;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

public interface IFactionEnemy<T extends LivingEntity> {

    void setRaidTarget(Player var1);

    @Nullable
    Player getRaidTarget();

    IFaction getFaction();

    default void setDamageResists(String typeID, int level) {
        this.getDamageResists().put(typeID, level);
    }

    HashMap<String, Integer> getDamageResists();

    int getTier();

    void setTier(int var1);

    default int getTierMax() {
        return 3;
    }

    default void raidTargetDespawn() {
        if (this.getRaidTarget() != null) {
            if (!this.getRaidTarget().m_6084_()) {
                this.onDespawnDueToTargetDeath();
                ((LivingEntity) this).remove(Entity.RemovalReason.DISCARDED);
            } else if (this.getRaidTarget().m_20280_((Entity) this) > 4096.0) {
                this.onDespawnDueToDistance();
                ((LivingEntity) this).remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    default boolean factionTargetPlayerPredicate(LivingEntity potentialTarget) {
        if (!(potentialTarget instanceof Player)) {
            return false;
        } else {
            IPlayerProgression progression = (IPlayerProgression) potentialTarget.getCapability(ManaAndArtificeMod.getProgressionCapability()).orElse(null);
            return progression == null ? false : potentialTarget.isAlive() && progression.getAlliedFaction() != null && progression.getAlliedFaction().getEnemyFactions().contains(this.getFaction());
        }
    }

    default boolean factionTargetHelpPredicate(LivingEntity potentialTarget) {
        if (!(potentialTarget instanceof Mob mob)) {
            return false;
        } else if (mob instanceof IFactionEnemy && ((IFactionEnemy) mob).getFaction() == this.getFaction()) {
            return false;
        } else if (mob.m_6084_() && mob.getTarget() instanceof Player) {
            MutableBoolean isFaction = new MutableBoolean(false);
            ((Player) mob.getTarget()).getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent(p -> {
                IFaction myFaction = this.getFaction();
                IFaction playerFaction = p.getAlliedFaction();
                if (myFaction != null && playerFaction != null && (myFaction == playerFaction || myFaction.getAlliedFactions().contains(playerFaction))) {
                    isFaction.setTrue();
                }
            });
            return isFaction.booleanValue();
        } else {
            return false;
        }
    }

    default void writeFactionData(CompoundTag nbt) {
        CompoundTag factionData = new CompoundTag();
        factionData.putInt("tier", this.getTier());
        nbt.put("faction_data", factionData);
    }

    default void readFactionData(CompoundTag nbt) {
        if (nbt.contains("faction_data")) {
            CompoundTag factionData = nbt.getCompound("faction_data");
            if (factionData.contains("tier")) {
                this.setTier(factionData.getInt("tier"));
            }
        }
    }

    default void onKilled(DamageSource source) {
        if (source.getEntity() instanceof Player) {
            ((Player) source.getEntity()).getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent(p -> p.getFactionDifficultyStats(this.getFaction()).onFactionMobKilled(source));
        }
    }

    default void onDespawnDueToDistance() {
        if (this.getRaidTarget() != null) {
            this.getRaidTarget().getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent(p -> p.getFactionDifficultyStats(this.getFaction()).onFactionMobDespawnDueToDistance());
        }
    }

    default void onDespawnDueToTargetDeath() {
        if (this.getRaidTarget() != null) {
            this.getRaidTarget().getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent(p -> p.getFactionDifficultyStats(this.getFaction()).onFactionKilledPlayer());
        }
    }

    default float applyDamageResists(DamageSource source, float amount) {
        return amount - amount * (float) ((Integer) this.getDamageResists().getOrDefault(source.getMsgId(), 0)).intValue() * 0.2F;
    }

    default void applyInitialSpawnTier(LevelAccessor world) {
        MutableInt enemyTiers = new MutableInt(0);
        world.m_6907_().forEach(player -> player.getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent(p -> {
            if (player.m_20280_((LivingEntity) this) < 4096.0) {
                enemyTiers.add(p.getTier());
            }
        }));
        float tierTotal = (float) enemyTiers.getValue().intValue();
        double[] weights = new double[] { this.getTierWeight(tierTotal, 0.0F, 8.0F), this.getTierWeight(tierTotal, 3.0F, 4.0F), tierTotal <= 10.0F ? this.getTierWeight(tierTotal, 6.0F, 8.0F) : 1.0 };
        double totalWeight = 0.0;
        for (int i = 0; i < weights.length; i++) {
            totalWeight += weights[i];
        }
        int tier = 0;
        for (double r = Math.random() * totalWeight; tier < weights.length - 1; tier++) {
            r -= weights[tier];
            if (r <= 0.0) {
                break;
            }
        }
        this.setTier(tier);
    }

    default double getTierWeight(float x, float shift, float steepness) {
        return Math.pow(Math.E, -1.0 * (Math.pow((double) (x - shift), 2.0) / (double) steepness));
    }
}