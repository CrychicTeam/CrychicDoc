package dev.xkmc.l2hostility.content.logic;

import dev.xkmc.l2hostility.content.config.WorldDifficultyConfig;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public class MobDifficultyCollector {

    public int min;

    public int base;

    public int count;

    public int difficulty;

    public int cap = Integer.MAX_VALUE;

    public int traitCap = TraitManager.getMaxLevel() + 1;

    public double scale;

    public double varSq;

    public double apply_chance;

    public double trait_chance;

    public double trait_cost;

    public double finalFactor = 1.0;

    private ServerPlayer player;

    private boolean fullChance;

    private boolean fullDrop;

    private boolean delegateTrait;

    public static MobDifficultyCollector noTrait(int lv) {
        MobDifficultyCollector ans = new MobDifficultyCollector();
        ans.trait_chance = 0.0;
        ans.base = lv;
        return ans;
    }

    public MobDifficultyCollector() {
        this.apply_chance = LHConfig.COMMON.globalApplyChance.get();
        this.trait_chance = LHConfig.COMMON.globalTraitChance.get();
        this.trait_cost = 1.0;
    }

    public void acceptConfig(WorldDifficultyConfig.DifficultyConfig config) {
        this.min = Math.max(this.min, config.min());
        this.base = this.base + config.base();
        this.scale = this.scale + config.scale();
        this.varSq = this.varSq + config.variation() * config.variation();
        this.count++;
        this.apply_chance = this.apply_chance * config.apply_chance();
        this.trait_chance = this.trait_chance * config.trait_chance();
        this.fullChance = this.fullChance | this.min > 0;
    }

    public void acceptBonus(DifficultyLevel difficulty) {
        this.difficulty = this.difficulty + difficulty.getLevel();
    }

    public void acceptBonusLevel(int difficulty) {
        this.base += difficulty;
    }

    public void acceptBonusFactor(double finalFactor) {
        this.finalFactor *= finalFactor;
    }

    public void traitCostFactor(double factor) {
        this.trait_cost *= factor;
    }

    public void setCap(int cap) {
        if (LHConfig.COMMON.allowBypassMinimum.get()) {
            this.min = Math.min(this.min, cap);
        } else {
            cap = Math.max(this.min, cap);
        }
        this.cap = Math.min(this.cap, cap);
    }

    public int getDifficulty(RandomSource random) {
        double mean = (double) this.base + (double) this.difficulty * this.scale;
        if (this.count > 0) {
            mean += random.nextGaussian() * Math.sqrt(this.varSq);
        }
        mean *= this.finalFactor;
        return (int) Math.round(Mth.clamp(mean, (double) this.min, (double) this.cap));
    }

    public void setTraitCap(int cap) {
        this.traitCap = Math.min(cap, this.traitCap);
    }

    public int getMaxTraitLevel() {
        return this.traitCap;
    }

    public double apply_chance() {
        return this.fullChance ? 1.0 : this.apply_chance;
    }

    public double trait_chance(int lv) {
        if (this.delegateTrait) {
            return 0.0;
        } else {
            return this.fullChance ? 1.0 : this.trait_chance * Math.min(1.0, (double) lv * LHConfig.COMMON.initialTraitChanceSlope.get());
        }
    }

    public int getBase() {
        return (int) Math.round((double) this.base + (double) this.difficulty * this.scale);
    }

    public void setFullChance() {
        this.fullChance = true;
    }

    public void delegateTrait() {
        this.delegateTrait = true;
    }

    public boolean isFullChance() {
        return this.fullChance;
    }

    public void setFullDrop() {
        this.fullDrop = true;
    }

    public boolean isFullDrop() {
        return this.fullDrop;
    }

    public void setPlayer(Player player) {
        this.player = player instanceof ServerPlayer sp ? sp : null;
    }

    public boolean hasAdvancement(ResourceLocation id) {
        if (this.player == null) {
            return true;
        } else {
            Advancement adv = this.player.server.getAdvancements().getAdvancement(id);
            if (adv == null) {
                return false;
            } else {
                AdvancementProgress prog = this.player.getAdvancements().getOrStartProgress(adv);
                return prog.isDone();
            }
        }
    }
}