package dev.architectury.networking.forge;

import dev.architectury.networking.NetworkManager;
import java.util.Collections;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

@OnlyIn(Dist.CLIENT)
public class ClientNetworkingManager {

    public static void initClient() {
        NetworkManagerImpl.CHANNEL.addListener(NetworkManagerImpl.createPacketHandler(NetworkEvent.ServerCustomPayloadEvent.class, NetworkManagerImpl.S2C_TRANSFORMERS));
        MinecraftForge.EVENT_BUS.register(ClientNetworkingManager.class);
        NetworkManagerImpl.registerS2CReceiver(NetworkManagerImpl.SYNC_IDS, Collections.emptyList(), (buffer, context) -> {
            Set<ResourceLocation> receivables = NetworkManagerImpl.serverReceivables;
            int size = buffer.readInt();
            receivables.clear();
            for (int i = 0; i < size; i++) {
                receivables.add(buffer.readResourceLocation());
            }
            context.queue(() -> NetworkManager.sendToServer(NetworkManagerImpl.SYNC_IDS, NetworkManagerImpl.sendSyncPacket(NetworkManagerImpl.C2S)));
        });
    }

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @SubscribeEvent
    public static void loggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
        NetworkManagerImpl.serverReceivables.clear();
    }
}