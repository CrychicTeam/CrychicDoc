package se.mickelus.tetra.mixin;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.tetra.items.modular.impl.toolbelt.suspend.SuspendPotionEffect;

@Mixin({ ServerGamePacketListenerImpl.class })
public abstract class ServerPlayNetHandlerMixin {

    @Inject(at = { @At("TAIL") }, method = { "handleMovePlayer" })
    private void processPlayer(ServerboundMovePlayerPacket packet, CallbackInfo callback) {
        if (this.getInstance().player.m_21023_(SuspendPotionEffect.instance)) {
            this.setClientIsFloating(false);
        }
    }

    private ServerGamePacketListenerImpl getInstance() {
        return (ServerGamePacketListenerImpl) this;
    }

    @Accessor
    public abstract void setClientIsFloating(boolean var1);
}