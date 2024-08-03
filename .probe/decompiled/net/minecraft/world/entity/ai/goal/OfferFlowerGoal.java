package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;

public class OfferFlowerGoal extends Goal {

    private static final TargetingConditions OFFER_TARGER_CONTEXT = TargetingConditions.forNonCombat().range(6.0);

    public static final int OFFER_TICKS = 400;

    private final IronGolem golem;

    private Villager villager;

    private int tick;

    public OfferFlowerGoal(IronGolem ironGolem0) {
        this.golem = ironGolem0;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.golem.m_9236_().isDay()) {
            return false;
        } else if (this.golem.m_217043_().nextInt(8000) != 0) {
            return false;
        } else {
            this.villager = (Villager) this.golem.m_9236_().m_45963_(Villager.class, OFFER_TARGER_CONTEXT, this.golem, this.golem.m_20185_(), this.golem.m_20186_(), this.golem.m_20189_(), this.golem.m_20191_().inflate(6.0, 2.0, 6.0));
            return this.villager != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.tick > 0;
    }

    @Override
    public void start() {
        this.tick = this.m_183277_(400);
        this.golem.offerFlower(true);
    }

    @Override
    public void stop() {
        this.golem.offerFlower(false);
        this.villager = null;
    }

    @Override
    public void tick() {
        this.golem.m_21563_().setLookAt(this.villager, 30.0F, 30.0F);
        this.tick--;
    }
}