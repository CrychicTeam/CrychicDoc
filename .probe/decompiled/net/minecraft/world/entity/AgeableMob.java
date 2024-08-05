package net.minecraft.world.entity;

import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public abstract class AgeableMob extends PathfinderMob {

    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.BOOLEAN);

    public static final int BABY_START_AGE = -24000;

    private static final int FORCED_AGE_PARTICLE_TICKS = 40;

    protected int age;

    protected int forcedAge;

    protected int forcedAgeTimer;

    protected AgeableMob(EntityType<? extends AgeableMob> entityTypeExtendsAgeableMob0, Level level1) {
        super(entityTypeExtendsAgeableMob0, level1);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        if (spawnGroupData3 == null) {
            spawnGroupData3 = new AgeableMob.AgeableMobGroupData(true);
        }
        AgeableMob.AgeableMobGroupData $$5 = (AgeableMob.AgeableMobGroupData) spawnGroupData3;
        if ($$5.isShouldSpawnBaby() && $$5.getGroupSize() > 0 && serverLevelAccessor0.m_213780_().nextFloat() <= $$5.getBabySpawnChance()) {
            this.setAge(-24000);
        }
        $$5.increaseGroupSizeByOne();
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Nullable
    public abstract AgeableMob getBreedOffspring(ServerLevel var1, AgeableMob var2);

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_BABY_ID, false);
    }

    public boolean canBreed() {
        return false;
    }

    public int getAge() {
        if (this.m_9236_().isClientSide) {
            return this.f_19804_.get(DATA_BABY_ID) ? -1 : 1;
        } else {
            return this.age;
        }
    }

    public void ageUp(int int0, boolean boolean1) {
        int $$2 = this.getAge();
        $$2 += int0 * 20;
        if ($$2 > 0) {
            $$2 = 0;
        }
        int $$4 = $$2 - $$2;
        this.setAge($$2);
        if (boolean1) {
            this.forcedAge += $$4;
            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = 40;
            }
        }
        if (this.getAge() == 0) {
            this.setAge(this.forcedAge);
        }
    }

    public void ageUp(int int0) {
        this.ageUp(int0, false);
    }

    public void setAge(int int0) {
        int $$1 = this.getAge();
        this.age = int0;
        if ($$1 < 0 && int0 >= 0 || $$1 >= 0 && int0 < 0) {
            this.f_19804_.set(DATA_BABY_ID, int0 < 0);
            this.ageBoundaryReached();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putInt("Age", this.getAge());
        compoundTag0.putInt("ForcedAge", this.forcedAge);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.setAge(compoundTag0.getInt("Age"));
        this.forcedAge = compoundTag0.getInt("ForcedAge");
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (DATA_BABY_ID.equals(entityDataAccessor0)) {
            this.m_6210_();
        }
        super.m_7350_(entityDataAccessor0);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.m_9236_().isClientSide) {
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.m_9236_().addParticle(ParticleTypes.HAPPY_VILLAGER, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), 0.0, 0.0, 0.0);
                }
                this.forcedAgeTimer--;
            }
        } else if (this.m_6084_()) {
            int $$0 = this.getAge();
            if ($$0 < 0) {
                this.setAge(++$$0);
            } else if ($$0 > 0) {
                this.setAge(--$$0);
            }
        }
    }

    protected void ageBoundaryReached() {
        if (!this.isBaby() && this.m_20159_() && this.m_20202_() instanceof Boat $$0 && !$$0.hasEnoughSpaceFor(this)) {
            this.m_8127_();
        }
    }

    @Override
    public boolean isBaby() {
        return this.getAge() < 0;
    }

    @Override
    public void setBaby(boolean boolean0) {
        this.setAge(boolean0 ? -24000 : 0);
    }

    public static int getSpeedUpSecondsWhenFeeding(int int0) {
        return (int) ((float) (int0 / 20) * 0.1F);
    }

    public static class AgeableMobGroupData implements SpawnGroupData {

        private int groupSize;

        private final boolean shouldSpawnBaby;

        private final float babySpawnChance;

        private AgeableMobGroupData(boolean boolean0, float float1) {
            this.shouldSpawnBaby = boolean0;
            this.babySpawnChance = float1;
        }

        public AgeableMobGroupData(boolean boolean0) {
            this(boolean0, 0.05F);
        }

        public AgeableMobGroupData(float float0) {
            this(true, float0);
        }

        public int getGroupSize() {
            return this.groupSize;
        }

        public void increaseGroupSizeByOne() {
            this.groupSize++;
        }

        public boolean isShouldSpawnBaby() {
            return this.shouldSpawnBaby;
        }

        public float getBabySpawnChance() {
            return this.babySpawnChance;
        }
    }
}