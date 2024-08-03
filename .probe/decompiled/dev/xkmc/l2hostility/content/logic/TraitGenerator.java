package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

public class TraitGenerator {

    private final LivingEntity entity;

    private final int mobLevel;

    private final MobDifficultyCollector ins;

    private final HashMap<MobTrait, Integer> traits;

    private final RandomSource rand;

    private final List<MobTrait> traitPool;

    private int level;

    private int weights;

    public static void generateTraits(MobTraitCap cap, LivingEntity le, int lv, HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins) {
        new TraitGenerator(cap, le, lv, traits, ins).generate();
    }

    private TraitGenerator(MobTraitCap cap, LivingEntity entity, int mobLevel, HashMap<MobTrait, Integer> traits, MobDifficultyCollector ins) {
        this.entity = entity;
        this.mobLevel = mobLevel;
        this.ins = ins;
        this.traits = traits;
        this.rand = entity.getRandom();
        this.level = mobLevel;
        EntityConfig.Config config = cap.getConfigCache(entity);
        if (config != null) {
            for (EntityConfig.TraitBase base : config.traits()) {
                if (base.condition() == null || base.condition().match(entity, mobLevel, ins)) {
                    this.genBase(base);
                }
            }
        }
        this.traitPool = new ArrayList(LHTraits.TRAITS.get().getValues().stream().filter(ex -> (config == null || !config.blacklist().contains(ex)) && !traits.containsKey(ex) && ex.allow(entity, mobLevel, ins.getMaxTraitLevel())).toList());
        this.weights = 0;
        for (MobTrait e : this.traitPool) {
            this.weights = this.weights + e.getConfig().weight;
        }
    }

    private int getRank(MobTrait e) {
        return (Integer) this.traits.getOrDefault(e, 0);
    }

    private void setRank(MobTrait e, int rank) {
        if (rank == 0) {
            this.traits.remove(e);
        } else {
            this.traits.put(e, rank);
        }
    }

    private MobTrait pop() {
        int val = this.rand.nextInt(this.weights);
        MobTrait e = (MobTrait) this.traitPool.get(0);
        for (MobTrait x : this.traitPool) {
            val -= x.getConfig().weight;
            if (val <= 0) {
                e = x;
                break;
            }
        }
        this.weights = this.weights - e.getConfig().weight;
        this.traitPool.remove(e);
        return e;
    }

    private void genBase(EntityConfig.TraitBase base) {
        MobTrait e = base.trait();
        if (e != null) {
            int maxTrait = TraitManager.getMaxLevel() + 1;
            if (e.allow(this.entity, this.mobLevel, maxTrait)) {
                int max = e.getMaxLevel();
                int cost = e.getCost(this.ins.trait_cost);
                int old = Math.min(e.getMaxLevel(), Math.max(this.getRank(e), base.free()));
                int expected = Math.min(max, Math.max(old, base.min()));
                int rank = Math.min(expected, old + this.level / cost);
                this.setRank(e, Math.max(old, rank));
                if (rank > old) {
                    this.level -= (rank - old) * cost;
                }
            }
        }
    }

    private void generate() {
        while (this.level > 0 && !this.traitPool.isEmpty()) {
            MobTrait e = this.pop();
            int cost = e.getCost(this.ins.trait_cost);
            if (cost <= this.level) {
                int max = Math.min(this.ins.getMaxTraitLevel(), e.getMaxLevel());
                int old = Math.min(e.getMaxLevel(), this.getRank(e));
                int rank = Math.min(max, old + this.rand.nextInt(this.level / cost) + 1);
                if (rank > old) {
                    this.setRank(e, rank);
                    this.level -= (rank - old) * cost;
                    if (this.ins.isFullChance() || !(this.rand.nextDouble() < LHConfig.COMMON.globalTraitSuppression.get())) {
                        continue;
                    }
                    break;
                }
            }
        }
        for (Entry<MobTrait, Integer> e : this.traits.entrySet()) {
            ((MobTrait) e.getKey()).initialize(this.entity, (Integer) e.getValue());
        }
    }
}