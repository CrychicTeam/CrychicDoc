package net.minecraft.world.entity.ai.goal.target;

import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class DefendVillageTargetGoal extends TargetGoal {

    private final IronGolem golem;

    @Nullable
    private LivingEntity potentialTarget;

    private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0);

    public DefendVillageTargetGoal(IronGolem ironGolem0) {
        super(ironGolem0, false, true);
        this.golem = ironGolem0;
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        AABB $$0 = this.golem.m_20191_().inflate(10.0, 8.0, 10.0);
        List<? extends LivingEntity> $$1 = this.golem.m_9236_().m_45971_(Villager.class, this.attackTargeting, this.golem, $$0);
        List<Player> $$2 = this.golem.m_9236_().m_45955_(this.attackTargeting, this.golem, $$0);
        for (LivingEntity $$3 : $$1) {
            Villager $$4 = (Villager) $$3;
            for (Player $$5 : $$2) {
                int $$6 = $$4.getPlayerReputation($$5);
                if ($$6 <= -100) {
                    this.potentialTarget = $$5;
                }
            }
        }
        return this.potentialTarget == null ? false : !(this.potentialTarget instanceof Player) || !this.potentialTarget.m_5833_() && !((Player) this.potentialTarget).isCreative();
    }

    @Override
    public void start() {
        this.golem.m_6710_(this.potentialTarget);
        super.start();
    }
}