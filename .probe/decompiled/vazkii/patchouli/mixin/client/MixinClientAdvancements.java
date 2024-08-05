package vazkii.patchouli.mixin.client;

import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ClientAdvancements.class })
public abstract class MixinClientAdvancements {

    @Inject(at = { @At("RETURN") }, method = { "update" })
    public void patchouli_onSync(ClientboundUpdateAdvancementsPacket packet, CallbackInfo info) {
        vazkii.patchouli.client.base.ClientAdvancements.onClientPacket();
    }
}