package me.jellysquid.mods.lithium.mixin.block.hopper;

import me.jellysquid.mods.lithium.common.entity.movement_tracker.ToggleableMovementTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ AbstractMinecart.class })
public abstract class AbstractMinecartEntityMixin extends Entity {

    private Vec3 beforeMoveOnRailPos;

    private int beforeMoveOnRailNotificationMask;

    public AbstractMinecartEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = { "moveOnRail" }, at = { @At("HEAD") })
    private void avoidNotifyingMovementListeners(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (this instanceof Container) {
            this.beforeMoveOnRailPos = this.m_20182_();
            if (((EntityAccessor) this).getChangeListener() instanceof ToggleableMovementTracker toggleableMovementTracker) {
                this.beforeMoveOnRailNotificationMask = toggleableMovementTracker.setNotificationMask(0);
            }
        }
    }

    @Inject(method = { "moveOnRail" }, at = { @At("RETURN") })
    private void notifyMovementListeners(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (this instanceof Container) {
            EntityInLevelCallback changeListener = ((EntityAccessor) this).getChangeListener();
            if (changeListener instanceof ToggleableMovementTracker toggleableMovementTracker) {
                this.beforeMoveOnRailNotificationMask = toggleableMovementTracker.setNotificationMask(this.beforeMoveOnRailNotificationMask);
                if (!this.beforeMoveOnRailPos.equals(this.m_20182_())) {
                    changeListener.onMove();
                }
            }
            this.beforeMoveOnRailPos = null;
        }
    }
}