package com.mna.tools;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableObject;

public class TeleportHelper {

    private static void teleport(Entity entityIn, ServerLevel worldIn, double x, double y, double z, float yaw, float pitch) {
        if (entityIn instanceof ServerPlayer spe) {
            spe.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.delayedTeleportTo(40, new Vec3(x, y, z), new Vec2(yaw, pitch), worldIn));
        } else {
            float wrappedYaw = Mth.wrapDegrees(yaw);
            float wrappedPitch = Mth.clamp(Mth.wrapDegrees(pitch), -90.0F, 90.0F);
            if (worldIn == entityIn.level()) {
                entityIn.moveTo(x, y, z, wrappedYaw, wrappedPitch);
                entityIn.setYHeadRot(wrappedYaw);
            } else {
                entityIn.unRide();
                Entity entity = entityIn.getType().create(worldIn);
                if (entity == null) {
                    return;
                }
                entity.restoreFrom(entityIn);
                entity.moveTo(x, y, z, wrappedYaw, wrappedPitch);
                entity.setYHeadRot(wrappedYaw);
                worldIn.addDuringTeleport(entity);
                entityIn.remove(Entity.RemovalReason.CHANGED_DIMENSION);
            }
        }
        if (!(entityIn instanceof LivingEntity) || !((LivingEntity) entityIn).isFallFlying()) {
            entityIn.setDeltaMovement(entityIn.getDeltaMovement().multiply(1.0, 0.0, 1.0));
            entityIn.setOnGround(true);
        }
        if (entityIn instanceof PathfinderMob) {
            ((PathfinderMob) entityIn).m_21573_().stop();
        }
    }

    public static void performDelayedTeleport(ServerPlayer spe, Vec3 location, Vec2 rotation, ServerLevel destination, IPlayerMagic magic) {
        Set<RelativeMovement> set = EnumSet.noneOf(RelativeMovement.class);
        set.add(RelativeMovement.X_ROT);
        set.add(RelativeMovement.Y_ROT);
        ChunkPos chunkPos = new ChunkPos(BlockPos.containing(location));
        destination.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkPos, 1, spe.m_19879_());
        spe.stopRiding();
        if (spe.m_5803_()) {
            spe.stopSleepInBed(true, true);
        }
        if (destination == spe.m_9236_()) {
            spe.connection.teleport(location.x, location.y, location.z, rotation.x, rotation.y, set);
        } else {
            spe.teleportTo(destination, location.x, location.y, location.z, rotation.x, rotation.y);
        }
        spe.m_5616_(rotation.y);
    }

    public static void teleportEntity(Entity entity, ResourceKey<Level> destType, Vec3 destPos) {
        if (!entity.level().isClientSide()) {
            ServerLevel world = entity.getServer().getLevel(destType);
            if (world != null) {
                teleport(entity, world, destPos.x(), destPos.y(), destPos.z(), entity.getYRot(), entity.getXRot());
            } else if (entity instanceof Player) {
                ((Player) entity).m_213846_(Component.literal("Error looking up world key. Teleport failed."));
            }
        }
    }

    public static boolean randomTeleport(LivingEntity entity, float maxDist, int tries) {
        do {
            double rX = entity.m_20208_((double) maxDist);
            double rY = entity.m_20227_((double) maxDist);
            double rZ = entity.m_20262_((double) maxDist);
            BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos(rX, rY, rZ);
            while (targetPos.m_123342_() > 0 && !entity.m_9236_().getBlockState(targetPos).m_280555_()) {
                targetPos.move(Direction.DOWN);
            }
            BlockState blockstate = entity.m_9236_().getBlockState(targetPos);
            boolean blocksMotion = blockstate.m_280555_();
            boolean isLava = blockstate.m_60819_().is(FluidTags.LAVA);
            if (blocksMotion && !isLava) {
                boolean teleported = entity.randomTeleport(rX, rY, rZ, true);
                if (teleported) {
                    if (!entity.m_20067_()) {
                        entity.m_9236_().playSound((Player) null, entity.f_19854_, entity.f_19855_, entity.f_19856_, SoundEvents.ENDERMAN_TELEPORT, entity.m_5720_(), 1.0F, 1.0F);
                        entity.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                    return true;
                }
            }
        } while (--tries >= 0);
        return false;
    }

    @Nullable
    public static ServerLevel resolveRegistryKey(ServerLevel world, ResourceKey<Level> dimension) {
        return resolveRegistryKey(world, dimension.location());
    }

    @Nullable
    public static ServerLevel resolveRegistryKey(ServerLevel world, ResourceLocation location) {
        if (world.m_46472_().location().equals(location)) {
            return world;
        } else {
            MinecraftServer server = world.getServer();
            if (server == null) {
                return null;
            } else {
                MutableObject<ServerLevel> targetWorld = new MutableObject(null);
                server.getAllLevels().iterator().forEachRemaining(sw -> {
                    if (sw != null) {
                        if (sw.m_46472_().location().equals(location)) {
                            targetWorld.setValue(sw);
                        }
                    }
                });
                return (ServerLevel) targetWorld.getValue();
            }
        }
    }

    public static Vec3 calculateBlinkPosition(double distance, LivingEntity tgt, Vec3 direction, Level world) {
        direction = direction.normalize();
        Vec3 motion = direction.scale(distance);
        double newX = tgt.m_20185_() + motion.x;
        double newZ = tgt.m_20189_() + motion.z;
        double newY = tgt.m_20186_() + motion.y;
        if (newY < 1.0) {
            newY = 1.0;
        }
        boolean coordsValid = false;
        while (!coordsValid && distance > 0.0) {
            motion = direction.scale(distance);
            newX = tgt.m_20185_() + motion.x;
            newZ = tgt.m_20189_() + motion.z;
            newY = tgt.m_20186_() + motion.y;
            if (distance < 0.0) {
                coordsValid = false;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.floor(newX), (int) newY, (int) Math.floor(newZ))) {
                newX = Math.floor(newX) + 0.5;
                newZ = Math.floor(newZ) + 0.5;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.floor(newX), (int) newY, (int) Math.ceil(newZ))) {
                newX = Math.floor(newX) + 0.5;
                newZ = Math.ceil(newZ) + 0.5;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.ceil(newX), (int) newY, (int) Math.floor(newZ))) {
                newX = Math.ceil(newX) + 0.5;
                newZ = Math.floor(newZ) + 0.5;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.ceil(newX), (int) newY, (int) Math.ceil(newZ))) {
                newX = Math.ceil(newX) + 0.5;
                newZ = Math.ceil(newZ) + 0.5;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.floor(newX), (int) newY - 1, (int) Math.floor(newZ))) {
                newX = Math.floor(newX) + 0.5;
                newZ = Math.floor(newZ) + 0.5;
                newY--;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.floor(newX), (int) newY - 1, (int) Math.ceil(newZ))) {
                newX = Math.floor(newX) + 0.5;
                newZ = Math.ceil(newZ) + 0.5;
                newY--;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.ceil(newX), (int) newY - 1, (int) Math.floor(newZ))) {
                newX = Math.ceil(newX) + 0.5;
                newZ = Math.floor(newZ) + 0.5;
                newY--;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.ceil(newX), (int) newY - 1, (int) Math.ceil(newZ))) {
                newX = Math.ceil(newX) + 0.5;
                newZ = Math.ceil(newZ) + 0.5;
                newY--;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.floor(newX), (int) newY + 1, (int) Math.floor(newZ))) {
                newX = Math.floor(newX) + 0.5;
                newZ = Math.floor(newZ) + 0.5;
                newY++;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.floor(newX), (int) newY + 1, (int) Math.ceil(newZ))) {
                newX = Math.floor(newX) + 0.5;
                newZ = Math.ceil(newZ) + 0.5;
                newY++;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.ceil(newX), (int) newY + 1, (int) Math.floor(newZ))) {
                newX = Math.ceil(newX) + 0.5;
                newZ = Math.floor(newZ) + 0.5;
                newY++;
                coordsValid = true;
                break;
            }
            if (coordsValidForBlink(world, (int) Math.ceil(newX), (int) newY + 1, (int) Math.ceil(newZ))) {
                newX = Math.ceil(newX) + 0.5;
                newZ = Math.ceil(newZ) + 0.5;
                newY++;
                coordsValid = true;
                break;
            }
            motion = direction.scale(--distance);
            newX = tgt.m_20185_() + motion.x;
            newZ = tgt.m_20189_() + motion.z;
            newY = tgt.m_20186_() + motion.y;
        }
        return coordsValid ? new Vec3(newX, newY, newZ) : null;
    }

    public static boolean coordsValidForBlink(Level world, int x, int y, int z) {
        if (y < world.m_141937_()) {
            return false;
        } else {
            BlockPos pos = new BlockPos(x, y, z);
            if (!world.isLoaded(pos)) {
                return false;
            } else {
                BlockState state = world.getBlockState(pos);
                BlockState up = world.getBlockState(pos.above());
                return !state.m_60815_() && !up.m_60815_();
            }
        }
    }
}