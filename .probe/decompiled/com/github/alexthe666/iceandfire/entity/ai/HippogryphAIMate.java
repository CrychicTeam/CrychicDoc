package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.item.ItemHippogryphEgg;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class HippogryphAIMate extends Goal {

    private final EntityHippogryph hippo;

    Level world;

    int spawnBabyDelay;

    double moveSpeed;

    private EntityHippogryph targetMate;

    public HippogryphAIMate(EntityHippogryph animal, double speedIn) {
        this(animal, speedIn, animal.getClass());
    }

    public HippogryphAIMate(EntityHippogryph hippogryph, double speed, Class<? extends Animal> mate) {
        this.hippo = hippogryph;
        this.world = hippogryph.m_9236_();
        this.moveSpeed = speed;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.hippo.m_27593_() && !this.hippo.isOrderedToSit()) {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetMate.m_6084_() && this.targetMate.m_27593_() && this.spawnBabyDelay < 60;
    }

    @Override
    public void stop() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    @Override
    public void tick() {
        this.hippo.m_21563_().setLookAt(this.targetMate, 10.0F, (float) this.hippo.m_8132_());
        this.hippo.m_21573_().moveTo(this.targetMate, this.moveSpeed);
        this.spawnBabyDelay++;
        if (this.spawnBabyDelay >= 60 && this.hippo.m_20280_(this.targetMate) < 9.0) {
            this.spawnBaby();
        }
    }

    private EntityHippogryph getNearbyMate() {
        List<EntityHippogryph> list = this.world.m_45976_(EntityHippogryph.class, this.hippo.m_20191_().inflate(8.0));
        double d0 = Double.MAX_VALUE;
        EntityHippogryph entityanimal = null;
        for (EntityHippogryph entityanimal1 : list) {
            if (this.hippo.m_7848_(entityanimal1) && this.hippo.m_20280_(entityanimal1) < d0) {
                entityanimal = entityanimal1;
                d0 = this.hippo.m_20280_(entityanimal1);
            }
        }
        return entityanimal;
    }

    private void spawnBaby() {
        ItemEntity egg = new ItemEntity(this.world, this.hippo.m_20185_(), this.hippo.m_20186_(), this.hippo.m_20189_(), ItemHippogryphEgg.createEggStack(this.hippo.getEnumVariant(), this.targetMate.getEnumVariant()));
        this.hippo.m_146762_(6000);
        this.targetMate.m_146762_(6000);
        this.hippo.m_27594_();
        this.targetMate.m_27594_();
        egg.m_7678_(this.hippo.m_20185_(), this.hippo.m_20186_(), this.hippo.m_20189_(), 0.0F, 0.0F);
        if (!this.world.isClientSide) {
            this.world.m_7967_(egg);
        }
        RandomSource random = this.hippo.m_217043_();
        for (int i = 0; i < 7; i++) {
            double d0 = random.nextGaussian() * 0.02;
            double d1 = random.nextGaussian() * 0.02;
            double d2 = random.nextGaussian() * 0.02;
            double d3 = random.nextDouble() * (double) this.hippo.m_20205_() * 2.0 - (double) this.hippo.m_20205_();
            double d4 = 0.5 + random.nextDouble() * (double) this.hippo.m_20206_();
            double d5 = random.nextDouble() * (double) this.hippo.m_20205_() * 2.0 - (double) this.hippo.m_20205_();
            this.world.addParticle(ParticleTypes.HEART, this.hippo.m_20185_() + d3, this.hippo.m_20186_() + d4, this.hippo.m_20189_() + d5, d0, d1, d2);
        }
        if (this.world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.world.m_7967_(new ExperienceOrb(this.world, this.hippo.m_20185_(), this.hippo.m_20186_(), this.hippo.m_20189_(), random.nextInt(7) + 1));
        }
    }
}