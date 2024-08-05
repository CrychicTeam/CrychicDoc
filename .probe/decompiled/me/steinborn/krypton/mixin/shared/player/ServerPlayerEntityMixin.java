package me.steinborn.krypton.mixin.shared.player;

import me.steinborn.krypton.mod.shared.player.KryptonServerPlayerEntity;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ServerPlayer.class })
@Implements({ @Interface(iface = KryptonServerPlayerEntity.class, prefix = "krypton$", unique = true) })
public class ServerPlayerEntityMixin implements KryptonServerPlayerEntity {

    @Unique
    private int playerViewDistance = -1;

    @Unique
    private boolean needsChunksReloaded = false;

    @Inject(method = { "setClientSettings" }, at = { @At("HEAD") })
    public void setClientSettings(ServerboundClientInformationPacket packet, CallbackInfo ci) {
        this.needsChunksReloaded = this.playerViewDistance != packet.viewDistance();
        this.playerViewDistance = packet.viewDistance();
    }

    @Override
    public boolean getNeedsChunksReloaded() {
        return this.needsChunksReloaded;
    }

    @Override
    public void setNeedsChunksReloaded(boolean needsChunksReloaded) {
        this.needsChunksReloaded = needsChunksReloaded;
    }

    @Override
    public int getPlayerViewDistance() {
        return this.playerViewDistance;
    }
}