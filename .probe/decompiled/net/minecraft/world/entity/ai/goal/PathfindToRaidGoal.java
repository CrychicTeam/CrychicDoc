package net.minecraft.world.entity.ai.goal;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.phys.Vec3;

public class PathfindToRaidGoal<T extends Raider> extends Goal {

    private static final int RECRUITMENT_SEARCH_TICK_DELAY = 20;

    private static final float SPEED_MODIFIER = 1.0F;

    private final T mob;

    private int recruitmentTick;

    public PathfindToRaidGoal(T t0) {
        this.mob = t0;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.m_5448_() == null && !this.mob.m_20160_() && this.mob.hasActiveRaid() && !this.mob.getCurrentRaid().isOver() && !((ServerLevel) this.mob.m_9236_()).isVillage(this.mob.m_20183_());
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.hasActiveRaid() && !this.mob.getCurrentRaid().isOver() && this.mob.m_9236_() instanceof ServerLevel && !((ServerLevel) this.mob.m_9236_()).isVillage(this.mob.m_20183_());
    }

    @Override
    public void tick() {
        if (this.mob.hasActiveRaid()) {
            Raid $$0 = this.mob.getCurrentRaid();
            if (this.mob.f_19797_ > this.recruitmentTick) {
                this.recruitmentTick = this.mob.f_19797_ + 20;
                this.recruitNearby($$0);
            }
            if (!this.mob.m_21691_()) {
                Vec3 $$1 = DefaultRandomPos.getPosTowards(this.mob, 15, 4, Vec3.atBottomCenterOf($$0.getCenter()), (float) (Math.PI / 2));
                if ($$1 != null) {
                    this.mob.m_21573_().moveTo($$1.x, $$1.y, $$1.z, 1.0);
                }
            }
        }
    }

    private void recruitNearby(Raid raid0) {
        if (raid0.isActive()) {
            Set<Raider> $$1 = Sets.newHashSet();
            List<Raider> $$2 = this.mob.m_9236_().m_6443_(Raider.class, this.mob.m_20191_().inflate(16.0), p_25712_ -> !p_25712_.hasActiveRaid() && Raids.canJoinRaid(p_25712_, raid0));
            $$1.addAll($$2);
            for (Raider $$3 : $$1) {
                raid0.joinRaid(raid0.getGroupsSpawned(), $$3, null, true);
            }
        }
    }
}