package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class PixieAIFlee<T extends Entity> extends Goal {

    private final float avoidDistance;

    private final Class<T> classToAvoid;

    protected EntityPixie pixie;

    protected T closestLivingEntity;

    private Vec3 hidePlace;

    @Nonnull
    private List<T> list = Collections.emptyList();

    public PixieAIFlee(EntityPixie pixie, Class<T> classToAvoidIn, float avoidDistanceIn, Predicate<? super T> avoidTargetSelectorIn) {
        this.pixie = pixie;
        this.classToAvoid = classToAvoidIn;
        this.avoidDistance = avoidDistanceIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.pixie.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.pixie.m_21824_()) {
            if (this.pixie.m_9236_().getGameTime() % 4L == 0L) {
                this.list = this.pixie.m_9236_().m_6443_(this.classToAvoid, this.pixie.m_20191_().inflate((double) this.avoidDistance, 3.0, (double) this.avoidDistance), EntitySelector.NO_SPECTATORS);
            }
            if (this.list.isEmpty()) {
                return false;
            } else {
                this.closestLivingEntity = (T) this.list.get(0);
                if (this.closestLivingEntity != null) {
                    Vec3 Vector3d = DefaultRandomPos.getPosAway(this.pixie, 16, 4, new Vec3(this.closestLivingEntity.getX(), this.closestLivingEntity.getY(), this.closestLivingEntity.getZ()));
                    if (Vector3d == null) {
                        return false;
                    } else {
                        Vector3d = Vector3d.add(0.0, 1.0, 0.0);
                        this.pixie.m_21566_().setWantedPosition(Vector3d.x, Vector3d.y, Vector3d.z, this.calculateRunSpeed());
                        this.pixie.m_21563_().setLookAt(Vector3d.x, Vector3d.y, Vector3d.z, 180.0F, 20.0F);
                        this.hidePlace = Vector3d;
                        this.pixie.slowSpeed = true;
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            this.list = Collections.emptyList();
            return false;
        }
    }

    private double calculateRunSpeed() {
        if (this.pixie.ticksHeldItemFor > 6000) {
            return 0.1;
        } else if (this.pixie.ticksHeldItemFor > 1200) {
            return 0.25;
        } else {
            return this.pixie.ticksHeldItemFor > 600 ? 0.25 : 1.0;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.hidePlace != null && this.pixie.m_20238_(this.hidePlace.add(0.5, 0.5, 0.5)) < 2.0;
    }

    @Override
    public void start() {
        this.pixie.m_21566_().setWantedPosition(this.hidePlace.x, this.hidePlace.y, this.hidePlace.z, this.calculateRunSpeed());
        this.pixie.m_21563_().setLookAt(this.hidePlace.x, this.hidePlace.y, this.hidePlace.z, 180.0F, 20.0F);
    }

    @Override
    public void stop() {
        this.closestLivingEntity = null;
        this.pixie.slowSpeed = false;
    }
}