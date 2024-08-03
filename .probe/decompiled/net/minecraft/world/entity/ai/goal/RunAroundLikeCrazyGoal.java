package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class RunAroundLikeCrazyGoal extends Goal {

    private final AbstractHorse horse;

    private final double speedModifier;

    private double posX;

    private double posY;

    private double posZ;

    public RunAroundLikeCrazyGoal(AbstractHorse abstractHorse0, double double1) {
        this.horse = abstractHorse0;
        this.speedModifier = double1;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.horse.isTamed() && this.horse.m_20160_()) {
            Vec3 $$0 = DefaultRandomPos.getPos(this.horse, 5, 4);
            if ($$0 == null) {
                return false;
            } else {
                this.posX = $$0.x;
                this.posY = $$0.y;
                this.posZ = $$0.z;
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.horse.m_21573_().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.horse.isTamed() && !this.horse.m_21573_().isDone() && this.horse.m_20160_();
    }

    @Override
    public void tick() {
        if (!this.horse.isTamed() && this.horse.m_217043_().nextInt(this.m_183277_(50)) == 0) {
            Entity $$0 = (Entity) this.horse.m_20197_().get(0);
            if ($$0 == null) {
                return;
            }
            if ($$0 instanceof Player) {
                int $$1 = this.horse.getTemper();
                int $$2 = this.horse.getMaxTemper();
                if ($$2 > 0 && this.horse.m_217043_().nextInt($$2) < $$1) {
                    this.horse.tameWithName((Player) $$0);
                    return;
                }
                this.horse.modifyTemper(5);
            }
            this.horse.m_20153_();
            this.horse.makeMad();
            this.horse.m_9236_().broadcastEntityEvent(this.horse, (byte) 6);
        }
    }
}