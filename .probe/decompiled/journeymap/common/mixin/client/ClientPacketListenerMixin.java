package journeymap.common.mixin.client;

import java.util.Optional;
import journeymap.client.JourneymapClient;
import journeymap.client.event.forge.ForgeChatEvents;
import journeymap.client.event.forge.ForgeEventHandlerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ ClientPacketListener.class })
public class ClientPacketListenerMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = { "handleLogin(Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetPos()V", shift = Shift.AFTER) })
    public void handleLogin(ClientboundLoginPacket packet, CallbackInfo callbackInfo) {
        if (JourneymapClient.getInstance().getCoreProperties().seedId.get() && !Minecraft.getInstance().hasSingleplayerServer()) {
            JourneymapClient.getInstance().setCurrentWorldId("id_" + packet.seed());
        }
    }

    @Inject(method = { "handleRespawn(Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetPos()V", shift = Shift.AFTER) })
    public void handleRespawn(ClientboundRespawnPacket packet, CallbackInfo callbackInfo) {
        if (JourneymapClient.getInstance().getCoreProperties().seedId.get() && !Minecraft.getInstance().hasSingleplayerServer()) {
            JourneymapClient.getInstance().setCurrentWorldId("id_" + packet.getSeed());
        }
    }

    @Inject(method = { "handleSystemChat(Lnet/minecraft/network/protocol/game/ClientboundSystemChatPacket;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handleSystemMessage(Lnet/minecraft/network/chat/Component;Z)V", shift = Shift.BEFORE) }, cancellable = true)
    public void journeymap_handleSystemChat_onClientChatEventReceived(ClientboundSystemChatPacket clientboundSystemChatPacket, CallbackInfo callbackInfo) {
        Component incomingMessage = clientboundSystemChatPacket.content();
        ForgeChatEvents event = (ForgeChatEvents) ForgeEventHandlerManager.getHandlers().get(ForgeChatEvents.class);
        Component newMessage = event.getHandler().onClientChatEventReceived(incomingMessage);
        if (incomingMessage != newMessage && newMessage != null) {
            this.minecraft.getChatListener().handleSystemMessage(newMessage, false);
            callbackInfo.cancel();
        }
    }

    @Inject(method = { "sendUnsignedCommand(Ljava/lang/String;)Z" }, at = { @At("HEAD") }, cancellable = true)
    public void journeymap_sendUnsignedCommand_onChatEvent(String string, CallbackInfoReturnable<Boolean> cir) {
        ForgeChatEvents chatEvents = (ForgeChatEvents) ForgeEventHandlerManager.getHandlers().get(ForgeChatEvents.class);
        if (chatEvents.onChatEvent(string)) {
            cir.cancel();
            cir.setReturnValue(true);
        }
    }

    @Inject(method = { "sendCommand(Ljava/lang/String;)V" }, at = { @At("HEAD") }, cancellable = true)
    public void journeymap_sendCommand_onChatEvent(String string, CallbackInfo ci) {
        ForgeChatEvents chatEvents = (ForgeChatEvents) ForgeEventHandlerManager.getHandlers().get(ForgeChatEvents.class);
        if (chatEvents.onChatEvent(string)) {
            ci.cancel();
        }
    }

    @Inject(method = { "handleDisguisedChat(Lnet/minecraft/network/protocol/game/ClientboundDisguisedChatPacket;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handleDisguisedChatMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType$Bound;)V", shift = Shift.BEFORE) }, cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void journeymap_handleDisguisedChat_onClientChatEventReceived(ClientboundDisguisedChatPacket clientboundSystemChatPacket, CallbackInfo callbackInfo, Optional<ChatType.Bound> chatType) {
        Component incomingMessage = clientboundSystemChatPacket.message();
        ForgeChatEvents chatEvents = (ForgeChatEvents) ForgeEventHandlerManager.getHandlers().get(ForgeChatEvents.class);
        Component newMessage = chatEvents.getHandler().onClientChatEventReceived(incomingMessage);
        if (incomingMessage != newMessage && newMessage != null) {
            Minecraft.getInstance().getChatListener().handleDisguisedChatMessage(newMessage, (ChatType.Bound) chatType.get());
            callbackInfo.cancel();
        }
    }
}