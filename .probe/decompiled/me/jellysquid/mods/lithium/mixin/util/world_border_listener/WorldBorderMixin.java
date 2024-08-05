package me.jellysquid.mods.lithium.mixin.util.world_border_listener;

import me.jellysquid.mods.lithium.common.world.listeners.WorldBorderListenerOnce;
import me.jellysquid.mods.lithium.common.world.listeners.WorldBorderListenerOnceMulti;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ WorldBorder.class })
public abstract class WorldBorderMixin {

    @Shadow
    private WorldBorder.BorderExtent extent;

    private final WorldBorderListenerOnceMulti worldBorderListenerOnceMulti = new WorldBorderListenerOnceMulti();

    @Shadow
    public abstract void addListener(BorderChangeListener var1);

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void registerSimpleWorldBorderListenerMulti(CallbackInfo ci) {
        this.addListener(this.worldBorderListenerOnceMulti);
    }

    @Inject(method = { "addListener" }, at = { @At("HEAD") }, cancellable = true)
    private void addSimpleListenerOnce(BorderChangeListener listener, CallbackInfo ci) {
        if (listener instanceof WorldBorderListenerOnce simpleListener) {
            ci.cancel();
            this.worldBorderListenerOnceMulti.add(simpleListener);
        }
    }

    @Redirect(method = { "tick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder$Area;getAreaInstance()Lnet/minecraft/world/border/WorldBorder$Area;"))
    public WorldBorder.BorderExtent getUpdatedArea(WorldBorder.BorderExtent instance) {
        WorldBorder.BorderExtent areaInstance = this.extent.update();
        if (areaInstance != this.extent) {
            this.extent = areaInstance;
            this.worldBorderListenerOnceMulti.onAreaReplaced((WorldBorder) this);
        }
        return areaInstance;
    }
}