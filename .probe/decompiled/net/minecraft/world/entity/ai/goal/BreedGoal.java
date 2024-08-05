package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public class BreedGoal extends Goal {

    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0).ignoreLineOfSight();

    protected final Animal animal;

    private final Class<? extends Animal> partnerClass;

    protected final Level level;

    @Nullable
    protected Animal partner;

    private int loveTime;

    private final double speedModifier;

    public BreedGoal(Animal animal0, double double1) {
        this(animal0, double1, animal0.getClass());
    }

    public BreedGoal(Animal animal0, double double1, Class<? extends Animal> classExtendsAnimal2) {
        this.animal = animal0;
        this.level = animal0.m_9236_();
        this.partnerClass = classExtendsAnimal2;
        this.speedModifier = double1;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.animal.isInLove()) {
            return false;
        } else {
            this.partner = this.getFreePartner();
            return this.partner != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.partner.m_6084_() && this.partner.isInLove() && this.loveTime < 60;
    }

    @Override
    public void stop() {
        this.partner = null;
        this.loveTime = 0;
    }

    @Override
    public void tick() {
        this.animal.m_21563_().setLookAt(this.partner, 10.0F, (float) this.animal.m_8132_());
        this.animal.m_21573_().moveTo(this.partner, this.speedModifier);
        this.loveTime++;
        if (this.loveTime >= this.m_183277_(60) && this.animal.m_20280_(this.partner) < 9.0) {
            this.breed();
        }
    }

    @Nullable
    private Animal getFreePartner() {
        List<? extends Animal> $$0 = this.level.m_45971_(this.partnerClass, PARTNER_TARGETING, this.animal, this.animal.m_20191_().inflate(8.0));
        double $$1 = Double.MAX_VALUE;
        Animal $$2 = null;
        for (Animal $$3 : $$0) {
            if (this.animal.canMate($$3) && this.animal.m_20280_($$3) < $$1) {
                $$2 = $$3;
                $$1 = this.animal.m_20280_($$3);
            }
        }
        return $$2;
    }

    protected void breed() {
        this.animal.spawnChildFromBreeding((ServerLevel) this.level, this.partner);
    }
}