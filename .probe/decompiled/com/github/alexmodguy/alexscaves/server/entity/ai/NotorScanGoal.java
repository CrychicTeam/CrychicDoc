package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class NotorScanGoal extends Goal {

    private NotorEntity notor;

    private LivingEntity scanTarget;

    private int scanTime = 0;

    public NotorScanGoal(NotorEntity notor) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.notor = notor;
    }

    private int getMaxScanTime() {
        return this.notor.m_9236_().m_46791_() == Difficulty.PEACEFUL ? 40 : 100;
    }

    @Override
    public boolean canUse() {
        long worldTime = this.notor.m_9236_().getGameTime() % 10L;
        if ((this.notor.m_217043_().nextInt(300) == 0 || worldTime == 0L) && this.notor.getHologramUUID() == null && this.notor.stopScanningFor <= 0) {
            AABB aabb = this.notor.m_20191_().inflate(25.0);
            List<LivingEntity> list = this.notor.m_9236_().m_6443_(LivingEntity.class, aabb, NotorEntity.SCAN_TARGET);
            if (list.isEmpty()) {
                return false;
            } else {
                LivingEntity closest = null;
                for (LivingEntity mob : list) {
                    if (!(mob instanceof NotorEntity) && ((closest == null || mob.m_20280_(this.notor) < closest.m_20280_(this.notor)) && this.notor.m_142582_(mob) || !(closest instanceof Player) && mob instanceof Player)) {
                        closest = mob;
                    }
                }
                this.scanTarget = closest;
                return this.scanTarget != null;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.scanTarget != null && this.scanTarget.isAlive() && this.notor.m_142582_(this.scanTarget) && this.scanTarget.m_20270_(this.notor) <= 40.0F && this.scanTime < this.getMaxScanTime() && this.notor.getHologramUUID() == null;
    }

    @Override
    public void start() {
        this.notor.m_21573_().stop();
        this.scanTime = 0;
        this.notor.setScanningId(-1);
    }

    @Override
    public void stop() {
        if (this.scanTime >= this.getMaxScanTime() && this.scanTarget != null && this.scanTarget.isAlive()) {
            this.notor.setHologramUUID(this.scanTarget.m_20148_());
            this.notor.setShowingHologram(false);
            this.notor.stopScanningFor = this.notor.m_217043_().nextInt(300) + 300;
        }
        this.notor.setScanningId(-1);
    }

    @Override
    public void tick() {
        double dist = (double) this.scanTarget.m_20270_(this.notor);
        this.notor.m_7618_(EntityAnchorArgument.Anchor.EYES, this.scanTarget.m_146892_());
        if (dist > 8.0) {
            this.notor.m_21573_().moveTo(this.scanTarget.m_20185_(), this.scanTarget.m_20227_(1.0) + 1.0, this.scanTarget.m_20189_(), 1.2F);
            if (dist > 15.0) {
                this.notor.setScanningId(-1);
            }
        } else {
            this.notor.m_21573_().stop();
            this.notor.setScanningId(this.scanTarget.m_19879_());
            this.scanTime++;
        }
    }
}