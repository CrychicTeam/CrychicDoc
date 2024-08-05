package com.mna.api.entities;

import com.mna.api.faction.IFaction;
import com.mojang.datafixers.util.Pair;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public class FactionRaidRegistry {

    private static final HashMap<IFaction, HashMap<EntityType<? extends IFactionEnemy<? extends Mob>>, HashMap<Integer, Integer>>> soldierMap = new HashMap();

    public static int getStrengthRating(IFaction faction, EntityType<? extends IFactionEnemy<? extends Mob>> entityType, int tier) {
        HashMap<?, HashMap<Integer, Integer>> soldiers = getFactionSoliderMap(faction);
        return soldiers.containsKey(entityType) ? keyFromValue((HashMap<Integer, Integer>) soldiers.get(entityType), tier) : -1;
    }

    private static HashMap<EntityType<? extends IFactionEnemy<? extends Mob>>, HashMap<Integer, Integer>> getFactionSoliderMap(IFaction faction) {
        return (HashMap<EntityType<? extends IFactionEnemy<? extends Mob>>, HashMap<Integer, Integer>>) soldierMap.computeIfAbsent(faction, k -> new HashMap());
    }

    private static int keyFromValue(HashMap<Integer, Integer> values, int target) {
        return (Integer) values.entrySet().stream().filter(e -> (Integer) e.getValue() == target).map(e -> (Integer) e.getKey()).findFirst().orElse(-1);
    }

    public static void registerSoldier(IFaction faction, EntityType<? extends IFactionEnemy<? extends Mob>> entityType, HashMap<Integer, Integer> tierStrengthRatings) {
        for (Integer strength : tierStrengthRatings.keySet()) {
            if ((Integer) tierStrengthRatings.get(strength) < 0) {
                throw new InvalidParameterException("Tier minimum is 0!");
            }
            if (strength <= 0) {
                throw new InvalidParameterException("Strength rating must be greater than zero or you risk infinite loops!");
            }
        }
        try {
            getFactionSoliderMap(faction).put(entityType, tierStrengthRatings);
        } catch (Throwable var5) {
            throw new InvalidParameterException("Cannot register faction enemies for faction " + faction.toString());
        }
    }

    @Nullable
    public static Pair<EntityType<? extends IFactionEnemy<? extends Mob>>, Integer> getSoldier(IFaction faction, int raidStrength) {
        return pickSoldier(getFactionSoliderMap(faction), raidStrength);
    }

    @Nullable
    private static Pair<EntityType<? extends IFactionEnemy<? extends Mob>>, Integer> pickSoldier(HashMap<EntityType<? extends IFactionEnemy<? extends Mob>>, HashMap<Integer, Integer>> list, int raidStrength) {
        if (list.size() == 0) {
            return null;
        } else {
            List<Entry<EntityType<? extends IFactionEnemy<? extends Mob>>, HashMap<Integer, Integer>>> possibles = (List<Entry<EntityType<? extends IFactionEnemy<? extends Mob>>, HashMap<Integer, Integer>>>) list.entrySet().stream().filter(e -> ((HashMap) e.getValue()).keySet().stream().anyMatch(i -> i <= raidStrength)).collect(Collectors.toList());
            if (possibles.size() == 0) {
                return null;
            } else {
                Entry<EntityType<? extends IFactionEnemy<? extends Mob>>, HashMap<Integer, Integer>> entry = (Entry<EntityType<? extends IFactionEnemy<? extends Mob>>, HashMap<Integer, Integer>>) possibles.get(new Random().nextInt(possibles.size()));
                int maxStr = (Integer) Collections.max((Collection) ((HashMap) entry.getValue()).keySet().stream().filter(i -> i <= raidStrength).collect(Collectors.toList()));
                int tier = (Integer) ((HashMap) entry.getValue()).get(maxStr);
                return new Pair((EntityType) entry.getKey(), tier);
            }
        }
    }
}