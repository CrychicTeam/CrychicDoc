package org.violetmoon.quark.content.mobs.ai;

import java.util.EnumSet;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.mobs.entity.Shiba;
import org.violetmoon.quark.mixin.mixins.accessor.AccessorAbstractArrow;

public class FetchArrowGoal extends Goal {

    private final Shiba shiba;

    private int timeToRecalcPath;

    private final PathNavigation navigator;

    private int timeTilNextJump = 20;

    public FetchArrowGoal(Shiba shiba) {
        this.shiba = shiba;
        this.navigator = shiba.m_21573_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public void tick() {
        AbstractArrow fetching = this.shiba.getFetching();
        if (fetching != null) {
            this.shiba.m_21563_().setLookAt(fetching, 10.0F, (float) this.shiba.m_8132_());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.shiba.m_21523_() && !this.shiba.m_20159_()) {
                    this.navigator.moveTo(fetching, 1.1);
                }
            }
            double dist = (double) this.shiba.m_20270_(fetching);
            if (dist < 3.0 && fetching.m_6084_()) {
                if (fetching.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.shiba.setMouthItem(((AccessorAbstractArrow) fetching).quark$getPickupItem());
                    fetching.m_146870_();
                }
                if (dist < 1.0 && (fetching.pickup == AbstractArrow.Pickup.DISALLOWED || fetching.pickup == AbstractArrow.Pickup.CREATIVE_ONLY)) {
                    this.shiba.m_9236_().m_247517_(null, this.shiba.m_20183_(), QuarkSounds.ENTITY_SHIBA_EAT_ARROW, SoundSource.NEUTRAL);
                    fetching.m_146870_();
                }
            }
            this.timeTilNextJump--;
            if (this.timeTilNextJump <= 0) {
                this.timeTilNextJump = this.shiba.m_9236_().random.nextInt(5) + 10;
                if (this.shiba.m_20096_()) {
                    this.shiba.m_5997_(0.0, 0.3, 0.0);
                    this.shiba.m_6862_(true);
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public boolean canUse() {
        AbstractArrow fetching = this.shiba.getFetching();
        return this.shiba.getMouthItem().isEmpty() && fetching != null && fetching.m_6084_() && fetching.m_9236_() == this.shiba.m_9236_() && fetching.pickup != AbstractArrow.Pickup.DISALLOWED;
    }
}