package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.LevelReader;

public class CatLieOnBedGoal extends MoveToBlockGoal {

    private final Cat cat;

    public CatLieOnBedGoal(Cat cat0, double double1, int int2) {
        super(cat0, double1, int2, 6);
        this.cat = cat0;
        this.f_25603_ = -2;
        this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.cat.m_21824_() && !this.cat.m_21827_() && !this.cat.isLying() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.cat.m_21837_(false);
    }

    @Override
    protected int nextStartTick(PathfinderMob pathfinderMob0) {
        return 40;
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.cat.setLying(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.cat.m_21837_(false);
        if (!this.m_25625_()) {
            this.cat.setLying(false);
        } else if (!this.cat.isLying()) {
            this.cat.setLying(true);
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader0, BlockPos blockPos1) {
        return levelReader0.isEmptyBlock(blockPos1.above()) && levelReader0.m_8055_(blockPos1).m_204336_(BlockTags.BEDS);
    }
}