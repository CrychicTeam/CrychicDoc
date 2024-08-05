package net.minecraftforge.common.util;

import java.util.function.Function;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalForcer;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface ITeleporter {

    default Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        return (Entity) repositionEntity.apply(true);
    }

    @Nullable
    default PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        return this.isVanilla() ? (PortalInfo) defaultPortalInfo.apply(destWorld) : new PortalInfo(entity.position(), Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }

    default boolean isVanilla() {
        return this.getClass() == PortalForcer.class;
    }

    default boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return true;
    }
}