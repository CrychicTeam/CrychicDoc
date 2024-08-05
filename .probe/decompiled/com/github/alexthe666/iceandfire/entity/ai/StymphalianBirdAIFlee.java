package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class StymphalianBirdAIFlee extends Goal {

    private final Predicate<Entity> canBeSeenSelector;

    private final float avoidDistance;

    protected EntityStymphalianBird stymphalianBird;

    protected LivingEntity closestLivingEntity;

    private Vec3 hidePlace;

    public StymphalianBirdAIFlee(EntityStymphalianBird stymphalianBird, float avoidDistanceIn) {
        this.stymphalianBird = stymphalianBird;
        this.canBeSeenSelector = new Predicate<Entity>() {

            public boolean test(Entity entity) {
                return entity instanceof Player && entity.isAlive() && StymphalianBirdAIFlee.this.stymphalianBird.m_21574_().hasLineOfSight(entity) && !StymphalianBirdAIFlee.this.stymphalianBird.m_7307_(entity);
            }
        };
        this.avoidDistance = avoidDistanceIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.stymphalianBird.getVictor() == null) {
            return false;
        } else {
            List<LivingEntity> list = this.stymphalianBird.m_9236_().m_6443_(LivingEntity.class, this.stymphalianBird.m_20191_().inflate((double) this.avoidDistance, 3.0, (double) this.avoidDistance), this.canBeSeenSelector);
            if (list.isEmpty()) {
                return false;
            } else {
                this.closestLivingEntity = (LivingEntity) list.get(0);
                if (this.closestLivingEntity != null && this.stymphalianBird.getVictor() != null && this.closestLivingEntity.equals(this.stymphalianBird.getVictor())) {
                    Vec3 Vector3d = DefaultRandomPos.getPosAway(this.stymphalianBird, 32, 7, new Vec3(this.closestLivingEntity.m_20185_(), this.closestLivingEntity.m_20186_(), this.closestLivingEntity.m_20189_()));
                    if (Vector3d == null) {
                        return false;
                    } else {
                        Vector3d = Vector3d.add(0.0, 3.0, 0.0);
                        this.stymphalianBird.m_21566_().setWantedPosition(Vector3d.x, Vector3d.y, Vector3d.z, 3.0);
                        this.stymphalianBird.m_21563_().setLookAt(Vector3d.x, Vector3d.y, Vector3d.z, 180.0F, 20.0F);
                        this.hidePlace = Vector3d;
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.hidePlace != null && this.stymphalianBird.m_20238_(this.hidePlace.add(0.5, 0.5, 0.5)) < 2.0;
    }

    @Override
    public void start() {
        this.stymphalianBird.m_21566_().setWantedPosition(this.hidePlace.x, this.hidePlace.y, this.hidePlace.z, 3.0);
        this.stymphalianBird.m_21563_().setLookAt(this.hidePlace.x, this.hidePlace.y, this.hidePlace.z, 180.0F, 20.0F);
    }

    @Override
    public void stop() {
        this.closestLivingEntity = null;
    }
}