package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.PathingStuckHandler;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class AdvancedPathNavigateNoTeleport extends AdvancedPathNavigate {

    public AdvancedPathNavigateNoTeleport(Mob entity, Level world, AdvancedPathNavigate.MovementType type) {
        super(entity, world, type, entity.m_20205_(), entity.m_20206_(), PathingStuckHandler.createStuckHandler());
    }

    public AdvancedPathNavigateNoTeleport(Mob entity, Level world) {
        this(entity, world, AdvancedPathNavigate.MovementType.WALKING);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }
}