package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.util.LaysEggs;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.GameRules;

public class AnimalBreedEggsGoal extends BreedGoal {

    private final Animal mob;

    private int eggLoveTime;

    private double speed;

    public AnimalBreedEggsGoal(Animal mob, double speed) {
        super(mob, speed);
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !((LaysEggs) this.mob).hasEgg();
    }

    @Override
    public boolean canContinueToUse() {
        return this.f_25115_.m_6084_() && this.f_25115_.isInLove() && this.eggLoveTime < 60;
    }

    @Override
    public void stop() {
        this.f_25115_ = null;
        this.eggLoveTime = 0;
    }

    @Override
    public void tick() {
        this.f_25113_.m_21563_().setLookAt(this.f_25115_, 10.0F, (float) this.f_25113_.m_8132_());
        this.f_25113_.m_21573_().moveTo(this.f_25115_, this.speed);
        this.eggLoveTime++;
        double width = Math.max((double) (this.f_25113_.m_20205_() * 2.0F + 0.5F), 3.0);
        if (this.eggLoveTime >= this.m_183277_(60) && (double) Mth.sqrt((float) this.f_25113_.m_20280_(this.f_25115_)) < width) {
            this.breed();
        }
    }

    @Override
    protected void breed() {
        ServerPlayer serverplayer = this.f_25113_.getLoveCause();
        if (serverplayer == null && this.f_25115_.getLoveCause() != null) {
            serverplayer = this.f_25115_.getLoveCause();
        }
        if (serverplayer != null) {
            serverplayer.m_36220_(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverplayer, this.f_25113_, this.f_25115_, (AgeableMob) null);
        }
        ((LaysEggs) this.mob).setHasEgg(true);
        this.f_25113_.m_146762_(6000);
        this.f_25115_.m_146762_(6000);
        this.f_25113_.resetLove();
        this.f_25115_.resetLove();
        RandomSource randomsource = this.f_25113_.m_217043_();
        if (this.f_25114_.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.f_25114_.m_7967_(new ExperienceOrb(this.f_25114_, this.f_25113_.m_20185_(), this.f_25113_.m_20186_(), this.f_25113_.m_20189_(), randomsource.nextInt(7) + 1));
        }
    }
}