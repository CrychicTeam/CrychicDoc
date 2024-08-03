package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSentinel;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MyrmexAIFindHidingSpot extends Goal {

    private static final int RADIUS = 32;

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;

    protected final Predicate<? super Entity> targetEntitySelector;

    private final EntityMyrmexSentinel myrmex;

    private BlockPos targetBlock = null;

    private int wanderRadius = 32;

    public MyrmexAIFindHidingSpot(EntityMyrmexSentinel myrmex) {
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(myrmex);
        this.targetEntitySelector = new Predicate<Entity>() {

            public boolean test(Entity myrmex) {
                return myrmex instanceof EntityMyrmexSentinel;
            }
        };
        this.myrmex = myrmex;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.targetBlock = this.getTargetPosition(this.wanderRadius);
        return this.myrmex.canMove() && this.myrmex.m_5448_() == null && this.myrmex.canSeeSky() && !this.myrmex.isHiding() && this.myrmex.visibleTicks <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.myrmex.shouldEnterHive() && this.myrmex.m_5448_() == null && !this.myrmex.isHiding() && this.myrmex.visibleTicks <= 0;
    }

    @Override
    public void tick() {
        if (this.targetBlock != null) {
            this.myrmex.m_21573_().moveTo((double) this.targetBlock.m_123341_() + 0.5, (double) this.targetBlock.m_123342_(), (double) this.targetBlock.m_123343_() + 0.5, 1.0);
            if (this.areMyrmexNear(5.0) || this.myrmex.isOnResin()) {
                if (this.myrmex.m_20238_(Vec3.atCenterOf(this.targetBlock)) < 9.0) {
                    this.wanderRadius += 32;
                    this.targetBlock = this.getTargetPosition(this.wanderRadius);
                }
            } else if (this.myrmex.m_5448_() == null && this.myrmex.m_7962_() == null && this.myrmex.visibleTicks == 0 && this.myrmex.m_20238_(Vec3.atCenterOf(this.targetBlock)) < 9.0) {
                this.myrmex.setHiding(true);
                this.myrmex.m_21573_().stop();
            }
        }
    }

    @Override
    public void stop() {
        this.targetBlock = null;
        this.wanderRadius = 32;
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.myrmex.m_20191_().inflate(targetDistance, 14.0, targetDistance);
    }

    public BlockPos getTargetPosition(int radius) {
        int x = (int) this.myrmex.m_20185_() + this.myrmex.m_217043_().nextInt(radius * 2) - radius;
        int z = (int) this.myrmex.m_20189_() + this.myrmex.m_217043_().nextInt(radius * 2) - radius;
        return this.myrmex.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));
    }

    private boolean areMyrmexNear(double distance) {
        List<Entity> sentinels = this.myrmex.m_9236_().getEntities(this.myrmex, this.getTargetableArea(distance), this.targetEntitySelector);
        List<Entity> hiddenSentinels = new ArrayList();
        for (Entity sentinel : sentinels) {
            if (sentinel instanceof EntityMyrmexSentinel && ((EntityMyrmexSentinel) sentinel).isHiding()) {
                hiddenSentinels.add(sentinel);
            }
        }
        return !hiddenSentinels.isEmpty();
    }
}