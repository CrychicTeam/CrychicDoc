package net.minecraft.world.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public abstract class WaterAnimal extends PathfinderMob {

    protected WaterAnimal(EntityType<? extends WaterAnimal> entityTypeExtendsWaterAnimal0, Level level1) {
        super(entityTypeExtendsWaterAnimal0, level1);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        return levelReader0.m_45784_(this);
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Override
    public int getExperienceReward() {
        return 1 + this.m_9236_().random.nextInt(3);
    }

    protected void handleAirSupply(int int0) {
        if (this.m_6084_() && !this.m_20072_()) {
            this.m_20301_(int0 - 1);
            if (this.m_20146_() == -20) {
                this.m_20301_(0);
                this.m_6469_(this.m_269291_().drown(), 2.0F);
            }
        } else {
            this.m_20301_(300);
        }
    }

    @Override
    public void baseTick() {
        int $$0 = this.m_20146_();
        super.m_6075_();
        this.handleAirSupply($$0);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player0) {
        return false;
    }

    public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> entityTypeExtendsWaterAnimal0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        int $$5 = levelAccessor1.m_5736_();
        int $$6 = $$5 - 13;
        return blockPos3.m_123342_() >= $$6 && blockPos3.m_123342_() <= $$5 && levelAccessor1.m_6425_(blockPos3.below()).is(FluidTags.WATER) && levelAccessor1.m_8055_(blockPos3.above()).m_60713_(Blocks.WATER);
    }
}