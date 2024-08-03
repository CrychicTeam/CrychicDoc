package com.simibubi.create.content.contraptions.behaviour;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.infrastructure.config.AllConfigs;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

public interface MovementBehaviour {

    default boolean isActive(MovementContext context) {
        return !context.disabled;
    }

    default void tick(MovementContext context) {
    }

    default void startMoving(MovementContext context) {
    }

    default void visitNewPosition(MovementContext context, BlockPos pos) {
    }

    default Vec3 getActiveAreaOffset(MovementContext context) {
        return Vec3.ZERO;
    }

    @Nullable
    default ItemStack canBeDisabledVia(MovementContext context) {
        Block block = context.state.m_60734_();
        return block == null ? null : new ItemStack(block);
    }

    default void onDisabledByControls(MovementContext context) {
        this.cancelStall(context);
    }

    default boolean mustTickWhileDisabled() {
        return false;
    }

    default void dropItem(MovementContext context, ItemStack stack) {
        ItemStack remainder;
        if (AllConfigs.server().kinetics.moveItemsToStorage.get()) {
            remainder = ItemHandlerHelper.insertItem(context.contraption.getSharedInventory(), stack, false);
        } else {
            remainder = stack;
        }
        if (!remainder.isEmpty()) {
            Vec3 vec = context.position;
            if (vec != null) {
                ItemEntity itemEntity = new ItemEntity(context.world, vec.x, vec.y, vec.z, remainder);
                itemEntity.m_20256_(context.motion.add(0.0, 0.5, 0.0).scale((double) (context.world.random.nextFloat() * 0.3F)));
                context.world.m_7967_(itemEntity);
            }
        }
    }

    default void onSpeedChanged(MovementContext context, Vec3 oldMotion, Vec3 motion) {
    }

    default void stopMoving(MovementContext context) {
    }

    default void cancelStall(MovementContext context) {
        context.stall = false;
    }

    default void writeExtraData(MovementContext context) {
    }

    default boolean renderAsNormalBlockEntity() {
        return false;
    }

    default boolean hasSpecialInstancedRendering() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    default void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    default ActorInstance createInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        return null;
    }
}