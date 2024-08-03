package fuzs.puzzleslib.impl.core;

import fuzs.puzzleslib.api.network.v3.ClientMessageListener;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;

public record ClientboundModListMessage(Collection<String> modList) implements ClientboundMessage<ClientboundModListMessage> {

    public ClientMessageListener<ClientboundModListMessage> getHandler() {
        return new ClientMessageListener<ClientboundModListMessage>() {

            public void handle(ClientboundModListMessage message, Minecraft client, ClientPacketListener handler, LocalPlayer player, ClientLevel level) {
                ModContext.acceptServersideMods(message.modList);
            }
        };
    }
}