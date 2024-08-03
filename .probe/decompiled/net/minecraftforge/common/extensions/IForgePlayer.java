package net.minecraftforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public interface IForgePlayer {

    private Player self() {
        return (Player) this;
    }

    default double getEntityReach() {
        double range = this.self().m_21133_(ForgeMod.ENTITY_REACH.get());
        return range == 0.0 ? 0.0 : range + (double) (this.self().isCreative() ? 3 : 0);
    }

    default double getBlockReach() {
        double reach = this.self().m_21133_(ForgeMod.BLOCK_REACH.get());
        return reach == 0.0 ? 0.0 : reach + (this.self().isCreative() ? 0.5 : 0.0);
    }

    default boolean canReach(Vec3 entityHitVec, double padding) {
        return this.self().m_146892_().closerThan(entityHitVec, this.getEntityReach() + padding);
    }

    default boolean canReach(Entity entity, double padding) {
        return this.isCloseEnough(entity, this.getEntityReach() + padding);
    }

    default boolean canReach(BlockPos pos, double padding) {
        double reach = this.getBlockReach() + padding;
        return this.self().m_146892_().distanceToSqr(Vec3.atCenterOf(pos)) < reach * reach;
    }

    default boolean isCloseEnough(Entity entity, double dist) {
        Vec3 eye = this.self().m_146892_();
        AABB aabb = entity.getBoundingBox().inflate((double) entity.getPickRadius());
        return aabb.distanceToSqr(eye) < dist * dist;
    }
}