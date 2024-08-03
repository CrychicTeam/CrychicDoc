package net.minecraft.world.entity.ai.goal.target;

import java.util.List;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;

public class ResetUniversalAngerTargetGoal<T extends Mob & NeutralMob> extends Goal {

    private static final int ALERT_RANGE_Y = 10;

    private final T mob;

    private final boolean alertOthersOfSameType;

    private int lastHurtByPlayerTimestamp;

    public ResetUniversalAngerTargetGoal(T t0, boolean boolean1) {
        this.mob = t0;
        this.alertOthersOfSameType = boolean1;
    }

    @Override
    public boolean canUse() {
        return this.mob.m_9236_().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER) && this.wasHurtByPlayer();
    }

    private boolean wasHurtByPlayer() {
        return this.mob.m_21188_() != null && this.mob.m_21188_().m_6095_() == EntityType.PLAYER && this.mob.m_21213_() > this.lastHurtByPlayerTimestamp;
    }

    @Override
    public void start() {
        this.lastHurtByPlayerTimestamp = this.mob.m_21213_();
        this.mob.forgetCurrentTargetAndRefreshUniversalAnger();
        if (this.alertOthersOfSameType) {
            this.getNearbyMobsOfSameType().stream().filter(p_26127_ -> p_26127_ != this.mob).map(p_26125_ -> (NeutralMob) p_26125_).forEach(NeutralMob::m_21661_);
        }
        super.start();
    }

    private List<? extends Mob> getNearbyMobsOfSameType() {
        double $$0 = this.mob.m_21133_(Attributes.FOLLOW_RANGE);
        AABB $$1 = AABB.unitCubeFromLowerCorner(this.mob.m_20182_()).inflate($$0, 10.0, $$0);
        return this.mob.m_9236_().m_6443_(this.mob.getClass(), $$1, EntitySelector.NO_SPECTATORS);
    }
}