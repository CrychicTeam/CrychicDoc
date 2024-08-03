package com.mna.tools;

import com.mna.api.IPortalHelper;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.Portal;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PortalHelper implements IPortalHelper {

    @Override
    public void openPortal(ServerLevel world, Vec3 position, DyeColor color, BlockPos teleportTarget, ResourceKey<Level> teleportDimension, boolean isPermanent) {
        Portal portal = new Portal(EntityInit.PORTAL_ENTITY.get(), world);
        portal.m_6034_(position.x, position.y, position.z);
        portal.setDyeColor(color);
        portal.setTeleportBlockPos(teleportTarget, teleportDimension);
        if (isPermanent) {
            portal.setPermanent();
        }
        world.addFreshEntity(portal);
    }
}