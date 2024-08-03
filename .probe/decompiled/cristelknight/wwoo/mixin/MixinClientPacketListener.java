package cristelknight.wwoo.mixin;

import cristelknight.wwoo.ExpandedEcosphere;
import cristelknight.wwoo.config.configs.EEConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ClientPacketListener.class })
public abstract class MixinClientPacketListener {

    @Inject(method = { "handleLogin" }, at = { @At("RETURN") })
    private void showUpdateMessage(ClientboundLoginPacket arg, CallbackInfo ci) {
        if (Minecraft.getInstance().player != null) {
            EEConfig config = EEConfig.DEFAULT.getConfig();
            if (config.showUpdates() || config.showBigUpdates() && ExpandedEcosphere.getUpdater().isBig()) {
                ExpandedEcosphere.getUpdater().getUpdateMessage().ifPresent(msg -> Minecraft.getInstance().player.displayClientMessage(msg, false));
            }
        }
    }
}