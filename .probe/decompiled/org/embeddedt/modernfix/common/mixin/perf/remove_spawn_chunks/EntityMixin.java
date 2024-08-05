package org.embeddedt.modernfix.common.mixin.perf.remove_spawn_chunks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ Entity.class })
public class EntityMixin {

    @ModifyExpressionValue(method = { "findDimensionEntryPoint" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getSharedSpawnPos()Lnet/minecraft/core/BlockPos;") }, require = 0)
    private BlockPos mfix$triggerChunkloadAtSpawnPos(BlockPos spawnPos, ServerLevel destination) {
        if (destination.m_46472_() == ServerLevel.f_46428_ && !destination.m_7232_(spawnPos.m_123341_() >> 4, spawnPos.m_123343_() >> 4)) {
            BlockPos key = spawnPos.immutable();
            destination.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(key), 3, key);
            destination.m_46865_(key);
        }
        return spawnPos;
    }
}