package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.github.alexthe666.iceandfire.entity.ai.StymphalianBirdAIAirTarget;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class StymphalianBirdFlock {

    private EntityStymphalianBird leader;

    private ArrayList<EntityStymphalianBird> members = new ArrayList();

    private BlockPos leaderTarget;

    private BlockPos prevLeaderTarget;

    private RandomSource random;

    private final int distance = 15;

    private StymphalianBirdFlock() {
    }

    public static StymphalianBirdFlock createFlock(EntityStymphalianBird bird) {
        StymphalianBirdFlock flock = new StymphalianBirdFlock();
        flock.leader = bird;
        flock.members = new ArrayList();
        flock.members.add(bird);
        flock.leaderTarget = bird.airTarget;
        flock.random = bird.m_217043_();
        return flock;
    }

    @Nullable
    public static StymphalianBirdFlock getNearbyFlock(EntityStymphalianBird bird) {
        float d0 = (float) IafConfig.stymphalianBirdFlockLength;
        List<Entity> list = bird.m_9236_().getEntities(bird, new AABB(bird.m_20185_(), bird.m_20186_(), bird.m_20189_(), bird.m_20185_() + 1.0, bird.m_20186_() + 1.0, bird.m_20189_() + 1.0).inflate((double) d0, 10.0, (double) d0), EntityStymphalianBird.STYMPHALIAN_PREDICATE);
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof EntityStymphalianBird other && other.flock != null) {
                    return other.flock;
                }
            }
        }
        return null;
    }

    public boolean isLeader(EntityStymphalianBird bird) {
        return this.leader != null && this.leader == bird;
    }

    public void addToFlock(EntityStymphalianBird bird) {
        this.members.add(bird);
    }

    public void update() {
        if (!this.members.isEmpty() && (this.leader == null || !this.leader.m_6084_())) {
            this.leader = (EntityStymphalianBird) this.members.get(this.random.nextInt(this.members.size()));
        }
        if (this.leader != null && this.leader.m_6084_()) {
            this.prevLeaderTarget = this.leaderTarget;
            this.leaderTarget = this.leader.airTarget;
        }
    }

    public void onLeaderAttack(LivingEntity attackTarget) {
        for (EntityStymphalianBird bird : this.members) {
            if (bird.m_5448_() == null && !this.isLeader(bird)) {
                bird.setTarget(attackTarget);
            }
        }
    }

    public EntityStymphalianBird getLeader() {
        return this.leader;
    }

    public void setTarget(BlockPos target) {
        this.leaderTarget = target;
        for (EntityStymphalianBird bird : this.members) {
            if (!this.isLeader(bird)) {
                bird.airTarget = StymphalianBirdAIAirTarget.getNearbyAirTarget(bird);
            }
        }
    }

    public void setFlying(boolean flying) {
        for (EntityStymphalianBird bird : this.members) {
            if (!this.isLeader(bird)) {
                bird.setFlying(flying);
            }
        }
    }

    public void setFearTarget(LivingEntity living) {
        for (EntityStymphalianBird bird : this.members) {
            bird.setVictor(living);
        }
    }
}