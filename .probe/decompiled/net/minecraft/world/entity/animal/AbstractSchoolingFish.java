package net.minecraft.world.entity.animal;

import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public abstract class AbstractSchoolingFish extends AbstractFish {

    @Nullable
    private AbstractSchoolingFish leader;

    private int schoolSize = 1;

    public AbstractSchoolingFish(EntityType<? extends AbstractSchoolingFish> entityTypeExtendsAbstractSchoolingFish0, Level level1) {
        super(entityTypeExtendsAbstractSchoolingFish0, level1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(5, new FollowFlockLeaderGoal(this));
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return this.getMaxSchoolSize();
    }

    public int getMaxSchoolSize() {
        return super.getMaxSpawnClusterSize();
    }

    @Override
    protected boolean canRandomSwim() {
        return !this.isFollower();
    }

    public boolean isFollower() {
        return this.leader != null && this.leader.m_6084_();
    }

    public AbstractSchoolingFish startFollowing(AbstractSchoolingFish abstractSchoolingFish0) {
        this.leader = abstractSchoolingFish0;
        abstractSchoolingFish0.addFollower();
        return abstractSchoolingFish0;
    }

    public void stopFollowing() {
        this.leader.removeFollower();
        this.leader = null;
    }

    private void addFollower() {
        this.schoolSize++;
    }

    private void removeFollower() {
        this.schoolSize--;
    }

    public boolean canBeFollowed() {
        return this.hasFollowers() && this.schoolSize < this.getMaxSchoolSize();
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.hasFollowers() && this.m_9236_().random.nextInt(200) == 1) {
            List<? extends AbstractFish> $$0 = this.m_9236_().m_45976_(this.getClass(), this.m_20191_().inflate(8.0, 8.0, 8.0));
            if ($$0.size() <= 1) {
                this.schoolSize = 1;
            }
        }
    }

    public boolean hasFollowers() {
        return this.schoolSize > 1;
    }

    public boolean inRangeOfLeader() {
        return this.m_20280_(this.leader) <= 121.0;
    }

    public void pathToLeader() {
        if (this.isFollower()) {
            this.m_21573_().moveTo(this.leader, 1.0);
        }
    }

    public void addFollowers(Stream<? extends AbstractSchoolingFish> streamExtendsAbstractSchoolingFish0) {
        streamExtendsAbstractSchoolingFish0.limit((long) (this.getMaxSchoolSize() - this.schoolSize)).filter(p_27538_ -> p_27538_ != this).forEach(p_27536_ -> p_27536_.startFollowing(this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
        if (spawnGroupData3 == null) {
            spawnGroupData3 = new AbstractSchoolingFish.SchoolSpawnGroupData(this);
        } else {
            this.startFollowing(((AbstractSchoolingFish.SchoolSpawnGroupData) spawnGroupData3).leader);
        }
        return spawnGroupData3;
    }

    public static class SchoolSpawnGroupData implements SpawnGroupData {

        public final AbstractSchoolingFish leader;

        public SchoolSpawnGroupData(AbstractSchoolingFish abstractSchoolingFish0) {
            this.leader = abstractSchoolingFish0;
        }
    }
}