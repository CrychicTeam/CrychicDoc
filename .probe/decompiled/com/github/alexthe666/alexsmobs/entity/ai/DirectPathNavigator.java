package com.github.alexthe666.alexsmobs.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

public class DirectPathNavigator extends GroundPathNavigation {

    private final Mob mob;

    private float yMobOffset = 0.0F;

    public DirectPathNavigator(Mob mob, Level world) {
        this(mob, world, 0.0F);
    }

    public DirectPathNavigator(Mob mob, Level world, float yMobOffset) {
        super(mob, world);
        this.mob = mob;
        this.yMobOffset = yMobOffset;
    }

    @Override
    public void tick() {
        this.f_26498_++;
    }

    @Override
    public boolean moveTo(double x, double y, double z, double speedIn) {
        this.mob.getMoveControl().setWantedPosition(x, y, z, speedIn);
        return true;
    }

    @Override
    public boolean moveTo(Entity entityIn, double speedIn) {
        this.mob.getMoveControl().setWantedPosition(entityIn.getX(), entityIn.getY() + (double) this.yMobOffset, entityIn.getZ(), speedIn);
        return true;
    }
}