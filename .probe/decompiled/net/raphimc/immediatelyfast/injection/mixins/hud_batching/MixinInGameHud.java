package net.raphimc.immediatelyfast.injection.mixins.hud_batching;

import net.minecraft.client.gui.Gui;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import net.raphimc.immediatelyfast.injection.processors.InjectAboveEverything;
import net.raphimc.immediatelyfast.injection.processors.InjectOnAllReturns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { Gui.class }, priority = 1500)
public abstract class MixinInGameHud {

    @InjectAboveEverything
    @Inject(method = { "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", "renderCrosshair", "renderStatusEffectOverlay", "renderHotbar", "renderMountJumpBar", "renderExperienceBar", "renderHeldItemTooltip", "renderStatusBars", "renderMountHealth" }, at = { @At("HEAD") })
    private void beginBatching(CallbackInfo ci) {
        if (ImmediatelyFast.runtimeConfig.hud_batching) {
            BatchingBuffers.beginHudBatching();
        }
    }

    @InjectOnAllReturns
    @Inject(method = { "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", "renderCrosshair", "renderStatusEffectOverlay", "renderHotbar", "renderMountJumpBar", "renderExperienceBar", "renderHeldItemTooltip", "renderStatusBars", "renderMountHealth" }, at = { @At("RETURN") })
    private void endBatching(CallbackInfo ci) {
        if (ImmediatelyFast.runtimeConfig.hud_batching) {
            BatchingBuffers.endHudBatching();
        }
    }
}