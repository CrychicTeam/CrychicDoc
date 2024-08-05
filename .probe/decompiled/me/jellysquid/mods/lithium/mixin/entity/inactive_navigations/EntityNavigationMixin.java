package me.jellysquid.mods.lithium.mixin.entity.inactive_navigations;

import me.jellysquid.mods.lithium.common.entity.NavigatingEntity;
import me.jellysquid.mods.lithium.common.world.ServerWorldExtended;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ PathNavigation.class })
public abstract class EntityNavigationMixin {

    @Shadow
    @Final
    protected Level level;

    @Shadow
    protected Path path;

    @Shadow
    @Final
    protected Mob mob;

    @Inject(method = { "recalculatePath()V" }, at = { @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/ai/pathing/EntityNavigation;findPathTo(Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/entity/ai/pathing/Path;", shift = Shift.AFTER) })
    private void updateListeningState(CallbackInfo ci) {
        if (((NavigatingEntity) this.mob).isRegisteredToWorld()) {
            if (this.path == null) {
                ((ServerWorldExtended) this.level).setNavigationInactive(this.mob);
            } else {
                ((ServerWorldExtended) this.level).setNavigationActive(this.mob);
            }
        }
    }

    @Inject(method = { "startMovingAlong(Lnet/minecraft/entity/ai/pathing/Path;D)Z" }, at = { @At("RETURN") })
    private void updateListeningState2(Path path, double speed, CallbackInfoReturnable<Boolean> cir) {
        if (((NavigatingEntity) this.mob).isRegisteredToWorld()) {
            if (this.path == null) {
                ((ServerWorldExtended) this.level).setNavigationInactive(this.mob);
            } else {
                ((ServerWorldExtended) this.level).setNavigationActive(this.mob);
            }
        }
    }

    @Inject(method = { "stop()V" }, at = { @At("RETURN") })
    private void stopListening(CallbackInfo ci) {
        if (((NavigatingEntity) this.mob).isRegisteredToWorld()) {
            ((ServerWorldExtended) this.level).setNavigationInactive(this.mob);
        }
    }
}