package snownee.loquat.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.loquat.core.AreaEvent;
import snownee.loquat.duck.LoquatMob;

@Mixin({ Mob.class })
public class MobMixin implements LoquatMob {

    @Unique
    private AreaEvent loquat$restriction;

    @Shadow
    private BlockPos restrictCenter;

    @Shadow
    private float restrictRadius;

    @Override
    public void loquat$setRestriction(AreaEvent event) {
        this.loquat$restriction = event;
    }

    @Inject(method = { "isWithinRestriction(Lnet/minecraft/core/BlockPos;)Z" }, at = { @At("HEAD") }, cancellable = true)
    private void loquat$isWithinRestriction(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (this.loquat$restriction != null) {
            info.setReturnValue(this.loquat$restriction.getArea().contains(pos));
        }
    }

    @Inject(method = { "hasRestriction" }, at = { @At("HEAD") }, cancellable = true)
    private void loquat$hasRestriction(CallbackInfoReturnable<Boolean> info) {
        if (this.loquat$restriction != null) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = { "clearRestriction" }, at = { @At("HEAD") })
    private void loquat$clearRestriction(CallbackInfo ci) {
        this.loquat$restriction = null;
    }

    @Inject(method = { "getRestrictCenter" }, at = { @At("HEAD") })
    private void loquat$getRestrictCenter(CallbackInfoReturnable<BlockPos> info) {
        if (this.loquat$restriction != null && this.restrictCenter == BlockPos.ZERO) {
            Vec3 center = this.loquat$restriction.getArea().getCenter();
            this.restrictCenter = new BlockPos((int) center.x, (int) center.y, (int) center.z);
        }
    }

    @Inject(method = { "getRestrictRadius" }, at = { @At("HEAD") })
    private void loquat$getRestrictRadius(CallbackInfoReturnable<Float> info) {
        if (this.loquat$restriction != null && this.restrictRadius == -1.0F) {
            AABB aabb = this.loquat$restriction.getArea().getRoughAABB();
            this.restrictRadius = (float) Math.max(aabb.getXsize(), aabb.getZsize()) / 2.0F;
        }
    }
}