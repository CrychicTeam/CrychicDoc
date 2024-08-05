package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class NotorHologramGoal extends Goal {

    private NotorEntity notor;

    private int checkForMonsterTime = 0;

    private Mob monster;

    private Vec3 moveTarget = null;

    private int hologramTime = 0;

    private static final int MAX_HOLOGRAM_TIME = 100;

    public NotorHologramGoal(NotorEntity notor) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.notor = notor;
    }

    @Override
    public boolean canUse() {
        return this.notor.getHologramUUID() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() && (this.monster == null || this.monster.m_6084_() && this.monster.m_20270_(this.notor) < 40.0F);
    }

    @Override
    public void start() {
        this.checkForMonsterTime = 0;
        this.hologramTime = 0;
        this.monster = null;
    }

    @Override
    public void stop() {
        this.notor.setScanningId(-1);
        this.notor.setHologramPos(null);
        this.notor.setShowingHologram(false);
        this.notor.stopScanningFor = this.notor.stopScanningFor + this.notor.m_217043_().nextInt(100) + 100;
        this.monster = null;
    }

    @Override
    public void tick() {
        Entity hologram = this.notor.getHologramEntity();
        double holoHeight = hologram == null ? 1.0 : (double) hologram.getBbHeight();
        if (this.checkForMonsterTime < 0) {
            this.checkForMonsterTime = 20 + this.notor.m_217043_().nextInt(10);
            if (this.monster == null || !this.monster.m_6084_()) {
                Predicate<Entity> monsterAway = entity -> entity instanceof Enemy && (hologram == null || !entity.equals(hologram)) && entity.distanceTo(this.notor) > 5.0F && !entity.isPassenger() && this.hasNoTarget(entity);
                List<Mob> list = this.notor.m_9236_().m_6443_(Mob.class, this.notor.m_20191_().inflate(30.0, 12.0, 30.0), EntitySelector.NO_SPECTATORS.and(monsterAway));
                list.sort(Comparator.comparingDouble(this.notor::m_20280_));
                if (!list.isEmpty()) {
                    this.monster = (Mob) list.get(0);
                }
            }
        } else {
            this.checkForMonsterTime--;
        }
        if (this.monster != null && this.monster.m_6084_() && this.hasNoTarget(this.monster)) {
            double distToMonster = (double) this.monster.m_20270_(this.notor);
            double distMonsterToPlayer = (double) this.monster.m_20270_(this.notor);
            if (this.hologramTime < 100) {
                if (distToMonster < 8.0 && this.notor.m_142582_(this.monster)) {
                    this.notor.m_21573_().stop();
                    if (this.notor.getHologramPos() == null) {
                        BlockPos set = this.monster.m_20183_();
                        for (int i = 0; i < 15; i++) {
                            BlockPos holoPos = this.monster.m_20183_().offset(this.notor.m_217043_().nextInt(10) - 5, (int) (this.monster.m_20206_() + 3.0F), this.notor.m_217043_().nextInt(10) - 5);
                            while (this.notor.m_9236_().m_46859_(holoPos) && holoPos.m_123342_() > this.notor.m_9236_().m_141937_()) {
                                holoPos = holoPos.below();
                            }
                            holoPos = holoPos.above();
                            Vec3 holoVec = Vec3.atCenterOf(holoPos);
                            if (!this.isTargetBlocked(this.monster, holoVec) && !this.isTargetBlocked(this.notor, holoVec)) {
                                set = holoPos;
                                break;
                            }
                        }
                        this.notor.setHologramPos(set.above((int) holoHeight));
                    }
                    BlockPos gotten = this.notor.getHologramPos();
                    Vec3 stareAt = gotten == null ? this.notor.m_146892_() : Vec3.atCenterOf(gotten).add(0.0, 0.0, 0.0);
                    this.monster.m_7618_(EntityAnchorArgument.Anchor.EYES, stareAt);
                    this.notor.m_7618_(EntityAnchorArgument.Anchor.EYES, stareAt);
                    this.monster.getNavigation().stop();
                    this.notor.setShowingHologram(true);
                    this.hologramTime++;
                } else {
                    this.notor.m_21573_().moveTo(this.monster.m_20185_(), this.monster.m_20227_(1.0) + 1.0, this.monster.m_20189_(), 1.2F);
                }
            } else {
                this.notor.setShowingHologram(false);
                if (hologram instanceof Player player && !player.isCreative()) {
                    this.monster.getNavigation().moveTo(this.notor.m_20185_(), this.notor.m_20186_(), this.notor.m_20189_(), 1.2F);
                    this.notor.m_21573_().moveTo(player.m_20185_(), player.m_20227_(1.0) + 2.0, player.m_20189_(), 1.2F);
                    this.monster.setTarget(player);
                    if (distMonsterToPlayer < Math.min(this.monster.m_21133_(Attributes.FOLLOW_RANGE) - 15.0, 10.0)) {
                        this.notor.setHologramUUID(null);
                    }
                    return;
                }
                this.notor.setHologramUUID(null);
            }
        } else {
            if (hologram != null) {
                for (int j = 0; (this.moveTarget == null || this.moveTarget.distanceTo(this.notor.m_20182_()) < 4.0) && j < 10; j++) {
                    this.moveTarget = DefaultRandomPos.getPosAway(this.notor, 40, 15, hologram.position());
                }
            }
            if (this.moveTarget != null && this.moveTarget.distanceTo(this.notor.m_20182_()) >= 4.0) {
                this.notor.m_21573_().moveTo(this.moveTarget.x, this.moveTarget.y, this.moveTarget.z, 1.2F);
            }
        }
    }

    private boolean hasNoTarget(Entity entity) {
        if (!(entity instanceof Mob living)) {
            return true;
        } else {
            LivingEntity target = living.getTarget();
            return target == null || !target.isAlive();
        }
    }

    public boolean isTargetBlocked(Mob mob, Vec3 target) {
        Vec3 Vector3d = new Vec3(mob.m_20185_(), mob.m_20188_(), mob.m_20189_());
        return mob.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob)).getType() != HitResult.Type.MISS;
    }
}