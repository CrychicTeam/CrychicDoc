package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.util.IGroundMount;
import java.util.EnumSet;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EntityGroundAIRide<T extends Mob & IGroundMount> extends Goal {

    private final T dragon;

    private Player player;

    public EntityGroundAIRide(T dragon) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.dragon = dragon;
    }

    @Override
    public boolean canUse() {
        this.player = this.dragon.getRidingPlayer();
        return this.player != null;
    }

    @Override
    public void start() {
        this.dragon.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.dragon.getNavigation().stop();
        this.dragon.setTarget(null);
        double x = this.dragon.m_20185_();
        double y = this.dragon.m_20186_();
        if (this.dragon instanceof EntityDeathWorm) {
            y = ((EntityDeathWorm) this.dragon).processRiderY(y);
        }
        double z = this.dragon.m_20189_();
        double speed = 1.8F * this.dragon.getRideSpeedModifier();
        if (this.player.f_20900_ != 0.0F || this.player.f_20902_ != 0.0F) {
            Vec3 lookVec = this.player.m_20154_();
            if (this.player.f_20902_ < 0.0F) {
                lookVec = lookVec.yRot((float) Math.PI);
            } else if (this.player.f_20900_ > 0.0F) {
                lookVec = lookVec.yRot((float) (Math.PI / 2));
            } else if (this.player.f_20900_ < 0.0F) {
                lookVec = lookVec.yRot((float) (-Math.PI / 2));
            }
            if ((double) Math.abs(this.player.f_20900_) > 0.0) {
                speed *= 0.25;
            }
            if ((double) this.player.f_20902_ < 0.0) {
                speed *= 0.15;
            }
            x += lookVec.x * 10.0;
            z += lookVec.z * 10.0;
        }
        this.dragon.getMoveControl().setWantedPosition(x, y, z, speed);
    }
}