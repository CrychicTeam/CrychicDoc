package noppes.npcs.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class EntityAIPanic extends Goal {

    private PathfinderMob entityCreature;

    private float speed;

    private double randPosX;

    private double randPosY;

    private double randPosZ;

    public EntityAIPanic(PathfinderMob par1Mob, float limbSwingAmount) {
        this.entityCreature = par1Mob;
        this.speed = limbSwingAmount;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.entityCreature.m_5448_() == null && !this.entityCreature.m_6060_()) {
            return false;
        } else {
            Vec3 var1 = DefaultRandomPos.getPos(this.entityCreature, 5, 4);
            if (var1 == null) {
                return false;
            } else {
                this.randPosX = var1.x;
                this.randPosY = var1.y;
                this.randPosZ = var1.z;
                return true;
            }
        }
    }

    @Override
    public void start() {
        this.entityCreature.m_21573_().moveTo(this.randPosX, this.randPosY, this.randPosZ, (double) this.speed);
    }

    @Override
    public boolean canContinueToUse() {
        return this.entityCreature.m_5448_() == null ? false : !this.entityCreature.m_21573_().isDone();
    }
}