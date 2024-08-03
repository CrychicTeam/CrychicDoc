package snownee.lychee.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.lychee.RecipeTypes;

@Mixin({ ClientPacketListener.class })
public class ClientPacketListenerMixin {

    @Inject(at = { @At("TAIL") }, method = { "handleUpdateTags" })
    private void lychee_handleUpdateTags(ClientboundUpdateTagsPacket packet, CallbackInfo ci) {
        RecipeTypes.buildCache();
    }
}