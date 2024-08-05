package com.mna.events.delayed;

import com.mna.api.timing.IDelayedEvent;
import com.mna.tools.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

public class DelayedDimensionTeleportEvent implements IDelayedEvent {

    final LivingEntity target;

    final ServerLevel origin;

    final ServerLevel destination;

    public DelayedDimensionTeleportEvent(LivingEntity entity, ServerLevel origin, ServerLevel destination) {
        this.origin = origin;
        this.destination = destination;
        this.target = entity;
    }

    @Override
    public boolean tick() {
        this.teleportOnSchedule();
        return true;
    }

    @Override
    public String getID() {
        return "";
    }

    private void teleportOnSchedule() {
        Vec3 targetPosition = this.calculateDimensionDifferencePosition(this.target.m_20182_(), this.origin, this.destination);
        TeleportHelper.teleportEntity(this.target, this.destination.m_46472_(), targetPosition);
    }

    protected Vec3 calculateDimensionDifferencePosition(Vec3 origin, ServerLevel from, ServerLevel to) {
        WorldBorder worldborder = to.m_6857_();
        double minX = Math.max(-2.9999872E7, worldborder.getMinX() + 16.0);
        double minZ = Math.max(-2.9999872E7, worldborder.getMinZ() + 16.0);
        double maxX = Math.min(2.9999872E7, worldborder.getMaxX() - 16.0);
        double maxZ = Math.min(2.9999872E7, worldborder.getMaxZ() - 16.0);
        double scale = DimensionType.getTeleportationScale(from.m_6042_(), to.m_6042_());
        BlockPos scaledPos = BlockPos.containing(Mth.clamp(origin.x * scale, minX, maxX), Mth.clamp(origin.y, 5.0, (double) (to.m_151558_() - 2)), Mth.clamp(origin.z * scale, minZ, maxZ));
        BlockPos searchPos = new BlockPos(scaledPos);
        int MIN_Y = 4;
        if (searchPos.m_123342_() >= to.m_6042_().logicalHeight()) {
            int delta = to.m_6042_().logicalHeight() - 3 - searchPos.m_123342_();
            searchPos = searchPos.offset(0, delta, 0);
        }
        while (searchPos.m_123342_() > MIN_Y && !to.m_46859_(searchPos) && !to.m_46859_(searchPos.above())) {
            searchPos = searchPos.below();
        }
        while (searchPos.m_123342_() > MIN_Y && to.m_46859_(searchPos.below())) {
            searchPos = searchPos.below();
        }
        while (searchPos.m_123342_() < to.m_141928_() - 3 && !to.m_46859_(searchPos) && !to.m_46859_(searchPos.above())) {
            searchPos = searchPos.above();
        }
        if (!to.m_46859_(searchPos) && !to.m_46859_(searchPos.above())) {
            searchPos.offset(0, to.m_141928_() - searchPos.m_123342_(), 0);
        }
        return new Vec3((double) searchPos.m_123341_() + 0.5, (double) searchPos.m_123342_(), (double) searchPos.m_123343_() + 0.5);
    }
}