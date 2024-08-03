package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public abstract class PatrollingMonster extends Monster {

    @Nullable
    private BlockPos patrolTarget;

    private boolean patrolLeader;

    private boolean patrolling;

    protected PatrollingMonster(EntityType<? extends PatrollingMonster> entityTypeExtendsPatrollingMonster0, Level level1) {
        super(entityTypeExtendsPatrollingMonster0, level1);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(4, new PatrollingMonster.LongDistancePatrolGoal<>(this, 0.7, 0.595));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        if (this.patrolTarget != null) {
            compoundTag0.put("PatrolTarget", NbtUtils.writeBlockPos(this.patrolTarget));
        }
        compoundTag0.putBoolean("PatrolLeader", this.patrolLeader);
        compoundTag0.putBoolean("Patrolling", this.patrolling);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        if (compoundTag0.contains("PatrolTarget")) {
            this.patrolTarget = NbtUtils.readBlockPos(compoundTag0.getCompound("PatrolTarget"));
        }
        this.patrolLeader = compoundTag0.getBoolean("PatrolLeader");
        this.patrolling = compoundTag0.getBoolean("Patrolling");
    }

    @Override
    public double getMyRidingOffset() {
        return -0.45;
    }

    public boolean canBeLeader() {
        return true;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        if (mobSpawnType2 != MobSpawnType.PATROL && mobSpawnType2 != MobSpawnType.EVENT && mobSpawnType2 != MobSpawnType.STRUCTURE && serverLevelAccessor0.m_213780_().nextFloat() < 0.06F && this.canBeLeader()) {
            this.patrolLeader = true;
        }
        if (this.isPatrolLeader()) {
            this.m_8061_(EquipmentSlot.HEAD, Raid.getLeaderBannerInstance());
            this.m_21409_(EquipmentSlot.HEAD, 2.0F);
        }
        if (mobSpawnType2 == MobSpawnType.PATROL) {
            this.patrolling = true;
        }
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    public static boolean checkPatrollingMonsterSpawnRules(EntityType<? extends PatrollingMonster> entityTypeExtendsPatrollingMonster0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return levelAccessor1.m_45517_(LightLayer.BLOCK, blockPos3) > 8 ? false : m_219019_(entityTypeExtendsPatrollingMonster0, levelAccessor1, mobSpawnType2, blockPos3, randomSource4);
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return !this.patrolling || double0 > 16384.0;
    }

    public void setPatrolTarget(BlockPos blockPos0) {
        this.patrolTarget = blockPos0;
        this.patrolling = true;
    }

    public BlockPos getPatrolTarget() {
        return this.patrolTarget;
    }

    public boolean hasPatrolTarget() {
        return this.patrolTarget != null;
    }

    public void setPatrolLeader(boolean boolean0) {
        this.patrolLeader = boolean0;
        this.patrolling = true;
    }

    public boolean isPatrolLeader() {
        return this.patrolLeader;
    }

    public boolean canJoinPatrol() {
        return true;
    }

    public void findPatrolTarget() {
        this.patrolTarget = this.m_20183_().offset(-500 + this.f_19796_.nextInt(1000), 0, -500 + this.f_19796_.nextInt(1000));
        this.patrolling = true;
    }

    protected boolean isPatrolling() {
        return this.patrolling;
    }

    protected void setPatrolling(boolean boolean0) {
        this.patrolling = boolean0;
    }

    public static class LongDistancePatrolGoal<T extends PatrollingMonster> extends Goal {

        private static final int NAVIGATION_FAILED_COOLDOWN = 200;

        private final T mob;

        private final double speedModifier;

        private final double leaderSpeedModifier;

        private long cooldownUntil;

        public LongDistancePatrolGoal(T t0, double double1, double double2) {
            this.mob = t0;
            this.speedModifier = double1;
            this.leaderSpeedModifier = double2;
            this.cooldownUntil = -1L;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            boolean $$0 = this.mob.m_9236_().getGameTime() < this.cooldownUntil;
            return this.mob.isPatrolling() && this.mob.m_5448_() == null && !this.mob.m_20160_() && this.mob.hasPatrolTarget() && !$$0;
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
        }

        @Override
        public void tick() {
            boolean $$0 = this.mob.isPatrolLeader();
            PathNavigation $$1 = this.mob.m_21573_();
            if ($$1.isDone()) {
                List<PatrollingMonster> $$2 = this.findPatrolCompanions();
                if (this.mob.isPatrolling() && $$2.isEmpty()) {
                    this.mob.setPatrolling(false);
                } else if ($$0 && this.mob.getPatrolTarget().m_203195_(this.mob.m_20182_(), 10.0)) {
                    this.mob.findPatrolTarget();
                } else {
                    Vec3 $$3 = Vec3.atBottomCenterOf(this.mob.getPatrolTarget());
                    Vec3 $$4 = this.mob.m_20182_();
                    Vec3 $$5 = $$4.subtract($$3);
                    $$3 = $$5.yRot(90.0F).scale(0.4).add($$3);
                    Vec3 $$6 = $$3.subtract($$4).normalize().scale(10.0).add($$4);
                    BlockPos $$7 = BlockPos.containing($$6);
                    $$7 = this.mob.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, $$7);
                    if (!$$1.moveTo((double) $$7.m_123341_(), (double) $$7.m_123342_(), (double) $$7.m_123343_(), $$0 ? this.leaderSpeedModifier : this.speedModifier)) {
                        this.moveRandomly();
                        this.cooldownUntil = this.mob.m_9236_().getGameTime() + 200L;
                    } else if ($$0) {
                        for (PatrollingMonster $$8 : $$2) {
                            $$8.setPatrolTarget($$7);
                        }
                    }
                }
            }
        }

        private List<PatrollingMonster> findPatrolCompanions() {
            return this.mob.m_9236_().m_6443_(PatrollingMonster.class, this.mob.m_20191_().inflate(16.0), p_264971_ -> p_264971_.canJoinPatrol() && !p_264971_.m_7306_(this.mob));
        }

        private boolean moveRandomly() {
            RandomSource $$0 = this.mob.m_217043_();
            BlockPos $$1 = this.mob.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.m_20183_().offset(-8 + $$0.nextInt(16), 0, -8 + $$0.nextInt(16)));
            return this.mob.m_21573_().moveTo((double) $$1.m_123341_(), (double) $$1.m_123342_(), (double) $$1.m_123343_(), this.speedModifier);
        }
    }
}