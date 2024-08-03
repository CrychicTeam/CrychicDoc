package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityMungus;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MungusAITemptMushroom extends Goal {

    private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(10.0).ignoreLineOfSight();

    private final TargetingConditions targetingConditions;

    protected final EntityMungus mob;

    private final double speedModifier;

    private double px;

    private double py;

    private double pz;

    private int calmDown;

    private double pRotX;

    private double pRotY;

    protected Player player;

    public MungusAITemptMushroom(EntityMungus entityMungus0, double double1) {
        this.mob = entityMungus0;
        this.speedModifier = double1;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetingConditions = TEMP_TARGETING.copy();
    }

    @Override
    public boolean canUse() {
        if (this.calmDown > 0) {
            this.calmDown--;
            return false;
        } else {
            this.player = this.mob.m_9236_().m_45946_(this.targetingConditions, this.mob);
            return this.player == null ? false : this.shouldFollow(this.player.m_21205_()) || this.shouldFollow(this.player.m_21206_());
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        this.px = this.player.m_20185_();
        this.py = this.player.m_20186_();
        this.pz = this.player.m_20189_();
    }

    @Override
    public void stop() {
        this.player = null;
        this.mob.m_21573_().stop();
    }

    @Override
    public void tick() {
        this.mob.m_21563_().setLookAt(this.player, (float) (this.mob.m_8085_() + 20), (float) this.mob.m_8132_());
        if (this.mob.m_20280_(this.player) < 6.25) {
            this.mob.m_21573_().stop();
        } else {
            this.mob.m_21573_().moveTo(this.player, this.speedModifier);
        }
    }

    protected boolean shouldFollow(ItemStack stack) {
        return this.mob.shouldFollowMushroom(stack) || stack.getItem() == AMItemRegistry.MUNGAL_SPORES.get();
    }
}