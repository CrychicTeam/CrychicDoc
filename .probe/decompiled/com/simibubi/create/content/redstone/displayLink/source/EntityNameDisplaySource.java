package com.simibubi.create.content.redstone.displayLink.source;

import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import java.util.List;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public class EntityNameDisplaySource extends SingleLineDisplaySource {

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        List<SeatEntity> seats = context.level().m_45976_(SeatEntity.class, new AABB(context.getSourcePos()));
        if (seats.isEmpty()) {
            return EMPTY_LINE;
        } else {
            SeatEntity seatEntity = (SeatEntity) seats.get(0);
            List<Entity> passengers = seatEntity.m_20197_();
            return passengers.isEmpty() ? EMPTY_LINE : ((Entity) passengers.get(0)).getDisplayName().copy();
        }
    }

    @Override
    protected String getTranslationKey() {
        return "entity_name";
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}