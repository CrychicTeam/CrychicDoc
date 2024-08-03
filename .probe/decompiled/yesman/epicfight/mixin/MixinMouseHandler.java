package yesman.epicfight.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.SmoothDouble;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.RenderEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin({ MouseHandler.class })
public abstract class MixinMouseHandler {

    @Shadow
    private Minecraft minecraft;

    @Final
    @Shadow
    private SmoothDouble smoothTurnX;

    @Final
    @Shadow
    private SmoothDouble smoothTurnY;

    @Shadow
    private double accumulatedDX;

    @Shadow
    private double accumulatedDY;

    @Shadow
    private double lastMouseEventTime;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), method = { "turnPlayer()V" })
    private void epicfight_turnPlayer(LocalPlayer entity, double d2, double d3) {
        LocalPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(entity, LocalPlayerPatch.class);
        RenderEngine renderEngine = ClientEngine.getInstance().renderEngine;
        if (playerpatch == null) {
            entity.m_19884_(d2, d3);
        } else if (playerpatch.getEntityState().turningLocked() && entity.jumpableVehicle() == null) {
            renderEngine.rotateCameraByMouseInput((float) d3, (float) d2);
        } else if (!playerpatch.isTargetLockedOn()) {
            entity.m_19884_(d2, d3);
        }
    }
}