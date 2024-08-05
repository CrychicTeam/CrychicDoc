package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityAIMoveIndoors extends Goal {

    private PathfinderMob theCreature;

    private double shelterX;

    private double shelterY;

    private double shelterZ;

    private Level level;

    public EntityAIMoveIndoors(PathfinderMob par1Mob) {
        this.theCreature = par1Mob;
        this.level = par1Mob.m_9236_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if ((!this.theCreature.m_9236_().isDay() || this.theCreature.m_9236_().isRaining()) && !this.theCreature.m_9236_().dimensionType().hasSkyLight()) {
            BlockPos pos = new BlockPos((int) this.theCreature.m_20185_(), (int) this.theCreature.m_20191_().minY, (int) this.theCreature.m_20189_());
            if (!this.level.m_45527_(pos) && this.level.m_7146_(pos) > 8) {
                return false;
            } else {
                Vec3 var1 = this.findPossibleShelter();
                if (var1 == null) {
                    return false;
                } else {
                    this.shelterX = var1.x;
                    this.shelterY = var1.y;
                    this.shelterZ = var1.z;
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.theCreature.m_21573_().isDone();
    }

    @Override
    public void start() {
        this.theCreature.m_21573_().moveTo(this.shelterX, this.shelterY, this.shelterZ, 1.0);
    }

    private Vec3 findPossibleShelter() {
        RandomSource random = this.theCreature.m_217043_();
        BlockPos blockpos = new BlockPos((int) this.theCreature.m_20185_(), (int) this.theCreature.m_20191_().minY, (int) this.theCreature.m_20189_());
        for (int i = 0; i < 10; i++) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (!this.level.m_45527_(blockpos1) && this.theCreature.getWalkTargetValue(blockpos1) < 0.0F) {
                return new Vec3((double) blockpos1.m_123341_(), (double) blockpos1.m_123342_(), (double) blockpos1.m_123343_());
            }
        }
        return null;
    }
}