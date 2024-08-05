package io.redspace.ironsspellbooks.entity.spells.portal;

import java.util.function.Function;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

public class PortalTeleporter implements ITeleporter {

    private final Vec3 destinationPosition;

    PortalTeleporter(Vec3 destinationPosition) {
        this.destinationPosition = destinationPosition;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        entity.fallDistance = 0.0F;
        return (Entity) repositionEntity.apply(false);
    }

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        return new PortalInfo(this.destinationPosition, Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }

    @Override
    public boolean isVanilla() {
        return false;
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }
}