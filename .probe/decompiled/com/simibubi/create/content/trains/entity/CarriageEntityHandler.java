package com.simibubi.create.content.trains.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityEvent;

public class CarriageEntityHandler {

    public static void onEntityEnterSection(EntityEvent.EnteringSection event) {
        if (event.didChunkChange()) {
            Entity entity = event.getEntity();
            if (entity instanceof CarriageContraptionEntity cce) {
                SectionPos newPos = event.getNewPos();
                Level level = entity.level();
                if (!level.isClientSide) {
                    if (!isActiveChunk(level, newPos.center())) {
                        cce.leftTickingChunks = true;
                    }
                }
            }
        }
    }

    public static void validateCarriageEntity(CarriageContraptionEntity entity) {
        if (entity.m_6084_()) {
            Level level = entity.m_9236_();
            if (!level.isClientSide) {
                if (!isActiveChunk(level, entity.m_20183_())) {
                    entity.leftTickingChunks = true;
                }
            }
        }
    }

    public static boolean isActiveChunk(Level level, BlockPos pos) {
        return level instanceof ServerLevel serverLevel ? serverLevel.isPositionEntityTicking(pos) : false;
    }
}