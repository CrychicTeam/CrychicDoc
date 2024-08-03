package me.jellysquid.mods.sodium.mixin.core.gui;

import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ReceivingLevelScreen.class })
public class DownloadingTerrainScreenMixin {

    @Redirect(method = { "tick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;blockPosition()Lnet/minecraft/core/BlockPos;"), require = 0)
    private BlockPos redirect$getPlayerBlockPosition(LocalPlayer instance) {
        return BlockPos.containing(instance.m_20185_(), instance.m_20188_(), instance.m_20189_());
    }
}