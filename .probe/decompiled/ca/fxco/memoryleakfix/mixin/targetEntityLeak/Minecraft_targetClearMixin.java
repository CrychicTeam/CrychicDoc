package ca.fxco.memoryleakfix.mixin.targetEntityLeak;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin({ Minecraft.class })
public abstract class Minecraft_targetClearMixin {

    @Shadow
    @Nullable
    public Entity crosshairPickEntity;

    @Shadow
    @Nullable
    public HitResult hitResult;

    @Inject(method = { "updateScreenAndTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;runTick(Z)V", shift = Shift.BEFORE) })
    private void memoryLeakFix$resetTarget(CallbackInfo ci) {
        this.crosshairPickEntity = null;
        this.hitResult = null;
    }
}