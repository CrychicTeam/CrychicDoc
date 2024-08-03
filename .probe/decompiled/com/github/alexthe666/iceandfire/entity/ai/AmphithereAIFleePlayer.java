package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.util.IAFMath;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class AmphithereAIFleePlayer extends Goal {

    private final double farSpeed;

    private final double nearSpeed;

    private final float avoidDistance;

    protected EntityAmphithere entity;

    protected Player closestLivingEntity;

    private Path path;

    @Nonnull
    private List<Player> list = IAFMath.emptyPlayerEntityList;

    public AmphithereAIFleePlayer(EntityAmphithere entityIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this.entity = entityIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.entity.isFlying() && !this.entity.m_21824_()) {
            if (this.entity.m_9236_().getGameTime() % 4L == 0L) {
                this.list = this.entity.m_9236_().m_6443_(Player.class, this.entity.m_20191_().inflate((double) this.avoidDistance, 6.0, (double) this.avoidDistance), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
            }
            if (this.list.isEmpty()) {
                return false;
            } else {
                this.closestLivingEntity = (Player) this.list.get(0);
                Vec3 Vector3d = DefaultRandomPos.getPosAway(this.entity, 20, 7, new Vec3(this.closestLivingEntity.m_20185_(), this.closestLivingEntity.m_20186_(), this.closestLivingEntity.m_20189_()));
                if (Vector3d == null) {
                    return false;
                } else if (this.closestLivingEntity.m_20238_(Vector3d) < this.closestLivingEntity.m_20280_(this.entity)) {
                    return false;
                } else {
                    this.path = this.entity.m_21573_().createPath(Vector3d.x, Vector3d.y, Vector3d.z, 0);
                    return this.path != null;
                }
            }
        } else {
            this.list = IAFMath.emptyPlayerEntityList;
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.m_21573_().isDone();
    }

    @Override
    public void start() {
        this.entity.m_21573_().moveTo(this.path, this.farSpeed);
    }

    @Override
    public void stop() {
        this.closestLivingEntity = null;
    }

    @Override
    public void tick() {
        if (this.entity.m_20280_(this.closestLivingEntity) < 49.0) {
            this.entity.m_21573_().setSpeedModifier(this.nearSpeed);
        } else {
            this.entity.m_21573_().setSpeedModifier(this.farSpeed);
        }
    }
}