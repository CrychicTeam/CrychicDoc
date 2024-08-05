package net.mehvahdjukaar.supplementaries.mixins;

import java.util.UUID;
import net.mehvahdjukaar.supplementaries.client.screens.PresentScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PlayerSocialManager.class })
public abstract class PlayerSocialManagerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = { "addPlayer" }, at = { @At("TAIL") })
    public void addPlayer(PlayerInfo info, CallbackInfo ci) {
        if (this.minecraft.screen instanceof PresentScreen gui) {
            gui.onAddPlayer(info);
        }
    }

    @Inject(method = { "removePlayer" }, at = { @At("TAIL") })
    public void removePlayer(UUID id, CallbackInfo ci) {
        if (this.minecraft.screen instanceof PresentScreen gui) {
            gui.onRemovePlayer(id);
        }
    }
}