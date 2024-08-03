package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityKomodoDragon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.BreedGoal;

public class KomodoDragonAIBreed extends BreedGoal {

    boolean withPartner;

    private final EntityKomodoDragon komodo;

    int selfBreedTime = 0;

    public KomodoDragonAIBreed(EntityKomodoDragon entityKomodoDragon, double v) {
        super(entityKomodoDragon, v);
        this.komodo = entityKomodoDragon;
    }

    @Override
    public boolean canUse() {
        boolean prev = super.canUse();
        this.withPartner = prev;
        return this.withPartner || this.f_25113_.isInLove();
    }

    @Override
    public boolean canContinueToUse() {
        return this.withPartner ? super.canContinueToUse() : this.selfBreedTime < 60;
    }

    @Override
    public void stop() {
        super.stop();
        this.selfBreedTime = 0;
    }

    @Override
    public void tick() {
        if (this.withPartner) {
            super.tick();
        } else {
            this.f_25113_.m_21573_().stop();
            this.selfBreedTime++;
            if (this.selfBreedTime >= 60) {
                this.spawnParthogenicBaby();
            }
        }
    }

    @Override
    protected void breed() {
        for (int i = 0; i < 2 + this.f_25113_.m_217043_().nextInt(2); i++) {
            this.f_25113_.spawnChildFromBreeding((ServerLevel) this.f_25114_, this.f_25115_);
        }
        this.komodo.slaughterCooldown = 200;
    }

    private void spawnParthogenicBaby() {
        for (int i = 0; i < 2 + this.f_25113_.m_217043_().nextInt(2); i++) {
            this.f_25113_.spawnChildFromBreeding((ServerLevel) this.f_25114_, this.f_25113_);
        }
        this.komodo.slaughterCooldown = 200;
    }
}