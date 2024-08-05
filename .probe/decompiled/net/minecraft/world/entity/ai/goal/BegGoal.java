package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BegGoal extends Goal {

    private final Wolf wolf;

    @Nullable
    private Player player;

    private final Level level;

    private final float lookDistance;

    private int lookTime;

    private final TargetingConditions begTargeting;

    public BegGoal(Wolf wolf0, float float1) {
        this.wolf = wolf0;
        this.level = wolf0.m_9236_();
        this.lookDistance = float1;
        this.begTargeting = TargetingConditions.forNonCombat().range((double) float1);
        this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.player = this.level.m_45946_(this.begTargeting, this.wolf);
        return this.player == null ? false : this.playerHoldingInteresting(this.player);
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.player.m_6084_()) {
            return false;
        } else {
            return this.wolf.m_20280_(this.player) > (double) (this.lookDistance * this.lookDistance) ? false : this.lookTime > 0 && this.playerHoldingInteresting(this.player);
        }
    }

    @Override
    public void start() {
        this.wolf.setIsInterested(true);
        this.lookTime = this.m_183277_(40 + this.wolf.m_217043_().nextInt(40));
    }

    @Override
    public void stop() {
        this.wolf.setIsInterested(false);
        this.player = null;
    }

    @Override
    public void tick() {
        this.wolf.m_21563_().setLookAt(this.player.m_20185_(), this.player.m_20188_(), this.player.m_20189_(), 10.0F, (float) this.wolf.getMaxHeadXRot());
        this.lookTime--;
    }

    private boolean playerHoldingInteresting(Player player0) {
        for (InteractionHand $$1 : InteractionHand.values()) {
            ItemStack $$2 = player0.m_21120_($$1);
            if (this.wolf.m_21824_() && $$2.is(Items.BONE)) {
                return true;
            }
            if (this.wolf.isFood($$2)) {
                return true;
            }
        }
        return false;
    }
}