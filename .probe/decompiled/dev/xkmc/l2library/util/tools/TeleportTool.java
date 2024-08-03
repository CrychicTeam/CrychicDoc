package dev.xkmc.l2library.util.tools;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

public class TeleportTool {

    public static void teleportHome(ServerLevel world, ServerPlayer player) {
        ServerLevel targetWorld = world.getServer().getLevel(player.getRespawnDimension());
        BlockPos blockpos = player.getRespawnPosition();
        float f = player.getRespawnAngle();
        boolean flag = player.isRespawnForced();
        Optional<Vec3> optional;
        if (targetWorld != null && blockpos != null) {
            optional = Player.findRespawnPositionAndUseSpawnBlock(targetWorld, blockpos, f, flag, true);
        } else {
            optional = Optional.empty();
        }
        Vec3 pos;
        if (targetWorld != null && !optional.isEmpty()) {
            pos = (Vec3) optional.get();
        } else {
            targetWorld = world.getServer().overworld();
            BlockPos bpos = targetWorld.m_220360_();
            pos = new Vec3((double) bpos.m_123341_() + 0.5, (double) (bpos.m_123342_() + 1), (double) bpos.m_123343_() + 0.5);
        }
        if (world == targetWorld) {
            player.teleportTo(pos.x, pos.y, pos.z);
        } else {
            performTeleport(player, targetWorld, pos.x, pos.y, pos.z, player.getRespawnAngle(), player.m_6080_());
        }
    }

    public static void performTeleport(Entity entity, ServerLevel level, double x, double y, double z, float xrot, float yrot) {
        Set<RelativeMovement> set = EnumSet.noneOf(RelativeMovement.class);
        float f = Mth.wrapDegrees(xrot);
        float f1 = Mth.wrapDegrees(yrot);
        if (entity instanceof ServerPlayer player) {
            ChunkPos chunkpos = new ChunkPos(BlockPos.containing(x, y, z));
            level.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 1, player.m_19879_());
            player.stopRiding();
            if (player.m_5803_()) {
                player.stopSleepInBed(true, true);
            }
            if (level == player.m_9236_()) {
                player.connection.teleport(x, y, z, f, f1, set);
            } else {
                player.teleportTo(level, x, y, z, f, f1);
            }
            player.m_5616_(f);
        } else {
            float f2 = Mth.clamp(f1, -90.0F, 90.0F);
            if (level == entity.level()) {
                entity.moveTo(x, y, z, f, f2);
                entity.setYHeadRot(f);
            } else {
                entity.unRide();
                Entity newEntity = entity.getType().create(level);
                if (newEntity == null) {
                    return;
                }
                newEntity.restoreFrom(entity);
                newEntity.moveTo(x, y, z, f, f2);
                newEntity.setYHeadRot(f);
                entity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
                level.addDuringTeleport(newEntity);
            }
        }
        if (!(entity instanceof LivingEntity) || !((LivingEntity) entity).isFallFlying()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
            entity.setOnGround(true);
        }
        if (entity instanceof PathfinderMob) {
            ((PathfinderMob) entity).m_21573_().stop();
        }
    }
}