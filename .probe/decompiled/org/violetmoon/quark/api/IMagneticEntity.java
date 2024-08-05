package org.violetmoon.quark.api;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public interface IMagneticEntity {

    default void moveByMagnet(Entity self, Vec3 moveDirection, BlockEntity tile) {
        self.push(moveDirection.x(), moveDirection.y(), moveDirection.z());
    }
}