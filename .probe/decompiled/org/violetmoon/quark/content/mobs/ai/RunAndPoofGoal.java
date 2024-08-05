package org.violetmoon.quark.content.mobs.ai;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.mobs.entity.Stoneling;

public class RunAndPoofGoal<T extends Entity> extends Goal {

    private final Predicate<Entity> canBeSeenSelector;

    protected Stoneling entity;

    private final double farSpeed;

    private final double nearSpeed;

    protected T closestLivingEntity;

    private final float avoidDistance;

    private Path path;

    private final PathNavigation navigation;

    private final Class<T> classToAvoid;

    private final Predicate<T> avoidTargetSelector;

    public RunAndPoofGoal(Stoneling entity, Class<T> classToAvoid, float avoidDistance, double farSpeed, double nearSpeed) {
        this(entity, classToAvoid, t -> true, avoidDistance, farSpeed, nearSpeed);
    }

    public RunAndPoofGoal(Stoneling entity, Class<T> classToAvoid, Predicate<T> avoidTargetSelector, float avoidDistance, double farSpeed, double nearSpeed) {
        this.canBeSeenSelector = target -> target != null && target.isAlive() && entity.m_21574_().hasLineOfSight(target) && !entity.m_7307_(target);
        this.entity = entity;
        this.classToAvoid = classToAvoid;
        this.avoidTargetSelector = avoidTargetSelector;
        this.avoidDistance = avoidDistance;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        this.navigation = entity.m_21573_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (!this.entity.isPlayerMade() && this.entity.isStartled()) {
            List<T> entities = this.entity.m_9236_().m_6443_(this.classToAvoid, this.entity.m_20191_().inflate((double) this.avoidDistance, 3.0, (double) this.avoidDistance), entity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && this.canBeSeenSelector.test(entity) && this.avoidTargetSelector.test(entity));
            if (entities.isEmpty()) {
                return false;
            } else {
                this.closestLivingEntity = (T) entities.get(0);
                Vec3 target = DefaultRandomPos.getPosAway(this.entity, 16, 7, this.closestLivingEntity.position());
                if (target != null && this.closestLivingEntity.distanceToSqr(target.x, target.y, target.z) < this.closestLivingEntity.distanceToSqr(this.entity)) {
                    return false;
                } else {
                    if (target != null) {
                        this.path = this.navigation.createPath(target.x, target.y, target.z, 0);
                    }
                    return target == null || this.path != null;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.path != null && !this.navigation.isDone()) {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            Vec3 epos = this.entity.m_20182_();
            for (int i = 0; i < 8; i++) {
                int j = Mth.floor(epos.x + (double) (((float) (i % 2) - 0.5F) * 0.1F) + (double) this.entity.m_20192_());
                int k = Mth.floor(epos.y + (double) (((float) ((i >> 1) % 2) - 0.5F) * this.entity.m_20205_() * 0.8F));
                int l = Mth.floor(epos.z + (double) (((float) ((i >> 2) % 2) - 0.5F) * this.entity.m_20205_() * 0.8F));
                if (pos.m_123341_() != k || pos.m_123342_() != j || pos.m_123343_() != l) {
                    pos.set(k, j, l);
                    if (this.entity.m_9236_().getBlockState(pos).m_280555_()) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        Vec3 epos = this.entity.m_20182_();
        if (this.path != null) {
            this.navigation.moveTo(this.path, this.farSpeed);
        }
        this.entity.m_9236_().playSound(null, epos.x, epos.y, epos.z, QuarkSounds.ENTITY_STONELING_MEEP, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    @Override
    public void stop() {
        this.closestLivingEntity = null;
        if (this.entity.m_9236_() instanceof ServerLevel ws) {
            Vec3 epos = this.entity.m_20182_();
            ws.sendParticles(ParticleTypes.CLOUD, epos.x, epos.y, epos.z, 40, 0.5, 0.5, 0.5, 0.1);
            ws.sendParticles(ParticleTypes.EXPLOSION, epos.x, epos.y, epos.z, 20, 0.5, 0.5, 0.5, 0.0);
        }
        for (Entity passenger : this.entity.m_146897_()) {
            if (!(passenger instanceof Player)) {
                passenger.discard();
            }
        }
        this.entity.m_146870_();
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