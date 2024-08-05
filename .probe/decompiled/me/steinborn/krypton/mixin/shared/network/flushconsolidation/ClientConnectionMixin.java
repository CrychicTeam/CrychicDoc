package me.steinborn.krypton.mixin.shared.network.flushconsolidation;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import java.util.concurrent.atomic.AtomicBoolean;
import me.steinborn.krypton.mod.shared.network.ConfigurableAutoFlush;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ Connection.class })
public abstract class ClientConnectionMixin implements ConfigurableAutoFlush {

    @Shadow
    private Channel channel;

    private AtomicBoolean autoFlush;

    @Shadow
    public abstract void setProtocol(ConnectionProtocol var1);

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void initAddedFields(CallbackInfo ci) {
        this.autoFlush = new AtomicBoolean(true);
    }

    @Inject(locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, method = { "sendImmediately" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/network/ClientConnection;packetsSentCounter:I", opcode = 181, shift = Shift.AFTER) })
    private void sendImmediately$rewrite(Packet<?> packet, @Nullable PacketSendListener callback, CallbackInfo info, ConnectionProtocol packetState, ConnectionProtocol protocolState) {
        boolean newState = packetState != protocolState;
        if (this.channel.eventLoop().inEventLoop()) {
            if (newState) {
                this.setProtocol(packetState);
            }
            this.doSendPacket(packet, callback);
        } else if (!newState && callback == null) {
            ChannelPromise voidPromise = this.channel.voidPromise();
            if (this.autoFlush.get()) {
                this.channel.writeAndFlush(packet, voidPromise);
            } else {
                this.channel.write(packet, voidPromise);
            }
        } else {
            if (newState) {
                this.channel.config().setAutoRead(false);
            }
            this.channel.eventLoop().execute(() -> {
                if (newState) {
                    this.setProtocol(packetState);
                }
                this.doSendPacket(packet, callback);
            });
        }
        info.cancel();
    }

    @Redirect(method = { "tick" }, at = @At(value = "FIELD", target = "Lnet/minecraft/network/ClientConnection;channel:Lio/netty/channel/Channel;", opcode = 180))
    public Channel disableForcedFlushEveryTick(Connection clientConnection) {
        return null;
    }

    private void doSendPacket(Packet<?> packet, PacketSendListener callback) {
        if (callback == null) {
            this.channel.write(packet, this.channel.voidPromise());
        } else {
            ChannelFuture channelFuture = this.channel.write(packet);
            channelFuture.addListener(listener -> {
                if (listener.isSuccess()) {
                    callback.onSuccess();
                } else {
                    Packet<?> failedPacket = callback.onFailure();
                    if (failedPacket != null) {
                        ChannelFuture failedChannelFuture = this.channel.writeAndFlush(failedPacket);
                        failedChannelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                    }
                }
            });
            channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
        if (this.autoFlush.get()) {
            this.channel.flush();
        }
    }

    @Override
    public void setShouldAutoFlush(boolean shouldAutoFlush) {
        boolean prev = this.autoFlush.getAndSet(shouldAutoFlush);
        if (!prev && shouldAutoFlush) {
            this.channel.flush();
        }
    }
}