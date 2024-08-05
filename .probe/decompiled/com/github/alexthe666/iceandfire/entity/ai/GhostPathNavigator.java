package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;

public class GhostPathNavigator extends FlyingPathNavigation {

    public EntityGhost ghost;

    public GhostPathNavigator(EntityGhost entityIn, Level worldIn) {
        super(entityIn, worldIn);
        this.ghost = entityIn;
    }

    @Override
    public boolean moveTo(Entity entityIn, double speedIn) {
        this.ghost.m_21566_().setWantedPosition(entityIn.getX(), entityIn.getY(), entityIn.getZ(), speedIn);
        return true;
    }

    @Override
    public boolean moveTo(double x, double y, double z, double speedIn) {
        this.ghost.m_21566_().setWantedPosition(x, y, z, speedIn);
        return true;
    }
}