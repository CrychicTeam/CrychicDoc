package com.mna.entities.ai;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;

public class ThreatTable {

    private static final int THREAT_STEAL_THRESHOLD = 10;

    private Map<Integer, Float> threat = Maps.newHashMap();

    private Predicate<Entity> threatAddPredicate = null;

    public ThreatTable() {
        this(null);
    }

    public ThreatTable(Predicate<Entity> threatAdd) {
        this.threatAddPredicate = threatAdd;
    }

    public void addThreat(LivingEntity attacker, float amount, LivingEntity currentTarget) {
        if (attacker != null && (this.threatAddPredicate == null || this.threatAddPredicate.test(attacker))) {
            float newThreat = (Float) this.threat.getOrDefault(attacker.m_19879_(), 0.0F) + amount;
            this.threat.put(attacker.m_19879_(), newThreat);
        }
    }

    public void initializeThreat(LivingEntity attacker) {
        if (attacker != null) {
            if (!this.threat.containsKey(attacker.m_19879_())) {
                this.threat.put(attacker.m_19879_(), 1.0F);
            }
        }
    }

    public boolean shouldSwitchTarget(LivingEntity currentTarget, LivingEntity potentialTarget) {
        if (currentTarget == null || !currentTarget.isAlive()) {
            return true;
        } else if (potentialTarget != null && potentialTarget.isAlive()) {
            if (currentTarget.m_19879_() == potentialTarget.m_19879_()) {
                return false;
            } else {
                float currentThreat = (Float) this.threat.getOrDefault(currentTarget.m_19879_(), 0.0F);
                float targetThreat = (Float) this.threat.getOrDefault(potentialTarget.m_19879_(), 0.0F);
                return targetThreat >= currentThreat + 10.0F;
            }
        } else {
            return false;
        }
    }

    public int getRandomThreatEntry(Level world, LivingEntity owner, double maxTargetDist, @Nullable Predicate<LivingEntity> selectionPredicate) {
        ArrayList<Integer> allKeys = new ArrayList();
        ArrayList<Integer> toRemove = new ArrayList();
        double maxDistSquared = maxTargetDist * maxTargetDist;
        this.threat.keySet().forEach(entityID -> {
            Entity e = world.getEntity(entityID);
            if (e != null && e.isAlive() && e instanceof LivingEntity && (selectionPredicate == null || selectionPredicate.test((LivingEntity) e))) {
                if (e.position().distanceToSqr(owner.m_20182_()) < maxDistSquared) {
                    allKeys.add(entityID);
                }
            } else {
                toRemove.add(entityID);
            }
        });
        toRemove.forEach(i -> this.threat.remove(i));
        return allKeys.size() == 0 ? -1 : (Integer) allKeys.get((int) (Math.random() * (double) allKeys.size()));
    }

    @Nullable
    private Pair<Integer, Float> getHighestThreatTarget() {
        if (this.threat.size() == 0) {
            return null;
        } else {
            MutableInt highestID = new MutableInt(-1);
            MutableFloat highestThreat = new MutableFloat(-1.0F);
            this.threat.forEach((k, v) -> {
                if (highestID.getValue() == -1 || highestThreat.getValue() < v) {
                    highestID.setValue(k);
                    highestThreat.setValue(v);
                }
            });
            return new Pair(highestID.getValue(), highestThreat.getValue());
        }
    }

    public void forEach(BiConsumer<Integer, Float> callback) {
        this.threat.forEach(callback);
    }

    public boolean isOn(LivingEntity e) {
        return this.threat.containsKey(e.m_19879_());
    }

    public int size() {
        return this.threat.size();
    }

    public ArrayList<Player> players(Level world) {
        ArrayList<Player> players = new ArrayList();
        this.threat.forEach((entityID, threat) -> {
            Entity e = world.getEntity(entityID);
            if (e != null && e.isAlive() && e instanceof Player) {
                players.add((Player) e);
            }
        });
        return players;
    }

    public void remove(LivingEntity target) {
        if (target != null) {
            this.threat.remove(target.m_19879_());
        }
    }
}