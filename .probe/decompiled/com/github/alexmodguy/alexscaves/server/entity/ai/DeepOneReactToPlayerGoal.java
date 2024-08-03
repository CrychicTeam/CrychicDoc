package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneMageEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.DeepOneReaction;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DeepOneReactToPlayerGoal extends Goal {

    private DeepOneBaseEntity deepOne;

    private Player player;

    private DeepOneReaction prevReaction;

    private DeepOneReaction reaction;

    private boolean following = false;

    private int refreshReactionTime = 0;

    private boolean isBeingLookedAt = false;

    private int lookAtTime = 0;

    private int chaseTime = 0;

    private int friendlyLookAtTime = 0;

    private int executionTime = 0;

    private Vec3 moveTarget = null;

    public DeepOneReactToPlayerGoal(DeepOneBaseEntity deepOne) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.deepOne = deepOne;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.deepOne.m_5448_();
        long worldTime = this.deepOne.m_9236_().getGameTime() % 20L;
        if ((worldTime == 0L || this.deepOne.m_217043_().nextInt(15) == 0) && (target == null || !target.isAlive()) && this.deepOne.getCorneringPlayer() == null) {
            AABB aabb = this.deepOne.m_20191_().inflate(80.0);
            List<Player> list = this.deepOne.m_9236_().m_45976_(Player.class, aabb);
            if (list.isEmpty()) {
                return false;
            } else {
                Player closest = null;
                int highestReputation = Integer.MIN_VALUE;
                for (Player scanningPlayer : list) {
                    if ((closest == null || scanningPlayer.m_20280_(this.deepOne) < closest.m_20280_(this.deepOne) || this.deepOne.getReputationOf(scanningPlayer.m_20148_()) > highestReputation) && this.deepOne.m_142582_(scanningPlayer)) {
                        closest = scanningPlayer;
                        highestReputation = this.deepOne.getReputationOf(scanningPlayer.m_20148_());
                    }
                }
                this.player = closest;
                DeepOneReaction reaction1 = DeepOneReaction.fromReputation(highestReputation);
                return this.player != null && (reaction1 != DeepOneReaction.AGGRESSIVE || this.deepOne.isSummoned() || !this.player.isCreative()) && reaction1.validPlayer(this.deepOne, this.player);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity attackTarget = this.deepOne.m_5448_();
        return this.player != null && !this.player.isSpectator() && this.reaction != null && this.deepOne.getCorneringPlayer() == null && (double) this.deepOne.m_20270_(this.player) >= this.reaction.getMinDistance() && (double) this.deepOne.m_20270_(this.player) <= this.reaction.getMaxDistance() && (attackTarget == null || !attackTarget.isAlive()) && (this.reaction != DeepOneReaction.AGGRESSIVE || !this.player.isCreative() || this.deepOne.isSummoned()) && !this.deepOne.isTradingLocked() && this.reaction.validPlayer(this.deepOne, this.player);
    }

    @Override
    public void start() {
        this.chaseTime = 0;
        this.executionTime = 0;
        this.refreshReaction();
    }

    @Override
    public void stop() {
        this.chaseTime = 0;
        this.executionTime = 0;
        this.following = false;
        this.isBeingLookedAt = false;
        this.moveTarget = null;
        this.deepOne.setSoundsAngry(false);
    }

    private void refreshReaction() {
        if (this.player != null) {
            this.prevReaction = this.reaction;
            this.reaction = this.deepOne.getReactionTo(this.player);
            if (this.prevReaction != this.reaction) {
                this.deepOne.m_21573_().stop();
            }
            this.refreshReactionTime = 20 + this.deepOne.m_217043_().nextInt(40);
        }
    }

    @Override
    public void tick() {
        this.executionTime++;
        if (this.refreshReactionTime-- < 0) {
            this.refreshReaction();
        }
        switch(this.reaction) {
            case STALKING:
                this.tickStalking();
                break;
            case AGGRESSIVE:
                if (!this.player.isCreative()) {
                    this.deepOne.m_6710_(this.player);
                }
                break;
            case NEUTRAL:
                this.tickFollow(0.1F);
                break;
            case HELPFUL:
                this.deepOne.copyTarget(this.player);
                this.tickFollow(0.4F);
        }
        this.deepOne.setSoundsAngry(this.reaction == DeepOneReaction.AGGRESSIVE);
        if (!this.deepOne.m_21573_().isDone() && (this.moveTarget == null || this.moveTarget.y < this.deepOne.m_20186_() + 2.0)) {
            this.deepOne.setDeepOneSwimming(!this.deepOne.m_20096_() && this.deepOne.m_20072_());
        }
    }

    private void tickFollow(float propensity) {
        float f = 0.1F;
        if (this.player.m_21206_().is(ACTagRegistry.DEEP_ONE_BARTERS) || this.player.m_21205_().is(ACTagRegistry.DEEP_ONE_BARTERS)) {
            f = 0.2F;
        }
        if (this.deepOne.isSummoned()) {
            f = 1000.0F;
        }
        double distance = (double) this.deepOne.m_20270_(this.player);
        if (this.deepOne.m_217043_().nextFloat() < propensity * f && this.friendlyLookAtTime <= 0) {
            this.friendlyLookAtTime = 10 + this.deepOne.m_217043_().nextInt(20);
        }
        if (this.friendlyLookAtTime > 0) {
            this.deepOne.m_21563_().setLookAt(this.player.m_20185_(), this.player.m_20188_(), this.player.m_20189_(), 10.0F, (float) this.deepOne.m_8132_());
            this.friendlyLookAtTime--;
        }
        if (this.following) {
            if (distance < 4.0) {
                this.following = false;
                this.deepOne.m_21573_().stop();
            } else {
                this.deepOne.m_21573_().moveTo(this.player, 1.0);
            }
        } else if (distance > 10.0 && this.deepOne.m_217043_().nextFloat() < propensity * 0.2F) {
            this.following = true;
        }
    }

    private void tickStalking() {
        double distance = (double) this.deepOne.m_20270_(this.player);
        double distanceXZ = (double) Mth.sqrt((float) this.deepOne.m_20275_(this.player.m_20185_(), this.deepOne.m_20186_(), this.player.m_20189_()));
        if (distance <= 8.0 && this.isBeingLookedAt) {
            this.chaseTime++;
        }
        if (distance > 40.0 && this.chaseTime > 0) {
            this.chaseTime = 0;
        }
        if (this.chaseTime >= (this.deepOne.m_21188_() == this.player ? 10 : 60)) {
            this.deepOne.setCorneredBy(this.player);
        } else {
            if (this.lookAtTime-- < 0) {
                boolean isLooking = this.isEntityLookingAt(this.player, this.deepOne, 1.2F);
                if (isLooking != this.isBeingLookedAt) {
                    this.deepOne.m_21573_().stop();
                    this.moveTarget = null;
                    if (this.executionTime > 20 && this.deepOne.m_20270_(this.player) < 20.0F) {
                        ACAdvancementTriggerRegistry.STALKED_BY_DEEP_ONE.triggerForEntity(this.player);
                    }
                }
                this.isBeingLookedAt = isLooking;
                this.lookAtTime = 5 + this.deepOne.m_217043_().nextInt(5);
            }
            this.deepOne.m_6842_(false);
            if (!this.isBeingLookedAt && this.deepOne.m_217043_().nextInt(100) != 0) {
                for (int j = 0; (this.moveTarget == null || this.moveTarget.distanceTo(this.deepOne.m_20182_()) < 3.0) && j < 10; j++) {
                    Vec3 vec3 = DefaultRandomPos.getPosTowards(this.deepOne, 15, 15, this.player.m_20182_(), (float) (Math.PI / 2));
                    if (vec3 != null) {
                        float mageUp = this.deepOne instanceof DeepOneMageEntity ? (float) (1 + this.deepOne.m_217043_().nextInt(2)) : 0.0F;
                        this.moveTarget = new Vec3(vec3.x, distanceXZ < 20.0 ? this.player.m_20186_() + (double) mageUp : vec3.y, vec3.z);
                    }
                }
                this.deepOne.m_21563_().setLookAt(this.player.m_20185_(), this.player.m_20188_(), this.player.m_20189_(), 10.0F, (float) this.deepOne.m_8132_());
                if (distance < 12.0) {
                    this.deepOne.m_21573_().stop();
                    if (this.deepOne.m_20096_()) {
                        this.deepOne.setDeepOneSwimming(false);
                    }
                }
            } else {
                for (int jx = 0; (this.moveTarget == null || this.moveTarget.distanceTo(this.deepOne.m_20182_()) < 3.0) && jx < 10; jx++) {
                    this.moveTarget = DefaultRandomPos.getPosAway(this.deepOne, 40, 15, this.player.m_20182_());
                }
            }
            if (this.moveTarget != null && this.moveTarget.distanceTo(this.deepOne.m_20182_()) > 3.0 && (this.isBeingLookedAt || distance >= 12.0)) {
                if (this.moveTarget.y > this.deepOne.m_20186_() + 1.0) {
                    this.deepOne.setDeepOneSwimming(this.deepOne.m_20072_());
                }
                float mageUp = this.deepOne instanceof DeepOneMageEntity ? 2.0F : 0.0F;
                this.deepOne.m_21573_().moveTo(this.moveTarget.x, this.moveTarget.y + (double) mageUp, this.moveTarget.z, this.isBeingLookedAt ? 2.0 : 1.0);
            }
        }
    }

    private boolean isEntityLookingAt(LivingEntity looker, LivingEntity seen, double degree) {
        degree *= 1.0 + (double) looker.m_20270_(seen) * 0.1;
        Vec3 vec3 = looker.m_20252_(1.0F).normalize();
        Vec3 vec31 = new Vec3(seen.m_20185_() - looker.m_20185_(), seen.m_20191_().minY + (double) seen.m_20192_() - (looker.m_20186_() + (double) looker.m_20192_()), seen.m_20189_() - looker.m_20189_());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0 - degree / d0 && looker.hasLineOfSight(seen);
    }
}